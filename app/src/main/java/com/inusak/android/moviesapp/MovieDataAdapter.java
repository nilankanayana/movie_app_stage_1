package com.inusak.android.moviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.inusak.android.moviesapp.data.MovieData;
import com.inusak.android.moviesapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles loading data from models, inflate ui components and binding them to view holders.
 * <p>
 * Created by Nilanka on 10/14/2017.
 */

public class MovieDataAdapter extends RecyclerView.Adapter<MovieDataAdapter.MovieDataViewHolder> {

    private final List<MovieData> movieDataList;

    private OnMovieItemClickListener onMovieItemClickListener;

    /**
     * Parameterized constructor with item click listner argument
     *
     * @param onMovieItemClickListener {@link OnMovieItemClickListener}
     */
    public MovieDataAdapter(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;

        movieDataList = new ArrayList<>();
    }

    @Override
    public MovieDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get context
        final Context context = parent.getContext();
        // id for the list item layout
        final int layoutId = R.layout.movie_list_item;
        // inflate view from the layout id
        final View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        // create new view holder
        return new MovieDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieDataViewHolder holder, int position) {
        // get context
        final Context context = holder.posterImageView.getContext();
        // retrieve poster path
        final String imagePath = movieDataList.get(position).getPosterPath();
        // load image using Picasso
        Picasso.with(context).load(NetworkUtils.DEFAULT_URL_POSTERS_WITH_SIZE + imagePath).into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return movieDataList.size();
    }

    /**
     * This method is used to set data to model once loaded from async task
     *
     * @param movieDataList
     */
    public void setMovieData(List<MovieData> movieDataList) {
        this.movieDataList.clear();
        if (movieDataList != null && !movieDataList.isEmpty()) {
            // add all objects if the list is not null and not empty
            this.movieDataList.addAll(movieDataList);
        }

        // notify about the change of data
        notifyDataSetChanged();
    }

    /**
     * This class acts as the view holder for MovieData objects for MovieDataAdapter.
     */
    class MovieDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView posterImageView;

        /**
         * @param itemView
         */
        MovieDataViewHolder(View itemView) {
            super(itemView);

            // find poster image view
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_poster_thumbnail);
            // set click listener
            posterImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // get position
            final int position = getAdapterPosition();
            // propagate events to listener
            onMovieItemClickListener.onClick(movieDataList.get(position));
        }
    }

    /**
     * This interface defines the behaviour for movie item click listener.
     */
    interface OnMovieItemClickListener {

        /**
         * This method should be implemented by all parties who is interested in item click events
         *
         * @param movieData {@link MovieData}
         */
        void onClick(MovieData movieData);
    }
}
