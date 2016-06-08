package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public interface Rule extends Predicate<Task>
{
    String getName();

    default Rule and(Rule other)
    {
        Rule thisOne = this;
        Objects.requireNonNull(other);
        return new Rule()
        {
            @Override public String getName()
            {
                return String.format("(%s & %s)", thisOne.getName(), other.getName());
            }

            @Override public boolean test(Task task)
            {
                return thisOne.test(task) && other.test(task);
            }

            @Override public String toString()
            {
                return getName();
            }
        };
    }

    default Rule or(Rule other)
    {
        Rule thisOne = this;
        Objects.requireNonNull(other);
        return new Rule()
        {
            @Override public String getName()
            {
                return String.format("(%s + %s)", thisOne.getName(), other.getName());
            }

            @Override public boolean test(Task task)
            {
                return thisOne.test(task) || other.test(task);
            }

            @Override public String toString()
            {
                return getName();
            }
        };
    }

    default Rule negate()
    {
        Rule thisOne = this;
        return new Rule()
        {
            @Override public String getName()
            {
                return String.format("!%s", thisOne.getName());
            }

            @Override public boolean test(Task task)
            {
                return !thisOne.test(task);
            }

            @Override public String toString()
            {
                return getName();
            }
        };
    }
}
