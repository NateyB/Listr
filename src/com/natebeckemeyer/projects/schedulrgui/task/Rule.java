package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public abstract class Rule implements Predicate<Task>
{
    /**
     * @return The name of the rule; this is both the name that will be displayed and used to identify the rule in the
     * mapping of all rules
     */
    public abstract String getName();

    /**
     * By default, performs a logical "and".
     *
     * @param other The rule with which to perform a logical and
     * @return A new rule that is the conjunction of this rule and the other rule.
     */
    public final Rule and(Rule other)
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
                return String.format("Composition: %s", getName());
            }
        };
    }

    /**
     * By default, performs a logical "or".
     *
     * @param other The rule with which to perform a logical or
     * @return A new rule that is the disjunction of this rule and the other rule.
     */
    public final Rule or(Rule other)
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
                return String.format("Composition: %s", getName());
            }
        };
    }

    /**
     * By default, performs a logical negation.
     *
     * @return The inverse of this rule.
     */
    public final Rule negate()
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
                return String.format("Composition: %s", getName());
            }
        };
    }
}
