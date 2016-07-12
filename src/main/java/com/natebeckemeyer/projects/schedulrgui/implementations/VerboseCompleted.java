package com.natebeckemeyer.projects.schedulrgui.implementations;

import com.natebeckemeyer.projects.schedulrgui.core.AbstractTask;
import com.natebeckemeyer.projects.schedulrgui.core.CompletionBehavior;

/**
 * This rule says which objects it marks as completed by printing to the console,
 * as opposed to the normal SimpleCompleted, which makes no such claims.
 * <p>
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-11.
 */
public class VerboseCompleted extends CompletionBehavior
{
    @Override public String toString()
    {
        return "Verbose";
    }

    /**
     * Marks {@code task} {@code completed}, if it isn't already, and outputs its action to the console.
     *
     * @param task      The implementations whose completion value the user has changed.
     * @param completed The boolean value the user wants to set the completion to. This may or may not be different
     */
    @Override public void markCompletion(AbstractTask task, boolean completed)
    {
        if (task.isCompleted() != completed)
        {
            setCompleted(task, completed);
            System.out.printf("<<Marked the completion flag for task \"%s\" as %b.>>%n", task.getName(), completed);
        } else
            System.out.printf("<<Completion for task \"%s\" remains at value %b.>>%n", task.getName(), completed);
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

    /**
     * This method will be called when a task is to be copied. Therefore, if the behavior must have separate
     * fields for the copied task, then this method should return a new instance of the behavior; furthermore, those
     * fields are to be initialized to their current values, rather than their default values, then those values most
     * also be copied over.
     *
     * @return The initialized, copied behavior
     */
    @Override protected CompletionBehavior copy()
    {
        return new VerboseCompleted();
    }
}
