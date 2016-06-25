package com.natebeckemeyer.projects.schedulrgui.graphics;


import com.natebeckemeyer.projects.schedulrgui.core.DynamicRuleParser;
import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.natebeckemeyer.projects.schedulrgui.task.Rule;
import com.natebeckemeyer.projects.schedulrgui.task.Tag;
import com.natebeckemeyer.projects.schedulrgui.task.Task;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainWindowController
{
    private static MainWindowController thisController;

    /**
     * The completion behavior column visibility checkbox
     */
    @FXML
    private CheckBox completionBehaviorCheckbox;

    /**
     * The tags column visibility checkbox
     */
    @FXML
    private CheckBox tagCheckbox;

    /**
     * The checkbox determining whether to show completed tasks or not.
     */
    @FXML
    private CheckBox showCompletedTasksCheckbox;

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
     * The ListView that displays all of the available rules.
     */
    @FXML
    private ListView<String> ruleListView;

    /**
     * This is the flag that tells displayTasks() whether or not to show the tag column.
     */
    private boolean tagColumnShowing;

    /**
     * This is the flag that tells displayTasks() whether or not to show the onCompletion behavior column.
     */
    private boolean onCompletionColumnShowing;

    /**
     * This is the flag that determines whether or not to show completed tasks in the listing.
     */
    private boolean showCompletedTasks;

    /**
     * This is the rule that's currently being displayed.
     */
    private Rule currentRule;

    static MainWindowController getInstance()
    {
        return thisController;
    }

    /**
     * Loads the tasks into the table for display.
     */
    private void displayTasks()
    {
        AtomicInteger count = new AtomicInteger(0);
        if (currentRule == null)
            identifyCurrentRule();

        List<Task> passed;
        if (!showCompletedTasks)
            passed = Schedulr.getTasksMatchingRule(currentRule.and(Schedulr.getRule("completed").negate()));
        else
            passed = Schedulr.getTasksMatchingRule(currentRule);
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
                selected = new SimpleBooleanProperty(
                        mainTaskList.getItems().get(count.getAndIncrement()).isCompleted());

            checkBox.setSelectedStateCallback(callback -> selected);
            selected.addListener((observable, oldValue, newValue) -> {
                Task corresponding = mainTaskList.getItems().get(checkBox.getIndex());
                if (corresponding.isCompleted() != newValue)
                {
                    corresponding.setCompleted(newValue);
                    displayTasks();
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

        // Tags
        @SuppressWarnings("unchecked")
        TableColumn<Task, String> tags = (TableColumn<Task, String>) mainTaskList.getColumns().get(3);
        if (tagColumnShowing)
        {
            tags.setCellValueFactory(task -> {
                Set<Tag> theseTags = task.getValue().getTags();
                String labels = "";
                for (Tag current : theseTags)
                    labels = String.format("%s%s ", labels, current.getName());

                return new SimpleStringProperty(labels.trim());
            });
            tags.setEditable(false);
        }
        tags.setVisible(tagColumnShowing);

        @SuppressWarnings("unchecked")
        TableColumn<Task, String> completionBehaviors = (TableColumn<Task, String>) mainTaskList.getColumns().get(4);
        if (onCompletionColumnShowing)
        {
            completionBehaviors.setCellValueFactory(
                    task -> new SimpleStringProperty(task.getValue().getOnComplete().getName()));
            completionBehaviors.setEditable(false);
            completionBehaviors.setVisible(true);
        } else
        {
            completionBehaviors.setVisible(false);
        }
        completionBehaviors.setVisible(onCompletionColumnShowing);
    }

    /**
     * Creates & configures window for the "add task" popup.
     */
    @FXML
    private void viewAddTaskButtonClicked()
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("addTaskPopup.fxml"));
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
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
            setAndDisplay();
    }

    @FXML
    private void setAndDisplay()
    {
        identifyCurrentRule();
        displayTasks();
    }

    @FXML
    private void initialize()
    {
        thisController = this;
        tagCheckbox.setOnAction(event ->
        {
            tagColumnShowing = tagCheckbox.isSelected();
            displayTasks();
        });

        completionBehaviorCheckbox.setOnAction(event ->
        {
            onCompletionColumnShowing = completionBehaviorCheckbox.isSelected();
            displayTasks();
        });

        showCompletedTasksCheckbox.setOnAction(event ->
        {
            showCompletedTasks = showCompletedTasksCheckbox.isSelected();
            displayTasks();
        });

        ruleListView.setEditable(false);
        updateRuleListing();
    }

    @FXML
    private void deleteTask(KeyEvent key)
    {
        if (key.getCode() == KeyCode.DELETE || key.getCode() == KeyCode.BACK_SPACE)
        {
            List<Task> tasks = Schedulr.getAllTasks();
            Task selected = mainTaskList.getSelectionModel().getSelectedItem();
            tasks = tasks.stream().filter(task -> task != selected).collect(Collectors.toList());
            Schedulr.setTasks(tasks);
            displayTasks();
        }
    }

    void updateRuleListing()
    {
        ruleListView.setItems(FXCollections.observableArrayList(Schedulr.getRules().keySet()));
    }

    @FXML
    private void viewAddRuleButtonClicked()
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("addRulePopup.fxml"));
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        Stage addTaskPopup = Main.createNewStage(root);

        addTaskPopup.setTitle("Add a Rule");
        addTaskPopup.initModality(Modality.WINDOW_MODAL);
        addTaskPopup.showAndWait();
    }

    private void identifyCurrentRule()
    {
        currentRule = DynamicRuleParser.processInput(taskListDefinition.getText());
    }
}
