package com.natebeckemeyer.projects.schedulrgui.core;

import com.natebeckemeyer.projects.schedulrgui.task.Rule;
import com.natebeckemeyer.projects.schedulrgui.task.Task;

import javax.naming.OperationNotSupportedException;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-19.
 */
public final class DynamicRuleParser
{
    /**
     * The compiler that will be used to analyze by
     */
    private static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    /**
     * Disables instantiation, as the only uses of this class should be static API calls
     */
    private DynamicRuleParser()
    {
    }

    /**
     * Given the name and function of a rule, writes the rule to a file, compiles the file, and then loads the rule from
     * the file into memory. Additionally, this function places the file into Schedulr.
     * This method was created so that users could define their own rules.
     *
     * @param name     The name of the rule. At any given point, each existing rule must have a unique name.
     * @param function The function of the rule; the test that task needs to pass.
     * @param imports  The imports that the test will need. Only the classpaths are needed; i.e., "java.io.File"
     * @return The new rule.
     */
    public static Rule compileAndLoadRule(String name, String function, List<String> imports)
    {
        String className = String.format("%s%s", name.substring(0, 1).toUpperCase(), name.substring(1));
        String fileName = String.format("%s%s%s.java", Config.userRulesLocation, Config.separator, className);

        try (Scanner readBlankTask = new Scanner(
                new File(String.format("%s%s%s", Config.corePrefix, Config.separator, "BlankTask.template")));
             FileWriter writeNewTask = new FileWriter(fileName))
        {
            // Prepare the imports
            StringBuilder importLines = new StringBuilder();
            imports.add("com.natebeckemeyer.projects.schedulrgui.task.Rule");
            imports.add("com.natebeckemeyer.projects.schedulrgui.task.Task");
            for (String item : imports)
                importLines.append(String.format("import %s;%n", item));

            StringBuilder replacement = new StringBuilder();

            while (readBlankTask.hasNextLine())
            {
                String line = readBlankTask.nextLine();
                line = line.replaceAll(Pattern.quote("$$$IMPORTS$$$"), importLines.toString());
                line = line.replaceAll(Pattern.quote("$$$CLASSNAME$$$"), className);
                line = line.replaceAll(Pattern.quote("$$$NAME$$$"), name);
                line = line.replaceAll(Pattern.quote("$$$TEST$$$"), function);
                replacement.append(String.format("%s%n", line));
            }
            writeNewTask.write(replacement.toString());
            writeNewTask.close();

            int compilationResult = compiler.run(null, null, null,
                    fileName);
            if (compilationResult != 0)
                throw new IllegalAccessException(String.format("Could not compile user-defined rule named %s", name));

            URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(Config.userRulesLocation)
                    .toURI().toURL()});
            Class<?> ruleClass = classLoader.loadClass(className);
            Object instantiation = ruleClass.newInstance();

            if (instantiation instanceof Rule)
            {
                Rule userRule = (Rule) instantiation;
                Schedulr.addRule(userRule);
                return userRule;
            } else
                throw new OperationNotSupportedException("Attempted to load class that does not extend Rule.");

        } catch (IOException | IllegalAccessException | ClassNotFoundException | InstantiationException |
                OperationNotSupportedException e)
        {
            e.printStackTrace();
            return null;
        }
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
                    return "Unnamed";
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
