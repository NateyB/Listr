package com.natebeckemeyer.projects.schedulrgui.task;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public class Tag implements Rule
{
    /**
     * The name of the tag.
     */
    private String name;

    /**
     * Creates a tag with name <i>name</i>.
     *
     * @param name The name to give the tag
     */
    public Tag(String name)
    {
        this.name = name;
    }

    /**
     * @return The name of the tag
     */
    public String toString()
    {
        return getName();
    }

    /**
     * @return The name of the tag
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns true if the task contains this tag, and false otherwise
     *
     * @param task The task to test
     * @return True if the task contains the tag; else, false
     */
    @Override public boolean test(Task task)
    {
        return task.contains(this);
    }
}
