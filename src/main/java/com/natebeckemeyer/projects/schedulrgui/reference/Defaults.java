package com.natebeckemeyer.projects.schedulrgui.reference;

import com.natebeckemeyer.projects.schedulrgui.core.AbstractTask;
import com.natebeckemeyer.projects.schedulrgui.core.CompletionBehavior;
import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.natebeckemeyer.projects.schedulrgui.implementations.SimpleCompleted;
import com.natebeckemeyer.projects.schedulrgui.implementations.SimpleTask;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-07-06.
 * <p>
 * This class contains many static methods for identifying appropriate default behaviors. Information about user
 * preferences is managed here. This class is not instantiable.
 * </p>
 */
public final class Defaults
{
    /**
     * Disables instantiation.
     */
    private Defaults()
    {
    }

    /**
     * This interface serves as a way to enable lambda expressions to define what to do with the value of a key-value
     * pair. That is, this interface provides a unified, syntactically concise way for to initialize the
     * {@link Defaults#handlers} mapping in {@link Defaults#initializeHandlers()}.
     */
    @FunctionalInterface
    private interface PropertyLoader
    {
        /**
         * @param value
         */
        void load(String value);
    }

    /**
     * The file from which tasks are loaded automatically.
     */
    private static String defaultAutoFile;

    /**
     * The flag that determines whether all task files are saved automatically (by their names).
     */
    private static boolean autoSaveAll;

    /**
     * The flag that determines if the default file is loaded automatically.
     */
    private static boolean autoLoadDefault;

    /**
     * The class of the default completion behavior, instantiated with getInstance its getter is called.
     */
    private static Class<? extends CompletionBehavior> completionBehavior;

    /**
     * @return Whether to autosave tasks to the files that they're loaded from (or the default) when the task listing
     * updates.
     */
    public static boolean getAutoSaveAll()
    {
        return autoSaveAll;
    }

    /**
     * @return Whether to load the default task file on startup.
     */
    public static boolean getAutoLoadDefault()
    {
        return autoLoadDefault;
    }

    /**
     * @return The default completion behavior as specified by user preferences, or an instance of
     * {@link SimpleCompleted} if that behavior could not be loaded for any reason.
     */
    public static CompletionBehavior getDefaultCompletionBehavior()
    {
        try
        {
            return completionBehavior.newInstance();
        } catch (InstantiationException | IllegalAccessException | NullPointerException e)
        {
            System.err.println("Error loading completion behavior '" + completionBehavior.getSimpleName() + "' from " +
                    "Defaults. Returning a " + SimpleCompleted.class.getSimpleName() + " object instead.");
            return new SimpleCompleted();
        }
    }

    /**
     * @return The path of the default file from which tasks should be stored/loaded on startup.
     */
    public static String getDefaultTaskFile()
    {
        return defaultAutoFile;
    }

    /**
     * @return The default class task to use.
     */
    public static Class<? extends AbstractTask> getDefaultTask()
    {
        return SimpleTask.class;
    }

    /**
     * The mapping from {@link Properties} keys to what to do with them.
     */
    private static HashMap<String, PropertyLoader> handlers = new HashMap<>();

    /**
     * The function that loads the user preferences. If the user has not defined a preference for a specific
     * {@link Properties} key, then the system default is used.
     * <p>
     * A static initializer calls the private methods {@link Defaults#initializeHandlers()} and
     * {@link Defaults#initializePreferences()} to populate the relevant data fields.
     */
    private static void initializePreferences()
    {
        Properties userProperties = new Properties();
        Properties systemProperties = new Properties();

        // Load user files
        try (InputStream userInput = new FileInputStream("user/properties/userProperties.properties"))
        {
            userProperties.load(userInput);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        // Load system files (in case the user preferences are not found)
        try (InputStream propertiesInput = Defaults.class.getResourceAsStream(
                ProjectPaths.sampleResources + ProjectPaths.fileSeparator + "defaultProperties.properties"))
        {
            systemProperties.load(propertiesInput);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        // Perform operations on each key-value pair.
        Enumeration<?> enumeration = systemProperties.propertyNames();
        for (String key; enumeration.hasMoreElements(); )
        {
            key = (String) enumeration.nextElement();
            String value = userProperties.getProperty(key, systemProperties.getProperty(key));
            try
            {
                handlers.get(key).load(value);
            } catch (NullPointerException e)
            {
                System.err.println("Could not locate handler for property '" + key + "'. Exiting.");
                System.exit(17);
            }
        }
    }

    /**
     * Places the handlers for values of keys into the {@link Defaults#handlers} mapping.
     * <p>
     * A static initializer calls the private methods {@link Defaults#initializeHandlers()} and
     * {@link Defaults#initializePreferences()} to populate the relevant data fields.
     */
    private static void initializeHandlers()
    {
        handlers.put("auto-save-file", value -> defaultAutoFile = value);
        handlers.put("auto-save", value -> autoSaveAll = Boolean.parseBoolean(value));
        handlers.put("auto-load", value -> autoLoadDefault = Boolean.parseBoolean(value));
        handlers.put("completion-behavior", value -> completionBehavior = Schedulr.getCompletionBehavior(value)
                .getClass());
    }

    static
    {
        initializeHandlers();
        initializePreferences();
    }
}
