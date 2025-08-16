package com.example.finalproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizController {

    @FXML
    public Label question;

    @FXML
    private VBox optionsBox;
    @FXML
    private VBox resultBox;
    private String topMovieId;

    private JSONArray movieList;
    private int currentMovieIndex = 0;
    private int counter = 0;
    private boolean wantsAgeRatingSelection =false;
    private String selectedGenre,selectedAgeRange;
    private final List<String> selectedRatings = new ArrayList<>();


    public void initialize() {
        loadQuestions();
    }
    private final Map<String, Integer> genreMap = Map.of(
            //used a Hash map to set the quiz answer options to a number
        "Action", 1,
        "Adventure",2,
        "Comedy",4,
        "Mystery",13,
        "Drama", 7,
        "Animation",3,
        "Thriller",17,
        "Romance",14
    );

    @FXML
    public void handleOptionClick(ActionEvent event)  {
        //gets the text of the option the user chooses
        Button clicked = (Button) event.getSource();
        String selectedOption = clicked.getText();

        switch(counter){
            // sets the users answer as a variable for the corresponding question
            case 0 -> selectedGenre = selectedOption;
            case 1 -> selectedAgeRange = selectedOption;
            case 2 -> wantsAgeRatingSelection = selectedOption.toLowerCase().contains("yes");
            case 3 -> selectedRatings.add(selectedOption);
        }
        counter++;

        if (counter == 3 && !wantsAgeRatingSelection){
            //to display the list of age ratings
            counter++;
        }
        if (counter>=4){
            //performs search of the movie
            performSearch();
        } else{
            //recursively calls itself
            loadQuestions();
        }
    }

    private void loadQuestions(){
        //switch statement to display questions and options on the screen
        switch(counter){
            case 0:
                question.setText("Please choose any genre you're interested in");
                setOptions("Action", "Adventure", "Comedy", "Mystery", "Drama", "Animation", "Thriller", "Romance");
                break;
            case 1:
                question.setText("How old would you like the movie to be?");
                setOptions("Within the year","Within 5 years", "Within 10 years","Within 20 years","Classic");
                break;
            case 2:
                question.setText("Is the age Rating important to you?");
                setOptions("Yes I would like to choose the ratings I'm okay with","No, it doesnt matter");
                break;
            case 3:
                question.setText("Which age ratings are you okay with?");
                setOptions("G","PG","PG-13","R");
                break;

        }

        }

    private void setOptions(String... options) {
        optionsBox.getChildren().clear();
        // sets each option as a button
        for (String option : options) {
            Button btn = new Button(option);
            btn.setPrefWidth(400);
            btn.setOnAction(this::handleOptionClick);
            optionsBox.getChildren().add(btn);
        }

    }
    private void showTopResult(String title) throws IOException {
        resultBox.getChildren().clear();
        resultBox.setVisible(true);
        optionsBox.setVisible(false);
        //Result label
        question.setText("Top Result");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        // calls the website to get the movie poster of the result
        Watchmode watchmode = new Watchmode();
        String url = "https://api.watchmode.com/v1/title/" + topMovieId + "/details/?apiKey=V5B4ysiBMWqtGcpJpRjLu8TBtgiFn90pst9vHaSa";
        String response = watchmode.getApiResponse(url);
        JSONObject json = new JSONObject(response);
        String posterURL = json.getString("poster");

        ImageView poster;
        //sets the poster to be a filler poster if the posterurl is empty
        if (posterURL == null || posterURL.isEmpty()) {
            File noname = new File("noname.jpg");  // Make sure noname.jpg is in your working directory
            poster = new ImageView(new Image(noname.toURI().toString()));
        } else {
            poster = new ImageView(new Image(posterURL, true));  // true for background loading
        }
        //button for showing movie details
        Button detailsButton = new Button("Details");
        detailsButton.setOnAction(e->showDetails());
        // back to homepage button
        Button backButton = new Button("Back to Homepage");
        backButton.setOnAction(e->{
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

            MovieRecommenderHomepage homepage = new MovieRecommenderHomepage();
            Stage homepageStage = new Stage();
            homepage.start(homepageStage,"");
        });
        //moves to the next movie
        Button nextButton = new Button("Next");
        nextButton.setOnAction(e->{
            currentMovieIndex++;
            try {
                showMovieAtIndex(currentMovieIndex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        resultBox.getChildren().addAll(titleLabel, poster, detailsButton, nextButton, backButton);
    }
    private void showMovieAtIndex(int index) throws IOException {
        //checks if there are more movies in the search
        if (movieList == null || movieList.isEmpty() || index >= movieList.length()) {
            question.setText("No more movies.");
            optionsBox.setVisible(true);
            resultBox.setVisible(false);
        }
        //gets the movie at the selected index and gets its information
        JSONObject movie = movieList.getJSONObject(index);
        String title = movie.getString("title");
        topMovieId = String.valueOf(movie.getInt("id"));
        showTopResult(title);

    }
private void showDetails(){
        String url = "https://api.watchmode.com/v1/title/" + topMovieId + "/details/?apiKey=V5B4ysiBMWqtGcpJpRjLu8TBtgiFn90pst9vHaSa";
        new Thread(() -> {
            try {
                Watchmode watchmode = new Watchmode();
                String response = watchmode.getApiResponse(url);
                JSONObject details = new JSONObject(response);
                //gets plot overview from api
                String overview = details.optString("plot_overview", "No description available.");
                Platform.runLater(() -> {
                    // Opens a new pane on the screen with the movie information
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Movie Details");
                    alert.setHeaderText(details.getString("title"));
                    alert.setContentText(overview);
                    alert.showAndWait();
                });
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }
    private void performSearch() {
        optionsBox.setVisible(false);
        question.setText("Searching...");
        //gets current year
        int currentyear = Year.now().getValue();
        int startYear = switch (selectedAgeRange){
            //subtracts the desired range from the current year to get starting year for api search
            case "Within 5 years" -> currentyear-5;
            case "Within 10 years" -> currentyear-10;
            case "Within 20 years" -> currentyear-20;
            case "Classic" -> 1900;
            default -> currentyear;
        };
        //gets the genreid for the selected genre
        Integer genreID = genreMap.get(selectedGenre);
        //different implementation of the url building using a string builder to append instead of adding variables
        StringBuilder urlBuilder = new StringBuilder("https://api.watchmode.com/v1/list-titles/?");
        String apiKey ="V5B4ysiBMWqtGcpJpRjLu8TBtgiFn90pst9vHaSa";
        urlBuilder.append("apiKey=").append(apiKey);
        //appends genreid
        if (genreID !=null){
            urlBuilder.append("&genres=").append(genreID);
        }
        //appends search range
        urlBuilder.append("&start_year=").append(startYear);
        urlBuilder.append(currentyear);
        if (wantsAgeRatingSelection && !selectedRatings.isEmpty()){
            //appends age rating
            String rating = String.join(",", selectedRatings);
            urlBuilder.append("&mpaa_rating=").append(URLEncoder.encode(rating, StandardCharsets.UTF_8));
        }
        String url = urlBuilder.toString();
        Watchmode watchmode = new Watchmode();
        new Thread(() ->{
            try{
                //gets the titles from the search
                String response = watchmode.getApiResponse(url);
                JSONObject json = new JSONObject(response);
                JSONArray titles = json.getJSONArray("titles");

                if (!titles.isEmpty()){
                    movieList = titles;
                    currentMovieIndex = 0;

                    //calls a method to get movie at index
                    Platform.runLater(()->{
                        JSONObject top = movieList.getJSONObject(currentMovieIndex);
                        String title = top.getString("title");
                        topMovieId = String.valueOf(top.getInt("id"));
                        try {
                            showTopResult(title);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else{
                    Platform.runLater(() -> {
                        question.setText("No results found.");
                        optionsBox.setVisible(true);});
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
