package com.natebeckemeyer.projects.listrgui.core;

import com.natebeckemeyer.projects.listrgui.reference.Defaults;
import com.natebeckemeyer.projects.listrgui.reference.ProjectPaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created for Listr by @author Nate Beckemeyer on 2016-05-14.
 * <p>
 * This class contains static methods to load and save tasks from and to a file.
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
     * The File from which the current tasks are loaded. This is the file to which tasks will be auto-saved, if the
     * flag {@link Defaults#autoSaveAll} is {@code true}.
     */
    private static File currentFile = null;

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
     * If {@link Defaults#getAutoSaveAll()} returns {@code true}, then saves tasks to the file from which the tasks
     * were loaded, or, if that file does not exist, to the file specified by the
     * path {@link Defaults#getDefaultTaskFile()}.
     *
     * @param tasks The tasks to save.
     * @return {@code true} if the tasks were saved successfully; {@code false} otherwise.
     */
    static boolean defaultSave(Collection<AbstractTask> tasks)
    {
        if (Defaults.getAutoSaveAll())
        {
            try
            {
                saveTasksToFile(currentFile != null ? currentFile : new File(Defaults.getDefaultTaskFile()), tasks);
                return true;
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * If {@link Defaults#getAutoLoadDefault()} returns {@code true}, then loads tasks automatically on startup from
     * the file specified by the path {@link Defaults#getDefaultTaskFile()}.
     *
     * @return {@code true} if the tasks were loaded successfully, {@code false} otherwise.
     */
    static boolean defaultLoad()
    {
        if (Defaults.getAutoLoadDefault())
        {
            LinkedList<AbstractTask> tasks = readTasksFromFile(Defaults.getDefaultTaskFile());
            if (tasks != null)
            {
                Listr.addTasks(tasks);
                return true;
            }
        }
        return false;
    }

    /**
     * Given a file, reads in tasks.
     *
     * @param sourceFile The file containing the tasks.
     * @return The list containing the loaded tasks, or null if the file does not exist.
     */
    public static LinkedList<AbstractTask> readTasksFromFile(File sourceFile)
    {
        currentFile = sourceFile;
        LinkedList<AbstractTask> tasks = new LinkedList<>();

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
                Class<? extends AbstractTask> taskClass = Listr.getTaskOfType(taskType);
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
    public static LinkedList<AbstractTask> readTasksFromFile(String filepath)
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
