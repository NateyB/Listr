package com.natebeckemeyer.projects.schedulrgui.implementations;

import com.natebeckemeyer.projects.schedulrgui.core.AbstractTask;
import com.natebeckemeyer.projects.schedulrgui.core.CompletionBehavior;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-02.
 * <p>
 * This class is the simple, default behavior for the completion of a implementations: It is marked as completed.
 */
public class SimpleCompleted extends CompletionBehavior
{
    public SimpleCompleted()
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
     * Only if the user wishes to change the implementations's completion flag, change the implementations's completion flag.
     *
     * @param task      The implementations whose completion value the user has changed.
     * @param completed The boolean value the user wants to set the completion to. This may or may not be different
     */
    @Override public void markCompletion(AbstractTask task, boolean completed)
    {
        if (completed != task.isCompleted())
            super.setCompleted(task, completed);
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
        return null;
    }
}