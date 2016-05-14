package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.function.Predicate;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 *
 * This interface is merely a layer of abstraction for the Predicate interface, in case I decide to add something later.
 * Of course, I've parametrized it to work only with tasks.
 */
public interface Rule extends Predicate<Task>
{
    String getName();
}
