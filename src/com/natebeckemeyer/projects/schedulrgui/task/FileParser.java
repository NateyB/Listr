package com.natebeckemeyer.projects.schedulrgui.task;

import com.natebeckemeyer.projects.schedulrgui.core.Config;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-14.
 */
public final class FileParser
{
    /**
     * This constructor is private to disable instantiation.
     */
    private FileParser()
    {
    }

    /**
     * Given a path to a file, writes the tasks provided to that file.
     *
     * @param sourceFile file to which this class will write these tasks
     * @param tasks      the tasks to write to that file
     * @throws IOException if the file is not found or could not be written to
     */
    public static void saveTasksToFile(File sourceFile, Collection<Task> tasks) throws IOException
    {
        FileWriter writer = new FileWriter(sourceFile);

        String output = "";
        for (Task task : tasks)
        {
            String labels = "";
            for (Tag tag : task.getTags())
                labels = String.format("%s%s ", labels, tag.toString());

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

    /**
     * Given a path to a file, writes the tasks provided to that file.
     *
     * @param sourceFile path to file
     * @param tasks      the tasks to write to that file
     * @throws IOException if the file is not found or could not be written to
     */
    public static void saveTasksToFile(String sourceFile, Collection<Task> tasks) throws IOException
    {
        saveTasksToFile(new File(sourceFile), tasks);
    }

    /**
     * Given a file, reads in tasks.
     *
     * @param sourceFile The file containing the tasks.
     * @return The priority queue containing the loaded tasks, or null if the file does not exist. Also null if the
     * CompletionBehavior rule specified does not exist.
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

                CompletionBehavior completionBehavior;

                try
                {
                    completionBehavior = ((CompletionBehavior) Class.forName(
                            String.format("%s.%s", Config.taskPackagePrefix, console.next()))
                            .newInstance());
                    completionBehavior.loadFromString(console.next());
                } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                    System.exit(12);
                    return null;
                }

                String name = console.next();
                Task toContribute = new Task(name, new GregorianCalendar(year, month, day), completionBehavior);
                toContribute.addTags(labels);
                if (completed.startsWith("y"))
                    toContribute.completed = true;
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

}
