package com.natebeckemeyer.projects.schedulrgui.graphics;

import com.natebeckemeyer.projects.schedulrgui.core.CompletionBehavior;
import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.natebeckemeyer.projects.schedulrgui.implementations.SimpleTask;
import com.natebeckemeyer.projects.schedulrgui.implementations.Tag;
import com.natebeckemeyer.projects.schedulrgui.reference.Defaults;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-09.
 * <p>
 * This is the controller for the "add implementations" window.
 */
public class AddTaskPopupController
{
    /**
     * The DatePicker object that selects the due date.
     */
    @FXML
    private DatePicker addTaskDueDatePicker;

    /**
     * The TextField object which users use to name the implementations.
     */
    @FXML
    private TextField addTaskNameField;

    /**
     * The TextField object which users use to specify labels (separated by spaces).
     */
    @FXML
    private TextField addTaskLabelField;

    /**
     * The ChoiceBox from which the user selects the CompletionBehavior behavior for the new implementations.
     */
    @FXML
    private ChoiceBox<String> addTaskChoiceBox;

    /**
     * Creates the SimpleTask from the population of the input parameters, and then inserts it into Schedulr's
     * implementations listing.
     */
    @FXML
    private void addTaskButtonClicked()
    {
        String taskName = addTaskNameField.getText();
        String labelInput = addTaskLabelField.getText();
        CompletionBehavior completionBehaviorValue = Schedulr.getCompletionBehavior(addTaskChoiceBox.getValue());

        GregorianCalendar dueDate = null;
        LocalDate localDueDate = addTaskDueDatePicker.getValue();
        if (localDueDate != null)
            dueDate = new GregorianCalendar(localDueDate.getYear(),
                    localDueDate.getMonth().getValue() - 1, localDueDate.getDayOfMonth());

        Scanner tagParser = new Scanner(labelInput);
        LinkedList<Tag> tagList = new LinkedList<>();
        while (tagParser.hasNext())
            tagList.add(Tag.getTag(tagParser.next()));

        SimpleTask newTask = new SimpleTask(taskName, dueDate, completionBehaviorValue);
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
        addTaskChoiceBox.setItems(FXCollections.observableArrayList(Schedulr.getCompletionBehaviorNames()));
        addTaskChoiceBox.setValue(Defaults.getDefaultCompletionBehavior().toString());
    }

}
