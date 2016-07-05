package com.natebeckemeyer.projects.schedulrgui.reference;

import java.io.File;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-23.
 */
final public class ProjectPaths
{
    /**
     * Instantiation disabled.
     */
    private ProjectPaths()
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
    public static final String filePrefix = String.format(thisLocation, fileSeparator, fileSeparator, fileSeparator,
            fileSeparator);

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

    /**
     * The directory where all resources are stored.
     */
    public static final String resourcesDirectory = "src" + fileSeparator + "main" + fileSeparator + "resources";

    /**
     * The directory containing the user resource files (those files which are user-specific).
     */
    public static final String userResources = resourcesDirectory + fileSeparator + "/user";

    /**
     * The location of the file that stores the user-defined rules.
     */
    public static final String userRulesFile = String.format("%s%s%s%s%s", userResources, fileSeparator,
            "userdefinitions", fileSeparator, "userrules");

    /**
     * The directory that contains the user-defined completion behaviors.
     */
    public static final String userCompletionsFile = String.format("%s%s%s%s%s", userResources, fileSeparator,
            "userdefinitions", fileSeparator, "usercompletionbehaviors");

    /**
     * The directory where the user tasks are stored.
     */
    public static final String userTasksDirectory = String.format("%s%s%s", userResources, fileSeparator, "tasks");

    /**
     * The directory containing the system resource files (those files which are native to Schedulr).
     */
    public static final String systemFile = resourcesDirectory + fileSeparator + "system";

    /**
     * The directory containing the FXML files.
     */
    public static final String fxmlDirectory = String.format("%s%s%s%s", fileSeparator, "system", fileSeparator,
            "windows");

    /**
     * The directory containing the templates used to compile user-defined behaviors.
     */
    public static final String templateDirectory = String.format("%s%s%s", systemFile, fileSeparator, "templates");

    static
    {
        File userRFiles = new File(userRulesFile);
        userRFiles.mkdirs();


        File userCFiles = new File(userCompletionsFile);
        userCFiles.mkdirs();
    }

}
