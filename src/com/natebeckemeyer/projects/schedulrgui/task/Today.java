package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.GregorianCalendar;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-11.
 */
public class Today implements Rule
{
    /**
     * Returns true if the task is due today or before today.
     *
     * @param task the task to check against
     * @return {@code true} if the task is due before tomorrow,
     * otherwise {@code false}
     */
    @Override public boolean test(Task task)
    {
        return new GregorianCalendar().compareTo(task.getDueDate()) >= 0;
    }

    @Override public String getName()
    {
        return "today";
    }
}
