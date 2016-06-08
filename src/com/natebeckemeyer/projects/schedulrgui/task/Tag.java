package com.natebeckemeyer.projects.schedulrgui.task;

import java.util.HashMap;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public class Tag implements Rule
{
    private static HashMap<String, Tag> tags = new HashMap<>();

    /**
     * The name of the tag. Note that this name can only contain alphabetic characters.
     */
    private String name;

    /**
     * Creates a tag with name {@code name}. Can only contain alphabetic characters.
     *
     * @param name The name to give the tag
     */
    private Tag(String name)
    {
        this.name = name;
    }

    public static Tag getTag(String name)
    {
        Tag returned = tags.get(name);

        if (returned == null)
        {
            returned = new Tag(name);
            tags.put(name, returned);
        }

        return returned;
    }

    /**
     * @return A string of the format {@code "Tag: name of tag"}
     */
    public String toString()
    {
        return String.format("Tag: %s", getName());
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
