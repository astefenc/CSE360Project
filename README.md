# CSE360Project

A simple grade analyzer, meant to make performing basic statistical operations simple.

The are two jar files you can use to run this program:

1. GradeAnalyzer.jar

This jar file is fully inclusive, including JavaFX to make running the program as simple as possible.  It can be run simply like:

    java -jar GradeAnalyzer.jar

2. GradeAnalyzerNoJavaFX.jar

This jar file does not include JavaFX and the user will have to provide some command line arguments for their installation of JavaFX.  It can be run like:

    java -jar --module-path [path to JavaFX lib folder] --add-modules=javafx.controls,javafx.fxml GradeAnalyzerNoJavaFX.jar

e.g.

    java -jar --module-path /home/user/javafx-sdk-11.0.2/lib --add-modules=javafx.controls,javafx.fxml GradeAnalyzerNoJavaFX.jar