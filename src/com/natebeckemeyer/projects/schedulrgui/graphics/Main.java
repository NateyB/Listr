package com.natebeckemeyer.projects.schedulrgui.graphics;

import com.natebeckemeyer.projects.schedulrgui.core.Parser;
import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Schedulr");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        Schedulr.addTasks(Parser.readTasksFromFile("tasks/current.dat"));

        /*
        Scanner console = new Scanner(System.in);

        while (console.hasNextLine())
        {
            String input = console.nextLine();
            if (input.equals("QUIT"))
                return;

            Schedulr.getTasksMatchingRule(Parser.processInput(input)).forEach(System.out::println);
            System.out.println();
        }*/

        launch(args);
    }
}
