package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Objects;

/**
 * This rule says which objects it marks as completed by printing to the console,
 * as opposed to the normal MarkCompleted, which makes no such claims.
 * <p>
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-11.
 */
public class VerboseCompleted implements DoOnComplete
{
    /**
     * Ensures that only one object is ever created.
     */
    private static DoOnComplete me = new VerboseCompleted();

    /**
     * @return The instance of this object that currently exists
     */
    public static DoOnComplete getInstance()
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
}
