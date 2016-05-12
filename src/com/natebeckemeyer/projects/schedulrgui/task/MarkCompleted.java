package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Objects;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-02.
 */
public class MarkCompleted implements DoOnComplete
{
    private static DoOnComplete me = new MarkCompleted();
    ;

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
     * Specifies behavior to perform when a task is completed.
     *
     * @param completed
     */
    @Override public void perform(Task completed)
    {
        completed.completed = true;
    }
}
