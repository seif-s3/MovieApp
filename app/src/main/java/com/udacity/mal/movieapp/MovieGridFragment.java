package com.udacity.mal.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import java.util.HashMap;

public class MovieGridFragment extends Fragment implements AdapterView.OnItemSelectedListener
{
    public static final String LOG_TAG = "MOVIE_GRID_FRAGMENT";
    private GridAdapter mGridAdapter;
    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeContainer;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public MovieGridFragment()
    {
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_settings:
                // TODO: Launch
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sharedPref = getContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void refreshMovieList(String sortOrder)
    {
        new FetchMoviesTask().execute(sortOrder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        refreshMovieList(sharedPref.getString(
                getString(R.string.sort_order_shared_pref_key), getString(R.string.most_popular_key)
        ));

        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        // Initialize Spinner
        // TODO: Move spinner to actionbar
        Spinner spinner = (Spinner) fragView.findViewById(R.id.sort_pref_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_prefrences_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        // Initialize Swipe Menu
        mSwipeContainer = (SwipeRefreshLayout) fragView.findViewById(R.id.swipePosterContainer);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refreshMovieList(sharedPref.getString(
                        getString(R.string.sort_order_shared_pref_key), getString(R.string.most_popular_key)
                ));
            }
        });

        // Initialize Recycler View
        mGridAdapter = new GridAdapter(getContext(), mMovieList);
        RecyclerView thumbnailsGrid = (RecyclerView) fragView.findViewById(R.id.thumbnails_grid);
        thumbnailsGrid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        thumbnailsGrid.setAdapter(mGridAdapter);

        return fragView;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String sortBy = (String) parent.getItemAtPosition(position);
        Log.i("SpinnerItemSelected", sortBy);
        if (sortBy.equals("Top Rated"))
        {
            // TODO: Refactor Keys and Values from R.string to Static class variables
            editor.putString(getString(R.string.sort_order_shared_pref_key), getString(R.string.top_rated_key));
            editor.commit();
            refreshMovieList(getString(R.string.top_rated_key));
        }
        else if (sortBy.equals("Most Popular"))
        {
            editor.putString(getString(R.string.sort_order_shared_pref_key), getString(R.string.most_popular_key));
            editor.commit();
            refreshMovieList(getString(R.string.most_popular_key));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

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

            HashMap<String, String> orderEndpoints = new HashMap<>();
            orderEndpoints.put("top_rated", ApiParams.TOP_RATED_ENDPOINT);
            orderEndpoints.put("most_popular", ApiParams.POPULAR_ENDPOINT);

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try
            {
                Uri builtUri = Uri.parse(ApiParams.MOVIES_BASE_URL + orderEndpoints.get(orderType)).buildUpon()
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
            mSwipeContainer.setRefreshing(false);
        }
    }
}
