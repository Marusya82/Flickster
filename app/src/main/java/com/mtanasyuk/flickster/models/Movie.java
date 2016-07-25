package com.mtanasyuk.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {

    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    double rating;
    int id;
    double popularity;
    String releaseDate;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.rating = jsonObject.getDouble("vote_average");
        this.id = jsonObject.getInt("id");
        this.popularity = jsonObject.getDouble("popularity");
        this.releaseDate = jsonObject.getString("release_date");
    }

    public static ArrayList<Movie> fromJSONArray (JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public Double getRating() {
        return this.rating;
    }

    public int isPopular() {
        if (this.rating < 5.0) return 0;
        else return 1;
    }

    public int getId() {
        return this.id;
    }

    public double getPopularity() {
        return this.popularity;
    }
}
