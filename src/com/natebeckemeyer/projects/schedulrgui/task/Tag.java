package com.natebeckemeyer.projects.schedulrgui.task;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-04-28.
 */
public class Tag
{
    private String name;

    public Tag(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return getName();
    }

    public String getName()
    {
        return name;
    }
}
