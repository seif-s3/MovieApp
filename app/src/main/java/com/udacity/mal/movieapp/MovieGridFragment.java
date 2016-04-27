package com.udacity.mal.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.udacity.mal.movieapp.adapters.FavoritesGridAdapter;
import com.udacity.mal.movieapp.adapters.GridAdapter;
import com.udacity.mal.movieapp.data.Movie;
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
import java.util.ArrayList;
import java.util.HashMap;

public class MovieGridFragment
        extends Fragment
        implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>
{
    public static final String LOG_TAG = "MOVIE_GRID_FRAGMENT";
    public static final int FAV_LOADER = 0;
    private GridAdapter mGridAdapter;
    private FavoritesGridAdapter mFavGridAdapter;
    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeContainer;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private RecyclerView thumbnailsGrid;
    Bundle state;

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
        if (sortOrder.equals(getString(R.string.fav_only_key)))
        {
            // Populate mMovieList with movies in Database
            // Select all movies in Database
            getLoaderManager().initLoader(FAV_LOADER, null, this);
            mSwipeContainer.setRefreshing(false);
            thumbnailsGrid.setAdapter(mFavGridAdapter);
//            Cursor movieCursor = getContext().getContentResolver().query(
//                    MoviesContract.MovieEntry.CONTENT_URI,
//                    null,
//                    null,
//                    null,
//                    null
//            );
//            mMovieList.clear();
//            if (movieCursor.moveToFirst())
//            {
//
//                int id_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_ID);
//                int title_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_TITLE);
//                int overview_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_OVERVIEW);
//                int popularity_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_POPULARITY);
//                int vote_count_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_VOTE_COUNT);
//                int release_date_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_RELEASE_DATE);
//                int favored_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_FAVORED);
//                int poster_path_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_POSTER_PATH);
//                int backdrop_path_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_BACKDROP_PATH);
//                int original_language_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_ORIGINAL_LANGUAGE);
//                int original_title_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_ORIGINAL_TITLE);
//                int adult_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_ADULT);
//                int video_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_VIDEO);
//                int genre_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_GENRE_IDS);
//                int vote_avg_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_VOTE_AVERAGE);
//
//                do
//                {
//                    Movie temp = new Movie();
//                    temp.setId(movieCursor.getInt(id_col));
//                    temp.setTitle(movieCursor.getString(title_col));
//                    temp.setOverview(movieCursor.getString(overview_col));
//                    temp.setPopularity(movieCursor.getDouble(popularity_col));
//                    temp.setVote_count(movieCursor.getInt(vote_count_col));
//                    temp.setRelease_date(movieCursor.getString(release_date_col));
//                    temp.setPoster_path(movieCursor.getString(poster_path_col));
//                    temp.setBackdrop_path(movieCursor.getString(backdrop_path_col));
//                    temp.setOriginal_language(movieCursor.getString(original_language_col));
//                    temp.setOriginal_title(movieCursor.getString(original_title_col));
//                    temp.setAdult(movieCursor.getInt(adult_col) == 1 ? true : false);
//                    temp.setVideo(movieCursor.getInt(video_col) == 1 ? true : false);
//                    temp.setGenre_ids(Utilities.parseGenres(movieCursor.getString(genre_col)));
//                    temp.setVote_average(movieCursor.getDouble(vote_avg_col));
//
//                    mMovieList.add(temp);
//
//                } while (movieCursor.moveToNext());
//                movieCursor.close();
//            }
//            mGridAdapter.notifyDataSetChanged();
//            mSwipeContainer.setRefreshing(false);
//            Log.i("GridPosition", String.valueOf(sharedPref.getInt(getString(R.string.poster_grid_position_shared_pref_key), 0)));
//            thumbnailsGrid.setVerticalScrollbarPosition(sharedPref.getInt(getString(R.string.poster_grid_position_shared_pref_key), 0));
        }
        else
        {

            new FetchMoviesTask().execute(sortOrder);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        // Initialize Spinner
        // TODO: Move spinner to actionbar
        Spinner spinner = (Spinner) fragView.findViewById(R.id.sort_pref_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_prefrences_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(sharedPref.getInt(getString(R.string.spinner_position_shared_pref_key), 0));
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
        mFavGridAdapter = new FavoritesGridAdapter(getContext(), null);

        thumbnailsGrid = (RecyclerView) fragView.findViewById(R.id.thumbnails_grid);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            thumbnailsGrid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        else
        {
            thumbnailsGrid.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }

        thumbnailsGrid.setAdapter(mGridAdapter);

        refreshMovieList(sharedPref.getString(
                getString(R.string.sort_order_shared_pref_key), getString(R.string.most_popular_key)
        ));
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
            editor.putInt(getString(R.string.spinner_position_shared_pref_key), 1);
            editor.putInt(getString(R.string.poster_grid_position_shared_pref_key), 0); // Reset Grid to 0
            editor.commit();
            refreshMovieList(getString(R.string.top_rated_key));
        }
        else if (sortBy.equals("Most Popular"))
        {
            editor.putString(getString(R.string.sort_order_shared_pref_key), getString(R.string.most_popular_key));
            editor.putInt(getString(R.string.spinner_position_shared_pref_key), 0);
            editor.putInt(getString(R.string.poster_grid_position_shared_pref_key), 0); // Reset Grid to 0
            editor.commit();
            refreshMovieList(getString(R.string.most_popular_key));
        }
        else if (sortBy.equals("Favorites"))
        {
            editor.putString(getString(R.string.sort_order_shared_pref_key), getString(R.string.fav_only_key));
            editor.putInt(getString(R.string.spinner_position_shared_pref_key), 2);
            editor.putInt(getString(R.string.poster_grid_position_shared_pref_key), 0); // Reset Grid to 0
            editor.commit();
            refreshMovieList(getString(R.string.fav_only_key));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new android.support.v4.content.CursorLoader(
                getActivity(),
                MoviesContract.MovieEntry.buildMovieUri(),
                null,
                null,
                null,
                null
        );
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mFavGridAdapter.swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mFavGridAdapter.swapCursor(null);
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
        protected void onPreExecute()
        {
            // This probably fixed the RV Bug
            mMovieList.clear();
            mGridAdapter.notifyDataSetChanged();
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
            thumbnailsGrid.setAdapter(mGridAdapter);
            mSwipeContainer.setRefreshing(false);
            Log.i("GridPosition", String.valueOf(sharedPref.getInt(getString(R.string.poster_grid_position_shared_pref_key), -1)));
            thumbnailsGrid.scrollToPosition(sharedPref.getInt(getString(R.string.poster_grid_position_shared_pref_key), 0));
        }
    }
}
