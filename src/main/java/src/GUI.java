package src;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;

public class GUI extends Application {
    VBox root;
    Scene scene;
    Stage stage;

    Label countValue, minValue, maxValue, meanValue, medianValue, modeValue;
    VBox graphDisplay, percentilesDisplay;
    ScrollPane valuesDisplay, errorsDisplay;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception{
        this.countValue = new Label();
        this.minValue = new Label();
        this.maxValue = new Label();
        this.meanValue = new Label();
        this.medianValue = new Label();
        this.modeValue = new Label();
        this.valuesDisplay = new ScrollPane();
        this.valuesDisplay.setStyle("-fx-background-color: #ece4b7");
        this.graphDisplay = new VBox();
        this.graphDisplay.setStyle("-fx-background-color: #c7ac92");
        this.percentilesDisplay = new VBox();
        this.percentilesDisplay.setStyle("-fx-background-color: #ece4b7");
        this.errorsDisplay = new ScrollPane();

        /*
        // <TEST> values for example
        VBox h = new VBox();
        for(int i=0; i<20; i++){
            Text t1 = new Text((300-i)+"  "+(300-5-i)+"  "+(300-10-i)+"  "+(300-15-i)+"  "+(300-20-i));
            h.getChildren().add(t1);
        }
        this.valuesDisplay.setContent(h);
        int val = 0;
        for(int i=0; i<10; i++){
            switch (i){
                case 0:
                    val = 2;
                    break;
                case 1:
                    val = 4;
                    break;
                case 2:
                    val = 6;
                    break;
                case 3:
                    val = 8;
                    break;
                case 4:
                    val = 10;
                    break;
                case 5:
                    val = 10;
                    break;
                case 6:
                    val = 12;
                    break;
                case 7:
                    val = 14;
                    break;
                case 8:
                    val = 16;
                    break;
                case 9:
                    val = 18;
                    break;
            }

            Text t = new Text(""+i*10+" to "+(i+1)*10+": "+val+"%");
            this.percentilesDisplay.getChildren().add(t);
        }

        for(int i=0; i<10; i++){
            String xs = "";
            switch (i){
                case 0:
                    xs = "██";
                    break;
                case 1:
                    xs = "████";
                    break;
                case 2:
                    xs = "██████";
                    break;
                case 3:
                    xs = "████████";
                    break;
                case 4:
                    xs = "██████████";
                    break;
                case 5:
                    xs = "██████████";
                    break;
                case 6:
                    xs = "████████████";
                    break;
                case 7:
                    xs = "██████████████";
                    break;
                case 8:
                    xs = "████████████████";
                    break;
                case 9:
                    xs = "██████████████████";
                    break;
            }
            Text t = new Text(xs);
            this.graphDisplay.getChildren().add(t);
        }

        this.maxValue.setText("300");
        this.minValue.setText("261");
        this.countValue.setText("80");
        // </TEST>
        /**/
    }

    @Override
    public void start(Stage primaryStage) {
        this.root = new VBox();
        this.scene = new Scene(root, 1600, 900); // default dimensions
        this.stage = primaryStage;

        this.root.setStyle("-fx-background-color: #ff0000");

        primaryStage.setTitle("Grade Analyzer"); // title of window

        HBox top = new HBox();
        VBox topLeft = new VBox();
        VBox topRight = new VBox();
        VBox bottom = new VBox();

        VBox.setVgrow(bottom, Priority.ALWAYS);  // bottom should expand to fill extra space

        topLeft.setStyle("-fx-background-color: #ece4b7");
        topRight.setStyle("-fx-background-color: #c7ac92");
        bottom.setStyle("-fx-background-color: #fcd581");

        initializeLeftArea(topLeft);    // top left     - inputs
        initializeRightArea(topRight);  // top right    - analyze options
        initializeBottomArea(bottom);   // bottom area  - output

        top.getChildren().addAll(topLeft,topRight);
        HBox.setHgrow(topLeft, Priority.ALWAYS);    // share total width 50-50
        HBox.setHgrow(topRight, Priority.ALWAYS);   // share total width 50-50

        root.getChildren().addAll(top,bottom,this.errorsDisplay);

        this.errorsDisplay.setMaxHeight(0);
        this.errorsDisplay.setMinHeight(0);
        this.errorsDisplay.setPrefHeight(0);

        primaryStage.setScene(scene);
        primaryStage.show();

/*
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(primaryStage);
*/
    }

    private void initializeLeftArea(VBox modifyMe) {
        Label title, singleValueLabel, rangeLabel, dashLabel, filenameLabel;
        TextField lowerBoundInput, upperBoundInput, filepathInput, singleValueInput;
        Button selectFileButton, loadfileButton, appendButton, deleteButton;
        HBox rangeRow, filenameRow, singleValueRow;
        Pane spacer1, spacer2;

        // Create all components for this section of the screen labels and inputs
        title = new Label("Input");
        singleValueLabel = new Label("Add/Remove once:");
        rangeLabel = new Label("Value Range:");
        dashLabel = new Label(" to ");
        filenameLabel = new Label("Filepath:");
        lowerBoundInput = new TextField("0");
        upperBoundInput = new TextField("100");
        filepathInput = new TextField();
        singleValueInput = new TextField();
        selectFileButton = new Button("...");
        loadfileButton = new Button("Load");
        appendButton = new Button("+");
        deleteButton = new Button("–");
        spacer1 = new Pane();
        spacer2 = new Pane();

        appendButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                errorsDisplay.setPrefHeight(300);
                errorsDisplay.setMinHeight(300);
                errorsDisplay.maxHeight(300);

                VBox h = new VBox();
                h.getChildren().add(new Label("ERROR"));
                h.getChildren().add(new Label("Failed to read input file.  Error on line 123"));

                errorsDisplay.setContent(h);
            }
        });

        // Rows for format positioning
        rangeRow = new HBox();
        filenameRow = new HBox();
        singleValueRow = new HBox();

        title.setFont(new Font(20.0)); // 30px font size

        // fill rows with components
        rangeRow.getChildren().addAll(rangeLabel, lowerBoundInput, dashLabel, upperBoundInput);
        filenameRow.getChildren().addAll(filenameLabel, filepathInput, selectFileButton, loadfileButton);
        singleValueRow.getChildren().addAll(singleValueLabel, singleValueInput, appendButton, deleteButton);

        // spacing and center align all
        modifyMe.setAlignment(Pos.CENTER);
        modifyMe.setSpacing(10);
        rangeRow.setAlignment(Pos.CENTER);
        rangeRow.setSpacing(5);
        filenameRow.setAlignment(Pos.CENTER);
        filenameRow.setSpacing(5);
        singleValueRow.setAlignment(Pos.CENTER);
        singleValueRow.setSpacing(5);

        // Set spacers to split extra space with component evenly
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        // and everything to this screen section
        modifyMe.getChildren().addAll(title, spacer1, rangeRow, filenameRow, singleValueRow, spacer2);
        modifyMe.setPadding(new Insets(10,10,10,10)); // 10px padding all around
    }
    private void initializeRightArea(VBox modifyMe) {
        Label title;
        CheckBox outputMinCheckbox, outputMaxCheckbox, outputMeanCheckbox,
            outputMedianCheckbox, outputModeCheckbox, outputCountCheckbox,
            outputPercentileCheckbox, outputValuesCheckbox, outputGraphCheckbox;
        Button goButton, createReportButton;
        VBox options;
        HBox buttons;

        // Create all components for this section of the screen checkboxes and buttons
        title = new Label("Analyze");
        goButton = new Button("Analyze");
        outputMinCheckbox = new CheckBox("Output minimum");
        outputMaxCheckbox = new CheckBox("Output maximum");
        outputMeanCheckbox = new CheckBox("Output mean");
        outputMedianCheckbox = new CheckBox("Output median");
        outputModeCheckbox = new CheckBox("Output mode");
        outputCountCheckbox = new CheckBox("Output count");
        outputPercentileCheckbox = new CheckBox("Output percentiles");
        outputValuesCheckbox = new CheckBox("Output values");
        outputGraphCheckbox = new CheckBox("Output graph");
        createReportButton = new Button("Create report");
        // row and columns for formating
        buttons = new HBox();
        options = new VBox();

        // spacing and center align all
        modifyMe.setAlignment(Pos.CENTER);
        modifyMe.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(5);
        options.setAlignment(Pos.CENTER);
        options.setSpacing(5);

        title.setFont(new Font(20.0)); // 30px font size

        // add all checkboxes
        options.getChildren().addAll(outputCountCheckbox, outputMinCheckbox, 
            outputMaxCheckbox, outputMeanCheckbox, outputMedianCheckbox,
            outputModeCheckbox, outputPercentileCheckbox, outputValuesCheckbox,
            outputGraphCheckbox);

        // and the buttons
        buttons.getChildren().addAll(createReportButton, goButton);

        // and everything to this screen section
        modifyMe.getChildren().addAll(title, options, buttons);
        modifyMe.setPadding(new Insets(10,10,10,10)); // 10px padding all around
    }
    private void initializeBottomArea(VBox modifyMe){
        Label title, countLabel, minLabel, maxLabel, meanLabel, medianLabel, modeLabel,
            displayLabel, detailsLabel;
        HBox mainOutput, splitRightColumn;
        GridPane leftColumnContents;
        VBox leftColumn, centerColumn, rightColumn;
        Pane spacer1, spacer2;

        // Create all components for this section of the screen checkboxes and buttons
        title = new Label("Output");
        countLabel = new Label("Amount: ");
        minLabel = new Label("Min: ");
        maxLabel = new Label("Max: ");
        meanLabel = new Label("Mean: ");
        medianLabel = new Label("Median: ");
        modeLabel = new Label("Mode: ");
        displayLabel = new Label("Values: ");
        detailsLabel = new Label("Details:");
        spacer1 = new Pane();
        spacer2 = new Pane();
        // Rows and columns for spacing and positioning
        mainOutput = new HBox();
        splitRightColumn = new HBox();
        leftColumnContents = new GridPane();
        leftColumn = new VBox();
        centerColumn = new VBox();
        rightColumn = new VBox();


        // spacing fill extra space
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        title.setFont(new Font(20.0)); // 30px font size
        title.setPadding(new Insets(0,0,10,0)); // 10px spacing below

        // main 3 sections of output
        mainOutput.getChildren().addAll(leftColumn, centerColumn, rightColumn);
        // expand left and right to fill extra space
        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);

        // left column add grid for output and label
        leftColumn.getChildren().addAll(detailsLabel,leftColumnContents, spacer1);
        leftColumn.setAlignment(Pos.CENTER); // center align elements
        // and all labels to the left section
        leftColumnContents.addRow(0, countLabel ,this.countValue);
        leftColumnContents.addRow(1, minLabel ,this.minValue);
        leftColumnContents.addRow(2, maxLabel ,this.maxValue);
        leftColumnContents.addRow(3, meanLabel ,this.meanValue);
        leftColumnContents.addRow(4, medianLabel ,this.medianValue);
        leftColumnContents.addRow(5, modeLabel ,this.modeValue);

        // center column
        centerColumn.getChildren().addAll(displayLabel, this.valuesDisplay); // label and scroll area
        centerColumn.setAlignment(Pos.CENTER); // center align

        // split right column in 2
        rightColumn.getChildren().addAll(splitRightColumn);
        // split space equally for 2 element in right column
        splitRightColumn.getChildren().addAll(this.graphDisplay, this.percentilesDisplay);
        HBox.setHgrow(this.graphDisplay, Priority.ALWAYS);
        HBox.setHgrow(this.percentilesDisplay, Priority.ALWAYS);

        // temp sizes
        this.valuesDisplay.setPrefSize(200, 200);
        this.percentilesDisplay.setPrefSize(200, 200);
        this.graphDisplay.setPrefSize(200, 200);

        // align all center and padding
        modifyMe.setAlignment(Pos.CENTER);
        modifyMe.setPadding(new Insets(10,10,10,10));
        // and everything to this screen section
        modifyMe.getChildren().addAll(title, mainOutput, spacer2);
    }

}

/*
    /home/general/jdk-13/bin/java --module-path /home/general/javafx-sdk-11.0.2/lib --add-modules=javafx.controls -Dfile.encoding=UTF-8 @/tmp/cp_9hq8wixwmz81os68n0z4x0h2o.argfile src.GUI
*/