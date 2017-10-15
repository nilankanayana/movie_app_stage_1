package com.inusak.android.moviesapp;

/**
 * Created by Nilanka on 10/15/2017.
 */

import android.os.AsyncTask;
import android.view.View;

import com.inusak.android.moviesapp.data.MovieData;
import com.inusak.android.moviesapp.utils.MovieDataJsonUtils;
import com.inusak.android.moviesapp.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

/**
 * This class handles background data loading from movie db api.
 */
public class FetchMovieDataTask extends AsyncTask<Integer, Void, List<MovieData>> {

    private MainActivity mainActivity;

    public FetchMovieDataTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected List<MovieData> doInBackground(Integer... params) {
        // no params specified
        if (params.length == 0) {
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
            return MovieDataJsonUtils.getMovieDataFromJson(mainActivity, movieDataJsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.getLoadingProgress().setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<MovieData> movieDataList) {
        super.onPostExecute(movieDataList);
        mainActivity.getLoadingProgress().setVisibility(View.INVISIBLE);

        if (movieDataList != null && !movieDataList.isEmpty()) {
            // if data is available from background task set data to adapter
            mainActivity.setMovieDataViewsVisible();
            mainActivity.getMovieDataAdapter().setMovieData(movieDataList);
        } else {
            // display error message to user
            mainActivity.setErrorViewsVisible();
        }
    }
}
