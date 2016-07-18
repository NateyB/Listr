package com.natebeckemeyer.projects.listrgui.implementations;

import com.natebeckemeyer.projects.listrgui.core.AbstractTask;
import com.natebeckemeyer.projects.listrgui.core.Rule;

/**
 * Created for Listr by @author Nate Beckemeyer on 2016-06-20.
 * <p>
 * This {@link Rule} returns true for all predicates for which {@link AbstractTask#isCompleted()} returns true.
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
