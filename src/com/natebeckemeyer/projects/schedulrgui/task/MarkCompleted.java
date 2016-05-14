package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Objects;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-02.
 * <p>
 * This class is the simple, default behavior for the completion of a task: It is marked as completed.
 */
public class MarkCompleted implements DoOnComplete
{
    /**
     * To ensure that only one object ever exists of this type.
     */
    private static DoOnComplete me = new MarkCompleted();

    private MarkCompleted()
    {
    }


    /**
     * This is a factory method so that only one item need exist (if that makes sense).
     *
     * @return An instance of the object
     */
    public static DoOnComplete getInstance()
    {
        return Objects.requireNonNull(me);
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
}
