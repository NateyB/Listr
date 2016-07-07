package com.natebeckemeyer.projects.schedulrgui.implementations;

import com.natebeckemeyer.projects.schedulrgui.core.AbstractTask;
import com.natebeckemeyer.projects.schedulrgui.core.Rule;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-20.
 */
public class Completed implements Rule
{
    /**
     * @return The name of the rule; this is both the name that will be displayed and used to identify the rule in the
     * mapping of all rules
     */
    @Override public String toString()
    {
        return "completed";
    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param task the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @Override public boolean test(AbstractTask task)
    {
        return task.isCompleted();
    }
}
