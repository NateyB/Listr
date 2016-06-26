package com.natebeckemeyer.projects.schedulrgui.core;

import com.natebeckemeyer.projects.schedulrgui.task.*;
import com.sun.istack.internal.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 * <p>
 * This class functions as the engine of the to-do app. It handles rules, their ruleMapping, and controls which tasks
 * will be displayed, etc.
 * <p>
 * A static initialization block places all of the rules included in the task package into the ruleMapping.
 */
public final class Schedulr
{
    /**
     * Disables instantiation (as this class was coded as a static reference class, not designed to be inherited from).
     */
    private Schedulr()
    {
    }

    /**
     * The mapping from the display name to the object of the rules (so that only one instance of each is made).
     */
    private static final Map<String, Rule> ruleMapping = new HashMap<>();

    // Place the rules into the ruleMapping
    // This static initializer allows me to hardcode in the package searching and initialization without storing
    // all of the options.
    static
    {
        List<Rule> rules = Arrays.asList(new Today(), new Week(), new Completed());
        rules.forEach(rule -> ruleMapping.put(rule.toString(), rule));
    }

    /**
     * The tasks that Schedulr is currently handling.
     */
    private static PriorityQueue<Task> tasks = new PriorityQueue<>();

    /**
     * Returns the rule corresponding to {@code name}.
     *
     * @param name The name of the rule to get (note that this should match exactly the value returned by the rule's
     *             {@code toString} method.)
     * @return The rule corresponding to {@code name} (or null, if such a rule does not exist).
     */
    @Nullable
    public static Rule getRule(String name)
    {
        Rule returned = ruleMapping.get(name);

        if (returned == null)
            return Tag.getTag(name);

        return returned;
    }

    static void addRule(Rule rule)
    {
        ruleMapping.put(rule.toString(), rule);
    }

    /**
     * @return The mapping of all rules inside of Schedulr.
     */
    public static Map<String, Rule> getRules()
    {
        return ruleMapping;
    }

    /**
     * Returns the list of tasks that pass a certain rule. This aim is acheived by creating a stream, filtering it,
     * and collecting the results into a list.
     *
     * @param toCompare The rule to test tasks against
     * @return The list of tasks that match the rule.
     */
    public static List<Task> getTasksMatchingRule(Rule toCompare)
    {
        return tasks.stream().filter(toCompare).collect(Collectors.toList());
    }

    public static List<Task> getAllTasks()
    {
        return tasks.stream().collect(Collectors.toList());
    }

    /**
     * Sets the list of Schedulr's {@code tasks} to be the {@code taskList} provided here.
     *
     * @param taskList The list of tasks to assign to be handled.
     */
    public static void setTasks(Collection<Task> taskList)
    {
        tasks = new PriorityQueue<>(taskList);
    }

    /**
     * Inserts the task into Schedulr's {@code tasks}.
     *
     * @param toAdd The task to add.
     * @return True if {@code tasks} is changed as a result of this call.
     */
    public static boolean addTask(Task toAdd)
    {
        return tasks.add(toAdd);
    }

    /**
     * Inserts the collection of tasks provided, {@code taskList}, into the {@code tasks} that Schedulr is handling.
     *
     * @param taskList The taskList to add to Schedulr's {@code tasks}.
     * @return True if {@code tasks} is changed as a result of this call.
     */
    public static boolean addTasks(Collection<Task> taskList)
    {
        return tasks.addAll(taskList);
    }

    /**
     * Removes the task {@code toRemove} from the Schedulr's {@code tasks}.
     *
     * @param toRemove The task to remove from Schedulr's {@code tasks}.
     * @return True if the task was removed.
     */
    public static boolean removeTask(Task toRemove)
    {
        return tasks.remove(toRemove);
    }

    /**
     * Removes the tasks {@code toRemove} from the Schedulr's {@code tasks}.
     *
     * @param toRemove The tasks to remove from Schedulr's {@code tasks}.
     * @return True if {@code tasks} was changed as a result of this call.
     */
    public static boolean removeTasks(Collection<Task> toRemove)
    {
        return tasks.removeAll(toRemove);
    }

}
