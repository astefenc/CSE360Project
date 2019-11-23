package src;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.*;

public class GUI extends Application {
    BorderPane root;
    Scene scene;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.root = new BorderPane();
        this.scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Grade Analyzer");

        VBox left = new VBox();
        VBox right = new VBox();
        VBox bottom = new VBox();

        left.setStyle("-fx-background-color: #ff0000");
        right.setStyle("-fx-background-color: #00ff00");
        bottom.setStyle("-fx-background-color: #0000ff");

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
                ((Pane)(root.getChildren().get(0))).setPrefHeight(arg2.intValue()/2);
                ((Pane)(root.getChildren().get(1))).setPrefHeight(arg2.intValue()/2);
                ((Pane)(root.getChildren().get(2))).setPrefHeight(arg2.intValue()/2);
            }
        });
        // Keep elements width at proper percentages
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                ((Pane)(root.getChildren().get(0))).setPrefWidth(arg2.intValue()/2);
                ((Pane)(root.getChildren().get(1))).setPrefWidth(arg2.intValue()/2);
                ((Pane)(root.getChildren().get(2))).setPrefWidth(arg2.intValue());
            }
        });

    }


    public void initializeLeftArea(VBox modifyMe){
        Label title, rangeLabel, dashLabel, filenameLabel;
        TextField lowerBound, upperBound, filepath, singleValue;
        Button append, delete;
        HBox rangeRow, filenameRow, singleValueRow;



        title = new Label("Input");
        lowerBound = new TextField();
        upperBound = new TextField();
        filepath = new TextField();
        singleValue = new TextField();

        rangeRow = new HBox();
        filenameRow = new HBox();
        singleValueRow = new HBox();

        modifyMe.getChildren().add(title);
    }
}

/*
    /home/general/jdk-13/bin/java --module-path /home/general/javafx-sdk-11.0.2/lib --add-modules=javafx.controls -Dfile.encoding=UTF-8 @/tmp/cp_9hq8wixwmz81os68n0z4x0h2o.argfile src.GUI
*/