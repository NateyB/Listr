package com.natebeckemeyer.projects.schedulrgui.graphics;


import com.natebeckemeyer.projects.schedulrgui.core.Parser;
import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.natebeckemeyer.projects.schedulrgui.task.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainWindowController
{
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
    private void viewAddTaskButtonClicked() throws IOException
    {
        Stage addTaskPopup = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("addTaskPopup.fxml"));
        addTaskPopup.setScene(new Scene(root));
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
