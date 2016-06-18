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
    /**
     * The application-wide menu bar that's used
     */
    private static MenuBar nativeMenuBar;

    /**
     * The primary stage at any given point.
     */
    private static Stage primary;

    /**
     * The object for selecting files to load from and save to. Initialized to the tasks directory.
     */
    private static final FileChooser fileChooser = new FileChooser();

    /**
     * @return The application-wide menu bar
     */
    public static MenuBar getMenuBar()
    {
        return nativeMenuBar;
    }

    static
    {
        fileChooser.setTitle("Task File");
        fileChooser.setInitialDirectory(new File("tasks"));
    }

    /**
     * @return A functional "Load" button which loads tasks from a file
     */
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

    /**
     * @return A functional "Save" button which saves tasks to a file
     */
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

    /**
     * Initializes the application-wide menu bar.
     *
     * @param reinitialize If true, the current menu bar will be replaced by a new one; else, it will just become
     *                     visible again.
     */
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

    /**
     * Initializes the application-wide menu bar without reinitializing it (a future update will fix this inaccuracy).
     */
    private static void initializeNativeBar()
    {
        // Initialize the File menu
        initializeNativeBar(true);
    }

    /**
     * Loads the main window
     *
     * @return The application's main window
     * @throws IOException If the file cannot be found
     */
    private Parent initializeMainWindow() throws IOException
    {
        return FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
    }

    /**
     * Initializes the main window & menu bar, configures them, and displays the stage.
     *
     * @param primaryStage The main stage of the application.
     * @throws IOException Resources not found.
     */
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
