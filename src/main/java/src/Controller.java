package src;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {
    @FXML TextField valueMinField;
    @FXML TextField valueMaxField;
    @FXML TextField filepathField;
    @FXML TextField singleValueField;

    @FXML CheckBox countCheckbox;
    @FXML CheckBox minCheckbox;
    @FXML CheckBox maxCheckbox;
    @FXML CheckBox meanCheckbox;
    @FXML CheckBox medianCheckbox;
    @FXML CheckBox modeCheckbox;
    @FXML CheckBox percentileCheckbox;
    @FXML CheckBox valuesCheckbox;
    @FXML CheckBox graphCheckbox;

    @FXML Label amountValue;
    @FXML Label minValue;
    @FXML Label maxValue;
    @FXML Label meanValue;
    @FXML Label medianValue;
    @FXML Label modeValue;

    @FXML ScrollPane valuesPane;
    @FXML Pane graphPane;
    @FXML Pane percentilePane;

    @FXML ScrollPane errorDisplayWrapper;
    @FXML VBox errorDisplayContent;

    GradeAnalyzer gradeAnalyzer;

    @FXML
    public void initialize() {
        gradeAnalyzer = new GradeAnalyzer();
    }


    @FXML
    public void selectFileExplorer(ActionEvent event) {
        File file;
        FileChooser fileChooser;

        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Grades File");

        file = fileChooser.showOpenDialog(filepathField.getScene().getWindow()); // let user select file

        this.filepathField.setText(file.toString()); // change text field to reflect selected file
    }
}
