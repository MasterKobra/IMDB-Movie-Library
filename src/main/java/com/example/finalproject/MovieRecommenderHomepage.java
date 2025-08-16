package com.example.finalproject;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MovieRecommenderHomepage {


    public void start(Stage primaryStage, String username) {
        // Create the welcome message
        Label welcome = new Label("Welcome, " + username + "!");
        if (username.isEmpty()) {
            welcome= new Label("Welcome!");
        }
        welcome.setStyle("-fx-font-size: 18px; -fx-text-fill: #007ACC; -fx-font-weight: bold;");

        // Create buttons for searching movies and taking the quiz
        Button searchButton = new Button("Search for a Movie");
        searchButton.setStyle("-fx-background-color: #007ACC; -fx-text-fill: white; -fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 5px;");

        Button quizButton = new Button("Take Movie Preference Quiz");
        quizButton.setStyle("-fx-background-color: #007ACC; -fx-text-fill: white; -fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 5px;");
        //Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #007ACC; -fx-text-fill: white; -fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 5px;");

        logoutButton.setOnAction(event -> {LoginScreen loginScreen = new LoginScreen();
        loginScreen.start(primaryStage);});
        // Action for the search button
        searchButton.setOnAction(e -> {
            // Create the MovieSearchRecommender instance and pass the current stage
            MovieSearch searchScreen = new MovieSearch();
            try {
                searchScreen.start(primaryStage);  // This will switch the scene to the movie search UI
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Action for the quiz button
        quizButton.setOnAction(e -> {
            try{
                Stage currentStage = (Stage) quizButton.getScene().getWindow();
                currentStage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
                Scene scene = new Scene(loader.load());
                scene.setFill(Color.TRANSPARENT);
                Stage quizStage = new Stage();
                quizStage.setScene(scene);
                quizStage.initStyle(StageStyle.TRANSPARENT);
                quizStage.show();

            } catch(IOException ex){
                ex.printStackTrace();
            }



        });

        // Layout for the homepage with improved spacing and alignment
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9f9f9;");
        layout.setAlignment(Pos.CENTER);

        // Adding all elements to the layout
        layout.getChildren().addAll(welcome, searchButton, quizButton,logoutButton);

        // Scene Setup
        Scene scene = new Scene(layout, 1280, 1024);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Movie Recommender Homepage");
        primaryStage.show();
    }
}
