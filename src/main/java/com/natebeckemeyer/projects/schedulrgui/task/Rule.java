package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
@FunctionalInterface
public interface Rule extends Predicate<Task>
{
    /**
     * By default, performs a logical "and".
     *
     * @param other The rule with which to perform a logical and
     * @return A new rule that is the conjunction of this rule and the other rule.
     */
    static Rule and(Rule first, Rule other)
    {
        Objects.requireNonNull(first);
        Objects.requireNonNull(other);
        return new Rule()
        {
            @Override public String toString()
            {
                return String.format("(%s & %s)", first.toString(), other.toString());
            }

            @Override public boolean test(Task task)
            {
                return first.test(task) && other.test(task);
            }
        };
    }

    /**
     * By default, performs a logical "or".
     *
     * @param other The rule with which to perform a logical or
     * @return A new rule that is the disjunction of this rule and the other rule.
     */
    static Rule or(Rule first, Rule other)
    {
        Objects.requireNonNull(first);
        Objects.requireNonNull(other);
        return new Rule()
        {
            @Override public String toString()
            {
                return String.format("(%s + %s)", first.toString(), other.toString());
            }

            @Override public boolean test(Task task)
            {
                return first.test(task) || other.test(task);
            }
        };
    }

    /**
     * By default, performs a logical negation.
     *
     * @return The inverse of this rule.
     */
    static Rule negate(Rule first)
    {
        return new Rule()
        {
            @Override public String toString()
            {
                return String.format("!%s", first.toString());
            }

            @Override public boolean test(Task task)
            {
                return !first.test(task);
            }
        };
    }
}
