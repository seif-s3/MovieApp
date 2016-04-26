package com.udacity.mal.movieapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.mal.movieapp.adapters.ReviewAdapter;
import com.udacity.mal.movieapp.adapters.TrailerAdapter;
import com.udacity.mal.movieapp.data.Movie;
import com.udacity.mal.movieapp.data.Review;
import com.udacity.mal.movieapp.data.Trailer;
import com.udacity.mal.movieapp.provider.MoviesContract;
import com.udacity.mal.movieapp.utilities.ApiParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieDetailFragment extends Fragment implements View.OnClickListener
{

    private Movie movieDetails;
    private ImageView mBackdropView;
    private ImageView mPosterView;
    private TextView mTitleView;
    private TextView mRatingView;
    private TextView mPlotView;
    private TextView mReleaseDateView;
    private ListView mTrailersBox;
    private ListView mReviewsBox;
    private View rootView;
    private Button favButton;
    private boolean isFav = false;

    private ArrayList<Review> mReviews = new ArrayList<>();
    private ReviewAdapter mReviewsAdapter;

    private ArrayList<Trailer> mTrailers = new ArrayList<>();
    private TrailerAdapter mTrailerAdapter;

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
        mReleaseDateView = (TextView) rootView.findViewById(R.id.movie_release_date);

        mTrailersBox = (ListView) rootView.findViewById(R.id.trailersBox);
        mTrailerAdapter = new TrailerAdapter(getContext(), mTrailers);
        mTrailersBox.setAdapter(mTrailerAdapter);

        mReviewsBox = (ListView) rootView.findViewById(R.id.reviewsBox);
        mReviewsAdapter = new ReviewAdapter(getContext(), mReviews);
        mReviewsBox.setAdapter(mReviewsAdapter);

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
        mReleaseDateView.setText(formatDate());

        new FetchDataTask().execute("videos");
        new FetchDataTask().execute("reviews");

        return rootView;
    }

    private String formatRating(Movie m)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getVote_average());
        sb.append("/10.0");
        return sb.toString();
    }

    private String formatDate()
    {
        String[] parts = movieDetails.getRelease_date().split("-");
        String month = new DateFormatSymbols().getMonths()[Integer.parseInt(parts[1]) - 1];
        return parts[2] + " " + month + " " + parts[0];
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

    private class FetchDataTask extends AsyncTask<String, Void, Void>
    {
        public static final String LOG_TAG = "FETCHMOVIES_TASK";

        public void parseTrailerJson(String json)
                throws JSONException
        {
            try
            {
                mTrailers.clear();
                JSONObject responseJson = new JSONObject(json);
                JSONArray resultsArray = responseJson.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++)
                {
                    mTrailers.add(new Trailer(resultsArray.getJSONObject(i)));
                }
                Log.i("TRAILERS", "Added " + mTrailers.size() + " trailers");
            } catch (JSONException e)
            {
                Log.e(LOG_TAG, "Parsing Trailers JSON failed");
                e.printStackTrace();
            }
        }

        public void parseReviewJson(String json)
                throws JSONException
        {
            try
            {
                mReviews.clear();
                JSONObject responseJson = new JSONObject(json);
                JSONArray resultsArray = responseJson.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++)
                {
                    mReviews.add(new Review(resultsArray.getJSONObject(i)));
                }
                Log.i("TRAILERS", "Added " + mReviews.size() + " reviews");
            } catch (JSONException e)
            {
                Log.e(LOG_TAG, "Parsing Reviews JSON failed");
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... params)
        {
            // Params might either be Popular or Top Rated

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            if (params.length == 0)
            {
                return null;
            }
            String reqType = params[0];

            HashMap<String, String> endpoints = new HashMap<>();
            endpoints.put("videos", ApiParams.VIDEOS_ENDPOINT);
            endpoints.put("reviews", ApiParams.REVIEWS_ENDPOINT);

            String moviesJsonStr = null;

            try
            {
                Uri builtUri = Uri.parse(ApiParams.MOVIES_BASE_URL).buildUpon()
                        .appendPath(ApiParams.MOVIE_ENDPOINT)
                        .appendPath(movieDetails.getId().toString())
                        .appendPath(endpoints.get(reqType))
                        .appendQueryParameter(ApiParams.API_KEY_PARAM, ApiParams.API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.i("API_URL", url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0)
                {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, moviesJsonStr.toString());

                if (reqType.equals("videos"))
                {
                    parseTrailerJson(moviesJsonStr);
                }
                else if (reqType.equals("reviews"))
                {
                    parseReviewJson(moviesJsonStr);
                }


            } catch (Exception e)
            {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    } catch (final IOException e)
                    {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            mTrailerAdapter.notifyDataSetChanged();
            mReviewsAdapter.notifyDataSetChanged();
        }
    }
}
