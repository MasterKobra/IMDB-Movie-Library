package com.example.finalproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class MovieQuiz extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Movie Quiz");

        Label movieRecommendatonLabel = new Label("Movie Recommendation");
        movieRecommendatonLabel.setFont(new Font("Stencil", 25));
        movieRecommendatonLabel.setTextFill(Color.WHITE);

        Label movieRecommendatonBodyLabel = new Label("Can't decide what movie to watch?\n        Answer these 6 questions!");
        movieRecommendatonBodyLabel.setFont(new Font("Roboto Mono", 15));
        movieRecommendatonBodyLabel.setTextFill(Color.WHITE);

        Button startButton = new Button("Start Now");
        startButton.setFont(Font.font("Roboto Mono", FontWeight.BOLD, FontPosture.REGULAR, 15));
        startButton.setTextFill(Color.WHITE);
        startButton.setStyle("-fx-background-color: #000000");

        startButton.setOnAction(e -> {
        });
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #000000");
        root.setAlignment(Pos.CENTER);

        // Adding all elements to the layout
        root.getChildren().addAll(movieRecommendatonLabel, movieRecommendatonBodyLabel, startButton);
        // Creating the scene
        Scene scene = new Scene(root, 350, 300);
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


