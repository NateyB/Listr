package com.natebeckemeyer.projects.schedulrgui.graphics;

import com.natebeckemeyer.projects.schedulrgui.core.DynamicRuleParser;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-23.
 */
public class AddBehaviorPopupController
{
    /**
     * The TextField object which users use to name the rule.
     */
    @FXML
    private TextField addRuleNameField;

    /**
     * The TextField object which users use to specify which interfaces this rule implements.
     */
    @FXML
    private TextField ruleImplementsField;

    /**
     * The TextField object in which users specify the class that this rule extends.
     */
    @FXML
    private TextField ruleExtendsField;

    /**
     * The TextField object which users use to specify imports (separated by spaces).
     */
    @FXML
    private TextField addRuleImportField;

    /**
     * The actual code that will be compiled to the rule file.
     */
    @FXML
    private TextArea addRuleCodeField;

    /**
     * The TextField object which users use to name the rule.
     */
    @FXML
    private TextField addCompletionNameField;

    /**
     * The TextField object which users use to specify which interfaces this rule implements.
     */
    @FXML
    private TextField completionImplementsField;

    /**
     * The TextField object in which users specify the class that this rule extends.
     */
    @FXML
    private TextField completionExtendsField;

    /**
     * The TextField object which users use to specify imports (separated by spaces).
     */
    @FXML
    private TextField addCompletionImportField;

    /**
     * The actual code that will be compiled to the rule file.
     */
    @FXML
    private TextArea addCompletionCodeField;

    /**
     * Creates the Rule from the population of the input parameters, and then loads it.
     */
    @FXML
    private void addRuleButtonClicked()
    {
        // Prepare imports
        String imports = addRuleImportField.getText();
        Scanner importParser = new Scanner(imports);
        LinkedList<String> importList = new LinkedList<>();
        while (importParser.hasNext())
        {
            importList.add(importParser.next());
        }
        importParser.close();

        String implementations = ruleImplementsField.getText();
        Scanner implementationsParser = new Scanner(implementations);
        LinkedList<String> implementationsList = new LinkedList<>();
        while (implementationsParser.hasNext())
        {
            implementationsList.add(implementationsParser.next());
        }
        implementationsParser.close();

        DynamicRuleParser.compileAndLoadRule(addRuleNameField.getText(), implementationsList,
                ruleExtendsField.getText(),
                addRuleCodeField.getText(), importList);
        MainWindowController.getInstance().updateSidebar();

        ((Stage) addRuleCodeField.getScene().getWindow()).close();
    }

    /**
     * Creates the CompletionBehavior behavior from the population of the input parameters, and then loads it.
     */
    @FXML
    private void addCompletionButtonClicked()
    {
        // Prepare imports
        String imports = addCompletionImportField.getText();
        Scanner importParser = new Scanner(imports);
        LinkedList<String> importList = new LinkedList<>();
        while (importParser.hasNext())
        {
            importList.add(importParser.next());
        }
        importParser.close();

        String implementations = completionImplementsField.getText();
        Scanner implementationsParser = new Scanner(implementations);
        LinkedList<String> implementationsList = new LinkedList<>();
        while (implementationsParser.hasNext())
        {
            implementationsList.add(implementationsParser.next());
        }
        implementationsParser.close();

        DynamicRuleParser.compileAndLoadCompletionBehavior(addCompletionNameField.getText(), implementationsList,
                completionExtendsField.getText(),
                addCompletionCodeField.getText(), importList);
        MainWindowController.getInstance().updateSidebar();

        ((Stage) addRuleCodeField.getScene().getWindow()).close();
    }

    /**
     * Specifies the initialization behavior for this popup; specifically, it specifies the default populations where
     * necessary.
     */
    @FXML
    private void initialize()
    {
    }

}