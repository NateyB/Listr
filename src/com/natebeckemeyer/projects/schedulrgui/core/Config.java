package com.natebeckemeyer.projects.schedulrgui.core;

import java.io.File;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-23.
 */
final public class Config
{
    /**
     * Instantiation disabled.
     */
    private Config()
    {
    }

    /**
     * The file separator used in this system
     */
    public static final String separator = File.separator;

    /**
     * The package prefix for the outermost package (where initial source code branching occurs).
     */
    public static final String prefix = String.format("src%scom%snatebeckemeyer%sprojects%sschedulrgui", separator,
            separator, separator, separator);

    /**
     * The package prefix for the core package.
     */
    public static final String corePrefix = String.format("%s%s%s", prefix, separator, "core");

    /**
     * The package prefix for the graphics package.
     */
    public static final String graphicsPrefix = String.format("%s%s%s", prefix, separator, "graphics");

    /**
     * The package prefix for the task package.
     */
    public static final String taskPrefix = String.format("%s%s%s", prefix, separator, "task");

    public static final String userRulesLocation = String.format("%s%s%s%s%s", "resources", separator,
            "userdefinitions", separator, "userrules");


}
