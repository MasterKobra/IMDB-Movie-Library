package com.example.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class QuizApp extends Application {
    public void start(Stage primaryStage) throws IOException {
        //loads the quiz from the home.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(QuizApp.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
        }
        public static void main(String[] args) {
        launch(args);
        }
}
