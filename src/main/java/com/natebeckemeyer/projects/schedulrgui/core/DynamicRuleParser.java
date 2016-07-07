package com.natebeckemeyer.projects.schedulrgui.core;

import com.natebeckemeyer.projects.schedulrgui.implementations.BasicRuleOperation;
import com.natebeckemeyer.projects.schedulrgui.implementations.Rule;
import com.natebeckemeyer.projects.schedulrgui.implementations.SimpleTask;
import com.natebeckemeyer.projects.schedulrgui.reference.ProjectPaths;

import javax.naming.OperationNotSupportedException;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
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
     * Given the name, function, and code of a behavior, writes the behavior to a file and compiles the file.
     * This method was created so that users could define their own rules.
     *
     * @param name             The name of the rule. At any given point, each existing rule must have a unique name.
     * @param implementationOf What (extra interfaces) this behavior implements.
     * @param extensionOf      What class this behavior extends (if any).
     * @param userCode         The code written by the user; in the case of a rule, only the test. In the case of a
     *                         completion behavior, the whole class except the toString method.
     * @param imports          The imports that the code will need. Only the classpaths are needed; i.e., "java.io.File"
     * @param type             The type of behavior.
     */
    public static void compileBehavior(String name, Collection<String> implementationOf, String extensionOf,
                                       String userCode, Collection<String> imports, Schedulr.Behavior type)
    {
        String className = name.substring(0, 1).toUpperCase() + name.substring(1);
        String fileName;
        switch (type)
        {
            case RULE:
                fileName = ProjectPaths.userRulesFile;
                break;

            case COMPLETIONBEHAVIOR:
                fileName = ProjectPaths.userCompletionsFile;
                break;

            default:
                System.err.printf("Could not locate appropriate directory for " + type + ". Using rule instead.");
                fileName = ProjectPaths.userRulesFile;
                break;
        }

        new File(fileName).mkdirs();

        fileName = fileName + ProjectPaths.fileSeparator + className + ".java";

        File fileLocation = ProjectPaths.getFile(fileName);
        File templateLocation = ProjectPaths.getFile(
                ProjectPaths.templateDirectory + ProjectPaths.fileSeparator + type + ".template");

        try (Scanner readTemplate = new Scanner(templateLocation);
             FileWriter writeNewTask = new FileWriter(fileLocation.getCanonicalFile()))
        {
            // Prepare the imports
            imports.add(ProjectPaths.taskPackagePrefix + ProjectPaths.packageSeparator + type);
            imports.add(ProjectPaths.taskPackagePrefix + ProjectPaths.packageSeparator + SimpleTask.class.getSimpleName());
            StringBuilder importLines = new StringBuilder();
            for (String item : imports)
            {
                importLines.append("import ");
                importLines.append(item);
                importLines.append(";");
                importLines.append(ProjectPaths.lineSeparator);
            }

            // Prepare the implementations
            StringBuilder implementations = new StringBuilder();
            for (String implementation : implementationOf)
            {
                implementations.append(", ");
                implementations.append(implementation);
            }

            // Prepare the extension
            extensionOf = (extensionOf == null) ? "" : extensionOf;
            if (!extensionOf.isEmpty())
                extensionOf = "extends " + extensionOf;

            // Do the actual parsing
            StringBuilder replacement = new StringBuilder();
            while (readTemplate.hasNextLine())
            {
                String line = readTemplate.nextLine();
                line = line.replaceAll(Pattern.quote("$$$IMPORTS$$$"), importLines.toString());
                line = line.replaceAll(Pattern.quote("$$$CLASSNAME$$$"), className);
                line = line.replaceAll(Pattern.quote("$$$EXTENDS$$$"), extensionOf);
                line = line.replaceAll(Pattern.quote("$$$IMPLEMENTATIONS$$$"), implementations.toString());

                line = line.replaceAll(Pattern.quote("$$$NAME$$$"), name);
                line = line.replaceAll(Pattern.quote("$$$CODE$$$"), userCode);


                replacement.append(line);
                replacement.append(ProjectPaths.lineSeparator);
            }

            // Write the file
            writeNewTask.write(replacement.toString());
            writeNewTask.close();

            int compilationResult = compiler.run(null, null, null, fileLocation.getCanonicalPath());
            if (compilationResult != 0) // Unsuccessful compilation
                throw new IllegalAccessException("Could not compile user-defined " + type + " named " + name);

        } catch (IOException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Compiles and loads a rule according to the compileBehavior and loadRule methods.
     *
     * @return The new rule.
     */
    public static Rule compileAndLoadRule(String name, Collection<String> implementationOf, String extensionOf,
                                          String function, Collection<String> imports)
    {
        compileBehavior(name, implementationOf, extensionOf, function, imports, Schedulr.Behavior.RULE);
        return loadRule(name);
    }

    /**
     * Compiles and loads a completion behavior according to the compileBehavior and loadCompletionBehavior methods.
     *
     * @return The new rule.
     */
    public static CompletionBehavior compileAndLoadCompletionBehavior(String name, Collection<String> implementationOf,
                                                                      String extensionOf, String function,
                                                                      Collection<String> imports)
    {
        compileBehavior(name, implementationOf, extensionOf, function, imports, Schedulr.Behavior.COMPLETIONBEHAVIOR);
        return loadCompletionBehavior(name);
    }

    /**
     * Loads the rule of Rule "className" from the user-defined resources. It also adds this rule into Schedulr.
     *
     * @param className The name of the Rule to instantiate
     * @return an object of Rule of name {@code className}
     */
    public static Rule loadRule(String className)
    {
        try (URLClassLoader classLoader = new URLClassLoader(
                new URL[]{ProjectPaths.getFile(ProjectPaths.userRulesFile).toURI().toURL()}))
        {
            String usedName = className.substring(0, 1).toUpperCase() + className.substring(1);
            Class<?> ruleClass = classLoader.loadClass(usedName);
            Object instantiation = ruleClass.newInstance();

            if (instantiation instanceof Rule)
            {
                Rule userRule = (Rule) instantiation;
                Schedulr.addRule(userRule);
                return userRule;
            } else
                throw new OperationNotSupportedException("Attempted to load class that does not implement Rule.");

        } catch (IOException | OperationNotSupportedException | ClassNotFoundException |
                InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads the behavior of CompletionBehavior {@code className} from the user-defined resources. Adds the class
     * into the
     * corresponding mapping in Schedulr.
     *
     * @param className The name of the CompletionBehavior to instantiate
     * @return an object of CompletionBehavior of name {@code className}
     */
    public static CompletionBehavior loadCompletionBehavior(String className)
    {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(ProjectPaths.userCompletionsFile)
                .toURI().toURL()}))
        {
            Class<?> completionBehavior = classLoader.loadClass(className);
            Object instantiation = completionBehavior.newInstance();

            if (instantiation instanceof CompletionBehavior)
            {
                CompletionBehavior result = (CompletionBehavior) instantiation;
                Schedulr.addCompletionBehavior(result.getClass());
                return result;
            } else
                throw new OperationNotSupportedException(
                        "Attempted to load class that does not implement CompletionBehavior.");

        } catch (IOException | OperationNotSupportedException | ClassNotFoundException | InstantiationException |
                IllegalAccessException e)
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

        while (input.matches(".*\\(.*\\).*"))
        {
            int end = input.indexOf(")") + 1;
            int begin = input.substring(0, end).lastIndexOf("(");

            subRules.add(input.substring(begin + 1, end - 1));
            input = String.format("%s%d%s", input.substring(0, begin), subRules.size() - 1, input.substring(end));
        }

        Rule result = processInput(input, subRules);
        if (result == null)
            return (task) -> false;

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
                current = Rule.negate(processInput(val.substring(1), subRules));
                input = input.substring(getEndIndex(input, val));
            } else
            {
                RuleOperation operation = checkForOperation(val.trim());
                if (operation != null)
                {
                    return operation.performOperation(current,
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
        }

        return current;
    }
}
