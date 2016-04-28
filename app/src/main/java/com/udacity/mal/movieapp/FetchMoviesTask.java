package com.udacity.mal.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.udacity.mal.movieapp.utilities.ApiParams;
import com.udacity.mal.movieapp.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Seif3 on 4/26/2016.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, Void>
{
    public static final String LOG_TAG = "FETCHMOVIES_TASK";
    private Context mContext;
    private SharedPreferences sharedPref;

    public FetchMoviesTask(Context ctx)
    {
        mContext = ctx;
    }

    public void parseMovieJson(String json)
            throws JSONException
    {
        try
        {
//            mMovieList.clear();
            JSONObject responseJson = new JSONObject(json);
            JSONArray resultsArray = responseJson.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++)
            {
//                mMovieList.add(new Movie(resultsArray.getJSONObject(i)));
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
//        mGridAdapter.notifyDataSetChanged();
//        mSwipeContainer.setRefreshing(false);
        Log.i("GridPosition", String.valueOf(sharedPref.getInt(Utilities.getGridPositionKey(mContext), 0)));

    }
}
