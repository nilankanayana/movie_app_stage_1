package com.inusak.android.moviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds Movie Data.
 *
 * Created by Nilanka on 10/12/2017.
 */

public class MovieData implements Parcelable {

    public static final String MOVIE_DATA_KEY = "MOVIE_DATA";

    public static final String DEFAULT_IMAGE_SIZE = "w185";

    private String id;

    private String title;

    private String releaseDate;

    private String posterPath;

    private String averageVotes;

    private String overview;

    /**
     * Parameterized constructor
     *
     * @param id
     * @param title
     * @param releaseDate
     * @param posterPath
     * @param averageVotes
     * @param overview
     */
    public MovieData(String id, String title, String releaseDate, String posterPath, String averageVotes, String overview) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.averageVotes = averageVotes;
        this.overview = overview;
    }

    /**
     * Constructor for creating data from parcel
     *
     * @param parcel
     */
    private MovieData(Parcel parcel) {
        final List<String> list = new ArrayList<>();
        parcel.readStringList(list);

        int i = 0;
        this.id = list.get(i++);
        this.title = list.get(i++);
        this.releaseDate = list.get(i++);
        this.posterPath = list.get(i++);
        this.averageVotes = list.get(i++);
        this.overview = list.get(i++);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getAverageVotes() {
        return averageVotes;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        final List<String> parcelableList = new ArrayList<>();
        parcelableList.add(getId());
        parcelableList.add(getTitle());
        parcelableList.add(getReleaseDate());
        parcelableList.add(getPosterPath());
        parcelableList.add(getAverageVotes());
        parcelableList.add(getOverview());

        dest.writeStringList(parcelableList);
    }

    public static final Creator<MovieData> CREATOR = new Creator() {

        @Override
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };


}
