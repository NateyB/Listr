package com.natebeckemeyer.projects.schedulrgui.graphics;

import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.natebeckemeyer.projects.schedulrgui.task.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-09.
 * <p>
 * This is the controller for the "add task" window.
 */
public class AddTaskPopupController
{
    /**
     * The DatePicker object that selects the due date.
     */
    @FXML
    private DatePicker addTaskDueDatePicker;

    /**
     * The TextField object which users use to name the task.
     */
    @FXML
    private TextField addTaskNameField;

    /**
     * The TextField object which users use to specify labels (separated by spaces).
     */
    @FXML
    private TextField addTaskLabelField;

    /**
     * The ChoiceBox from which the user selects the OnCompletion behavior for the new task.
     */
    @FXML
    private ChoiceBox<OnCompletion> addTaskChoiceBox;

    /**
     * Creates the Task from the population of the input parameters, and then inserts it into Schedulr's task listing.
     */
    @FXML
    private void addTaskButtonClicked()
    {
        String taskName = addTaskNameField.getText();
        String labelInput = addTaskLabelField.getText();
        OnCompletion onCompletionValue = addTaskChoiceBox.getValue();

        LocalDate localDueDate = addTaskDueDatePicker.getValue();
        GregorianCalendar dueDate = new GregorianCalendar(localDueDate.getYear(),
                localDueDate.getMonth().getValue() - 1, localDueDate.getDayOfMonth());

        Scanner tagParser = new Scanner(labelInput);
        LinkedList<Tag> tagList = new LinkedList<>();
        while (tagParser.hasNext())
        {
            tagList.add(Tag.getTag(tagParser.next()));
        }

        Task newTask = new Task(taskName, dueDate, onCompletionValue);
        newTask.setTags(tagList);

        Schedulr.addTask(newTask);
        ((Stage) addTaskChoiceBox.getScene().getWindow()).close();
    }

    /**
     * Specifies the initialization behavior for this popup; specifically, it specifies the default populations where
     * necessary.
     */
    @FXML
    private void initialize()
    {
        MarkCompleted defaultVal = new MarkCompleted();
        addTaskChoiceBox.setItems(FXCollections.observableArrayList(defaultVal, new VerboseCompleted()));
        addTaskChoiceBox.setValue(defaultVal);
        addTaskChoiceBox.converterProperty().setValue(new StringConverter<OnCompletion>()
        {
            @Override public String toString(OnCompletion behavior)
            {
                return behavior.getName();
            }

            @Override public OnCompletion fromString(String string)
            {
                throw new RuntimeException(
                        "Attempted to instantiate OnCompletion from string. This operation is not supported.");
            }
        });

        addTaskDueDatePicker.setValue(LocalDate.now());
    }

}
