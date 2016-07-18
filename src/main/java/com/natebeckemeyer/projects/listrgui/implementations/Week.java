package com.natebeckemeyer.projects.listrgui.implementations;

import com.natebeckemeyer.projects.listrgui.core.AbstractTask;
import com.natebeckemeyer.projects.listrgui.core.Rule;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created for Listr by @author Nate Beckemeyer on 2016-06-08.
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
    @Override public boolean test(AbstractTask task)
    {
        Calendar nextWeek = new GregorianCalendar();
        nextWeek.add(Calendar.WEEK_OF_YEAR, 1);

        return nextWeek.compareTo(task.getDueDate()) >= 0;
    }
}
