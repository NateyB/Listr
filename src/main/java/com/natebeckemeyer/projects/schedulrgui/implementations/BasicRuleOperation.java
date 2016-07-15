package com.natebeckemeyer.projects.schedulrgui.implementations;

import com.natebeckemeyer.projects.schedulrgui.core.Rule;
import com.natebeckemeyer.projects.schedulrgui.core.RuleOperation;

import java.util.HashMap;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-20.
 */
public enum BasicRuleOperation implements RuleOperation
{
    /**
     * Performs a logical "and" on two {@link Rule Rules}.
     */
    AND("&")
            {
                @Override public Rule performOperation(Rule a, Rule b)
                {
                    return Rule.and(a, b);
                }
            },
    /**
     * Performs a logical "or" on two {@link Rule Rules}.
     */
    OR("+")
            {
                @Override public Rule performOperation(Rule a, Rule b)
                {

                    return Rule.or(a, b);
                }
            },

    /**
     * Performs a logical subtraction on two {@link Rule Rules} — that is, '{@code rule a & !(rule b)}'.
     */
    DIFFERENCE("-")
            {
                @Override public Rule performOperation(Rule a, Rule b)
                {
                    return Rule.and(a, Rule.negate(b));
                }
            },

    /**
     * Performs a symmetric difference operation (XOR) on two {@link Rule Rules} — that is, '{@code (rule a + rule b)
     * - (rule a & rule b)}'.
     */
    SYMMETRICDIFFERENCE("$")
            {
                @Override public Rule performOperation(Rule a, Rule b)
                {
                    return Rule.and(Rule.or(a, b), Rule.negate(Rule.and(a, b)));
                }
            },;

    private static final HashMap<String, RuleOperation> mapping;

    static
    {
        mapping = new HashMap<>();
        for (RuleOperation operation : BasicRuleOperation.values())
            mapping.put(operation.toString(), operation);
    }


    private final String identifier;

    @Override
    public String toString()
    {
        return identifier;
    }

    BasicRuleOperation(String name)
    {
        identifier = name;
    }

    @Override
    public RuleOperation lookup(String name)
    {
        return mapping.get(name);
    }
}
