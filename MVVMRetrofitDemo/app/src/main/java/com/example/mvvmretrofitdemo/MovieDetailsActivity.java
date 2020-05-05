package com.example.mvvmretrofitdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mvvmretrofitdemo.model.Result;

public class MovieDetailsActivity extends AppCompatActivity {

    private Result result;
    private ImageView posterImageView;
    private String posterPath;
    private TextView titleTextView;
    private TextView voteCountTextView;
    private TextView overviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        posterImageView = findViewById(R.id.poster_image_view);
        titleTextView = findViewById(R.id.title_text_view);
        voteCountTextView = findViewById(R.id.vote_count_text_view);
        overviewTextView = findViewById(R.id.overview_text_view);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("movieData")) {
            result = intent.getParcelableExtra("movieData");
            posterPath = result.getPosterPath();
            String imagePath = "https://image.tmdb.org/t/p/w500/" + posterPath;
            Glide.with(this).load(imagePath).placeholder(getDrawable(R.drawable.original)).into(posterImageView);

            titleTextView.setText(result.getOriginalTitle());
            voteCountTextView.setText(String.valueOf(result.getVoteCount()));
            overviewTextView.setText(result.getOverview());
        }
    }
}
