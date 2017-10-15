package com.inusak.android.moviesapp.utils;

import android.content.Context;

import com.inusak.android.moviesapp.data.MovieData;
import com.inusak.android.moviesapp.exception.InvalidStatusException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class provides fascilities to interpret json data in terms of movie data.
 * <p>
 * Created by Nilanka on 10/12/2017.
 */

public class MovieDataJsonUtils {

    private static final String TAG_STATUS_CODE = "status_code";

    private static final String TAG_STATUS_MESSAGE = "status_message";

    private static final String TAG_STATUS_SUCCESS = "success";

    private static final String TAG_RESULTS = "results";

    private static final String TAG_MOVIE_ID = "id";

    private static final String TAG_MOVIE_TITLE = "title";

    private static final String TAG_MOVIE_RELEASE_DATE = "release_date";

    private static final String TAG_MOVIE_POSTER_PATH = "poster_path";

    private static final String TAG_MOVIE_AVERAGE_VOTES = "vote_average";

    private static final String TAG_MOVIE_OVERVIEW = "overview";

    /**
     * This method processes the provided json string and produces a list of movie data objects
     *
     * @param context             {@link Context}
     * @param movieDataJsonString {@link String}
     * @return
     * @throws JSONException
     * @throws InvalidStatusException
     */
    public static List<MovieData> getMovieDataFromJson(Context context, String movieDataJsonString) throws JSONException, InvalidStatusException {
        final List<MovieData> list = new ArrayList<>();

        final JSONObject movieDataJson = new JSONObject(movieDataJsonString);
        if (movieDataJson.has(TAG_STATUS_CODE) && !movieDataJson.getBoolean(TAG_STATUS_SUCCESS)) {
            throw new InvalidStatusException(movieDataJson.getInt(TAG_STATUS_CODE), movieDataJson.getString(TAG_STATUS_CODE));
        }

        final JSONArray moviesJsonArray = movieDataJson.getJSONArray(TAG_RESULTS);
        for (int i = 0; i < moviesJsonArray.length(); i++) {
            final JSONObject movie = moviesJsonArray.getJSONObject(i);
            final String id = movie.getString(TAG_MOVIE_ID);
            final String title = movie.getString(TAG_MOVIE_TITLE);
            final String releaseDate = movie.getString(TAG_MOVIE_RELEASE_DATE);
            final String posterPath = movie.getString(TAG_MOVIE_POSTER_PATH);
            final String averageVotes = movie.getString(TAG_MOVIE_AVERAGE_VOTES);
            final String overview = movie.getString(TAG_MOVIE_OVERVIEW);

            list.add(new MovieData(id, title, releaseDate, posterPath, averageVotes, overview));
        }

        return list;
    }

}
