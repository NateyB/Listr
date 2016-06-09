package com.natebeckemeyer.projects.schedulrgui.graphics;


import com.natebeckemeyer.projects.schedulrgui.core.Parser;
import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.natebeckemeyer.projects.schedulrgui.task.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Controller
{
    @FXML
    private Button addTaskButton;

    @FXML
    private DatePicker addTaskDueDatePicker;

    @FXML
    private TextField addTaskNameField;

    @FXML
    private TextField addTaskLabelField;

    @FXML
    private ChoiceBox<OnCompletion> addTaskChoiceBox;

    @FXML
    private TableView<Task> mainTaskList;

    @FXML
    private TextField taskListDefinition;

    @FXML
    private void provideView()
    {
        List<Task> passed = Schedulr.getTasksMatchingRule(Parser.processInput(taskListDefinition.getText()));
        passed.sort(null);

        ObservableList<Task> tasks = FXCollections.observableArrayList(passed);
        mainTaskList.setItems(tasks);

        // Due dates
        @SuppressWarnings("unchecked")
        TableColumn<Task, String> dueDates = (TableColumn<Task, String>) mainTaskList.getColumns().get(0);
        dueDates.setCellValueFactory(new PropertyValueFactory<>("dueString"));

        // Names
        @SuppressWarnings("unchecked")
        TableColumn<Task, String> names = (TableColumn<Task, String>) mainTaskList.getColumns().get(1);
        names.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

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
    }

    @FXML
    private void enterTyping(KeyEvent key)
    {
        if (key.getCode() == KeyCode.ENTER)
            provideView();
    }

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
                        "Attempted to convert to OnCompletion from string. This code should never be reached.");
            }
        });

        addTaskDueDatePicker.setValue(LocalDate.now());
    }
}
