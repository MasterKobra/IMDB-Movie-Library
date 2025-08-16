package com.example.finalproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MovieSearch extends Application {
    private static final String API_KEY = "V5B4ysiBMWqtGcpJpRjLu8TBtgiFn90pst9vHaSa";
    private Scene movieListScene;
    private final List<Button> cachedMovieButtons = new ArrayList<>();
    private final ListView<Button> movieListView = new ListView<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Movie Recommender");
        //Back to homepage Button
        Button homepage = new Button("⬅ Back to List");
        MovieRecommenderHomepage movieHomepage = new MovieRecommenderHomepage();
        homepage.setOnAction(e -> movieHomepage.start(primaryStage,""));
        homepage.setStyle("-fx-background-color: #dddddd;");
        //creates search menu for the movie
        Label searchLabel = new Label("Enter Movie Name:");
        searchLabel.setFont(new Font("Arial", 16));
        TextField movieSearchField = new TextField();
        Button searchButton = new Button("Search"); //search button
        searchButton.setStyle("-fx-background-color: #007ACC; -fx-text-fill: white;");

        movieListView.setVisible(false);
// Search Action
        searchButton.setOnAction(e -> {
            String movieName = movieSearchField.getText(); //gets the movie name
            if (!movieName.isEmpty()) {
                searchMovies(movieName, primaryStage); // if its empty, it doesn't change screen
            }
        });
        //Layout Setup
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #f5f5f5;");
        layout.getChildren().addAll(homepage,searchLabel, movieSearchField, searchButton, movieListView);
        //Scene layout
        movieListScene = new Scene(layout, 1280, 1024);
        primaryStage.setScene(movieListScene);
        primaryStage.show();
    }

    private void searchMovies(String movieName, Stage primaryStage) {
        try {
            if (movieName.contains(" ")) {
                movieName = movieName.replace(" ", "%20"); //changes spaces to url spaces
            }
            Watchmode watchmodeClient = new Watchmode();
            String url = "https://api.watchmode.com/v1/autocomplete-search/?apiKey=" + API_KEY + "&search_value=" + movieName + "&searchtype=1"; //movie search api url
            String jsonResponse = watchmodeClient.getApiResponse(url); //checks if it exists
            JSONObject json = new JSONObject(jsonResponse); //makes an object that can be manipulated

            if (json.has("results")) {
                JSONArray searchResults = json.getJSONArray("results"); //gets any search results and saves it to an array
                movieListView.getItems().clear();
                cachedMovieButtons.clear();

                for (int i = 0; i < searchResults.length(); i++) {
                    JSONObject movie = searchResults.getJSONObject(i);
                    String title = movie.getString("name");
                    int ID = movie.getInt("id"); //loops through movies and gets the name and id for searching

                    Button movieButton = new Button(title); //movie button
                    movieButton.setStyle("-fx-background-color: #e0e0e0;");
                    movieButton.setOnAction(event -> showMovieDetails(ID, primaryStage)); //shows movie details on click
                    movieListView.getItems().add(movieButton); // displays results on page
                    cachedMovieButtons.add(movieButton);
                }

                movieListView.setVisible(true);
            } else {
                movieListView.getItems().clear();
                movieListView.getItems().add(new Button("No results found."));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMovieDetails(int ID, Stage primaryStage) throws NullPointerException {
        try {
            Watchmode watchmodeClient = new Watchmode();
            String url = "https://api.watchmode.com/v1/title/" + ID + "/details/?apiKey=" + API_KEY; //searches movie on api
            String jsonResponse = watchmodeClient.getApiResponse(url);
            JSONObject json = new JSONObject(jsonResponse);
            //saves each element of the movie as a variable
            String title = json.optString("title");
            String plot = json.optString("plot_overview");
            String genre = json.optString("genre_names").split(",")[0].trim();
            String yearStr = json.optString("year").split("–")[0];
            int year = Integer.parseInt(yearStr);
            String rating = json.optString("user_rating", "N/A");
            String ageRating = json.optString("us_rating");
            JSONArray similarMovies = json.optJSONArray("similar_titles");
            String posterUrl = json.optString("posterLarge");
            //checks if the fields are empty and replaces them
            title = checkAndReplace(title);
            plot = checkAndReplace(plot);
            genre =checkAndReplace(genre);
            rating = checkAndReplace(rating);
            ageRating =checkAndReplace(ageRating);
            // Detail layout
            VBox detailLayout = new VBox(10);
            detailLayout.setPadding(new Insets(15));
            detailLayout.setStyle("-fx-background-color: #ffffff;");
                //movie title label
            Text titleText = new Text("🎥 " + title);
            titleText.setFont(new Font("Arial", 20));
            titleText.setFill(Color.DARKBLUE);
            // adding api elements to labels
            Label yearLabel = new Label("Year: " + year);
            Label ratingLabel = new Label("Rating: " + rating);
            Label ageRatingLabel = new Label("Age Rating: " + ageRating);
            Label genreLabel = new Label("Genre: " + genre + "]");
            Label plotLabel = new Label("Plot: " + plot);
            plotLabel.setWrapText(true);
            plotLabel.setMaxWidth(400);

            ImageView poster;
            //checks if posterURL is empty and uses a placeholder image instead
            if (posterUrl.isEmpty()) {
                File noname = new File("noname.jpg");
                poster = new ImageView(new Image(noname.toURI().toString()));
            } else{
                poster = new ImageView(new Image(posterUrl));
            }

            poster.setFitHeight(300);
            poster.setPreserveRatio(true);
            //back to search button
            Button backButton = new Button("⬅ Back to List");
            backButton.setOnAction(e -> restoreSearchResults(primaryStage));
            backButton.setStyle("-fx-background-color: #dddddd;");
            //adding elements to layout
            detailLayout.getChildren().addAll(titleText, yearLabel, ratingLabel, ageRatingLabel, genreLabel, plotLabel, poster, backButton);
            //similar movies label
            Label similarLabel = new Label("Similar Movies:");
            similarLabel.setFont(new Font("Arial", 16));
            GridPane similarGrid = new GridPane();
            similarGrid.setHgap(10);
            similarGrid.setVgap(10);
            similarGrid.setPadding(new Insets(10));
            int row = 0, col = 0;
            //loops through similar movies array and gets recommendations
            for (int i = 0; i < 6; i++) {
                if (similarMovies == null ) {
                    break;
                } else if(i == similarMovies.length()){
                    break;
                }
                // gets movie id from the array
                int smID = Integer.parseInt(String.valueOf(similarMovies.get(i)));
                String url2 = "https://api.watchmode.com/v1/title/" + smID + "/details/?apiKey=" + API_KEY; //searches for the similar movies
                String jsonResponse2 = watchmodeClient.getApiResponse(url2);
                JSONObject json2 = new JSONObject(jsonResponse2);
                String smTitle = json2.optString("title");
                String recPoster = json2.optString("posterLarge"); //gets title and poster for each movie
                ImageView smPoster;

                if (recPoster.isEmpty()) {
                    FileInputStream fis = new FileInputStream("C:\\Users\\27072011\\IdeaProjects\\Final Project\noname.jpg");
                    Image image = new Image(fis);
                    smPoster = new ImageView(image);


                } else{
                    smPoster = new ImageView(new Image(recPoster));
                }
                smPoster.setFitHeight(150);
                smPoster.setPreserveRatio(true);
                // Similar movie box
                VBox smBox = new VBox(5);
                smBox.setAlignment(Pos.CENTER);
                // title labels for movies
                Button smButton = new Button(smTitle);
                smButton.setOnAction(ev -> showMovieDetails(smID, primaryStage));
                smButton.setMaxWidth(180);
                //adds elements to layout
                smBox.getChildren().addAll(smPoster, smButton);
                // adds movie to the grid
                similarGrid.add(smBox, col, row);
                col++;

            }

            ScrollPane scrollPane = new ScrollPane(similarGrid); //scroll pane for movies
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setPannable(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setPrefHeight(300);
            //adds similar movies label to layout
            detailLayout.getChildren().addAll(similarLabel, scrollPane);
            Scene detailScene = new Scene(detailLayout, 1280, 1024);
            primaryStage.setScene(detailScene);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restoreSearchResults(Stage primaryStage) {
        //restores the previous searched movie after the back button is clicked
        movieListView.getItems().clear();
        movieListView.getItems().addAll(cachedMovieButtons);
        movieListView.setVisible(true);
        primaryStage.setScene(movieListScene);
    }
    public static String checkAndReplace(String str){
        if (str.isEmpty()){
            str = "N/A";
            return str;
        }
        return str;
    }
}
