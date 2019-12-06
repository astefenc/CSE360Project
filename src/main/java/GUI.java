import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;


public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init(){
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setScene(new Scene(parent, 1600, 900));
        primaryStage.setTitle("Grade Analyzer");
        primaryStage.show();

/*
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(primaryStage);
*/
    }

}

/*
    /home/general/jdk-13/bin/java --module-path /home/general/javafx-sdk-11.0.2/lib --add-modules=javafx.controls -Dfile.encoding=UTF-8 @/tmp/cp_9hq8wixwmz81os68n0z4x0h2o.argfile src.GUI
*/