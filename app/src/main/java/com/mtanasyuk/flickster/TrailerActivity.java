package com.mtanasyuk.flickster;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TrailerActivity extends YouTubeBaseActivity {

    int id;
    double rating;
    String overview;
    String title;
    double popularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        // unpack the data of the intent
        id = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        popularity = getIntent().getDoubleExtra("popularity", 0);
        rating = getIntent().getDoubleExtra("rating", 0);
        overview = getIntent().getStringExtra("overview");

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize("AIzaSyDCREL_g9kwUOzjBD-7R0pcmxsiVoRYnHY",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        final YouTubePlayer youTubePlayer, boolean b) {

                        String url = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", id);
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get(url, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                JSONArray trailerJsonResults;
                                try {
                                    trailerJsonResults = response.getJSONArray("results");
                                    String key = trailerJsonResults.getJSONObject(0).getString("key");

                                    if (rating < 5.0) {
                                        youTubePlayer.cueVideo(key);
                                        TextView titleView = (TextView) findViewById(R.id.tvTitleTrailer);
                                        TextView popularityView = (TextView) findViewById(R.id.tvPopularity);
                                        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                                        TextView overviewView = (TextView) findViewById(R.id.tvOverviewTrailer);
                                        titleView.setText(title);
                                        String result = "Popularity (out of 40): " + (int) popularity;
                                        popularityView.setText(result);
                                        ratingBar.setVisibility(RatingBar.VISIBLE);
                                        ratingBar.setRating((float) rating);
                                        overviewView.setText(overview);
                                    } else {
                                        youTubePlayer.loadVideo(key);
                                        youTubePlayer.setFullscreen(true);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                            }
                        });
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {}
                });
    }
}
