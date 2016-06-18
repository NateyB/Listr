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
     * The object specifying the action to markCompleted upon completion of the task.
     */
    private OnCompletion onComplete;

    /**
     * The list of tags associated with the task.
     */
    private Set<Tag> tags = new HashSet<>();

    /**
     * Returns the OnCompletion object associated with this task
     */
    OnCompletion getOnComplete()
    {
        return onComplete;
    }

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
     * @return The month in which the item is due (on a scale from {@code 0} to {@code 11}).
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
     * @return Whether or not this task has been completed
     */
    public boolean isCompleted()
    {
        return completed;
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
     * Used by JavaFX to display the date that the task is due.
     *
     * @return A string of the format {@code yyyy-mm-dd}.
     */
    public String getDueString()
    {
        return String.format("%04d-%02d-%02d", getDueYear(), getDueMonth() + 1, getDueDay());
    }

    /**
     * Returns the due date of the item in array form. Note that month is on a scale from {@code 0} to {@code 11}.
     *
     * @return A 3-item array of the form {@code [year, month, day]}.
     */
    public int[] getDueArray()
    {
        return new int[]{getDueYear(), getDueMonth(), getDueDay()};
    }

    /**
     * Provides a formatted string representing the task (for console display).
     *
     * @return Returns a string of the format {@code yyyy-mm-dd: name of task}
     */
    public String toString()
    {
        return String.format("%s: %s", getDueString(), getName());
    }

    public Set<Tag> getTags()
    {
        return tags;
    }

    /**
     * Sets the tags associated with this task to collection of tags supplied.
     *
     * @param tags The tags to associate with this task.
     */
    public void setTags(Collection<Tag> tags)
    {
        this.tags = new HashSet<>(tags);
    }

    /**
     * Adds the tags supplied to the set of tags associated with this task.
     * More specifically, this operation treats the tags supplied as a set and performs the union operation
     * using the "equals" Object method.
     *
     * @param tags The additional tags to associate with this task
     */
    public void addTags(Collection<Tag> tags)
    {
        this.tags.addAll(tags);
    }

    /**
     * Adds the tag supplied to the set of tags associated with this task.
     * More specifically, this operation treats the tag supplied as a single-item set and performs the union operation
     * using the "equals" Object method.
     *
     * @param tag The tag to add (if possible) to the list of tags associated with this task.
     */
    public void addTag(Tag tag)
    {
        this.tags.add(tag);
    }

    /**
     * Removes the tag from the set of tags associated with this task.
     * More specifically, this operation treats the tag supplied as a single-item set and performs the set difference
     * operation using the 'equals" Object method.
     *
     * @param tag The tag to remove (if possible) from the list of tags associated with this task.
     * @return True if the set of tags contained this tag. The set will no longer contain this tag after the call.
     */
    public boolean removeTag(Tag tag)
    {
        return this.tags.remove(tag);
    }

    /**
     * Removes the tags from the set of tags associated with this task.
     * More specifically, this operation treats the tags supplied as a set and performs the set difference
     * operation using the 'equals" Object method.
     *
     * @param tags The tags to remove (if possible) from the list of tags associated with this task.
     * @return True if the set of tags is changed as a result of this call
     */
    public boolean removeTags(Collection<Tag> tags)
    {
        return this.tags.removeAll(tags);
    }


    /**
     * This method is called when the task is checked as completed, and handles the completion of the object.
     */
    public void setCompleted(boolean completed)
    {
        if (completed)
            onComplete.markCompleted(this);
        else
            onComplete.markUncompleted(this);
    }

    /**
     * Given a tag, this method checks to see if it's a member of the tag list for this task.
     *
     * @param toCheck The tag to check
     * @return True if this item contains that tag; false otherwise
     */
    // TODO This is currently n*m, where n is the number of tasks and m the number of tags. See if I can improve it
    // (perhaps using a multiply linked list). Of course, this compromises the generalization of a tag to a rule,
    // so I will have to revisit this issue later.
    public boolean contains(Tag toCheck)
    {
        return tags.contains(toCheck);
    }

    /**
     * Constructs a task named {@code name}, due on the date specified {@code dueDate}. The task is created with the
     * completion behavior specified by the {@code onComplete} object.
     *
     * @param name       The name of the task
     * @param dueDate    The Calendar object representing the due date of the task.
     * @param onComplete The specified completion behavior.
     */
    public Task(String name, Calendar dueDate, OnCompletion onComplete)
    {
        this.name = name;
        this.due = dueDate;
        this.onComplete = onComplete;
    }

    /**
     * Constructs a task named {@code name}, due on the date specified by {@code dueDate}. The task is created with the
     * default completion behavior (having its {@code completed} flag set to true).
     *
     * @param name    The name of the task.
     * @param dueDate The Calendar object representing the date that the object is due.
     */
    // TODO These calendar objects seem like they hold too much information. Perhaps create something that parses only
    // relevant information about the date.
    public Task(String name, Calendar dueDate)
    {
        this(name, dueDate, new MarkCompleted());
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
