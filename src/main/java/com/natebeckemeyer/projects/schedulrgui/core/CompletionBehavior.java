package com.natebeckemeyer.projects.schedulrgui.core;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public abstract class CompletionBehavior
{
    /**
     * Sets the completed tag in {@code task} to {@code completed} without invoking the task's completion method.
     */
    protected static void setCompleted(AbstractTask task, boolean completed)
    {
        task.completed = completed;
    }

    /**
     * Specifies behavior as to how to mark a implementations as completed, or uncompleted as the case may be.
     * Specifically, markCompletion(implementations, false) should undo all of the changes made by markCompletion
     * (implementations, true), so that side effects don't get out of hand.
     *
     * @param task      The implementations whose completion value the user has changed.
     * @param completed The boolean value the user wants to set the completion to. This may or may not be different
     *                  from what the value already is.
     */
    public abstract void markCompletion(AbstractTask task, boolean completed);

    /**
     * Deserializes an object from a string. Note that the string <i>cannot</i> contain a pipe character.
     */
    public abstract void loadFromString(String serialized);

    /**
     * Serializes an CompletionBehavior object into a string. Note that the string <i>cannot</i> contain a pipe
     * character. Must not return null.
     * Including the pipe character will result in an InvalidCharacterException.
     *
     * @return the serialized behavior
     */
    public abstract String convertToString();

    /**
     * This method will be called when a task is to be copied. Therefore, if the behavior must have separate
     * fields for the copied task, then this method should return a new instance of the behavior; furthermore, those
     * fields are to be initialized to their current values, rather than their default values, then those values most
     * also be copied over.
     *
     * @return The initialized, copied behavior
     */
    protected abstract CompletionBehavior copy();
}
