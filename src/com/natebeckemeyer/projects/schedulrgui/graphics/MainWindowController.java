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
    /**
     * The table that displays task according to the user-defined rules.
     */
    @FXML
    private TableView<Task> mainTaskList;

    /**
     * The text field that contains the rule that controls which tasks are seen.
     */
    @FXML
    private TextField taskListDefinition;

    /**
     * Loads the tasks into the table for display.
     */
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
        dueDates.setEditable(false);

        // Names
        @SuppressWarnings("unchecked")
        TableColumn<Task, String> names = (TableColumn<Task, String>) mainTaskList.getColumns().get(2);
        names.setCellValueFactory(new PropertyValueFactory<>("name"));
        names.setEditable(false);
    }

    /**
     * Creates & configures window for the "add task" popup.
     */
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

    /**
     * If enter is pressed while typing into the taskListDefinition box, update the task listing.
     *
     * @param key The key pressed
     */
    @FXML
    private void enterTyping(KeyEvent key)
    {
        if (key.getCode() == KeyCode.ENTER)
            provideView();
    }
}
