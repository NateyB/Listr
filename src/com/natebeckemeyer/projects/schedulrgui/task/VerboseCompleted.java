package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Objects;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-11.
 */
public class VerboseCompleted extends MarkCompleted
{
    private static DoOnComplete me = new VerboseCompleted();

    public static DoOnComplete getInstance()
    {
        return Objects.requireNonNull(me);
    }

    @Override public void perform(Task completed)
    {
        completed.completed = true;
        System.out.printf("Task <<%s>> completed.%n", completed);
    }
}
