package com.example.finalproject;

import java.io.*;
import java.net.*;

public class Watchmode {
    public String getApiResponse(String url) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection(); //opens connection to website
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode(); //gets response code
        if(responseCode != HttpURLConnection.HTTP_OK){ // throws error if the link doesn't exist
            throw new IOException("HTTP request failed with status code " + responseCode);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);  // Getting data from the api and adding it to the response
        }
        in.close();

        return response.toString();
    }

}
