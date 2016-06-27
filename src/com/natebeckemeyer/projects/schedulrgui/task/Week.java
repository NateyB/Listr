package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-08.
 */
public class Week implements Rule
{
    @Override public String toString()
    {
        return "week";
    }

    /**
     * Returns true if the task is due before the end of the week
     *
     * @param task the task to be tested
     * @return {@code true} if the task is due before the end of the week,
     * otherwise {@code false}
     */
    @Override public boolean test(Task task)
    {
        Calendar nextWeek = new GregorianCalendar();
        nextWeek.add(Calendar.WEEK_OF_YEAR, 1);

        return nextWeek.compareTo(task.getDueDate()) >= 0;
    }
}
