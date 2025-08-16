package com.example.finalproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginScreen extends Application {
    private final File usernames = new File("usernames.txt");
    private final File passwords = new File("passwords.txt");


    public void start(Stage primaryStage) {
        // Labels with custom styling
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(new Font("Arial", 14));
        usernameLabel.setStyle("-fx-text-fill: #007ACC;");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(new Font("Arial", 14));
        passwordLabel.setStyle("-fx-text-fill: #007ACC;");

        // Text fields with styling
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-radius: 5px; -fx-padding: 10px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-radius: 5px; -fx-padding: 10px;");

        // Result message label
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-text-fill: #ff0000;");

        // Buttons with styling
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #007ACC; -fx-text-fill: white; -fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 5px;");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        Button signUpButton = new Button("Go to Sign Up");
        signUpButton.setStyle("-fx-background-color: #f4f4f4; -fx-text-fill: #007ACC; -fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 5px;");
        signUpButton.setMaxWidth(Double.MAX_VALUE);

        loginButton.setOnAction(event -> {
            String enteredUsername = usernameField.getText().trim();
            String enteredPassword = passwordField.getText().trim();
            //Checking login credentials
            if (checkCredentials(enteredUsername, enteredPassword)) {
                MovieRecommenderHomepage homepage = new MovieRecommenderHomepage(); //send user to movie homepage
                homepage.start(primaryStage,enteredUsername);
            } else {
                resultLabel.setText("Login Failed. Try again.");
            }
        });

        signUpButton.setOnAction(event -> {
            SignUp signUpScreen = new SignUp(); //sends user to signup screen
            signUpScreen.start(primaryStage);
        });

        // Main layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Adding all elements to the layout
        root.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, signUpButton, resultLabel);

        // Creating the scene
        Scene scene = new Scene(root, 1280, 1024);
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean checkCredentials(String username, String password) {
        try {
            ArrayList<String> usernameList = new ArrayList<>();
            ArrayList<String> passwordList = new ArrayList<>();

            Scanner usernameScanner = new Scanner(usernames);
            Scanner passwordScanner = new Scanner(passwords);

            while (usernameScanner.hasNextLine()) {
                usernameList.add(usernameScanner.nextLine()); //adds usernames to arraylist
            }

            while (passwordScanner.hasNextLine()) {
                passwordList.add(passwordScanner.nextLine());
            }

            // Check if username exists and if password matches the same line index
            for (int i = 0; i < usernameList.size(); i++) {
                if (username.equals(usernameList.get(i)) && i < passwordList.size() && password.equals(passwordList.get(i))) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
