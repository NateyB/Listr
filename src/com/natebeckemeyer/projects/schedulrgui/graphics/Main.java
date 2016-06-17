package com.natebeckemeyer.projects.schedulrgui.graphics;

import com.natebeckemeyer.projects.schedulrgui.core.Parser;
import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main extends Application
{
    private static MenuBar nativeMenuBar;

    public static MenuBar getMenuBar()
    {
        return nativeMenuBar;
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
            menu[0].getItems().addAll(new MenuItem("Save"), new MenuItem("Load"));

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
        Schedulr.addTasks(Parser.readTasksFromFile("tasks/current.dat"));
        launch(args);
    }
}
