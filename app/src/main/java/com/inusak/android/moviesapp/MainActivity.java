package com.inusak.android.moviesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.inusak.android.moviesapp.data.MovieData;
import com.inusak.android.moviesapp.exception.InvalidStatusException;
import com.inusak.android.moviesapp.utils.MovieDataJsonUtils;
import com.inusak.android.moviesapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Main Activity for displaying all movie posters and selecting the desired sort criteria.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, MovieDataAdapter.OnMovieItemClickListener {

    private Spinner searchSpinner;

    private RecyclerView recylerView;

    private TextView errorTextView;

    private ProgressBar loadingProgress;

    private MovieDataAdapter movieDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find ui components
        searchSpinner = (Spinner) findViewById(R.id.spinner_search);
        recylerView = (RecyclerView) findViewById(R.id.rv_movies);
        errorTextView = (TextView) findViewById(R.id.tv_error_message);
        loadingProgress = (ProgressBar) findViewById(R.id.pb_loading);

        // create grid layout with 2 columns
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recylerView.setLayoutManager(layoutManager);

        // performance improvements via fixed size attribute
        recylerView.setHasFixedSize(true);

        // create data adapter
        movieDataAdapter = new MovieDataAdapter(this);
        recylerView.setAdapter(movieDataAdapter);

        // add spinner listener
        searchSpinner.setOnItemSelectedListener(this);

        // load search criteria to spinner
        loadSearchTypes();

        // load movie data from moviedb
        loadMovieData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.action_refresh) {
            // reset data set when search action is performed
            movieDataAdapter.setMovieData(null);
            // load data from moviedb
            loadMovieData();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will load search criteria and set them to spinner
     */
    private void loadSearchTypes() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(adapter);
        searchSpinner.setSelection(0);
    }

    /**
     * This method will load movie data from MovieDb and display recycler view
     */
    private void loadMovieData() {
        setMovieDataViewsVisible();

        // no network scenario
        if (NetworkUtils.isOnline(MainActivity.this)) {
            // async task creation and execution
            new FetchMovieDataTask(this).execute(searchSpinner.getSelectedItemPosition());
        } else {
            setErrorViewsVisible();
        }
    }

    /**
     * This method sets movie data views visible
     */
    public void setMovieDataViewsVisible() {
        errorTextView.setVisibility(View.INVISIBLE);
        recylerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method sets error views visible
     */
    public void setErrorViewsVisible() {
        recylerView.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // reset data set when spinner selection is changed
        movieDataAdapter.setMovieData(null);

        // load data from moviedb
        loadMovieData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    @Override
    public void onClick(MovieData movieData) {
        // create an intent when a movie thumbnail is clicked
        final Intent intent = new Intent(this, MovieInfoChildActivity.class);
        // add movie data as extra params
        intent.putExtra(MovieData.MOVIE_DATA_KEY, movieData);
        // start activity
        startActivity(intent);
    }

    public ProgressBar getLoadingProgress() {
        return loadingProgress;
    }

    public MovieDataAdapter getMovieDataAdapter() {
        return movieDataAdapter;
    }
}
