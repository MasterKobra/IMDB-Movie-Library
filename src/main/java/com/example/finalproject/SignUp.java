package com.example.finalproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SignUp {
    private final File usernames = new File("usernames.txt");
    private final File passwords = new File("passwords.txt");

    public void start(Stage primaryStage) {
        // Labels with custom styling
        Label usernameLabel = new Label("New Username:");
        usernameLabel.setFont(new Font("Arial", 14));
        usernameLabel.setStyle("-fx-text-fill: #007ACC;");

        Label passwordLabel = new Label("New Password:");
        passwordLabel.setFont(new Font("Arial", 14));
        passwordLabel.setStyle("-fx-text-fill: #007ACC;");

        // Text fields with styling
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter a new username");
        usernameField.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-radius: 5px; -fx-padding: 10px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter a new password");
        passwordField.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-radius: 5px; -fx-padding: 10px;");

        // Result message label
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-text-fill: red;");

        // Buttons with styling
        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #007ACC; -fx-text-fill: white; -fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 5px;");
        signUpButton.setMaxWidth(Double.MAX_VALUE);

        Button backButton = new Button("Back to Login");
        backButton.setStyle("-fx-background-color: #f4f4f4; -fx-text-fill: #007ACC; -fx-padding: 10px; -fx-font-size: 14px; -fx-border-radius: 5px;");
        backButton.setMaxWidth(Double.MAX_VALUE);

        // Handling the Sign-Up action
        signUpButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            //Error Handling: if fields are empty
            if (username.isEmpty() || password.isEmpty()) {
                resultLabel.setText("Please fill both fields.");
                return;
            }
            //Error handling: if username exists
            if (existsInFile(usernames, username)) {
                resultLabel.setText("Username already exists.");
            } else {
                saveToFile(usernames, username);
                saveToFile(passwords, password);
                MovieRecommenderHomepage homepage = new MovieRecommenderHomepage();
                homepage.start(primaryStage,username);
            }
        });

        // Handling the Back to Login action
        backButton.setOnAction(event -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.start(primaryStage);
        });

        // Layout setup
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Adding all elements to the layout
        root.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, signUpButton, backButton, resultLabel);

        // Creating the scene
        Scene scene = new Scene(root, 1280, 1024);
        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveToFile(File file, String data) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(data + "\n"); //writes username and password to file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean existsInFile(File file, String newData) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals(newData)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
