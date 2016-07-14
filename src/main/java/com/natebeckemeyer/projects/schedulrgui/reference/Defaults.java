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
 */
public final class Defaults
{
    /**
     * Disable instantiation.
     */
    private Defaults()
    {
    }

    @FunctionalInterface
    private interface PropertyLoader
    {
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


    public static boolean getAutoSaveAll()
    {
        return autoSaveAll;
    }

    public static boolean getAutoLoadDefault()
    {
        return autoLoadDefault;
    }


    public static CompletionBehavior getDefaultCompletionBehavior()
    {
        try
        {
            return completionBehavior.newInstance();
        } catch (InstantiationException | IllegalAccessException | NullPointerException e)
        {
            e.printStackTrace();
            return new SimpleCompleted();
        }
    }


    public static String getDefaultSaveFile()
    {
        return defaultAutoFile;
    }


    public static Class<? extends AbstractTask> getDefaultTask()
    {
        return SimpleTask.class;
    }


    private static HashMap<String, PropertyLoader> handlers = new HashMap<>();


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
     * Places the handlers into the hashmap.
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
