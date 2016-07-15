package com.natebeckemeyer.projects.schedulrgui.core;

import com.natebeckemeyer.projects.schedulrgui.implementations.*;
import com.natebeckemeyer.projects.schedulrgui.reference.Defaults;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 * <p>
 * This class functions as the engine of the to-do app. It handles rules, their ruleMapping, and controls which tasks
 * will be displayed, etc.
 * <p>
 * A static initialization block places all of the behaviors included in the implementations package into the
 * ruleMapping.
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
     * The mapping from the display name to the objects of the rules (so that only one instance of each is made).
     */
    private static final Map<String, Rule> ruleMapping = new HashMap<>();

    /**
     * The mapping from the display name to the classes of the rules (so that a new instance can be instantiated).
     */
    private static final Map<String, Class<? extends CompletionBehavior>> completionMapping = new HashMap<>();

    /**
     * The mapping from the task type names to the classes of the types (so that a new instance can be instantiated).
     */
    private static final Map<String, Class<? extends AbstractTask>> taskTypeMapping = new HashMap<>();

    /**
     * The tasks that Schedulr is currently handling.
     */
    private static LinkedList<AbstractTask> tasks = new LinkedList<>();

    // Place the rules into the ruleMapping
    // This static initializer allows me to hardcode in the package searching and initialization without storing
    // all of the options.
    static
    {
        List<Rule> rules = Arrays.asList(new Today(), new Week(), new Completed());
        setRules(rules);

        List<Class<? extends CompletionBehavior>> completions = Arrays.asList(SimpleCompleted.class,
                VerboseCompleted.class);
        setCompletionBehaviors(completions);

        List<Class<? extends AbstractTask>> tasks = Arrays.asList(SimpleTask.class, DatelessTask.class);
        for (Class<? extends AbstractTask> task : tasks)
            taskTypeMapping.put(task.getSimpleName(), task);

        FileParser.defaultLoad();
    }

    /**
     * Given the simple name of a subclass of {@link AbstractTask}, returns the actual corresponding class value.
     *
     * @param name The class name of the {@link AbstractTask} to return.
     * @return The corresponding class value, as an extension of {@link AbstractTask}.
     */
    static Class<? extends AbstractTask> getTaskOfType(String name)
    {
        return taskTypeMapping.get(name);
    }

    /**
     * Returns the rule corresponding to {@code name}.
     *
     * @param name The name of the rule to get (note that this should match exactly the value returned by the rule's
     *             {@code toString} method.)
     * @return The rule corresponding to {@code name} (or null, if such a rule does not exist).
     */
    public static Rule getRule(String name)
    {
        Rule returned = ruleMapping.get(name);

        if (returned == null)
            return Tag.getTag(name);

        return returned;
    }

    /**
     * Gets the class of the completion behavior corresponding to {@code name}. If the class does not exist or cannot be
     * instantiated, then an error message will be written and the default value (see
     * {@link Defaults#getDefaultCompletionBehavior()}) used instead.
     *
     * @param name The name of the completion behavior to get.
     * @return An instance of the corresponding completion behavior.
     */
    public static CompletionBehavior getCompletionBehavior(String name)
    {
        Class<? extends CompletionBehavior> behavior = completionMapping.get(name);

        try
        {
            return behavior.newInstance();
        } catch (NullPointerException | InstantiationException | IllegalAccessException e)
        {
            System.err.printf("Could not locate completion behavior %s, or it is null. Using default instead.%n", name);
            return Defaults.getDefaultCompletionBehavior();
        }
    }

    /**
     * @return A set of strings of names of completion behavior classes which are known to {@link Schedulr}.
     */
    public static Set<String> getCompletionBehaviorNames()
    {
        return completionMapping.keySet();
    }

    /**
     * Adds a rule to the mapping from string names to rules in Schedulr.
     *
     * @param rule The rule to add into the mapping.
     */
    static void addRule(Rule rule)
    {
        ruleMapping.put(rule.toString(), rule);
    }

    /**
     * @param behavior The completion behavior to insert into the mapping.
     */
    static void addCompletionBehavior(Class<? extends CompletionBehavior> behavior)
    {
        completionMapping.put(behavior.getSimpleName(), behavior);
    }

    /**
     * @param rules Sets the rules known to Schedulr, populating {@link Schedulr#ruleMapping}.
     */
    private static void setRules(Collection<Rule> rules)
    {
        ruleMapping.clear();
        rules.forEach(rule -> ruleMapping.put(rule.toString(), rule));
    }

    /**
     * Removes a rule from Schedulr. This call is a security hole and will be removed in a future update.
     *
     * @param which The name of the rule to remove
     * @return true if the the mapping changed as a result of this call; false otherwise
     */
    public static boolean removeRule(String which)
    {
        return ruleMapping.remove(which) != null;
    }

    /**
     * Populates the completion behavior mapping, {@link Schedulr#completionMapping}, with the parameter completions.
     *
     * @param completions The collection of classes to place into the completion behavior mapping.
     */
    private static void setCompletionBehaviors(Collection<Class<? extends CompletionBehavior>> completions)
    {
        completionMapping.clear();
        completions.forEach(behavior -> completionMapping.put(behavior.getSimpleName(), behavior));
    }

    /**
     * Removes a completion behavior from the {@link Schedulr#completionMapping}. Note that this call is a security hole
     * and will be removed in a future update.
     *
     * @param which The simple name of the class to remove from the mapping.
     * @return true if the mapping changed as a result of this call, false otherwise.
     */
    public static boolean removeCompletionBehavior(String which)
    {
        return completionMapping.remove(which) != null;
    }

    /**
     * @return The names of all rules known to {@link Schedulr}.
     */
    public static Set<String> getRuleNames()
    {
        return ruleMapping.keySet();
    }

    /**
     * Returns the list of tasks that pass a certain rule. This aim is acheived by creating a stream, filtering it,
     * and collecting the results into a list.
     *
     * @param toCompare The rule to test tasks against
     * @return The list of tasks that match the rule.
     */
    public static List<AbstractTask> getTasksMatchingRule(Rule toCompare)
    {
        if (toCompare == null)
            toCompare = (task) -> false;

        return tasks.stream().filter(toCompare).collect(Collectors.toList());
    }

    /**
     * @return All tasks inside of {@link Schedulr}.
     */
    public static List<AbstractTask> getAllTasks()
    {
        return tasks.stream().collect(Collectors.toList());
    }

    /**
     * Sets the list of Schedulr's {@code tasks} to be the {@code taskList} provided here.
     *
     * @param taskList The list of tasks to assign to be handled.
     */
    public static void setTasks(Collection<AbstractTask> taskList)
    {
        tasks = new LinkedList<>(taskList);
    }

    /**
     * Inserts the implementations into the Schedulr's task list.
     *
     * @param toAdd The implementations to add.
     * @return True if {@code tasks} is changed as a result of this call.
     */
    public static boolean addTask(AbstractTask toAdd)
    {
        boolean result = tasks.add(toAdd);
        FileParser.defaultSave(tasks);
        return result;
    }

    /**
     * Inserts the collection of tasks provided, {@code taskList}, into the {@code tasks} that Schedulr is handling.
     *
     * @param taskList The taskList to add to Schedulr's {@code tasks}.
     * @return True if {@code tasks} is changed as a result of this call.
     */
    public static boolean addTasks(Collection<AbstractTask> taskList)
    {
        boolean result = tasks.addAll(taskList);
        FileParser.defaultSave(tasks);
        return result;
    }

    /**
     * Removes the implementations {@code toRemove} from the Schedulr's {@code tasks}.
     *
     * @param toRemove The implementations to remove from Schedulr's {@code tasks}.
     * @return True if the implementations was removed.
     */
    public static boolean removeTask(AbstractTask toRemove)
    {
        boolean result = tasks.remove(toRemove);
        FileParser.defaultSave(tasks);
        return result;
    }

    /**
     * Removes the tasks {@code toRemove} from the Schedulr's {@code tasks}.
     *
     * @param toRemove The tasks to remove from Schedulr's {@code tasks}.
     * @return True if {@code tasks} was changed as a result of this call.
     */
    public static boolean removeTasks(Collection<AbstractTask> toRemove)
    {
        boolean result = tasks.removeAll(toRemove);
        FileParser.defaultSave(tasks);
        return result;
    }


    /**
     * The types of behaviors that are possible: {@link Rule} and {@link CompletionBehavior}.
     */
    enum Behavior
    {
        /**
         * Represents the {@link Rule} class.
         */
        RULE(Rule.class),
        /**
         * Represents the {@link CompletionBehavior} class.
         */
        COMPLETION(CompletionBehavior.class);

        /**
         * The actual class object to which each enum refers.
         */
        private Class thisClass;

        Behavior(Class origin)
        {
            thisClass = origin;
        }

        /**
         * @return The class value of each enum.
         */
        public Class getValueClass()
        {
            return thisClass;
        }

        /**
         * @return The name of each class object.
         */
        @Override public String toString()
        {
            return thisClass.getSimpleName();
        }
    }

}
