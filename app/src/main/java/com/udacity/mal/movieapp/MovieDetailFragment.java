package com.udacity.mal.movieapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.mal.movieapp.data.Movie;
import com.udacity.mal.movieapp.provider.MoviesContract;
import com.udacity.mal.movieapp.utilities.ApiParams;

public class MovieDetailFragment extends Fragment implements View.OnClickListener
{

    private Movie movieDetails;
    private ImageView mBackdropView;
    private ImageView mPosterView;
    private TextView mTitleView;
    private TextView mRatingView;
    private TextView mPlotView;
    private View rootView;
    private Button favButton;
    private boolean isFav = false;

    public MovieDetailFragment()
    {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Intent details = getActivity().getIntent();
        movieDetails = details.getParcelableExtra("MOVIE");

        checkFav();

        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mBackdropView = (ImageView) rootView.findViewById(R.id.movie_detail_backdrop);
        mPosterView = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
        mTitleView = (TextView) rootView.findViewById(R.id.movie_detail_title);
        mRatingView = (TextView) rootView.findViewById(R.id.movie_detail_rating);
        mPlotView = (TextView) rootView.findViewById(R.id.movie_detail_plot);
        favButton = (Button) rootView.findViewById(R.id.favButton);
        if (isFav)
        {
            favButton.setText("Remove from Fav");
        }
        favButton.setOnClickListener(this);

        Picasso.with(getContext()).load(ApiParams.BASE_IMG_URL + movieDetails.getBackdrop_path()).into(mBackdropView);
        Picasso.with(getContext()).load(ApiParams.BASE_IMG_URL + movieDetails.getPoster_path()).into(mPosterView);
        mTitleView.setText(movieDetails.getTitle());
        mRatingView.setText(formatRating(movieDetails));
        mPlotView.setText(movieDetails.getOverview());

        return rootView;
    }

    private String formatRating(Movie m)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getVote_average());
        sb.append("/10.0");
        return sb.toString();
    }

    private void checkFav()
    {
        Cursor movieCursor = getContext().getContentResolver().query(
                MoviesContract.MovieEntry.CONTENT_URI,
                new String[]{MoviesContract.MovieEntry.MOVIE_ID},
                MoviesContract.MovieEntry.MOVIE_ID + " = ? ",
                new String[]{movieDetails.getId().toString()},
                null
        );
        long movieId;
        if (movieCursor.moveToFirst())
        {
            // Database already contains movie (i.e already favorited)
            isFav = true;
        }

    }

    public void addToFav()
    {
        long movieId;
        if (isFav)
        {
            // Database already contains movie (i.e already favorited)
            int rowsDelted = getContext().getContentResolver().delete(
                    MoviesContract.MovieEntry.CONTENT_URI,
                    MoviesContract.MovieEntry.MOVIE_ID + " = ?",
                    new String[]{movieDetails.getId().toString()}
            );
            isFav = false;
            favButton.setText("Add to Fav");
            if (rowsDelted > 0)
            {
                Snackbar.make(rootView, "Removed from favorites", Snackbar.LENGTH_SHORT).show();
            }
        }
        else
        {
            // Insert movie into database
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.MovieEntry.MOVIE_ID, movieDetails.getId());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_TITLE, movieDetails.getTitle());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_OVERVIEW, movieDetails.getOverview());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_POPULARITY, movieDetails.getPopularity());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_VOTE_COUNT, movieDetails.getVote_count());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_VOTE_AVERAGE, movieDetails.getVote_average());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_RELEASE_DATE, movieDetails.getRelease_date());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_FAVORED, true);
            contentValues.put(MoviesContract.MovieEntry.MOVIE_POSTER_PATH, movieDetails.getPoster_path());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_BACKDROP_PATH, movieDetails.getBackdrop_path());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_ADULT, movieDetails.getAdult());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_ORIGINAL_LANGUAGE, movieDetails.getOriginal_language());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_ORIGINAL_TITLE, movieDetails.getOriginal_title());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_VIDEO, movieDetails.getVideo());
            contentValues.put(MoviesContract.MovieEntry.MOVIE_GENRE_IDS, movieDetails.getGenre_ids().toString());


            Uri insertedUri = getContext().getContentResolver().insert(
                    MoviesContract.MovieEntry.CONTENT_URI,
                    contentValues
            );
            movieId = ContentUris.parseId(insertedUri);
            if (movieId > 0)
            {
                Snackbar.make(rootView, "Movie added to favorites", Snackbar.LENGTH_SHORT).show();
                isFav = true;
                favButton.setText("Remove from Fav");
            }
        }
        return;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.favButton)
        {
            addToFav();
        }
    }
}
