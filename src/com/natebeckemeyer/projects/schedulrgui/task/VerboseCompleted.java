package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Objects;

/**
 * This rule says which objects it marks as completed by printing to the console,
 * as opposed to the normal MarkCompleted, which makes no such claims.
 * <p>
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-11.
 */
public class VerboseCompleted implements OnCompletion
{
    /**
     * Ensures that only one object is ever created.
     */
    private static OnCompletion me = new VerboseCompleted();

    /**
     * @return The instance of this object that currently exists
     */
    public static OnCompletion getInstance()
    {
        return Objects.requireNonNull(me);
    }

    @Override public String getName()
    {
        return "Verbose";
    }

    /**
     * The important function: Provides the code for the completion of a task. In this case, it prints the item that
     * was completed to the console.
     *
     * @param completed The task that's been completed
     */
    @Override public void perform(Task completed)
    {
        completed.completed = true;
        System.out.printf("Task <<%s>> completed.%n", completed);
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