/**
 * @author  Robert Nordman
 *          ASUID: 1215721572
 *          Class: Arizona State University CSE360 - #70641
 *          Assignment #: Group Project - Grade Analyzer
 *
 * Controller.java - The primary controller for GradeAnalyzer.
 */


/**
 * Controller.java - The primary controller for GradeAnalyzer.
 * <p>
 * This controller is responsible for reacting to any button presses.
 * Additionally it reads input from the textboxes and other input fields
 *      and alters it's out depending on these values.
 * Also, it keeps track of every action performed so that it can make a log file
 *      if required.
 * Lastly, it is responsible for handling all Exceptions thrown at any stage,
 *      showing an error message in the error log (shown at bottom of the window).
 *
 *
 * @author Robert Nordman
 * @version 1.4
 */

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
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

    private GradeAnalyzer gradeAnalyzer;
    private boolean setGraphAxis;
    private boolean boundariesLocked;
    private LinkedList<String> recordOfActions;
    private VBox axis,graphical;


    /**
     * Initialize everything that is needed
     * <p>
     * Create private objects.
     * And generate graph area of screen (no axis yet)
     */
    @FXML
    public void initialize() {
        gradeAnalyzer = new GradeAnalyzer();
        recordOfActions = new LinkedList<>();
        setGraphAxis = false;
        boundariesLocked = false;


        /*
        *  Generate Graph area of screen
        */
        HBox row;
        Region pixel;
        graphical = new VBox();
        axis = new VBox();


        /* Generate graph axis */
        for(double i=0; i<=10; i++){
            Label axisMarker = new Label();
            axisMarker.setFont(new Font(12));
            axisMarker.setPadding(new Insets(0,0,8,0));
            axis.getChildren().add(axisMarker);
        }
        axis.setAlignment(Pos.TOP_RIGHT);

        /* Generate graph */
        // fill graph as 100x10 pixel 'array' of sorts.
        for(int i=0; i<10; i++) { // 10 high
            row = new HBox();
            for (int j=0; j<100; j++) { // 100 wide
                pixel = new Region();
                pixel.setPrefSize(4,23);
                HBox.setHgrow(pixel, Priority.ALWAYS);
                pixel.setStyle("-fx-background-color: #ffffff");
                row.getChildren().add(pixel);
            }
            graphical.getChildren().add(row);
        }

        graphical.setPadding(new Insets(10,0,0,10));
        graphPane.getChildren().addAll(axis, graphical);
    }

    /**
     * Reset all properties as if GradeAnalyzer had just been opened (delete current data)
     */
    @FXML
    public void reset(){
        // unlock upper and lower boundaries
        valueMinField.setDisable(false);
        valueMaxField.setDisable(false);
        // Clear existing data
        gradeAnalyzer.clearValues();
        // clear existing graph
        displayGraph(); // should be empty now

        boundariesLocked = false;
    }
    @FXML
    public void selectFileExplorer() {
        File file;
        FileChooser fileChooser;

        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Grades File");

        file = fileChooser.showOpenDialog(filepathField.getScene().getWindow()); // let user select file

        this.filepathField.setText(file.toString()); // change text field to reflect selected file
    }
    @FXML
    public void loadFile(){
        this.reset();

        if(!boundariesLocked){
            setBoundaries();  // update boundaries upper and lower
        }

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
    public void appendFile(){
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
    public void addValue(){
        if(!boundariesLocked){
            setBoundaries();  // update boundaries upper and lower
        }

        try {
            gradeAnalyzer.addValue(Double.parseDouble(singleValueField.getText())); // add this one value
        } catch (NumberFormatException e) { // wrong data type
            showError(new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value.")); // if it isn't a number
        } catch (Exception e) {
            showError(e); // any other exceptions
        }
    }
    @FXML
    public void deleteValue(){
        try {
            gradeAnalyzer.deleteValue(Double.parseDouble(singleValueField.getText())); // delete this one value
        } catch (NumberFormatException e) { // wrong data type
            showError(new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value.")); // if it isn't a number
        } catch (Exception e) {
            showError(e); // any other exceptions
        }
    }
    @FXML
    public void createReport(){

    }
    @FXML
    public void analyze(){
        if(countCheckbox.isSelected())
            displayAmount();
        if(minCheckbox.isSelected())
            displayMin();
        if(maxCheckbox.isSelected())
            displayMax();
        if(meanCheckbox.isSelected())
            displayMean();
        if(medianCheckbox.isSelected())
            displayMedian();
        if(modeCheckbox.isSelected())
            displayMode();
        if(valuesCheckbox.isSelected())
            displayValues();
        if(graphCheckbox.isSelected())
            displayGraph();
        if(percentileCheckbox.isSelected())
            displayPercentiles();
    }

    private void setBoundaries(){
        double min,max,range,step;

        try {
            // Get min and max and range between them
            min = Double.parseDouble(this.valueMinField.getText());
            max = Double.parseDouble(this.valueMaxField.getText());
            range = max-min;
            step = range/10;

            if(range<=0){ // range is invalid
                throw new RuntimeException("Max must be greater than min for range");
            }

            // set upper and lower boundaries for reading files later
            this.gradeAnalyzer.setLowerBound(min);
            this.gradeAnalyzer.setUpperBound(max);
            // lock upper and lower boundaries - can't change anymore
            valueMinField.setDisable(true);
            valueMaxField.setDisable(true);
            this.boundariesLocked = true;


            /* Generate graph axis */
            double j = min;
            for(int i=0; j<=max; i++, j+=step){
                Label axisMarker = (Label) axis.getChildren().get(i);
                axisMarker.setText(String.format("%.2f",j));
            }
        } catch (NumberFormatException e) { // wrong data type
            showError(new RuntimeException("Data point(s) is of the wrong data type. Please enter an integer or decimal value for range. (Boundaries have wrong data type)")); // if it isn't a number
        } catch (Exception e) {
            showError(e); // any other exceptions
        }
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


    private void displayAmount(){
        minValue.setText(""+gradeAnalyzer.getAmount());
    }
    private void displayMin(){
        minValue.setText(""+gradeAnalyzer.getMinimum());
    }
    private void displayMax(){
        minValue.setText(""+gradeAnalyzer.getMaximum());
    }
    private void displayMean(){
        minValue.setText(""+gradeAnalyzer.getMean());
    }
    private void displayMedian(){
        minValue.setText(""+gradeAnalyzer.getMedian());
    }
    private void displayMode(){
        List<Double> modes;
        StringBuilder concat;
        String modeOutput;

        modeOutput = "";
        concat = new StringBuilder();

        try{
            modes = gradeAnalyzer.getMode();

            for (Double mode : modes)
                concat.append(mode).append(", ");

            modeOutput = concat.substring(0, concat.length()-2);
        }catch(RuntimeException e){
            if(e.getMessage().equals("No mode")) {
                modeOutput = "No mode";
            }else{
                showError(e);
            }
        }


        minValue.setText(modeOutput);
    }
    private void displayValues(){
        HBox contentsOfValuesPane;
        LinkedList<Double> copyOfValues;
        VBox column;
        int groupSize, remainingBigGroups;
        double averageGroupSize;

        copyOfValues = new LinkedList<>(gradeAnalyzer.getValues());
        contentsOfValuesPane = new HBox();

        averageGroupSize = copyOfValues.size()/5.0;
        groupSize = ((int) (averageGroupSize+.01)); // add .01 to handle off floating point errors
        remainingBigGroups = 5;



        //  Determine how far off groupSize and averageGroupSize are to determine if different columns need to be
        // different lengths. (Using ranges to handle Floating Point value errors)
        //
        // Increment groupSize by One in some cases to handle different size groups
        // mark remainingBigGroups to indicate how many of these bigger groups there are
        if(averageGroupSize-groupSize < .1){ // They are equal
                                             // .1 to account for floating point errors - if they are meaningfully
                                             // different they will be off by .2 or more (since we divided by 5)
            remainingBigGroups = 5;
        }else if (averageGroupSize-groupSize < .3){ // off by 1
            remainingBigGroups = 1;
            groupSize = groupSize+1;
        }else if (averageGroupSize-groupSize < .5){ // off by 2
            remainingBigGroups = 2;
            groupSize = groupSize+1;
        }else if (averageGroupSize-groupSize < .7){ // off by 3
            remainingBigGroups = 3;
            groupSize = groupSize+1;
        }else if (averageGroupSize-groupSize < .9){ // off by 4
            remainingBigGroups = 4;
            groupSize = groupSize+1;
        }


        // sort values increasing order
        copyOfValues.sort(Double::compareTo);

        for(int i=0; i<5; i++){ // for each column
            column = new VBox();
            column.setSpacing(5);
            if(remainingBigGroups == 0){
                groupSize = groupSize-1;
            }

            for(int j=0; j<groupSize; j++){
                column.getChildren().add(new Label(""+copyOfValues.pop()));
            }

            remainingBigGroups--;

            contentsOfValuesPane.getChildren().add(column);
        }


        contentsOfValuesPane.setSpacing(15);


        valuesPane.setContent(contentsOfValuesPane);
        valuesPane.setPrefHeight(300);

    }
    private void displayGraph(){
        List<Double> graphData = gradeAnalyzer.getGraph();
        HBox row;
        Region pixel;

        // Color all pixels white
        for(int i=0; i<10; i++) { // 10 high
            row = (HBox) graphical.getChildren().get(i);
            for (int j=0; j<100; j++) { // 100 wide
                pixel = (Region) row.getChildren().get(j);
                pixel.setStyle("-fx-background-color: #ffffff");
            }
        }

        // color pixels corresponding to %-ages in histogram as shades of gray
        for(int i=0; i<10; i++){
            row = (HBox) graphical.getChildren().get(i);
            String color = grayShade((float) (((float) i)/10*.8+.2));
            for(int j=0; j<graphData.get(i)*100; j++){
                pixel = (Region) row.getChildren().get(j);
                pixel.setStyle("-fx-background-color: #"+color);
            }
        }
    }
    private void displayPercentiles(){

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




}
