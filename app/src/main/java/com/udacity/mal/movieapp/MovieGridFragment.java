package com.udacity.mal.movieapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.mal.movieapp.adapters.GridAdapter;
import com.udacity.mal.movieapp.data.Movie;
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
import java.util.ArrayList;

public class MovieGridFragment extends Fragment
{
    public static final String LOG_TAG = "MOVIE_GRID_FRAGMENT";
    private GridAdapter mGridAdapter;
    private ArrayList<Movie> mMovieList = new ArrayList<>();

    public MovieGridFragment()
    {
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void refreshMovieList()
    {
        new FetchMoviesTask().execute("top_rated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        refreshMovieList();
        // Inflate the layout for this fragment
        mGridAdapter = new GridAdapter(getContext(), mMovieList);
        View fragView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        RecyclerView thumbnailsGrid = (RecyclerView) fragView.findViewById(R.id.thumbnails_grid);
        thumbnailsGrid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        thumbnailsGrid.setAdapter(mGridAdapter);
        return fragView;

    }

    private class FetchMoviesTask extends AsyncTask<String, Void, Void>
    {
        public static final String LOG_TAG = "FETCHMOVIES_TASK";

        public void parseMovieJson(String json)
                throws JSONException
        {
            try
            {
                mMovieList.clear();
                JSONObject responseJson = new JSONObject(json);
                JSONArray resultsArray = responseJson.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++)
                {
                    mMovieList.add(new Movie(resultsArray.getJSONObject(i)));
                }
            } catch (JSONException e)
            {
                Log.e(LOG_TAG, "Parsing JSON failed");
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... params)
        {
            // Params might either be Popular or Top Rated
            if (params.length == 0)
            {
                return null;
            }
            String orderType = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try
            {
                Uri builtUri = Uri.parse(ApiParams.MOVIES_BASE_URL + ApiParams.POPULAR_ENDPOINT).buildUpon()
                        .appendQueryParameter(ApiParams.API_KEY_PARAM, ApiParams.API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

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

                parseMovieJson(moviesJsonStr);
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
            mGridAdapter.notifyDataSetChanged();
        }
    }
}
