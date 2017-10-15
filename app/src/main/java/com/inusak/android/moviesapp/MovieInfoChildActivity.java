package com.inusak.android.moviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.inusak.android.moviesapp.data.MovieData;
import com.inusak.android.moviesapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * This class handles displaying movie information once a poster item is clocked from the main activity.
 */
public class MovieInfoChildActivity extends AppCompatActivity {

    private ImageView posterImageView;

    private TextView titleTextView;

    private TextView averageVotesTextView;

    private TextView releaseDateTextView;

    private TextView overviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info_child);

        // find views
        posterImageView = (ImageView) findViewById(R.id.iv_poster);
        titleTextView = (TextView) findViewById(R.id.tv_title);
        averageVotesTextView = (TextView) findViewById(R.id.tv_average_votes);
        releaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        overviewTextView = (TextView) findViewById(R.id.tv_overview);

        // set data to ui received from intent
        setDataToUI();
    }

    /**
     * This method is used to set data to ui elements
     */
    private void setDataToUI() {
        // get intent
        final Intent intentStartedThisActivity = getIntent();

        // if intent is valid and has extra params, then start processing
        if (intentStartedThisActivity != null && intentStartedThisActivity.hasExtra(MovieData.MOVIE_DATA_KEY)) {
            // retrieve movie data from parcel
            final MovieData movieData = intentStartedThisActivity.getParcelableExtra(MovieData.MOVIE_DATA_KEY);

            // load image using Picasso and set to image view
            Picasso.with(this).load(NetworkUtils.DEFAULT_URL_POSTERS_WITH_SIZE + movieData.getPosterPath()).into(posterImageView);
            // set all other data ui elements
            titleTextView.setText(movieData.getTitle());
            averageVotesTextView.setText(movieData.getAverageVotes() + "/10");
            releaseDateTextView.setText(movieData.getReleaseDate());
            overviewTextView.setText(movieData.getOverview());
        }
    }

}
