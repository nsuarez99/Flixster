package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {


    String posterPath;
    String backdropPath;
    String title;
    String overview;
    Double popularity;
    Double rating;
    int idd;

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        popularity = jsonObject.getDouble("popularity");
        rating = jsonObject.getDouble("vote_average");
        idd = jsonObject.getInt("id");

    }

    public static List<Movie> fromJSONArray(JSONArray moviesJSONArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < moviesJSONArray.length(); i++){
            movies.add(new Movie(moviesJSONArray.getJSONObject(i)));
        }
        return movies;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Double getRating() {
        return rating;
    }

    public int getIdd() {
        return idd;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}
