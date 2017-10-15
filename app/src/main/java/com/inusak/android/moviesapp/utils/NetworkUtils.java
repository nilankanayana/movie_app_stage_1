package com.inusak.android.moviesapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This class handles all network requests.
 * <p>
 * Created by Nilanka on 10/12/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String DEFAULT_DELIMITER = "\\A";

    /**
     * discover movies
     */
    private static final String DEFAULT_ROOT_URL_DISCOVER_MOVIES = "https://api.themoviedb.org/3/discover/movie";

    private static final String API_KEY = "api_key";

    // TODO: PLEASE ADD YOUR API_KEY HERE
    private static final String API_KEY_VALUE = "";

    private static final String MOST_POPULAR_SEARCH = "popularity.desc";

    private static final int MOST_POPULAR_SWITCH = 0;

    private static final String HIGHEST_RATED_SEARCH = "vote_average.desc";

    private static final int HIGHEST_RATED_SWITCH = 1;

    private static final String SORT_BY = "sort_by";

    /**
     * find posters
     */
    private static final String DEFAULT_ROOT_URL_POSTERS = "https://image.tmdb.org/t/p";

    private static final String DEFAULT_POSTER_SIZE = "w500";

    public static final String DEFAULT_URL_POSTERS_WITH_SIZE = DEFAULT_ROOT_URL_POSTERS + "/" + DEFAULT_POSTER_SIZE;

    /**
     * This method is used to build a url from the search switch provided
     *
     * @param searchSwitch
     * @return
     */
    public static URL buildUrl(int searchSwitch) {
        Uri builtUri = Uri.parse(DEFAULT_ROOT_URL_DISCOVER_MOVIES).buildUpon()
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .appendQueryParameter(SORT_BY, getSearchTagFromSwitch(searchSwitch))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method is used to fetch data from movie db web api using the provided url
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter(DEFAULT_DELIMITER);

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method is used to verify whether a proper network connection is available
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    /**
     * This is a convenience method to decide on the search tag from the selected index of sort by spinner
     *
     * @param searchSwitch
     * @return
     */
    private static String getSearchTagFromSwitch(int searchSwitch) {
        String searchTag = null;
        switch (searchSwitch) {
            case MOST_POPULAR_SWITCH:
                searchTag = MOST_POPULAR_SEARCH;
                break;
            case HIGHEST_RATED_SWITCH:
                searchTag = HIGHEST_RATED_SEARCH;
                break;
            default:
                searchTag = MOST_POPULAR_SEARCH;
        }

        return searchTag;
    }
}
