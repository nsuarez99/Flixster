package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    ImageView ivPlay;
    Intent intent;
    ImageView ivTrailerBackdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        tvInfoOverview = findViewById(R.id.tvInfoOverview);
        ivInfoPoster = findViewById(R.id.ivInfoPoster);
        ratingBar = findViewById(R.id.ratingBar);
        tvPopularity = findViewById(R.id.tvPopularity);
        ivPlay = findViewById(R.id.ivPlay);

        //set toolbar and movie title and overview
        String title = getIntent().getStringExtra("title");
        String overview = getIntent().getStringExtra("overview");
        if (MovieInfoActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            tvInfoTitle = findViewById(R.id.tvInfoTitle);
            tvInfoTitle.setText(title);
        }
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
        intent = new Intent(MovieInfoActivity.this, MovieTrailerActivity.class);
        getYoutubeUrl(idd);

    }

    public void launchTrailer(View view){
        startActivity(intent);
    }

    protected void setPosterImage(String portrait, String landscape){
        String urlImage;
        int placeholder;
        int radius = 20; // corner radius, higher value = more rounded
        int margin = 5; // crop margin, set to 0 for corners with no crop

        if (MovieInfoActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            ivTrailerBackdrop = findViewById(R.id.ivTrailerBackdrop);
            Glide.with(MovieInfoActivity.this).load(landscape).transform(new RoundedCornersTransformation(radius, margin)).placeholder(R.drawable.backdrop_placeholder).into(ivTrailerBackdrop);
        }
        Glide.with(MovieInfoActivity.this).load(portrait).transform(new RoundedCornersTransformation(radius, margin)).placeholder(R.drawable.portrait_placeholder).into(ivInfoPoster);
    }

    protected void getYoutubeUrl(String idd){
        final String apiURL = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", idd);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(apiURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSucess");
                JSONObject jsonObject = json.jsonObject;
                try{
                    JSONArray results = jsonObject.getJSONArray("results");
                    intent.putExtra("key", results.getJSONObject(0).getString("key"));

                }
                catch (JSONException e){
                    intent.putExtra("key", "");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
                intent.putExtra("key", "");
            }
        });
    }
}