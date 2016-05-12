package com.natebeckemeyer.projects.schedulrgui.task;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public interface DoOnComplete
{
    /**
     * Specifies behavior to perform when a task is completed.
     */
    void perform(Task completed);
}
