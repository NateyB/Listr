package com.natebeckemeyer.projects.schedulrgui.graphics;

import com.natebeckemeyer.projects.schedulrgui.core.DynamicRuleParser;
import com.natebeckemeyer.projects.schedulrgui.task.OnCompletion;
import com.natebeckemeyer.projects.schedulrgui.task.Rule;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created for Schedulr by @author Nate Beckemeyer on 2016-06-23.
 */
public class AddRulePopupController
{
    /**
     * The TextField object which users use to name the task.
     */
    @FXML
    private TextField addRuleNameField;

    /**
     * The TextField object which users use to specify labels (separated by spaces).
     */
    @FXML
    private TextField addRuleImportField;

    /**
     * The ChoiceBox from which the user selects the behavior for the new rule.
     */
    @FXML
    private ChoiceBox<String> addRuleChoiceBox;

    /**
     * The actual code that will be compiled to file.
     */
    @FXML
    private TextArea addRuleCodeField;

    /**
     * Creates the Task from the population of the input parameters, and then inserts it into Schedulr's task listing.
     */
    @FXML
    private void addRuleButtonClicked()
    {
        String imports = addRuleImportField.getText();

        Scanner importParser = new Scanner(imports);
        LinkedList<String> importList = new LinkedList<>();

        while (importParser.hasNext())
        {
            importList.add(importParser.next());
        }

        DynamicRuleParser.compileAndLoadRule(addRuleNameField.getText(), addRuleCodeField.getText(), importList);

        ((Stage) addRuleChoiceBox.getScene().getWindow()).close();
    }

    /**
     * Specifies the initialization behavior for this popup; specifically, it specifies the default populations where
     * necessary.
     */
    @FXML
    private void initialize()
    {
        String defaultVal = Rule.class.getSimpleName();
        addRuleChoiceBox.setItems(FXCollections.observableArrayList(defaultVal, OnCompletion.class.getSimpleName()));
        addRuleChoiceBox.setValue(defaultVal);
    }

}