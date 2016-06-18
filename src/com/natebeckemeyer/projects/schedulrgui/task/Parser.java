package com.natebeckemeyer.projects.schedulrgui.task;

import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
     * Given a file, save the current tasks to that file.
     */
    public static void saveTasksToFile(File sourceFile, Collection<Task> tasks) throws IOException
    {
        FileWriter writer = new FileWriter(sourceFile);

        String output = "";
        for (Task task : tasks)
        {
            String labels = "";
            for (Tag tag : task.getTags())
                labels = String.format("%s%s ", labels, tag.getName());

            String onCompleteOut = task.getOnComplete().convertToString();
            if (onCompleteOut.isEmpty())
                onCompleteOut = " ";

            output = String.format("%s%4d|%02d|%02d|%s|%s|%s|%s|%s%n", output,
                    task.getDueYear(), task.getDueMonth(), task.getDueDay(),
                    task.isCompleted() ? "y" : "n", labels.trim(),
                    task.getOnComplete().getClass().getSimpleName(),
                    onCompleteOut, task.getName());
        }

        writer.write(output.trim());
        writer.close();
    }

    public static void saveTasksToFile(String sourceFile, Collection<Task> tasks) throws IOException
    {
        saveTasksToFile(new File(sourceFile), tasks);
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
                String completed = console.next();
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
                Task toContribute = new Task(name, new GregorianCalendar(year, month, day), onCompletion);
                toContribute.addTags(labels);
                if (completed.startsWith("y"))
                    toContribute.setCompleted(true);
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
     * Creates a rule based on the string input. + indicates set union, - indicates set difference, & indicates set
     * intersection, and ! indicates set inverse. TODO I intend to add an XOR (symmetric difference) symbol ($) in a
     * future update.
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
