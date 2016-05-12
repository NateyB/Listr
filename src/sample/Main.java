package sample;

import com.natebeckemeyer.projects.schedulrgui.core.Schedulr;
import com.natebeckemeyer.projects.schedulrgui.task.Rule;
import com.natebeckemeyer.projects.schedulrgui.task.Task;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application
{

    /**
     * This is the prefix for all of the rules that will be instantiated by name.
     */
    private static String prefix = "com.natebeckemeyer.projects.schedulrgui.task";

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        //launch(args);
        LinkedList<Task> test = new LinkedList<>();
        test.add(new Task("Upgrade Schedulr", new GregorianCalendar(2016, 4, 12)));

        Rule today = Schedulr.getRule("Today");
        List<Task> display = test.stream().filter(today).collect(Collectors.toList());

        for (Task item : display)
        {
            System.out.println(item);
        }
    }
}
