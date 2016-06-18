package com.natebeckemeyer.projects.schedulrgui.task;

/**
 * This rule says which objects it marks as completed by printing to the console,
 * as opposed to the normal MarkCompleted, which makes no such claims.
 * <p>
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-11.
 */
public class VerboseCompleted implements OnCompletion
{
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
    @Override public void markCompleted(Task completed)
    {
        completed.completed = true;
        System.out.printf("Task <<%s>> completed.%n", completed);
    }

    /**
     * Specifies behavior to mark a task uncompleted
     *
     * @param uncompleted
     */
    @Override public void markUncompleted(Task uncompleted)
    {
        uncompleted.completed = false;
        System.out.printf("Task <<%s>> uncompleted.%n", uncompleted);
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
        return "";
    }
}
