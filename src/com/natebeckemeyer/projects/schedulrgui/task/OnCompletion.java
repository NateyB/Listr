package com.natebeckemeyer.projects.schedulrgui.task;

import com.sun.istack.internal.NotNull;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public interface OnCompletion
{

    /**
     * Returns the name of the completion behavior (to display to the user).
     *
     * @return The name of the completion behavior.
     */
    String getName();

    /**
     * Specifies behavior to markCompleted when a task is completed.
     */
    void markCompleted(Task completed);

    /**
     * Specifies behavior to mark a task uncompleted
     */
    void markUncompleted(Task uncompleted);

    /**
     * Deserializes an object from a string. Note that the string <i>cannot</i> contain a pipe character.
     *
     * @return The new object containing the relevant fields.
     */
    void loadFromString(String serialized);

    /**
     * Serializes an OnCompletion object into a string. Note that the string <i>cannot</i> contain a pipe character.
     * Including the pipe character will result in an InvalidCharacterException.
     */
    @NotNull
    String convertToString();
}
