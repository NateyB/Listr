package com.natebeckemeyer.projects.schedulrgui.graphics;

import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.natebeckemeyer.projects.schedulrgui.task.Parser;
import com.natebeckemeyer.projects.schedulrgui.task.Rule;
import com.natebeckemeyer.projects.schedulrgui.task.Task;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main extends Application
{
    private static MenuBar nativeMenuBar;

    private static Stage primary;

    private static FileChooser fileChooser = new FileChooser();

    public static MenuBar getMenuBar()
    {
        return nativeMenuBar;
    }

    static
    {
        fileChooser.setTitle("Task File");
        fileChooser.setInitialDirectory(new File("tasks"));
    }

    private static MenuItem initializeLoadButton()
    {
        MenuItem loadButton = new MenuItem("Load");
        loadButton.onActionProperty().setValue(event -> {
            File taskFile = fileChooser.showOpenDialog(primary);
            if (taskFile != null)
                Schedulr.setTasks(Parser.readTasksFromFile(taskFile));
        });
        return loadButton;
    }

    private static MenuItem initializeSaveButton()
    {
        MenuItem saveButton = new MenuItem("Save");
        saveButton.onActionProperty().setValue(event -> {
            File taskFile = fileChooser.showSaveDialog(primary);
            if (taskFile != null)
                try
                {
                    Parser.saveTasksToFile(taskFile, Schedulr.getTasksMatchingRule(new Rule()
                    {
                        @Override public String getName()
                        {
                            return "All";
                        }

                        @Override public boolean test(Task task)
                        {
                            return true;
                        }
                    }));
                } catch (IOException e)
                {
                    e.printStackTrace();
                    System.exit(13);
                }
        });

        return saveButton;
    }

    private static void initializeNativeBar(boolean reinitialize)
    {
        if (!reinitialize && nativeMenuBar != null)
        {
            nativeMenuBar.getParent().setVisible(true);
            nativeMenuBar.setVisible(true);
            nativeMenuBar.setManaged(false);
            nativeMenuBar.getMenus().forEach(menu ->
            {
                menu.setVisible(true);
                menu.getItems().forEach(item -> item.setVisible(true));
            });
        } else
        {
            Menu[] menu = new Menu[]{new Menu("File")};
            menu[0].getItems().addAll(initializeSaveButton(), initializeLoadButton());

            MenuBar nativeBar = new MenuBar();
            nativeBar.getMenus().addAll(Arrays.asList(menu));
            nativeBar.setManaged(false);

            nativeBar.setUseSystemMenuBar(true);

            nativeMenuBar = nativeBar;
        }

    }

    private static void initializeNativeBar()
    {
        // Initialize the File menu
        initializeNativeBar(true); // TODO Fix this up
    }

    private Parent initializeMainWindow() throws IOException
    {
        return FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        Parent mainWindow = initializeMainWindow();
        initializeNativeBar();

        Group root = new Group();
        root.getChildren().addAll(getMenuBar(), mainWindow);

        primaryStage.setTitle("Schedulr");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primary = primaryStage;
    }

    /**
     * Creates a new stage (attached to the native menu bar) with children {@code elements}.
     *
     * @param elements Nodes to include in the stage.
     * @return The new stage
     */
    public static Stage createNewStage(Node... elements)
    {
        Stage thisStage = new Stage();

        Group rootGroup = new Group();
        List<Node> elementList = new LinkedList<>(Arrays.asList(elements));

        initializeNativeBar();
        MenuBar menuBar = getMenuBar();
        menuBar.setVisible(true);

        elementList.add(menuBar);
        rootGroup.getChildren().addAll(elementList);

        thisStage.setScene(new Scene(rootGroup));

        return thisStage;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
