package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public class Task
{
    /**
     * The calendar object that represents the due-date for the task.
     */
    private Calendar due;

    /**
     * The name of the task.
     */
    private String name;

    /**
     * The action to perform upon completion of the task.
     */
    private DoOnComplete onComplete;

    /**
     * The list of tags under which task falls
     */
    private ArrayList<Tag> tags;

    /**
     * @return The name of the task
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The calendar year in which the item is due
     */
    public int getDueYear()
    {
        return due.get(Calendar.YEAR);
    }

    /**
     * @return The month in which the item is due (on a scale from 1 to 12).
     */
    public int getDueMonth()
    {
        return due.get(Calendar.MONTH) + 1;
    }

    /**
     * @return The day in a month on which the item is due.
     */
    public int getDueDay()
    {
        return due.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return A 3-item array of the form [year, month, day].
     */
    public int[] getDueDate()
    {
        return new int[]{getDueYear(), getDueMonth(), getDueDay()};
    }

    /**
     * @return Returns a string of the format
     *
     * yyyy-mm-dd: name of task
     */
    public String toString()
    {
        return String.format("%04d-%02d-%02d: %s", getDueYear(), getDueMonth(), getDueDay(), name);
    }

    /**
     * This method is the one called when the item is checked.
     */
    public void markCompleted()
    {
        onComplete.perform(this);
    }

    /**
     * @param toCheck The tag to check
     * @return True if this item contains that tag; false otherwise
     */
    // TODO This is currently n*m, where n is the number of tasks and m the number of tags. See if I can improve it (perhaps using a multiply linked list).
    public boolean contains(Tag toCheck)
    {
        return tags.contains(toCheck);
    }

    public Task(String name, Calendar dueDate, DoOnComplete onComplete)
    {
        this.name = name;
        this.due = dueDate;
        this.onComplete = onComplete;
    }

    public Task(String name, Calendar dueDate)
    {
        this(name, dueDate, null);
    }
}
