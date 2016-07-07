package com.natebeckemeyer.projects.schedulrgui.implementations;

import com.natebeckemeyer.projects.schedulrgui.core.AbstractTask;
import com.natebeckemeyer.projects.schedulrgui.core.CompletionBehavior;
import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-07-06.
 */
public class DatelessTask extends AbstractTask
{
    /**
     * Sets the due date for this task to Integer.MIN_VALUE for each field.
     */
    private void setDueDate()
    {
        if (this.due == null)
            this.due = new GregorianCalendar();

        this.due.set(Calendar.YEAR, Integer.MIN_VALUE);
        this.due.set(Calendar.MONTH, Integer.MIN_VALUE);
        this.due.set(Calendar.DAY_OF_MONTH, Integer.MIN_VALUE);
    }

    /**
     * Constructs a implementations named {@code name}, due on the date specified {@code dueDate}. The
     * implementations is created with the
     * completion behavior specified by the {@code onComplete} object.
     *
     * @param name       The name of the implementations
     * @param dueDate    The Calendar object representing the due date of the implementations.
     * @param onComplete The specified completion behavior.
     */
    public DatelessTask(String name, Calendar dueDate,
                        CompletionBehavior onComplete)
    {
        super(name, dueDate, onComplete);
        setDueDate();
    }

    /**
     * Copies the instance fields of implementations {@code other} into a new implementations.
     *
     * @param other The implementations to copy.
     */
    public DatelessTask(AbstractTask other)
    {
        super(other);
        setDueDate();
    }

    /**
     * Returns the due date of the item in Calendar form. This calendar object is generated
     * when this object is instantiated, and is therefore subject to the locale at that time.
     *
     * @return The calendar object containing the due date.
     */
    @Override public Calendar getDueDate()
    {
        return due;
    }

    /**
     * Used by JavaFX to display the date that the implementations is due.
     */
    @Override public String getDueString()
    {
        return "Eventually";
    }

    /**
     * Provides a formatted string representing the task (for console display).
     */
    @Override public String toString()
    {
        return getDueString() + ": " + getName();
    }

    /**
     * Serializes all of the important data to a string so that it is recoverable. This serialization
     * may include things like the due date, the name, the tags, the completion flag, and behavior.
     * Cannot include the pipe character, '|'.
     *
     * @return The string containing the serialization of this task.
     */
    @Override public String serialize()
    {
        StringBuilder tags = new StringBuilder("");
        for (Tag tag : getTags())
        {
            tags.append(tag.toString());
            tags.append(" ");
        }

        String onCompleteOut = onComplete.convertToString();
        if (onCompleteOut == null || onCompleteOut.isEmpty())
            onCompleteOut = " ";

        return String.format("%b|%s|%s|%s|%s", isCompleted(), tags.toString().trim(),
                getOnComplete().getSimpleName(), onCompleteOut, getName());
    }

    /**
     * Loads all of the important data from the string that this task serialized.
     *
     * @param serialization The string containing the serialization of this task.
     */
    @Override public void loadFromSerialzation(String serialization)
    {

        Scanner parser = new Scanner(serialization);
        parser.useDelimiter(Pattern.quote("|"));

        completed = parser.nextBoolean();

        Scanner labelParser = new Scanner(parser.next());
        while (labelParser.hasNext())
            this.addTag(Tag.getTag(labelParser.next()));
        labelParser.close();

        this.onComplete = Schedulr.getCompletionBehavior(parser.next());
        this.onComplete.loadFromString(parser.next());
        this.name = parser.next();

        parser.close();
        setDueDate();
    }
}
