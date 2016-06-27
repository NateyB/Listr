package com.natebeckemeyer.projects.schedulrgui.task;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-02.
 * <p>
 * This class is the simple, default behavior for the completion of a task: It is marked as completed.
 */
public class MarkCompleted implements CompletionBehavior
{
    public MarkCompleted()
    {
    }

    /**
     * Returns the name of the completion behavior (to display to the user). In this case, "Default."
     *
     * @return The name of the completion behavior.
     */
    @Override public String toString()
    {
        return "Default";
    }

    /**
     * Specifies behavior to markCompleted when a task is completed.
     *
     * @param completed
     */
    @Override public void markCompleted(Task completed)
    {
        completed.completed = true;
    }

    /**
     * Specifies behavior to mark a task uncompleted
     *
     * @param uncompleted
     */
    @Override public void markUncompleted(Task uncompleted)
    {
        uncompleted.completed = false;
    }

    /**
     * Deserializes an object from a string. Note that the string <i>cannot</i> contain a pipe character.
     *
     * @param serialized
     * @return The new object containing the relevant fields.
     */
    @Override public void loadFromString(String serialized)
    {
        // NOOP
    }

    /**
     * Serializes an CompletionBehavior object into a string. Note that the string <i>cannot</i> contain a pipe
     * character.
     * Including the pipe character will result in an InvalidCharacterException.
     */
    @Override public String convertToString()
    {
        return "";
    }
}
