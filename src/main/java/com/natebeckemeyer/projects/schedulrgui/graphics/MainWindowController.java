package com.natebeckemeyer.projects.schedulrgui.graphics;


import com.natebeckemeyer.projects.schedulrgui.core.AbstractTask;
import com.natebeckemeyer.projects.schedulrgui.core.DynamicBehaviorEngine;
import com.natebeckemeyer.projects.schedulrgui.core.Rule;
import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.natebeckemeyer.projects.schedulrgui.implementations.DatelessTask;
import com.natebeckemeyer.projects.schedulrgui.implementations.SimpleTask;
import com.natebeckemeyer.projects.schedulrgui.implementations.Tag;
import com.natebeckemeyer.projects.schedulrgui.reference.ProjectPaths;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
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
     * The table that displays implementations according to the user-defined rules.
     */
    @FXML
    private TableView<AbstractTask> mainTaskList;

    /**
     * The text field that contains the rule that controls which tasks are seen.
     */
    @FXML
    private TextField taskListDefinition;

    /**
     * The ListView that displays all of the available rules.
     */
    @FXML
    private TreeView<String> behaviorTreeView;

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

    private TreeItem<String> ruleNode;

    private TreeItem<String> completionBehaviorNode;

    private TreeItem<String> rootNode;

    static MainWindowController getInstance()
    {
        return thisController;
    }

    /**
     * Loads the tasks into the table for display.
     */
    private void displayTasks()
    {
        if (currentRule == null)
            identifyCurrentRule();

        List<AbstractTask> passed;
        if (!showCompletedTasks)
            passed = Schedulr.getTasksMatchingRule(Rule.and(currentRule, Rule.negate(Schedulr.getRule("completed"))));
        else
            passed = Schedulr.getTasksMatchingRule(currentRule);
        passed.sort(null);

        ObservableList<AbstractTask> tasks = FXCollections.observableArrayList(passed);
        mainTaskList.setItems(tasks);

        AtomicInteger count = new AtomicInteger(0);
        @SuppressWarnings("unchecked")
        TableColumn<AbstractTask, AbstractTask> checkMarks = (TableColumn<AbstractTask, AbstractTask>) mainTaskList
                .getColumns()
                .get(0);

        checkMarks.setCellFactory(column -> {
            CheckBoxTableCell<AbstractTask, AbstractTask> checkBox = new CheckBoxTableCell<>();
            BooleanProperty selected;
            if (count.intValue() >= passed.size())
                selected = new SimpleBooleanProperty();
            else
                selected = new SimpleBooleanProperty(
                        mainTaskList.getItems().get(count.getAndIncrement()).isCompleted());

            checkBox.setSelectedStateCallback(callback -> selected);
            selected.addListener((observable, oldValue, newValue) -> {
                AbstractTask corresponding = mainTaskList.getItems().get(checkBox.getIndex());
                if (corresponding.isCompleted() != newValue)
                {
                    corresponding.setCompleted(newValue);
                    displayTasks();
                }
            });
            return checkBox;
        });
        /*checkMarks.setCellFactory(CheckBoxTableCell.forTableColumn(checkMarks));
        checkMarks.setCellValueFactory(new PropertyValueFactory<>("completed"));
        checkMarks.setEditable(true);
        checkMarks.setCellFactory(item -> new CheckBoxTableCell());
        checkMarks.setCellValueFactory(new PropertyValueFactory<>("completed"));*/

        // Tags
        @SuppressWarnings("unchecked")
        TableColumn<AbstractTask, String> tags = (TableColumn<AbstractTask, String>) mainTaskList.getColumns().get(3);
        tags.setVisible(tagColumnShowing);

        @SuppressWarnings("unchecked")
        TableColumn<AbstractTask, String> completionBehaviors = (TableColumn<AbstractTask, String>) mainTaskList
                .getColumns().get(4);
        completionBehaviors.setVisible(onCompletionColumnShowing);
    }

    /**
     * Creates & configures window for the "add rules" popup.
     */
    @FXML
    private void viewAddTaskButtonClicked()
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass()
                    .getResource(ProjectPaths.fxmlDirectory + ProjectPaths.fileSeparator + "addTaskPopup.fxml"));
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
     * If enter is pressed while typing into the taskListDefinition box, update the implementations listing.
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

        behaviorTreeView.setEditable(false);
        updateSidebar();


        initializeDueDatesColumn();
        initializeNamesColumn();
        initializeTagsColumn();
        initializeCompletionBehaviorsColumn();
    }

    /**
     * Sets the due date factories to the getDueString method in the {@link AbstractTask}s. Allows due dates to be
     * updated.
     * If the task is a {@link SimpleTask} and the user changes its due date to "Eventually," then a new DatelessTask
     * is created which has the same other properties. Conversely, if a {@link DatelessTask} is given a concrete due
     * date, then a SimpleTask is created with the specified due date, and all other properties the same.
     */
    private void initializeDueDatesColumn()
    {
        // Due dates
        @SuppressWarnings("unchecked")
        TableColumn<AbstractTask, String> dueDates = (TableColumn<AbstractTask, String>) mainTaskList.getColumns().get(
                1);
        dueDates.setCellFactory(TextFieldTableCell.forTableColumn());
        dueDates.setCellValueFactory(new PropertyValueFactory<>("dueString"));
        dueDates.setOnEditCommit(event -> {
                    AbstractTask task = event.getRowValue();
                    try
                    {
                        Scanner parser = new Scanner(event.getNewValue());
                        if (parser.hasNext(Pattern.quote("Eventually")))
                        {
                            if (task instanceof SimpleTask)
                            {
                                Schedulr.removeTask(task);
                                Schedulr.addTask(new DatelessTask(task));
                            }
                        } else
                        {
                            AbstractTask newTask;
                            if (task instanceof DatelessTask)
                            {
                                newTask = new SimpleTask(task);
                            } else
                                newTask = task;

                            parser.useDelimiter(Pattern.quote("-"));

                            int year = parser.nextInt();
                            int month = parser.nextInt();
                            int day_of_month = parser.nextInt();

                            newTask.getDueDate().set(Calendar.YEAR, year);
                            newTask.getDueDate().set(Calendar.MONTH, month - 1);
                            newTask.getDueDate().set(Calendar.DAY_OF_MONTH, day_of_month);

                            Schedulr.removeTask(task);
                            Schedulr.addTask(newTask);
                        }
                    } catch (NoSuchElementException e)
                    {
                        System.err.println("Formatting of date is incorrect in user attempt to edit task " +
                                task.getName() + " date graphically.");
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    displayTasks();
                }
        );
    }

    /**
     * Ties the "names" in the column to those in the corresponding {@link AbstractTask}s.
     */
    private void initializeNamesColumn()
    {
        @SuppressWarnings("unchecked")
        TableColumn<AbstractTask, String> names = (TableColumn<AbstractTask, String>) mainTaskList.getColumns().get(2);
        names.setCellFactory(TextFieldTableCell.forTableColumn());
        names.setCellValueFactory(new PropertyValueFactory<>("name"));
        names.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));
        displayTasks();
    }

    /**
     * Initializes the tags column in the table view.
     * Specifically, it sets the
     * <pre>
     *  * cellFactory
     *  * cellValueFactory
     *  * onEditCommit handler
     * </pre>
     */
    private void initializeTagsColumn()
    {
        @SuppressWarnings("unchecked")
        TableColumn<AbstractTask, String> tags = (TableColumn<AbstractTask, String>) mainTaskList.getColumns().get(3);
        tags.setCellFactory(TextFieldTableCell.forTableColumn());
        tags.setCellValueFactory(task -> {
            Set<Tag> theseTags = task.getValue().getTags();
            String labels = "";
            for (Tag current : theseTags)
                labels = String.format("%s%s ", labels, current.toString());

            return new SimpleStringProperty(labels.trim());
        });
        tags.setOnEditCommit(event -> {
            AbstractTask task = event.getRowValue();
            try (Scanner parser = new Scanner(event.getNewValue()))
            {
                List<Tag> labels = new LinkedList<>();
                while (parser.hasNext())
                    labels.add(Tag.getTag(parser.next()));

                task.setTags(labels);
            } catch (NoSuchElementException e)
            {
                System.err.println("Formatting of tags in graphical edit of user task " +
                        event.getRowValue().getName() + " is incorrect.");
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            displayTasks();
        });
    }

    private void initializeCompletionBehaviorsColumn()
    {
        @SuppressWarnings("unchecked")
        TableColumn<AbstractTask, String> completionBehaviors = (TableColumn<AbstractTask, String>) mainTaskList
                .getColumns().get(4);
        completionBehaviors.setCellFactory(TextFieldTableCell.forTableColumn());
        completionBehaviors.setCellValueFactory(
                task -> new SimpleStringProperty(task.getValue().getOnComplete().getSimpleName()));
        completionBehaviors.setOnEditCommit(
                event -> event.getRowValue().setOnComplete(Schedulr.getCompletionBehavior(event.getNewValue())));
        displayTasks();
    }

    @FXML
    private void deleteTask(KeyEvent key)
    {
        if (key.getCode() == KeyCode.DELETE || key.getCode() == KeyCode.BACK_SPACE)
        {
            List<AbstractTask> tasks = Schedulr.getAllTasks();
            AbstractTask selected = mainTaskList.getSelectionModel().getSelectedItem();
            tasks = tasks.stream().filter(task -> task != selected).collect(Collectors.toList());
            Schedulr.setTasks(tasks);
            displayTasks();
        }
    }

    @FXML
    private void deleteBehavior(KeyEvent key)
    {
        if (key.getCode() == KeyCode.DELETE || key.getCode() == KeyCode.BACK_SPACE)
        {
            TreeItem<String> item = behaviorTreeView.getSelectionModel().getSelectedItem();
            if (item != ruleNode && item != completionBehaviorNode && item != rootNode)
            {
                String selected = item.getValue();
                if (Schedulr.removeRule(selected) || Schedulr.removeCompletionBehavior(selected))
                {
                    updateSidebar();
                    setAndDisplay();
                }
            }
        }
    }

    void updateSidebar()
    {
        ruleNode = new TreeItem<>("Rules");
        ruleNode.setExpanded(true);
        for (String ruleName : Schedulr.getRuleNames())
            ruleNode.getChildren().add(new TreeItem<>(ruleName));

        completionBehaviorNode = new TreeItem<>("Completion Behaviors");
        completionBehaviorNode.setExpanded(true);
        for (String completionBehaviorName : Schedulr.getCompletionBehaviorNames())
            completionBehaviorNode.getChildren().add(new TreeItem<>(completionBehaviorName));

        rootNode = new TreeItem<>("Behaviors");
        rootNode.getChildren().add(ruleNode);
        rootNode.getChildren().add(completionBehaviorNode);

        behaviorTreeView.setRoot(rootNode);
    }

    @FXML
    private void viewAddRuleButtonClicked()
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass()
                    .getResource(ProjectPaths.fxmlDirectory + ProjectPaths.fileSeparator + "addBehaviorPopup.fxml"));
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
        currentRule = DynamicBehaviorEngine.processInput(taskListDefinition.getText());
    }
}
