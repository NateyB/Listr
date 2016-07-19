package com.natebeckemeyer.projects.listrgui.implementations;

import com.natebeckemeyer.projects.listrgui.core.AbstractTask;
import com.natebeckemeyer.projects.listrgui.core.Rule;

import java.util.GregorianCalendar;

/**
 * Created for Listr by @author Nate Beckemeyer on 2016-05-11.
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
    @Override public boolean test(AbstractTask task)
    {
        return new GregorianCalendar().compareTo(task.getDueDate()) >= 0;
    }

    @Override public String toString()
    {
        return "today";
    }
}
