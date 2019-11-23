package src;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.*;

public class GUI extends Application {
    BorderPane root;
    Scene scene;

    Label countValue, minValue, maxValue, meanValue, medianValue, modeValue;
    VBox valuesDisplay, graphDisplay, percentilesDisplay;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.countValue = new Label();
        this.minValue = new Label();
        this.maxValue = new Label();
        this.meanValue = new Label();
        this.medianValue = new Label();
        this.modeValue = new Label();
        this.valuesDisplay = new VBox();
        this.valuesDisplay.setStyle("-fx-background-color: #ece4b7");
        this.graphDisplay = new VBox();
        this.graphDisplay.setStyle("-fx-background-color: #c7ac92");
        this.percentilesDisplay = new VBox();
        this.percentilesDisplay.setStyle("-fx-background-color: #ece4b7");

        this.root = new BorderPane();
        this.scene = new Scene(root, 1600, 900);

        primaryStage.setTitle("Grade Analyzer");

        VBox left = new VBox();
        VBox right = new VBox();
        VBox bottom = new VBox();
        VBox errorsDisplay = new VBox();

        left.setStyle("-fx-background-color: #ece4b7");
        right.setStyle("-fx-background-color: #c7ac92");
        bottom.setStyle("-fx-background-color: #fcd581");
        errorsDisplay.setStyle("-fx-background-color: #c7ac92");
        errorsDisplay.setMinHeight(100);

        initializeLeftArea(left);
        initializeRightArea(right);
        initializeBottomArea(bottom);

        root.setBottom(bottom);
        root.setLeft(left);
        root.setRight(right);

        primaryStage.setScene(scene);
        primaryStage.show();

        left.setPrefHeight(primaryStage.getHeight() / 2);
        left.setPrefWidth(primaryStage.getWidth() / 2);
        right.setPrefHeight(primaryStage.getHeight() / 2);
        right.setPrefWidth(primaryStage.getWidth() / 2);
        bottom.setPrefHeight(primaryStage.getHeight() / 2);

        // Keep elements height at proper percentages
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                ((Pane) (root.getChildren().get(0))).setPrefHeight(arg2.intValue() / 2);
                ((Pane) (root.getChildren().get(1))).setPrefHeight(arg2.intValue() / 2);
                ((Pane) (root.getChildren().get(2))).setPrefHeight(arg2.intValue() / 2);
            }
        });
        // Keep elements width at proper percentages
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                ((Pane) (root.getChildren().get(0))).setPrefWidth(arg2.intValue() / 2);
                ((Pane) (root.getChildren().get(1))).setPrefWidth(arg2.intValue() / 2);
                ((Pane) (root.getChildren().get(2))).setPrefWidth(arg2.intValue());
            }
        });

    }

    public void initializeLeftArea(VBox modifyMe) {
        Label title, rangeLabel, dashLabel, filenameLabel;
        TextField lowerBoundInput, upperBoundInput, filepathInput, singleValueInput;
        Button appendButton, deleteButton;
        HBox rangeRow, filenameRow, singleValueRow;

        title = new Label("Input");
        rangeLabel = new Label("Range:");
        dashLabel = new Label(" to ");
        filenameLabel = new Label("Filename:");
        lowerBoundInput = new TextField();
        upperBoundInput = new TextField();
        filepathInput = new TextField();
        singleValueInput = new TextField();
        appendButton = new Button("+");
        deleteButton = new Button("-");

        rangeRow = new HBox();
        filenameRow = new HBox();
        singleValueRow = new HBox();

        rangeRow.getChildren().addAll(rangeLabel, lowerBoundInput, dashLabel, upperBoundInput);
        filenameRow.getChildren().addAll(filenameLabel, filepathInput);
        singleValueRow.getChildren().addAll(singleValueInput, appendButton, deleteButton);

        modifyMe.getChildren().addAll(title, rangeRow, filenameRow, singleValueRow);
    }

    public void initializeRightArea(VBox modifyMe) {
        Label title;
        CheckBox outputMinCheckbox, outputMaxCheckbox, outputMeanCheckbox,
            outputMedianCheckbox, outputModeCheckbox, outputCountCheckbox,
            outputPercentileCheckbox, outputValuesCheckbox, outputGraphCheckbox;
        Button goButton, createReportButton;
        VBox options;
        HBox buttons;

        modifyMe.setAlignment(Pos.CENTER); // align all to center
        modifyMe.setSpacing(20);

        buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(5);

        options = new VBox();
        options.setAlignment(Pos.CENTER);
        options.setSpacing(5);

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

        options.getChildren().addAll(outputCountCheckbox, outputMinCheckbox, 
            outputMaxCheckbox, outputMeanCheckbox, outputMedianCheckbox,
            outputModeCheckbox, outputPercentileCheckbox, outputValuesCheckbox,
            outputGraphCheckbox);

        buttons.getChildren().addAll(createReportButton, goButton);

        modifyMe.getChildren().addAll(title, options, buttons);
    }
    public void initializeBottomArea(VBox modifyMe){
        Label title, countLabel, minLabel, maxLabel, meanLabel, medianLabel, modeLabel,
            displayLabel;
        HBox mainOutput, splitRightColumn;
        GridPane leftColumnContents;
        VBox leftColumn, centerColumn, rightColumn;
        
        title = new Label("Output");

        mainOutput = new HBox();
        splitRightColumn = new HBox();
        leftColumnContents = new GridPane();
        leftColumn = new VBox();
        centerColumn = new VBox();
        rightColumn = new VBox();

        countLabel = new Label("Amount: ");
        minLabel = new Label("Min: ");
        maxLabel = new Label("Max: ");
        meanLabel = new Label("Mean: ");
        medianLabel = new Label("Median: ");
        modeLabel = new Label("Mode: ");
        displayLabel = new Label("Values: ");

        mainOutput.getChildren().addAll(leftColumn, centerColumn, rightColumn);

        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        HBox.setHgrow(centerColumn, Priority.ALWAYS);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);

        leftColumn.getChildren().add(leftColumnContents);

        leftColumnContents.addRow(0, countLabel ,this.countValue);
        leftColumnContents.addRow(1, minLabel ,this.minValue);
        leftColumnContents.addRow(2, maxLabel ,this.maxValue);
        leftColumnContents.addRow(3, meanLabel ,this.meanValue);
        leftColumnContents.addRow(4, medianLabel ,this.medianValue);
        leftColumnContents.addRow(5, modeLabel ,this.modeValue);

        centerColumn.getChildren().addAll(displayLabel, this.valuesDisplay);

        rightColumn.getChildren().addAll(splitRightColumn);

        splitRightColumn.getChildren().addAll(this.graphDisplay, this.percentilesDisplay);
        HBox.setHgrow(this.graphDisplay, Priority.ALWAYS);
        HBox.setHgrow(this.percentilesDisplay, Priority.ALWAYS);

        this.valuesDisplay.setPrefSize(200, 200);
        this.percentilesDisplay.setPrefSize(200, 200);
        this.graphDisplay.setPrefSize(200, 200);

        modifyMe.getChildren().addAll(title, mainOutput);
    }


}

/*
    /home/general/jdk-13/bin/java --module-path /home/general/javafx-sdk-11.0.2/lib --add-modules=javafx.controls -Dfile.encoding=UTF-8 @/tmp/cp_9hq8wixwmz81os68n0z4x0h2o.argfile src.GUI
*/