package com.natebeckemeyer.projects.schedulrgui.core;

import com.natebeckemeyer.projects.schedulrgui.implementations.Tag;
import com.natebeckemeyer.projects.schedulrgui.reference.Defaults;

import java.util.*;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-07-06.
 */
public abstract class AbstractTask implements Comparable<AbstractTask>
{
    /**
     * The flag for whether this implementations has been completed.
     */
    protected boolean completed = false;

    /**
     * The Calendar object containing the due date for the object.
     */
    protected Calendar due;

    /**
     * The name of the implementations.
     */
    protected String name;

    /**
     * The object specifying the action to markCompletion upon completion of the implementations.
     */
    protected CompletionBehavior onComplete;

    /**
     * The list of tags associated with the implementations.
     */
    private Set<Tag> tags = new HashSet<>();

    /**
     * Returns the CompletionBehavior object associated with this implementations
     */
    public Class<? extends CompletionBehavior> getOnComplete()
    {
        return onComplete.getClass();
    }

    /**
     * @return The name of the implementations
     */
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Whether or not this implementations has been completed
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
    public abstract Calendar getDueDate();

    /**
     * Used by JavaFX to display the date that the implementations is due.
     */
    public abstract String getDueString();

    /**
     * Provides a formatted string representing the implementations (for console display).
     */
    public abstract String toString();

    /**
     * Serializes all of the important data to a string so that it is recoverable. This serialization
     * may include things like the due date, the name, the tags, the completion flag, and behavior.
     * Cannot include the pipe character, '|'.
     *
     * @return The string containing the serialization of this task.
     */
    public abstract String serialize();

    /**
     * Loads all of the important data from the string that this task serialized.
     *
     * @param serialization The string containing the serialization of this task.
     */
    public abstract void loadFromSerialzation(String serialization);

    /**
     * @return The set of tags associated with this object
     */
    public Set<Tag> getTags()
    {
        return tags;
    }

    /**
     * Sets the tags associated with this implementations to collection of tags supplied.
     *
     * @param tags The tags to associate with this implementations.
     */
    public void setTags(Collection<Tag> tags)
    {
        this.tags = new HashSet<>(tags);
    }

    /**
     * Adds the tags supplied to the set of tags associated with this implementations.
     * More specifically, this operation treats the tags supplied as a set and performs the union operation
     * using the "equals" Object method.
     *
     * @param tags The additional tags to associate with this implementations
     */
    public void addTags(Collection<Tag> tags)
    {
        this.tags.addAll(tags);
    }

    /**
     * Adds the tag supplied to the set of tags associated with this implementations.
     * More specifically, this operation treats the tag supplied as a single-item set and performs the union operation
     * using the "equals" Object method.
     *
     * @param tag The tag to add (if possible) to the list of tags associated with this implementations.
     */
    public void addTag(Tag tag)
    {
        this.tags.add(tag);
    }

    /**
     * Removes the tag from the set of tags associated with this implementations.
     * More specifically, this operation treats the tag supplied as a single-item set and performs the set difference
     * operation using the 'equals" Object method.
     *
     * @param tag The tag to remove (if possible) from the list of tags associated with this implementations.
     * @return True if the set of tags contained this tag. The set will no longer contain this tag after the call.
     */
    public boolean removeTag(Tag tag)
    {
        return this.tags.remove(tag);
    }

    /**
     * Removes the tags from the set of tags associated with this implementations.
     * More specifically, this operation treats the tags supplied as a set and performs the set difference
     * operation using the 'equals" Object method.
     *
     * @param tags The tags to remove (if possible) from the list of tags associated with this implementations.
     * @return True if the set of tags is changed as a result of this call
     */
    public boolean removeTags(Collection<Tag> tags)
    {
        return this.tags.removeAll(tags);
    }


    /**
     * Marks the task as completed or uncompleted, according to its completion behavior.
     */
    public void setCompleted(boolean completed)
    {
        onComplete.setCompleted(this, completed);
    }

    /**
     * Given a tag, this method checks to see if it's a member of the tag list for this implementations.
     *
     * @param toCheck The tag to check
     * @return True if this item contains that tag; false otherwise
     */
    public boolean contains(Tag toCheck)
    {
        return tags.contains(toCheck);
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
    public AbstractTask(String name, Calendar dueDate, CompletionBehavior onComplete)
    {
        this.name = name;
        this.onComplete = onComplete;
        this.due = dueDate;
    }

    /**
     * Copies the instance fields of implementations {@code other} into a new implementations.
     *
     * @param other The implementations to copy.
     */
    public AbstractTask(AbstractTask other)
    {
        this.name = other.getName();
        Calendar dueDate = other.getDueDate();
        if (dueDate != null)
            this.due = new GregorianCalendar(dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH),
                    dueDate.get(Calendar.DAY_OF_MONTH));
        this.completed = other.isCompleted();
        try
        {
            this.onComplete = other.getOnComplete().newInstance();
        } catch (IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
            this.onComplete = Defaults.getDefaultCompletionBehavior();
        }
        this.tags = new HashSet<>(other.tags);
    }

    /**
     * Compares two tasks, exactly in accordance with the following algorithm.
     * First, uncompleted tasks always take priority over completed ones. Second, the tasks are ordered in accordance
     * with their calendar due dates (older first). Third, the names are compared for lexicographical ordering.
     *
     * @param other The implementations to compare this implementations against
     * @return -1 if this implementations is ordered before the other implementations,
     * 0 if they're ordered the same, and
     * 1 if this implementations ordered after the other implementations.
     */
    @Override public int compareTo(AbstractTask other)
    {
        // Compare completed status
        int compareResult = Boolean.compare(this.isCompleted(), other.isCompleted());
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
