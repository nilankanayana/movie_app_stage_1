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

        // async task creation and execution
        new FetchMovieDataTask().execute(searchSpinner.getSelectedItemPosition());
    }

    /**
     * This method sets movie data views visible
     */
    private void setMovieDataViewsVisible() {
        errorTextView.setVisibility(View.INVISIBLE);
        recylerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method sets error views visible
     */
    private void setErrorViewsVisible() {
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

    /**
     * This class handles background data loading from movie db api.
     */
    private class FetchMovieDataTask extends AsyncTask<Integer, Void, List<MovieData>> {

        @Override
        protected List<MovieData> doInBackground(Integer... params) {
            // no params specified
            if (params.length == 0) {
                return null;
            }

            // no network scenario
            if (!NetworkUtils.isOnline(MainActivity.this)) {
                return null;
            }

            // first param
            final int searchSwitch = params[0];
            // generate url
            final URL movieDataUrl = NetworkUtils.buildUrl(searchSwitch);

            try {
                // load json data string
                final String movieDataJsonString = NetworkUtils.getResponseFromHttpUrl(movieDataUrl);
                // convert to list of movie data
                return MovieDataJsonUtils.getMovieDataFromJson(MainActivity.this, movieDataJsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<MovieData> movieDataList) {
            super.onPostExecute(movieDataList);
            loadingProgress.setVisibility(View.INVISIBLE);

            if (movieDataList != null && !movieDataList.isEmpty()) {
                // if data is available from background task set data to adapter
                setMovieDataViewsVisible();
                movieDataAdapter.setMovieData(movieDataList);
            } else {
                // display error message to user
                setErrorViewsVisible();
            }
        }
    }
}
