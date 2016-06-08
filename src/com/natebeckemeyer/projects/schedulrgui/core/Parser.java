package com.natebeckemeyer.projects.schedulrgui.core;

import com.natebeckemeyer.projects.schedulrgui.task.OnCompletion;
import com.natebeckemeyer.projects.schedulrgui.task.Rule;
import com.natebeckemeyer.projects.schedulrgui.task.Tag;
import com.natebeckemeyer.projects.schedulrgui.task.Task;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-14.
 */
public final class Parser
{

    /**
     * The package prefix for where the OnCompletion items are located.
     */
    private static final String prefix = "com.natebeckemeyer.projects.schedulrgui.task";

    // Disable instantiation
    private Parser()
    {
    }

    /**
     * Given a file, reads in tasks.
     *
     * @param sourceFile The file containing the tasks.
     * @return The priority queue containing the loaded tasks, or null if the file does not exist. Also null if the
     * OnCompletion rule specified does not exist.
     */
    public static PriorityQueue<Task> readTasksFromFile(File sourceFile)
    {
        PriorityQueue<Task> tasks = new PriorityQueue<>();

        try
        {
            Scanner console = new Scanner(sourceFile);
            console.useDelimiter("[\\|\n]");

            while (console.hasNextLine())
            {
                // Read in the numbers:
                int year = console.nextInt();
                int month = console.nextInt();
                int day = console.nextInt();
                LinkedList<Tag> labels = new LinkedList<>();

                Scanner labelParser = new Scanner(console.next());

                while (labelParser.hasNext())
                {
                    labels.add(Tag.getTag(labelParser.next()));
                }
                labelParser.close();

                OnCompletion onCompletion;

                try
                {
                    onCompletion = ((OnCompletion) Class.forName(String.format("%s.%s", prefix, console.next()))
                            .newInstance());
                    onCompletion.loadFromString(console.next());
                } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                    System.exit(12);
                    return null;
                }

                String name = console.next();
                Task toContribute = new Task(name, new GregorianCalendar(year, month - 1, day), onCompletion);
                toContribute.addTags(labels);
                tasks.add(toContribute);
            }

            console.close();

        } catch (FileNotFoundException e)
        {
            return null;
        }

        return tasks;

    }

    /**
     * Given a filepath, reads in task from the corresponding file.
     *
     * @param filepath The filepath of the file where the tasks are located.
     * @return The priority queue containing the loaded tasks, or null if the file does not exist.
     */
    @Nullable
    public static PriorityQueue<Task> readTasksFromFile(String filepath)
    {
        File source;
        try
        {
            source = new File(filepath);
            Objects.requireNonNull(source);
        } catch (NullPointerException e)
        {
            return null;
        }

        return readTasksFromFile(source);
    }

    /**
     * Returns the index at which the first substring ends
     *
     * @param sup
     * @param sub
     * @return
     */
    private static int getEndIndex(String sup, String sub)
    {
        return sup.indexOf(sub) + sub.length();
    }

    /**
     * Creates a rule based on the string input.
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

        return processInput(input, subRules);
    }

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
            } else if (val.startsWith("+"))
            {
                return current.or(processInput(input.substring(getEndIndex(input, val)), subRules));
            } else if (val.startsWith("&"))
            {
                return current.and(processInput(input.substring(getEndIndex(input, val)), subRules));
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
