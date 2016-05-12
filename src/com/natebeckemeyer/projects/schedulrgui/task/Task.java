package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public class Task implements Comparable<Task>
{
    /**
     * The flag for whether this task has been completed.
     */
    boolean completed = false;

    /**
     * The Calendar object containing the due date for the object.
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
    private Set<Tag> tags;

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
     * @return The month in which the item is due (on a scale from 0 to 11).
     */
    public int getDueMonth()
    {
        return due.get(Calendar.MONTH);
    }

    /**
     * @return The day in a month on which the item is due.
     */
    public int getDueDay()
    {
        return due.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns the due date of the item in Calendar form. This calendar object is generated
     * when this object is instantiated, and is therefore subject to the locale at that time.
     *
     * @return The calendar object containing the due date.
     */
    public Calendar getDueDate()
    {
        return due;
    }

    /**
     * Returns the due date of the item in array form. Note that month is on a scale from 0 to 11.
     *
     * @return A 3-item array of the form [year, month, day].
     */
    public int[] getDueArray()
    {
        return new int[]{getDueYear(), getDueMonth(), getDueDay()};
    }

    /**
     * @return Returns a string of the format
     * <p>
     * yyyy-mm-dd: name of task
     */
    public String toString()
    {
        return String.format("%04d-%02d-%02d: %s", getDueYear(), getDueMonth() + 1, getDueDay(), getName());
    }

    public void setTags(Collection<Tag> tags)
    {
        this.tags = new HashSet<>(tags);
    }

    public void addTags(Collection<Tag> tags)
    {
        this.tags.addAll(tags);
    }

    public void addTag(Tag tag)
    {
        this.tags.add(tag);
    }

    public boolean removeTag(Tag tag)
    {
        return this.tags.remove(tag);
    }

    public boolean removeTags(Collection<Tag> tags)
    {
        return this.tags.removeAll(tags);
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
    // TODO This is currently n*m, where n is the number of tasks and m the number of tags. See if I can improve it
    // (perhaps using a multiply linked list).
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
        this(name, dueDate, MarkCompleted.getInstance());
    }

    /**
     * Compares two items according to their due dates, exactly in accordance with the following algorithm.
     * First, uncompleted tasks always take priority over completed ones. Second, the tasks are ordered in accordance
     * with their calendar due dates. Third, the names are compared for lexicographical ordering.
     *
     * @param other The task to compare this task against
     * @return -1 if this task is ordered before the other task, 0 if they're ordered same time, and 1 if this task
     * ordered after the other task.
     */
    @Override public int compareTo(Task other)
    {
        // Compare completed status
        int compareResult = Boolean.compare(this.completed, other.completed);
        if (compareResult != 0)
            return compareResult;

        // Compare dates
        compareResult = this.getDueDate().compareTo(other.getDueDate());
        if (compareResult != 0)
            return compareResult;

        // Compare names
        return this.getName().compareTo(other.getName());
    }
}
