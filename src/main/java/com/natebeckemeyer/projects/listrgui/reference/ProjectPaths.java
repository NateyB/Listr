package com.natebeckemeyer.projects.listrgui.reference;

import java.io.File;
import java.io.InputStream;

/**
 * Created for Listr by @author Nate Beckemeyer on 2016-06-23.
 * <p>
 * This class contains many static final strings that refer to locations of important files used by the application.
 * It is a convenience class for locating (and updating the location of) files.
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
    private static final String thisLocation = "com%snatebeckemeyer%sprojects%slistrgui";

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
     * The file prefix for the implementations package.
     */
    public static final String taskFilePrefix = String.format("%s%s%s", filePrefix, fileSeparator, "implementations");

    /**
     * The package prefix for the implementations package.
     */
    public static final String implementationsPackagePrefix = String.format("%s%s%s", packagePrefix, packageSeparator,
            "implementations");

    /**
     * The root directory for resources.
     */
    public static final String resourcesPrefix = getResourcesPrefix();

    private static String getResourcesPrefix()
    {
        return "";
    }

    /**
     * The directory containing the user resource files (those files which are user-specific).
     */
    public static final String userResources = "user";

    /**
     * The location of the file that stores the user-defined rules.
     */
    public static final String userRulesFile =
            userResources + fileSeparator + "userdefinitions" + fileSeparator + "userrules";

    /**
     * The directory that contains the user-defined completion behaviors.
     */
    public static final String userCompletionsFile =
            userResources + fileSeparator + "userdefinitions" + fileSeparator + "usercompletionbehaviors";

    /**
     * The directory where the user tasks are stored.
     */
    public static final String userTasksDirectory = userResources + fileSeparator + "tasks";

    /**
     * The directory containing the system resource files (those files which are native to Listr).
     */
    public static final String systemFile = resourcesPrefix + fileSeparator + "system";

    /**
     * The resource directory containing the samples.
     */
    public static final String sampleResources = resourcesPrefix + fileSeparator + "samples";

    /**
     * The directory containing the FXML files.
     */
    public static final String fxmlDirectory = systemFile + fileSeparator + "windows";

    /**
     * The directory containing the templates used to compile user-defined behaviors.
     */
    public static final String templateDirectory = systemFile + fileSeparator + "templates";

    static
    {
        File userRFiles = new File(userRulesFile);
        userRFiles.mkdirs();


        File userCFiles = new File(userCompletionsFile);
        userCFiles.mkdirs();
    }

    /**
     * This method first tries to load the file as a resource from the path; if that attempt is unsuccessful, then it
     * tries to load the file from the actual path, making directories as needed. If that operation fails, returns null.
     *
     * @param path The path of the file to open.
     * @return The file if found or created, null otherwise.
     */
    public static File getExternalFile(String path)
    {
        return new File(path);
    }

    /**
     * Attempts to load a resource using this class's {@link ClassLoader}.
     *
     * @param path The path of the resource to load.
     * @return The resource.
     */
    public static InputStream getResource(String path)
    {
        return ProjectPaths.class.getResourceAsStream(path);
    }

}
