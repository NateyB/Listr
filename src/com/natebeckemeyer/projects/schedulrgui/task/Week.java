package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-08.
 */
public class Week implements Rule
{
    @Override public String getName()
    {
        return "week";
    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param task the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @Override public boolean test(Task task)
    {
        Calendar nextWeek = new GregorianCalendar();
        nextWeek.add(Calendar.WEEK_OF_YEAR, 1);

        return nextWeek.compareTo(task.getDueDate()) >= 0;
    }
}
