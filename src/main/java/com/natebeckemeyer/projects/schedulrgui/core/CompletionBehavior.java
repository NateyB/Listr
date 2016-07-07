package com.natebeckemeyer.projects.schedulrgui.core;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public abstract class CompletionBehavior
{
    /**
     * Sets the completed tag in
     */
    final protected void setCompleted(AbstractTask task, boolean completed)
    {
        task.completed = completed;
    }

    /**
     * Specifies behavior as to how to mark a implementations as completed, or uncompleted as the case may be.
     * Specifically, markCompletion(implementations, false) should undo all of the changes made by markCompletion(implementations, true), so
     * that side effects don't get out of hand.
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
     */
    public abstract String convertToString();
}
