package com.natebeckemeyer.projects.schedulrgui.core;

import com.natebeckemeyer.projects.schedulrgui.task.Rule;
import com.natebeckemeyer.projects.schedulrgui.task.Task;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-19.
 */
public final class DynamicRuleParser
{
    /**
     * Disables instantiation, as the only uses of this class should be static API calls
     */
    private DynamicRuleParser()
    {
    }

    /**
     * Returns the index in the superstring at which the first substring ends
     *
     * @param sup The superstring
     * @param sub The substring
     * @return The index in the superstring at which the first substring ends
     */
    private static int getEndIndex(String sup, String sub)
    {
        return sup.indexOf(sub) + sub.length();
    }

    /**
     * Creates a rule based on the string input. + indicates set union, - indicates set difference, & indicates set
     * intersection, ! indicates set inverse, and $ indicates symmetric difference (XOR).
     * The order of operations is simply right-associative; use parentheses to change the order of evaluation.
     *
     * @param input Input to parse.
     * @return The filtering rule.
     */
    public static Rule processInput(String input)
    {
        ArrayList<String> subRules = new ArrayList<>();

        input = input.replaceAll(" - ", " & !");

        while (input.matches(".*\\(.*\\).*"))
        {
            int end = input.indexOf(")") + 1;
            int begin = input.substring(0, end).lastIndexOf("(");

            subRules.add(input.substring(begin + 1, end - 1));
            input = String.format("%s%d%s", input.substring(0, begin), subRules.size() - 1, input.substring(end));
        }

        Rule result = processInput(input, subRules);
        if (result == null)
            return new Rule()
            {
                @Override public String getName()
                {
                    return "Default (none)";
                }

                @Override public boolean test(Task task)
                {
                    return false;
                }
            };
        return result;
    }

    private static RuleOperation checkForOperation(String start)
    {
        return checkForOperation(start, BasicRuleOperation.class);
    }

    private static <T extends Enum<T> & RuleOperation> RuleOperation checkForOperation(String start, Class<T> ruleset)
    {
        return ruleset.getEnumConstants().length > 0 ? ruleset.getEnumConstants()[0].lookup(start) : null;
    }

    /**
     * Performs the actual processing of the text describing the rule.
     *
     * @param input    The text (wherein nested parentheses have been replaced with indexes of rules in subRules)
     * @param subRules The array containing the rules inside of parentheses
     * @return The filtering rule.
     */
    private static Rule processInput(String input, ArrayList<String> subRules)
    {
        Scanner parser = new Scanner(input);
        parser.useDelimiter(" ");

        Rule current = null;
        while (parser.hasNext())
        {
            String val = parser.next();

            if (val.startsWith("!"))
            {
                current = processInput(val.substring(1), subRules).negate();
                input = input.substring(getEndIndex(input, val));
            } else if (checkForOperation(val.trim()) != null)
            {
                return checkForOperation(val.trim()).performOperation(current,
                        processInput(input.substring(getEndIndex(input, val)), subRules));
            } else if (val.matches("\\d+"))
            {
                current = processInput(subRules.get(Integer.parseInt(val)), subRules);
                input = input.substring(getEndIndex(input, val));
            } else
            {
                current = Schedulr.getRule(val);
                input = input.substring(getEndIndex(input, val));
            }
        }

        return current;
    }
}
