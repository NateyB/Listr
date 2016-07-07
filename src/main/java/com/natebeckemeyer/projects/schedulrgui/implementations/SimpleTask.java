package com.natebeckemeyer.projects.schedulrgui.implementations;

import com.natebeckemeyer.projects.schedulrgui.core.AbstractTask;
import com.natebeckemeyer.projects.schedulrgui.core.CompletionBehavior;
import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public class SimpleTask extends AbstractTask
{
    /**
     * Constructs a implementations named {@code name}, due on the date specified {@code dueDate}. The
     * implementations is created with the
     * completion behavior specified by the {@code onComplete} object.
     *
     * @param name       The name of the implementations
     * @param dueDate    The Calendar object representing the due date of the implementations.
     * @param onComplete The specified completion behavior.
     */
    public SimpleTask(String name, Calendar dueDate,
                      CompletionBehavior onComplete)
    {
        super(name, dueDate, onComplete);
    }

    /**
     * Copies the instance fields of implementations {@code other} into a new implementations.
     *
     * @param other The implementations to copy.
     */
    public SimpleTask(AbstractTask other)
    {
        super(other);
    }

    /**
     * @return The year that the task is scheduled to be completed.
     */
    private int getDueYear()
    {
        return due.get(Calendar.YEAR);
    }

    /**
     * @return The month of the year that the task is scheduled to be completed (on a 0-11 scale).
     */
    private int getDueMonth()
    {
        return due.get(Calendar.MONTH);
    }

    /**
     * @return The day of the month that the task is scheduled to be completed.
     */
    private int getDueDay()
    {
        return due.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns the due date of the item in Calendar form. This calendar object is generated
     * when this object is instantiated, and is therefore subject to the locale at that time.
     *
     * @return The calendar object containing the due date.
     */
    @Override public Calendar getDueDate()
    {
        return (Calendar) due.clone();
    }

    /**
     * Used by JavaFX to display the date that the implementations is due.
     */
    @Override public String getDueString()
    {
        return String.format("%04d-%02d-%02d", getDueYear(), getDueMonth() + 1, getDueDay());
    }

    @Override
    public String toString()
    {
        return String.format("%s: %s", getDueString(), getName());
    }

    /**
     * Serializes all of the important data to a string so that it is recoverable. This serialization
     * may include things like the due date, the name, the tags, the completion flag, and behavior.
     * Cannot include a newline character.
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


        return String.format("%04d|%02d|%02d|%b|%s|%s|%s|%s",
                getDueYear(), getDueMonth(), getDueDay(), isCompleted(),
                tags.toString(), getOnComplete().getSimpleName(), onCompleteOut, this.getName());
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

        due = new GregorianCalendar(parser.nextInt(), parser.nextInt(), parser.nextInt());
        completed = parser.nextBoolean();

        Scanner labelParser = new Scanner(parser.next());
        while (labelParser.hasNext())
            this.addTag(Tag.getTag(labelParser.next()));
        labelParser.close();

        this.onComplete = Schedulr.getCompletionBehavior(parser.next());
        this.onComplete.loadFromString(parser.next());
        this.name = parser.next();

        parser.close();
    }
}