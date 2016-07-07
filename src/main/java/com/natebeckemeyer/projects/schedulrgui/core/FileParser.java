package com.natebeckemeyer.projects.schedulrgui.core;

import com.natebeckemeyer.projects.schedulrgui.reference.ProjectPaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

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
    public static void saveTasksToFile(File sourceFile, Collection<AbstractTask> tasks) throws IOException
    {
        FileWriter writer = new FileWriter(sourceFile);

        StringBuilder output = new StringBuilder();
        for (AbstractTask task : tasks)
        {
            output.append(task.getClass().getSimpleName());
            output.append("|");
            output.append(task.serialize());
            output.append(ProjectPaths.lineSeparator);
        }

        writer.write(output.toString().trim());
        writer.close();
    }

    /**
     * Given a path to a file, writes the tasks provided to that file.
     *
     * @param sourceFile path to file
     * @param tasks      the tasks to write to that file
     * @throws IOException if the file is not found or could not be written to
     */
    public static void saveTasksToFile(String sourceFile, Collection<AbstractTask> tasks) throws IOException
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
    public static PriorityQueue<AbstractTask> readTasksFromFile(File sourceFile)
    {
        PriorityQueue<AbstractTask> tasks = new PriorityQueue<>();

        try (Scanner parser = new Scanner(sourceFile))
        {

            while (parser.hasNextLine())
            {
                String nextLine = parser.nextLine();

                if (nextLine.isEmpty())
                    return tasks;

                Scanner console = new Scanner(nextLine);
                console.useDelimiter(Pattern.quote("|"));

                String taskType = console.next();
                Class<? extends AbstractTask> taskClass = Schedulr.getTaskOfType(taskType);
                AbstractTask task;

                try
                {
                    Constructor<? extends AbstractTask> taskConstructor = taskClass.getConstructor(String.class,
                            Calendar.class, CompletionBehavior.class);
                    task = taskConstructor.newInstance(null, null, null);

                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                        InvocationTargetException e)
                {
                    e.printStackTrace();
                    return null;
                }

                task.loadFromSerialization(nextLine.substring(taskType.length() + 1));
                tasks.add(task);

                console.close();
            }

        } catch (FileNotFoundException e)
        {
            return null;
        }

        return tasks;

    }

    /**
     * Given a filepath, reads in implementations from the corresponding file.
     *
     * @param filepath The filepath of the file where the tasks are located.
     * @return The priority queue containing the loaded tasks, or null if the file does not exist.
     */
    public static PriorityQueue<AbstractTask> readTasksFromFile(String filepath)
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
