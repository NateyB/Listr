package com.natebeckemeyer.projects.schedulrgui.core;

import com.natebeckemeyer.projects.schedulrgui.task.Rule;

import java.util.HashMap;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-20.
 */
public enum BasicRuleOperation implements RuleOperation
{
    AND("&")
            {
                @Override public Rule performOperation(Rule a, Rule b)
                {
                    return a.and(b);
                }
            },
    XOR("$")
            {
                @Override public Rule performOperation(Rule a, Rule b)
                {
                    return a.or(b).and((a.and(b)).negate());
                }
            },
    OR("+")
            {
                @Override public Rule performOperation(Rule a, Rule b)
                {
                    return a.or(b);
                }
            };

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
