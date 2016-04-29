package com.natebeckemeyer.projects.schedulrgui.task;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public interface Rule
{
    /**
     * A function which returns true if a task passes and false otherwise.
     */
    boolean performTest(Task task);

}
