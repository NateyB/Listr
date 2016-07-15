package com.natebeckemeyer.projects.schedulrgui.core;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-20.
 * <p>
 * This interface is intended to be extended exclusively by {@link Enum} types. It's used in text-to-Rule parsing.
 */
public interface RuleOperation
{
    /**
     * Performs an operation that creates a rule from two old ones. And, or, xor--these are some simple possibilities.
     *
     * @param a
     * @param b
     * @return The combination of Rule a and Rule b.
     */
    Rule performOperation(Rule a, Rule b);

    /**
     * Return an operation based off of its name, or null if there is no operation matching that name.
     *
     * @param name The name of the operation
     * @return The operation corresponding to {@code name}, or null if none exists
     */
    RuleOperation lookup(String name);
}
