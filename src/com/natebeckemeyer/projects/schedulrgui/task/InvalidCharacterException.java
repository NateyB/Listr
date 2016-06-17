package com.natebeckemeyer.projects.schedulrgui.task;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-05-27.
 */
public class InvalidCharacterException extends RuntimeException
{
    public InvalidCharacterException(String message)
    {
        super(message);
    }
}
