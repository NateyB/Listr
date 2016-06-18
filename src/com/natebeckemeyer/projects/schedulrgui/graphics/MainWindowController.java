package com.natebeckemeyer.projects.schedulrgui.graphics;


import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.natebeckemeyer.projects.schedulrgui.task.Parser;
import com.natebeckemeyer.projects.schedulrgui.task.Task;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainWindowController
{
    @FXML
    private TableView<Task> mainTaskList;

    @FXML
    private TextField taskListDefinition;

    @FXML
    private void provideView()
    {
        AtomicInteger count = new AtomicInteger(0);
        List<Task> passed = Schedulr.getTasksMatchingRule(Parser.processInput(taskListDefinition.getText()));
        passed.sort(null);

        ObservableList<Task> tasks = FXCollections.observableArrayList(passed);
        mainTaskList.setItems(tasks);

        @SuppressWarnings("unchecked")
        TableColumn<Task, Boolean> checkMarks = (TableColumn<Task, Boolean>) mainTaskList.getColumns().get(0);
        // TODO Fix this up. It's very hacky (and not functioning perfectly).
        // Specifically, it messes up the first time that the list loads.
        // Additionally, when you attempt to do a column-based resort, the checkboxes
        // don't sync up with the new order.
        checkMarks.setCellFactory(column -> {
            CheckBoxTableCell<Task, Boolean> checkBox = new CheckBoxTableCell<>();
            BooleanProperty selected;
            if (count.intValue() >= passed.size())
                selected = new SimpleBooleanProperty();
            else
                selected = new SimpleBooleanProperty(passed.get(count.getAndIncrement()).isCompleted());

            checkBox.setSelectedStateCallback(callback -> selected);
            selected.addListener((observable, oldValue, newValue) -> {
                Task corresponding = mainTaskList.getItems().get(checkBox.getIndex());
                if (corresponding.isCompleted() != newValue)
                {
                    corresponding.setCompleted(newValue);
                    provideView();
                }
            });
            return checkBox;
        });
        checkMarks.setCellValueFactory(task -> new SimpleBooleanProperty(task.getValue().isCompleted()));
        checkMarks.setEditable(true);

        // Due dates
        @SuppressWarnings("unchecked")
        TableColumn<Task, String> dueDates = (TableColumn<Task, String>) mainTaskList.getColumns().get(1);
        dueDates.setCellValueFactory(new PropertyValueFactory<>("dueString"));
        dueDates.setEditable(false); // TODO Change this (for names as well) later so that users can alter tasks

        // Names
        @SuppressWarnings("unchecked")
        TableColumn<Task, String> names = (TableColumn<Task, String>) mainTaskList.getColumns().get(2);
        names.setCellValueFactory(new PropertyValueFactory<>("name"));
        names.setEditable(false);
    }

    @FXML
    private void viewAddTaskButtonClicked()
    {
        Parent root = null;
        try
        {
            root = FXMLLoader.load(getClass().getResource("addTaskPopup.fxml"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Stage addTaskPopup = Main.createNewStage(root);

        addTaskPopup.setResizable(false);
        addTaskPopup.setTitle("Add a Task");
        addTaskPopup.initModality(Modality.WINDOW_MODAL);
        addTaskPopup.showAndWait();
    }

    @FXML
    private void enterTyping(KeyEvent key)
    {
        if (key.getCode() == KeyCode.ENTER)
            provideView();
    }
}
