package com.natebeckemeyer.projects.schedulrgui.core;

import com.natebeckemeyer.projects.schedulrgui.task.Rule;
import com.natebeckemeyer.projects.schedulrgui.task.Task;
import com.natebeckemeyer.projects.schedulrgui.task.Today;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 * <p>
 * This class functions as the engine of the to-do app.
 */
public final class Schedulr
{
    // Disables instantiation
    private Schedulr()
    {
    }

    private static Map<String, Rule> mapping = new HashMap<>();

    private static PriorityQueue<Task> tasks = new PriorityQueue<>();

    static
    {
        mapping.put("Today", new Today());
    }

    public static Rule getRule(String name)
    {
        return mapping.get(name);
    }

    public static List<Task> getTasksMatchingRule(Rule toCompare)
    {
        return tasks.stream().filter(toCompare).collect(Collectors.toList());
    }

    public static void setTasks(Collection<Task> taskList)
    {
        tasks = new PriorityQueue<>(taskList);
    }

    public static void addTask(Task toAdd)
    {
        tasks.add(toAdd);
    }

    public static void addTasks(Collection<Task> taskList)
    {
        tasks.addAll(taskList);
    }

    public static boolean removeTask(Task toRemove)
    {
        return tasks.remove(toRemove);
    }

    public static boolean removeTasks(Collection<Task> toRemove)
    {
        return tasks.removeAll(toRemove);
    }

}
