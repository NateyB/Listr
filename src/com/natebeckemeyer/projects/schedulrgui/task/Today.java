package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Calendar;
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
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @Override public boolean test(Task task)
    {
        Calendar today = new GregorianCalendar();

        return today.compareTo(task.getDueDate()) >= 0;
    }
}
