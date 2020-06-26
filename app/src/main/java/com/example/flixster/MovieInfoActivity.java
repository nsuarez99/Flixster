package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieInfoActivity extends AppCompatActivity {

    private static final String TAG = "MovieInfoActiviy";
    TextView tvInfoTitle;
    TextView tvInfoOverview;
    ImageView ivInfoPoster;
    RatingBar ratingBar;
    TextView tvPopularity;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        tvInfoTitle = findViewById(R.id.tvInfoTitle);
        tvInfoOverview = findViewById(R.id.tvInfoOverview);
        ivInfoPoster = findViewById(R.id.ivInfoPoster);
        ratingBar = findViewById(R.id.ratingBar);
        tvPopularity = findViewById(R.id.tvPopularity);
        videoView = findViewById(R.id.videoInfo);

        //set toolbar and movie title and overview
        String title = getIntent().getStringExtra("title");
        String overview = getIntent().getStringExtra("overview");
        tvInfoTitle.setText(title);
        tvInfoOverview.setText(overview);
        getSupportActionBar().setTitle(title);

        //set poster image
        String landscape = getIntent().getStringExtra("landscape");
        String portrait = getIntent().getStringExtra("portrait");
        setPosterImage(portrait, landscape);

        //set rating and popularity
        String popularity = getIntent().getStringExtra("popularity");
        tvPopularity.setText("Popularity: " + popularity);
        float rating = (float) getIntent().getDoubleExtra("rating", 0);
        ratingBar.setRating(rating);
        Log.d(TAG, popularity + " " + rating);

        //set video
        String idd = getIntent().getStringExtra("idd");
        setVideoView(idd);

    }

    protected void setPosterImage(String portrait, String landscape){
        String urlImage;
        int placeholder;
        if (MovieInfoActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            urlImage = landscape;
            placeholder = R.drawable.backdrop_placeholder;
        }
        else{
            urlImage = portrait;
            placeholder = R.drawable.portrait_placeholder;
        }
        int radius = 20; // corner radius, higher value = more rounded
        int margin = 5; // crop margin, set to 0 for corners with no crop
        Glide.with(MovieInfoActivity.this).load(urlImage).transform(new RoundedCornersTransformation(radius, margin)).placeholder(placeholder).into(ivInfoPoster);
    }

    protected void setVideoView(String idd){
        final String apiURL = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", idd);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(apiURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSucess");
                JSONObject jsonObject = json.jsonObject;
                try{
                    JSONArray results = jsonObject.getJSONArray("results");
                    String youtubeURL = "https://www.youtube.com/watch?v=" + results.getJSONObject(0).getString("key");
                    Log.i(TAG, "URL: " + youtubeURL);
                    videoView.setVideoPath(youtubeURL);
                    videoView.start();
                }
                catch (JSONException e){
                    Log.e(TAG, "Hit json exception " + e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}