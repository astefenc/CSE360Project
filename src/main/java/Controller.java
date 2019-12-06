import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

 /**
 * Controller.java - The primary controller for GradeAnalyzer.
 * <p>
 * This controller is responsible for reacting to any button presses.
 * Additionally it reads input from the textboxes and other input fields
 *      and alters it's outputs depending on these values.
 * Also, it keeps track of every action performed so that it can make a log file
 *      if required.
 * Lastly, it is responsible for handling all Exceptions thrown at any stage,
 *      showing an error message in the error log (shown at bottom of the window).
 *
 *
 * @author Robert Nordman
 * @version 1.5
 */
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
    @FXML VBox percentileVBox;

    @FXML ScrollPane errorDisplayWrapper;
    @FXML VBox errorDisplayContent;

    private GradeAnalyzer gradeAnalyzer;
    private boolean boundariesLocked;
    private LinkedList<String> recordOfActions;
    private VBox axis,graphical;
    private boolean errorState;


    /**
     * Initialize everything that is needed
     * <p>
     * Create private objects.
     * And generate graph area of screen (no data yet, but make components to be used)
     */
    @FXML
    public void initialize() {
        gradeAnalyzer = new GradeAnalyzer();
        recordOfActions = new LinkedList<>();
        boundariesLocked = false;
        errorState = false;


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

        /*
        *   Generate percentile region of the screen
        */
        for(int i=0; i<10; i++){
            percentileVBox.getChildren().add(new Label());
        }


        recordAction("GradeAnalyzer begun.\n");
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
        showNothing();
        errorState = false;


        boundariesLocked = false;
        recordAction("Clear current dataset.\n");
    }


    /**
     * Allow user to select file to load data from
     * <p>
     * Opens a standard file explorer to allow user to select file to load dataset from
     * */
    @FXML
    public void selectFileExplorer() {
        File file;
        FileChooser fileChooser;

        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Grades File");

        file = fileChooser.showOpenDialog(filepathField.getScene().getWindow()); // let user select file

        this.filepathField.setText(file.toString()); // change text field to reflect selected file
    }
     /**
      * Load file in file input textbox
      * <p>
      * Loads the set file.  Handles any errors in loading.
      * Overwrite current dataset.
      * */
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
            recordAction(String.format("Load file: %s\n",this.filepathField.getText()));
        } catch (Exception e){
            recordAction(String.format("Fail to load file: %s\n",this.filepathField.getText()));
            showError(e);  // show any exceptions that occur
        }

    }
     /**
      * Load file in file input textbox
      * <p>
      * Loads the set file.  Handles any errors in loading.
      * Adds to current dataset.
      * */
    @FXML
    public void appendFile(){
        showNothing();
        try {
            // hide error display
            this.errorDisplayWrapper.setMinHeight(0);
            this.errorDisplayWrapper.setPrefHeight(0);
            this.errorDisplayWrapper.setMaxHeight(0);

            // load file
            gradeAnalyzer.loadFile(this.filepathField.getText());
            recordAction(String.format("Append file: %s\n",this.filepathField.getText()));
            errorState = false;
        } catch (Exception e){
            recordAction(String.format("Fail append file: %s\n",this.filepathField.getText()));
            showError(e);  // show any exceptions that occur
        }
    }
     /**
      * Add a single value to the dataset
      * <p>
      * Adds to single value to the current dataset.
      * Handles any errors in adding it.
      * */
    @FXML
    public void addValue(){
        if(!boundariesLocked){
            setBoundaries();  // update boundaries upper and lower
        }

        try {
            gradeAnalyzer.addValue(Double.parseDouble(singleValueField.getText())); // add this one value
            recordAction(String.format("Add data value: %s\n",singleValueField.getText()));
            errorState=false;
        } catch (NumberFormatException e) { // wrong data type
            recordAction(String.format("Fail add data value: %s\n",singleValueField.getText()));
            showError(new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value.")); // if it isn't a number
        } catch (Exception e) {
            recordAction(String.format("Fail add data value: %s\n",singleValueField.getText()));
            showError(e); // any other exceptions
        }
    }
     /**
      * Delete a single value to the dataset
      * <p>
      * Deletes the first occurrence of a single value from the current dataset.
      * Handles any errors in removing it.
      * */
    @FXML
    public void deleteValue(){
        try {
            double toDelete = Double.parseDouble(singleValueField.getText());

            if(!gradeAnalyzer.getValues().contains(toDelete)){
                recordAction(String.format("Fail delete data value: %s\n",singleValueField.getText()));
                showError(new RuntimeException("Unable to delete data.")); // if it isn't a number
                errorState=false;
            }else{
                gradeAnalyzer.deleteValue(toDelete); // delete this one value
                recordAction(String.format("Delete data value: %s\n",singleValueField.getText()));
            }
        } catch (NumberFormatException e) { // wrong data type
            recordAction(String.format("Fail delete data value: %s\n",singleValueField.getText()));
            showError(new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value.")); // if it isn't a number
        } catch (Exception e) {
            recordAction(String.format("Fail delete data value: %s\n",singleValueField.getText()));
            showError(e); // any other exceptions
        }
    }
     /**
      * Create a .txt report of all actions performed
      * <p>
      * Opens a file navigator to allow user to set where to save the sessions report.
      * */
    @FXML
    public void createReport(){
        File file;
        FileChooser fileChooser;
        BufferedWriter fileWriter;
        Iterator<String> iter;

        fileChooser = new FileChooser();
        fileChooser.setTitle("Where would you like to save this report to?");
        file = fileChooser.showSaveDialog(filepathField.getScene().getWindow()); // let user select file

        try {
            fileWriter = new BufferedWriter(new FileWriter(file));

            iter = recordOfActions.iterator();
            while(iter.hasNext()){
                fileWriter.append(iter.next());
            }

            fileWriter.close();


            recordAction(String.format("Create report to: %s\n",file.toString()));
        } catch (IOException e) {
            recordAction(String.format("Fail create report to: %s\n",file.toString()));
            e.printStackTrace();
        } catch (NullPointerException ignored){} // don't do anything here - not an issue
    }
     /**
      * Run analysis on all selected functions
      * <p>
      * Runs analysis and shows output for all checked functions
      * */
    @FXML
    public void analyze(){
        try {
            if(gradeAnalyzer.getAmount()==0) {
                throw new RuntimeException("Dataset does not exist. Cannot perform analysis functions without a dataset.");
            }else if(errorState){
                this.errorDisplayContent.getChildren().add(new Label("\nCannot analyze while there are errors."));
            }else{
                if (countCheckbox.isSelected())
                    displayAmount();
                if (minCheckbox.isSelected())
                    displayMin();
                if (maxCheckbox.isSelected())
                    displayMax();
                if (meanCheckbox.isSelected())
                    displayMean();
                if (medianCheckbox.isSelected())
                    displayMedian();
                if (modeCheckbox.isSelected())
                    displayMode();
                if (valuesCheckbox.isSelected())
                    displayValues();
                if (graphCheckbox.isSelected())
                    displayGraph();
                if (percentileCheckbox.isSelected())
                    displayPercentiles();
            }
        }catch (Exception e){
            recordAction("Fail analyze: \n");
            showError(e);
        }

    }


     /**
      * Set and lock in boundaries
      * <p>
      * Reads upper and lower bounds and either shows error (invalid limits)
      * or locks the limits for further operations.
      * */
    private void setBoundaries(){
        double min,max;

        try {
            // Get min and max and range between them
            min = Double.parseDouble(this.valueMinField.getText());
            max = Double.parseDouble(this.valueMaxField.getText());

            if(min >= max){ // range is invalid
                throw new RuntimeException("Max must be greater than min for range");
            }

            // set upper and lower boundaries for reading files later
            this.gradeAnalyzer.setLowerBound(min);
            this.gradeAnalyzer.setUpperBound(max);

            // lock upper and lower boundaries - can't change anymore
            valueMinField.setDisable(true);
            valueMaxField.setDisable(true);
            this.boundariesLocked = true;


            recordAction(String.format("Set boundaries: %s to %s\n", valueMinField.getText(), valueMaxField.getText()));
        } catch (NumberFormatException e) { // wrong data type
            recordAction(String.format("Failed set boundaries: %s to %s\n", valueMinField.getText(), valueMaxField.getText()));
            showError(new RuntimeException("Data point(s) is of the wrong data type. Please enter an integer or decimal value for range. (Boundaries have wrong data type)")); // if it isn't a number
        } catch (Exception e) {
            recordAction(String.format("Failed set boundaries: %s to %s\n", valueMinField.getText(), valueMaxField.getText()));
            showError(e); // any other exceptions
        }
    }
     /**
      * Show error to screen
      * <p>
      * Show an error to the screen.  Open error log section of screen and write most recent error to it
      * */
    private void showError(Exception e){
        // show error display
        this.errorDisplayWrapper.setMinHeight(300);
        this.errorDisplayWrapper.setPrefHeight(300);
        this.errorDisplayWrapper.setMaxHeight(300);

        // remove existing and add new error message
        this.errorDisplayContent.getChildren().clear();
        this.errorDisplayContent.getChildren().add(new Label(e.getMessage()));
        recordAction(String.format("Show error log.  Error: %s\n", e.getMessage()));

        errorState=true;
    }
     /**
      * Record an action in the log
      * <p>
      * Make a record of the action performed in case user wants a log
      * */
    private void recordAction(String action){
        recordOfActions.add(action);
    }
     /***
      * Show nothing on screen
      * <p>
      * Removes all calculations and graphics displayed.  Show as if program just stared
      */
     private void showNothing(){
         // clear everything now that there are no values
         clearGraph();
         clearValues();
         clearPercentiles();
         amountValue.setText("0");
         minValue.setText("Minimum not calculated");
         maxValue.setText("Maximum not calculated");
         meanValue.setText("Mean not calculated");
         medianValue.setText("Median not calculated");
         modeValue.setText("Mode not calculated");
     }

    /**
     * Show the size of the dataset.
     * */
    private void displayAmount(){
        amountValue.setText(""+gradeAnalyzer.getAmount());
        recordAction(String.format("Show amount: %s\n", amountValue.getText()));
    }
     /**
      * Show the minimum value in the dataset.
      * */
    private void displayMin(){
        minValue.setText(String.format("%.3f",gradeAnalyzer.getMinimum()));
        recordAction(String.format("Show minimum value: %s\n", minValue.getText()));
    }
     /**
      * Show the maximum value in the dataset.
      * */
    private void displayMax(){
        maxValue.setText(String.format("%.3f",gradeAnalyzer.getMaximum()));
        recordAction(String.format("Show maximum value: %s\n", maxValue.getText()));
    }
     /**
      * Show the mean of the dataset.
      * */
    private void displayMean(){
        meanValue.setText(String.format("%.3f",gradeAnalyzer.getMean()));
        recordAction(String.format("Show mean: %s\n", meanValue.getText()));
    }
     /**
      * Show the median of the dataset.
      * */
    private void displayMedian(){
        medianValue.setText(String.format("%.3f",gradeAnalyzer.getMedian()));
        recordAction(String.format("Show median: %s\n", medianValue.getText()));
    }
     /**
      * Show the mode(s) of the dataset.
      * */
    private void displayMode(){
        List<Double> modes;
        StringBuilder concat;
        String modeOutput;

        modeOutput = "";
        concat = new StringBuilder();

        try{
            modes = gradeAnalyzer.getMode();

            for (Double mode : modes)
                concat.append(String.format("%.3f",mode)).append(", ");

            modeOutput = concat.substring(0, concat.length()-2);
        }catch(RuntimeException e){
            if(e.getMessage().equals("No mode")) {
                modeOutput = "No mode";
            }else{
                showError(e);
            }
        }


        modeValue.setText(modeOutput);
        recordAction(String.format("Show Mode(s): %s\n", modeValue.getText()));
    }
    /**
     * Show all the values in the dataset on screen
     * <p>
     * Shows all the values in the dataset (With one value after the decimal point)
     * in a scrollable list.  5 columns of values in increasing order top-down-left-right
     *
     * E.g
     *    1    7     13    19    24
     *    2    8     14    20    25
     *    3    9     15    21    26
     *    4    10    16    22    27
     *    5    11    17    23    28
     *    6    12    18
     * */
    private void displayValues(){
        HBox contentsOfValuesPane;
        LinkedList<Double> copyOfValues;
        VBox column;
        int groupSize, remainingBigGroups;
        double averageGroupSize;
        StringBuilder valuesAll;
        Label label;

        copyOfValues = new LinkedList<>(gradeAnalyzer.getValues()); // a copy of the increasing values (so we can safely pop values of list)
        contentsOfValuesPane = new HBox();

        averageGroupSize = copyOfValues.size()/5.0;
        groupSize = ((int) (averageGroupSize+.01)); // add .01 to handle off floating point errors
        remainingBigGroups = 10; // is changed to 1-5 later


        if(copyOfValues.size() == 0){
            return;
        }

        copyOfValues.sort(Double::compareTo); // sort values


        //  Determine how far off groupSize and averageGroupSize are to determine if different columns need to be
        // different lengths. (Using odd decimal values to prevent Floating Point value errors)
        //
        // Increment groupSize by One in some cases to handle different size groups
        // mark remainingBigGroups to indicate how many of these bigger groups there are
        if (averageGroupSize-groupSize < .1){
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


        for(int i=0; i<5; i++){ // for each column
            column = new VBox();
            column.setSpacing(5);
            if(remainingBigGroups == 0){
                groupSize = groupSize-1;
            }

            for(int j=0; j<groupSize; j++){ // insert all values for this column
                label=new Label(String.format("%.1f",copyOfValues.pop()));
                column.getChildren().add(label);
            }

            remainingBigGroups--;

            contentsOfValuesPane.getChildren().add(column);
        }


        contentsOfValuesPane.setSpacing(15);


        valuesPane.setContent(contentsOfValuesPane);
        valuesPane.setPrefHeight(300);


        /* Record data for log */
        valuesAll = new StringBuilder();
        for(int i=0; i<groupSize+1; i++){ // each row
            for(int j=0; j<5; j++){ // each column
                try{
                    column = (VBox) contentsOfValuesPane.getChildren().get(j);
                    label = (Label) column.getChildren().get(i);

                    valuesAll.append(String.format("%s        ",label.getText()));
                }catch (Exception ignored){}
            }
            valuesAll.append("\n");

        }
        recordAction(String.format("Show values:\n%s\n", valuesAll.toString()));
    }
    /**
     * Show a histogram of the dataset (10 groups wide)
     * <p>
     * Show a histogram of the dataset that is 10 bins wide.
     * Update the axis to represent the range of the data.
     * */
    private void displayGraph(){
        List<Double> graphData = gradeAnalyzer.getGraph();
        HBox row;
        Region pixel;
        double minTick,maxTick,tickWidth,currentTick,tickRange;
        int valuesAfterTheDecimalPoint;
        StringBuilder asciiGraph = new StringBuilder();

        // Display axis info
        minTick = graphData.get(10);
        maxTick = graphData.get(11);
        tickWidth = graphData.get(12);

        currentTick = minTick;
        tickRange = maxTick - minTick;


        // Use tickRange (range of valid values) to determine how many decimal points to use
        valuesAfterTheDecimalPoint = 0;
        if(tickRange < .1){
            valuesAfterTheDecimalPoint = 3;
        }else if(tickRange < 1){
            valuesAfterTheDecimalPoint = 2;
        }else if(tickRange < 10){
            valuesAfterTheDecimalPoint = 1;
        }

        for(int i=0; currentTick<=maxTick; i++, currentTick+=tickWidth){
            Label axisMarker = (Label) axis.getChildren().get(i);
            axisMarker.setText(String.format("%."+valuesAfterTheDecimalPoint+"f",currentTick));
        }

        // Color all pixels white
        for(int i=0; i<10; i++) { // 10 high
            row = (HBox) graphical.getChildren().get(i);
            for (int j=0; j<100; j++) { // 100 wide
                pixel = (Region) row.getChildren().get(j);
                pixel.setStyle("-fx-background-color: #ffffff");
            }
        }

        asciiGraph.append(String.format("%.2f\n",minTick));
        currentTick = minTick;
        // color pixels corresponding to %-ages in histogram as shades of gray
        for(int i=0; i<10; i++){
            row = (HBox) graphical.getChildren().get(i);
            String color = grayShade((float) (((float) i)/10*.8+.2));
            asciiGraph.append("      ");
            for(int j=0; j<graphData.get(i)*100; j++){
                pixel = (Region) row.getChildren().get(j);
                pixel.setStyle("-fx-background-color: #"+color);
                asciiGraph.append("X");
            }

            currentTick += tickWidth;
            asciiGraph.append(String.format("\n%.2f\n",currentTick));
        }

        /* Record data for log */
        recordAction(String.format("Show graph:\n%s\n", asciiGraph.toString()));
    }
    /**
     * Show average grades for each percentile
     * <p>
     * The average grade for the bottom 10%, the next 10%, etc. up to the top 10%
     * */
    private void displayPercentiles(){
        LinkedList<Double> percentileGrades = gradeAnalyzer.getPercentiles();
        Label currentLabel;
        StringBuilder allPercentiles = new StringBuilder();

        for(int i=0; i<10; i++){
            currentLabel = (Label) percentileVBox.getChildren().get(i);
            currentLabel.setText(String.format("The average grade for the %d%% to %d%% range is %.2f",i*10,i*10+10,percentileGrades.get(i)));
            allPercentiles.append(currentLabel.getText()).append("\n");
        }

        /* Record for log */
        recordAction(String.format("Show percentiles:\n%s", allPercentiles.toString()));
    }



     /**
      * Clear all the values in the dataset on screen
      * <p>
      * Removes all values from scrollable list of values
      * */
     private void clearGraph(){
         HBox row;
         Region pixel;

         // Color all pixels white
         for(int i=0; i<10; i++) { // 10 high
             row = (HBox) graphical.getChildren().get(i);
             for (int j=0; j<100; j++) { // 100 wide
                 pixel = (Region) row.getChildren().get(j);
                 pixel.setStyle("-fx-background-color: #ffffff");
             }
         }}
     /**
      * Clears the histogram of the dataset
      * <p>
      * Remove the data from the histogram
      * Set all pixels white and clear axis labels
      * */
     private void clearValues(){
         valuesPane.setContent(null);
     }
     /**
      * Clear the average grades for each percentile
      * <p>
      * Clear out this section of the screen
      * */
     private void clearPercentiles(){
         Label currentLabel;

         for(int i=0; i<10; i++){
             currentLabel = (Label) percentileVBox.getChildren().get(i);
             currentLabel.setText("");
         }
     }




    /**
     * Get a string of the hexcode that is percentBlack % black.
     * <p>
     * Generates a string represting the hexcode of the gray shade that
     * is percentBlack % of the way between white(#ffffff) and black (#000000)
     *
     * @return Hexcode of gray shade (without #)
     * */
    private static String grayShade(float percentBlack){
        String grayHex;

        grayHex = Integer.toHexString(255- ((int) (255 * percentBlack)));

        if(grayHex.length()==1){
            grayHex = "0"+grayHex;
        }

        return grayHex+grayHex+grayHex;
    }




}
