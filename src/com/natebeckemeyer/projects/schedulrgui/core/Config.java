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
     * The file separator used in this system.
     */
    public static final String fileSeparator = File.separator;

    /**
     * The line separator used in this system.
     */
    public static final String lineSeparator = System.lineSeparator();

    /**
     * The package fileSeparator used in this system.
     */
    public static final String packageSeparator = ".";

    /**
     * The unformatted prefix for the outermost package (where initial source code branching occurs).
     */
    private static final String thisLocation = "com%snatebeckemeyer%sprojects%sschedulrgui";

    /**
     * The file prefix for the outermost package (where initial source code branching occurs).
     */
    public static final String filePrefix = String.format(String.format("%s%s%s", "src", fileSeparator, thisLocation),
            fileSeparator, fileSeparator, fileSeparator, fileSeparator);

    /**
     * The package prefix for the outermost package (where initial source code branching occurs).
     */
    public static final String packagePrefix = String.format(thisLocation,
            packageSeparator, packageSeparator, packageSeparator, packageSeparator);

    /**
     * The file prefix for the core package.
     */
    public static final String coreFilePrefix = String.format("%s%s%s", filePrefix, fileSeparator, "core");

    /**
     * The package prefix for the core package.
     */
    public static final String corePackagePrefix = String.format("%s%s%s", packagePrefix, packageSeparator, "core");

    /**
     * The file prefix for the graphics package.
     */
    public static final String graphicsFilePrefix = String.format("%s%s%s", filePrefix, fileSeparator, "graphics");

    /**
     * The package prefix for the graphics package.
     */
    public static final String graphicsPackagePrefix = String.format("%s%s%s", packagePrefix, packageSeparator,
            "graphics");

    /**
     * The file prefix for the task package.
     */
    public static final String taskFilePrefix = String.format("%s%s%s", filePrefix, fileSeparator, "task");

    /**
     * The package prefix for the task package.
     */
    public static final String taskPackagePrefix = String.format("%s%s%s", packagePrefix, packageSeparator, "task");


    public static final String userFile = String.format("%s%s%s", "resources", fileSeparator, "user");

    /**
     * The location of the file that stores the user-defined rules.
     */
    public static final String userRulesFile = String.format("%s%s%s%s%s", userFile, fileSeparator,
            "userdefinitions", fileSeparator, "userrules");


    public static final String userCompletionsFile = String.format("%s%s%s%s%s", userFile, fileSeparator,
            "userdefinitions", fileSeparator, "usercompletionbehaviors");

    public static final String userTasksDirectory = String.format("%s%s%s", userFile, fileSeparator, "tasks");


    static
    {
        File userRFiles = new File(userRulesFile);
        userRFiles.mkdirs();


        File userCFiles = new File(userCompletionsFile);
        userCFiles.mkdirs();
    }

}
