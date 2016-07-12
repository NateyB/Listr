package com.natebeckemeyer.projects.schedulrgui.reference;

import com.natebeckemeyer.projects.schedulrgui.core.AbstractTask;
import com.natebeckemeyer.projects.schedulrgui.core.CompletionBehavior;
import com.natebeckemeyer.projects.schedulrgui.implementations.SimpleCompleted;
import com.natebeckemeyer.projects.schedulrgui.implementations.SimpleTask;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-07-06.
 */
public final class Defaults
{
    /**
     * Disable instantiation.
     */
    private Defaults()
    {
    }

    public static CompletionBehavior getDefaultCompletionBehavior()
    {
        return new SimpleCompleted();
    }

    public static Class<? extends AbstractTask> getDefaultTask()
    {
        return SimpleTask.class;
    }
}
