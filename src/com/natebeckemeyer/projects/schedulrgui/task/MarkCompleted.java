package com.natebeckemeyer.projects.schedulrgui.task;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-02.
 * <p>
 * This class is the simple, default behavior for the completion of a task: It is marked as completed.
 */
public class MarkCompleted implements OnCompletion
{
    public MarkCompleted()
    {
    }

    /**
     * Returns the name of the completion behavior (to display to the user). In this case, "Default."
     *
     * @return The name of the completion behavior.
     */
    @Override public String getName()
    {
        return "Default";
    }

    /**
     * Specifies behavior to perform when a task is completed.
     *
     * @param completed
     */
    @Override public void perform(Task completed)
    {
        completed.completed = true;
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
     * Serializes an OnCompletion object into a string. Note that the string <i>cannot</i> contain a pipe character.
     * Including the pipe character will result in an InvalidCharacterException.
     */
    @Override public String convertToString()
    {
        return " ";
    }
}