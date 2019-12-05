import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

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
    @FXML HBox graphPane;
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
    @FXML
    public void loadFile(ActionEvent event){
        gradeAnalyzer.clearValues(); // clear existing dataset

        updateBoundaries();  // update boundaries upper and lower

        try {
            // hide error display
            this.errorDisplayWrapper.setMinHeight(0);
            this.errorDisplayWrapper.setPrefHeight(0);
            this.errorDisplayWrapper.setMaxHeight(0);

            // load file
            gradeAnalyzer.loadFile(this.filepathField.getText());
        } catch (Exception e){
            showError(e);  // show any exceptions that occur
        }
    }
    @FXML
    public void appendFile(ActionEvent event){
        try {
            // hide error display
            this.errorDisplayWrapper.setMinHeight(0);
            this.errorDisplayWrapper.setPrefHeight(0);
            this.errorDisplayWrapper.setMaxHeight(0);

            // load file
            gradeAnalyzer.loadFile(this.filepathField.getText());
        } catch (Exception e){
            showError(e);  // show any exceptions that occur
        }
    }
    @FXML
    public void addValue(ActionEvent event){
        updateBoundaries();  // update boundaries upper and lower


        try {
            gradeAnalyzer.addValue(Double.parseDouble(singleValueField.getText())); // add this one value
        } catch (NumberFormatException e) { // wrong data type
            showError(new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value.")); // if it isn't a number
        } catch (Exception e) {
            showError(e); // any other exceptions
        }
    }
    @FXML
    public void deleteValue(ActionEvent event){
        try {
            gradeAnalyzer.deleteValue(Double.parseDouble(singleValueField.getText())); // delete this one value
        } catch (NumberFormatException e) { // wrong data type
            showError(new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value.")); // if it isn't a number
        } catch (Exception e) {
            showError(e); // any other exceptions
        }
    }


    private void updateBoundaries(){
        // set upper and lower boundaries
        this.gradeAnalyzer.setLowerBound(Double.parseDouble(this.valueMinField.getText()));
        this.gradeAnalyzer.setUpperBound(Double.parseDouble(this.valueMaxField.getText()));
        // lock upper and lower boundaries
        valueMinField.setDisable(true);
        valueMaxField.setDisable(true);
    }


    @FXML
    public void displayGraph(ActionEvent event){
        List<Double> graphData = gradeAnalyzer.getGraph();
        HBox row;
        VBox graphical, axis;
        Region pixel;

        graphical = new VBox();
        axis = new VBox();

        // clear old
        graphPane.getChildren().clear();

        /* Generate graph axis */
        for(double i=graphData.get(0); i<=graphData.get(1); i+=graphData.get(2)){
            Label axisMarker = new Label(""+i);
            axisMarker.setFont(new Font(12));
            axisMarker.setPadding(new Insets(0,0,8,0));
            axis.getChildren().add(axisMarker);
        }
        axis.setAlignment(Pos.TOP_RIGHT);


        /* Generate graph */
        // fill graph as 100x10 pixel 'array' of sorts.  Color all pixels white
        for(int i=0; i<10; i++) { // 10 high
            row = new HBox();
            for (int j=0; j<100; j++) { // 100 wide
                pixel = new Region();
                pixel.setPrefSize(4,23);
                pixel.setStyle("-fx-background-color: #ffffff");
                HBox.setHgrow(pixel, Priority.ALWAYS);
                row.getChildren().add(pixel);
            }
            graphical.getChildren().add(row);
        }
        // color pixels corresponding to %-ages in histogram as shades of gray
        for(int i=0; i<10; i++){
            row = (HBox) graphical.getChildren().get(i);
            String color = grayShade((float) (((float) i)/10*.8+.2));
            for(int j=0; j<graphData.get(i+3)*100; j++){
                pixel = (Region) row.getChildren().get(j);
                pixel.setStyle("-fx-background-color: #"+color);
            }
        }
        graphical.setPadding(new Insets(10,0,0,10));

        graphPane.getChildren().addAll(axis, graphical);
    }

    // Return a hexcode representing a shade of gray that is percentBlack % black.
    private static String grayShade(float percentBlack){
        String grayHex;

        grayHex = Integer.toHexString(255- ((int) (255 * percentBlack)));

        if(grayHex.length()==1){
            grayHex = "0"+grayHex;
        }

        return grayHex+grayHex+grayHex;
    }



    private void showError(Exception e){
        // show error display
        this.errorDisplayWrapper.setMinHeight(300);
        this.errorDisplayWrapper.setPrefHeight(300);
        this.errorDisplayWrapper.setMaxHeight(300);

        // remove existing and add new error message
        this.errorDisplayContent.getChildren().clear();
        this.errorDisplayContent.getChildren().add(new Label(e.getMessage()));
    }
}
