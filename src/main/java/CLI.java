/*
 * @author  Robert Nordman
 *          ASUID: 1215721572
 *          Class: Arizona State University CSE360 - #70641
 *          Assignment #: Group Project - Grade Analyzer
 *
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * CLI.java - A command line interface for GradeAnalyzer.
 * <p>
 * This class is responsible for reacting to any user CLI inputs.
 * Additionally it reads input from the command line
 *      and alters it's outputs depending on these values.
 * Also, it keeps track of every action performed so that it can make a log file
 *      if required.
 * Lastly, it is responsible for handling all Exceptions thrown at any stage,
 *      showing an error message.
 *
 *
 * Based off of and heavily inspired by GUI.java.
 * Not optimized for CLI interactions.  Main purpose is for testing therefore refactoring is not a priority.
 *
 *
 * @author Robert Nordman
 * @version 1.2
 */
public class CLI {
    private String valueMinField;
    private String valueMaxField;
    private String filepathField;
    private String singleValueField;
    private String filepathFieldOut;


    private boolean countCheckbox;
    private boolean minCheckbox;
    private boolean maxCheckbox;
    private boolean meanCheckbox;
    private boolean medianCheckbox;
    private boolean modeCheckbox;
    private boolean percentileCheckbox;
    private boolean valuesCheckbox;
    private boolean graphCheckbox;

    private String amountValue;
    private String minValue;
    private String maxValue;
    private String meanValue;
    private String medianValue;
    private String modeValue;

    private GradeAnalyzer gradeAnalyzer;
    private boolean boundariesLocked;
    private LinkedList<String> recordOfActions;
    private boolean errorState;

    public static void main(String[] args){
        CLI cli = new CLI();
        cli.driver();
    }

    /**
     * Driver function for CLI
     * <p>
     * Reads and responds to user input
     */
    private void driver(){
        Scanner scan = new Scanner(System.in);
        Scanner subscan;
        String inputLine;

        showMenu();
        do {
            inputLine = scan.nextLine();


            if(inputLine.contains("SET_BOUNDARIES")){
                inputLine = inputLine.substring(14);
                subscan = new Scanner(inputLine);
                this.valueMinField = ""+subscan.nextFloat();
                this.valueMaxField = ""+subscan.nextFloat();
                this.setBoundaries();
            }else if(inputLine.contains("LOAD_FILE")){
                this.filepathField = inputLine.substring(10);
                this.loadFile();
            }else if(inputLine.contains("APPEND_FILE")){
                this.filepathField = inputLine.substring(12);
                this.appendFile();
            }else if(inputLine.contains("ADD_VALUE")){
                this.singleValueField = inputLine.substring(10);
                this.addValue();
            }else if(inputLine.contains("DELETE_VALUE")){
                this.singleValueField = inputLine.substring(13);
                this.deleteValue();
            }else if(inputLine.equals("CLEAR")){
                this.reset();
            }else if(inputLine.contains("SET_COUNT")){
                this.countCheckbox = inputLine.contains("1");
            }else if(inputLine.contains("SET_MINIMUM")){
                this.minCheckbox = inputLine.contains("1");
            }else if(inputLine.contains("SET_MAXIMUM")){
                this.maxCheckbox = inputLine.contains("1");
            }else if(inputLine.contains("SET_MEAN")){
                this.meanCheckbox = inputLine.contains("1");
            }else if(inputLine.contains("SET_MEDIAN")){
                this.medianCheckbox = inputLine.contains("1");
            }else if(inputLine.contains("SET_MODE")){
                this.modeCheckbox = inputLine.contains("1");
            }else if(inputLine.contains("SET_PERCENTILES")){
                this.percentileCheckbox = inputLine.contains("1");
            }else if(inputLine.contains("SET_VALUES")){
                this.valuesCheckbox = inputLine.contains("1");
            }else if(inputLine.contains("SET_GRAPH")){
                this.graphCheckbox = inputLine.contains("1");
            }else if(inputLine.contains("CREATE_REPORT")){
                this.filepathFieldOut = inputLine.substring(14);
                this.createReport();
            }else if(inputLine.equals("ANALYZE")){
                this.analyze();
            }
        } while(! inputLine.equals("EXIT"));


    }

    /**
     * Show user menu
     * */
    private void showMenu(){
        System.out.println(
                "What would you like to do?\n" +
                        "SET_BOUNDARIES [lower] [upper]: Set upper and lower boundaries\n" +
                        "LOAD_FILE [filepath]: Load file, overwrite dataset\n" +
                        "APPEND_FILE [filepath]: Append file data to dataset\n" +
                        "ADD_VALUE [value]: Add one value to dataset\n" +
                        "DELETE_VALUE [value]: Delete single value from dataset\n" +
                        "CLEAR: Clear dateset\n" +
                        "SET_COUNT [0/1]: Change count state\n" +
                        "SET_MINIMUM [0/1]: Change minimum state\n" +
                        "SET_MAXIMUM [0/1]: Change maximum state\n" +
                        "SET_MEAN [0/1]: Change mean state\n" +
                        "SET_MEDIAN [0/1]: Change median state\n" +
                        "SET_MODE [0/1]: Change mode state\n" +
                        "SET_PERCENTILES [0/1]: Change percentiles state\n" +
                        "SET_VALUES [0/1]: Change values display state\n" +
                        "SET_GRAPH [0/1]: Change graph state\n" +
                        "CREATE_REPORT [filepath]: Make a report of session\n" +
                        "ANALYZE: Analyze dataset\n" +
                        "EXIT: Leave program\n"
        );
    }



    /**
     * Initialize everything that is needed
     * <p>
     * prepare for user inputs
     */
    private CLI() {
        gradeAnalyzer = new GradeAnalyzer();
        recordOfActions = new LinkedList<>();
        boundariesLocked = false;
        errorState = false;

        valueMinField = "0";
        valueMaxField = "100";
        filepathField = "";
        singleValueField = "";
        filepathFieldOut = "";

        countCheckbox = false;
        minCheckbox = false;
        maxCheckbox = false;
        meanCheckbox = false;
        medianCheckbox = false;
        modeCheckbox = false;
        percentileCheckbox = false;
        valuesCheckbox = false;
        graphCheckbox = false;

        amountValue = "0";
        minValue = "Not calculated";
        maxValue = "Not calculated";
        meanValue = "Not calculated";
        medianValue = "Not calculated";
        modeValue = "Not calculated";


        recordAction("GradeAnalyzer begun.\n");
    }

    /**
     * Reset all properties as if GradeAnalyzer had just been opened (delete current data)
     */
    private void reset(){
        // unlock upper and lower boundaries
        boundariesLocked = false;
        // Clear existing data
        gradeAnalyzer.clearValues();
        errorState = false;
        showNothing();

        recordAction("Clear current dataset.\n");
    }

    /**
     * Load file in filepathField
     * <p>
     * Loads the set file.  Handles any errors in loading.
     * Overwrite current dataset.
     * */
    private void loadFile(){
        this.reset();

        if(!boundariesLocked){
            setBoundaries();  // update boundaries upper and lower
        }

        try {
            // load file
            gradeAnalyzer.loadFile(this.filepathField);
            recordAction(String.format("Load file: %s\n",this.filepathField));
        } catch (Exception e){
            recordAction(String.format("Fail to load file: %s\n",this.filepathField));
            showError(e);  // show any exceptions that occur
        }

    }
    /**
     * Load file in filepathField
     * <p>
     * Loads the set file.  Handles any errors in loading.
     * Adds to current dataset.
     * */
    private void appendFile(){
        try {
            // load file
            gradeAnalyzer.loadFile(this.filepathField);
            recordAction(String.format("Append file: %s\n",this.filepathField));
        } catch (Exception e){
            recordAction(String.format("Fail to append file: %s\n",this.filepathField));
            showError(e);  // show any exceptions that occur
        }
    }
    /**
     * Add a single value to the dataset
     * <p>
     * Adds to single value to the current dataset.
     * Handles any errors in adding it.
     * */
    private void addValue(){
        if(!boundariesLocked){
            setBoundaries();  // update boundaries upper and lower
        }

        try {
            gradeAnalyzer.addValue(Double.parseDouble(singleValueField)); // add this one value
            recordAction(String.format("Add data value: %s\n",singleValueField));
            errorState=false;
        } catch (NumberFormatException e) { // wrong data type
            recordAction(String.format("Fail add data value: %s\n",singleValueField));
            showError(new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value.")); // if it isn't a number
        } catch (Exception e) {
            recordAction(String.format("Fail add data value: %s\n",singleValueField));
            showError(e); // any other exceptions
        }
    }
    /**
     * Delete a single value to the dataset
     * <p>
     * Deletes the first occurrence of a single value from the current dataset.
     * Handles any errors in removing it.
     * */
    private void deleteValue(){
        try {
            double toDelete = Double.parseDouble(singleValueField);

            if(!gradeAnalyzer.getValues().contains(toDelete)){
                recordAction(String.format("Fail delete data value: %s\n",singleValueField));
                showError(new RuntimeException("Unable to delete data.")); // if it isn't a number
                errorState=false;
            }else{
                gradeAnalyzer.deleteValue(toDelete); // delete this one value
                recordAction(String.format("Delete data value: %s\n",singleValueField));
            }
        } catch (NumberFormatException e) { // wrong data type
            recordAction(String.format("Fail delete data value: %s\n",singleValueField));
            showError(new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value.")); // if it isn't a number
        } catch (Exception e) {
            recordAction(String.format("Fail delete data value: %s\n",singleValueField));
            showError(e); // any other exceptions
        }
    }
    /**
     * Create a text report of all actions performed
     * <p>
     * Write a text report of the session to filepathFieldOut.
     * */
    private void createReport(){
        BufferedWriter fileWriter;
        Iterator<String> iter;

        try {
            fileWriter = new BufferedWriter(new FileWriter(new File(filepathFieldOut)));

            iter = recordOfActions.iterator();
            while(iter.hasNext()){
                fileWriter.append(iter.next());
            }

            fileWriter.close();


            recordAction(String.format("Create report to: %s\n",filepathFieldOut));
        } catch (IOException e) {
            recordAction(String.format("Fail create report to: %s\n",filepathFieldOut));
            e.printStackTrace();
        } catch (NullPointerException ignored){} // don't do anything here - not an issue
    }
    /**
     * Run analysis on all selected functions
     * <p>
     * Runs analysis and shows output for all set functions
     * */
    private void analyze(){
        try {
            if(gradeAnalyzer.getAmount()==0) {
                throw new RuntimeException("Dataset does not exist. Cannot perform analysis functions without a dataset.");
            }else if(errorState){
                System.out.println("Cannot analyze while there are errors.");
            }else{
                if (countCheckbox)
                    displayAmount();
                if (minCheckbox)
                    displayMin();
                if (maxCheckbox)
                    displayMax();
                if (meanCheckbox)
                    displayMean();
                if (medianCheckbox)
                    displayMedian();
                if (modeCheckbox)
                    displayMode();
                if (valuesCheckbox)
                    displayValues();
                if (graphCheckbox)
                    displayGraph();
                if (percentileCheckbox)
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
            min = Double.parseDouble(this.valueMinField);
            max = Double.parseDouble(this.valueMaxField);

            if(min >= max){ // range is invalid
                throw new RuntimeException("Max must be greater than min for range");
            }

            // set upper and lower boundaries for reading files later
            this.gradeAnalyzer.setLowerBound(min);
            this.gradeAnalyzer.setUpperBound(max);

            // lock upper and lower boundaries - can't change anymore
            this.boundariesLocked = true;


            recordAction(String.format("Set boundaries: %s to %s\n", valueMinField, valueMaxField));
        } catch (NumberFormatException e) { // wrong data type
            recordAction(String.format("Failed set boundaries: %s to %s\n", valueMinField, valueMaxField));
            showError(new RuntimeException("Data point(s) is of the wrong data type. Please enter an integer or decimal value for range. (Boundaries have wrong data type)")); // if it isn't a number
        } catch (Exception e) {
            recordAction(String.format("Failed set boundaries: %s to %s\n", valueMinField, valueMaxField));
            showError(e); // any other exceptions
        }
    }
    /**
     * Show error to screen
     * <p>
     * Show an error to the screen.  Open error log section of screen and write most recent error to it
     * */
    private void showError(Exception e){
        // remove existing and add new error message
        System.out.println(e.getMessage());
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
     * Set everything to not calculated
     * <p>
     * Removes all calculations and ASCII graphics displayed.  Set as if program just stared
     */
    private void showNothing(){
        // clear everything now that there are no values
        this.amountValue = "0";
        this.minValue = "Not calculated";
        this.maxValue = "Not calculated";
        this.meanValue = "Not calculated";
        this.medianValue = "Not calculated";
        this.modeValue = "Not calculated";

    }

    /**
     * Show the size of the dataset.
     * */
    private void displayAmount(){
        amountValue = (""+gradeAnalyzer.getAmount());
        recordAction(String.format("Show amount: %s\n", amountValue));
        System.out.print(String.format("Show amount: %s\n", amountValue));
    }
    /**
     * Show the minimum value in the dataset.
     * */
    private void displayMin(){
        minValue = (String.format("%.3f",gradeAnalyzer.getMinimum()));
        recordAction(String.format("Show minimum value: %s\n", minValue));
        System.out.print(String.format("Show minimum value: %s\n", minValue));
    }
    /**
     * Show the maximum value in the dataset.
     * */
    private void displayMax(){
        maxValue = (String.format("%.3f",gradeAnalyzer.getMaximum()));
        recordAction(String.format("Show maximum value: %s\n", maxValue));
        System.out.print(String.format("Show maximum value: %s\n", maxValue));
    }
    /**
     * Show the mean of the dataset.
     * */
    private void displayMean(){
        meanValue = (String.format("%.3f",gradeAnalyzer.getMean()));
        recordAction(String.format("Show mean: %s\n", meanValue));
        System.out.print(String.format("Show mean: %s\n", meanValue));
    }
    /**
     * Show the median of the dataset.
     * */
    private void displayMedian(){
        medianValue = (String.format("%.3f",gradeAnalyzer.getMedian()));
        recordAction(String.format("Show median: %s\n", medianValue));
        System.out.print(String.format("Show median: %s\n", medianValue));
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


        modeValue = (modeOutput);
        recordAction(String.format("Show Mode(s): %s\n", modeValue));
        System.out.print(String.format("Show Mode(s): %s\n", modeValue));
    }
    /**
     * Show all the values in the dataset on screen
     * <p>
     * Shows all the values in the dataset (With one value after the decimal point)
     * in a list.  5 columns of values in increasing order top-down-left-right
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
        ArrayList<LinkedList<String>> allValues = new ArrayList<>();
        LinkedList<Double> copyOfValues;
        int groupSize, remainingBigGroups;
        double averageGroupSize;
        StringBuilder valuesAll;
        String string;

        copyOfValues = new LinkedList<>(gradeAnalyzer.getValues()); // a copy of the increasing values (so we can safely pop values of list)
        for(int i=0;i<5; i++){
            allValues.add(i, new LinkedList<>());
        }

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
            if(remainingBigGroups == 0){
                groupSize = groupSize-1;
            }

            for(int j=0; j<groupSize; j++){ // insert all values for this column
                allValues.get(i).add(String.format("%.1f",copyOfValues.pop()));
            }

            remainingBigGroups--;
        }

        /* Record data for log */
        valuesAll = new StringBuilder();
        for(int i=0; i<groupSize+1; i++){ // each row
            for(int j=0; j<5; j++){ // each column
                try{
                    string = allValues.get(j).get(i);

                    valuesAll.append(String.format("%s        ",string));
                }catch (Exception ignored){}
            }
            valuesAll.append("\n");

        }
        recordAction(String.format("Show values:\n%s\n", valuesAll.toString()));

        System.out.print(String.format("Show values:\n%s", valuesAll.toString()));
    }
    /**
     * Create and show an ASCII histogram of the dataset (10 groups wide)
     * <p>
     * Show a histogram of the dataset that is 10 bins wide.
     * Have the axis represent the range of the data.
     * */
    private void displayGraph(){
        List<Double> graphData = gradeAnalyzer.getGraph();
        double minTick,tickWidth,currentTick;
        StringBuilder asciiGraph = new StringBuilder();


        // Display axis info
        minTick = graphData.get(10);
        tickWidth = graphData.get(12);

        asciiGraph.append(String.format("%.2f\n",minTick));
        currentTick = minTick;
        // color pixels corresponding to %-ages in histogram as shades of gray
        for(int i=0; i<10; i++){
            asciiGraph.append("      ");
            for(int j=0; j<graphData.get(i)*100; j++){
                asciiGraph.append("X");
            }
            currentTick += tickWidth;
            asciiGraph.append(String.format("\n%.2f\n",currentTick));
        }

        /* Record data for log */
        recordAction(String.format("Show graph:\n%s\n", asciiGraph.toString()));
        System.out.print((String.format("Show graph:\n%s", asciiGraph.toString())));
    }
    /**
     * Show average grades for each percentile
     * <p>
     * The average grade for the bottom 10%, the next 10%, etc. up to the top 10%
     * */
    private void displayPercentiles(){
        LinkedList<Double> percentileGrades = gradeAnalyzer.getPercentiles();
        StringBuilder allPercentiles = new StringBuilder();

        for(int i=0; i<10; i++){
            allPercentiles.append(String.format("The average grade for the %d%% to %d%% range is %.2f\n",i*10,i*10+10,percentileGrades.get(i)));
        }

        /* Record for log */
        recordAction(String.format("Show percentiles:\n%s", allPercentiles.toString()));
        System.out.print(allPercentiles.toString());
    }
}