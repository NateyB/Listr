package com.natebeckemeyer.projects.schedulrgui.task;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public interface DoOnComplete
{
    /**
     * Returns the name of the completion behavior (to display to the user).
     *
     * @return The name of the completion behavior.
     */
    String getName();

    /**
     * Specifies behavior to perform when a task is completed.
     */
    void perform(Task completed);
}
