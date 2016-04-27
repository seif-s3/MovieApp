package com.udacity.mal.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.udacity.mal.movieapp.data.Movie;
import com.udacity.mal.movieapp.interfaces.MovieChosenListener;

public class MainActivity extends AppCompatActivity implements MovieChosenListener
{
    public static String LOG_TAG = "MAIN";
    public boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout framePanel = (FrameLayout) findViewById(R.id.movie_details_pane);
        if (framePanel == null)
        {
            // Single Pane UI
            mTwoPane = false;
            Log.i(LOG_TAG, "One Pane");

        }
        else
        {
            mTwoPane = true;
        }
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_selector_pane, new MovieGridFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    public void paneHandleItemClick(Movie m)
    {
        if (!mTwoPane)
        {
            Intent detail = new Intent(this, DetailActivity.class);
            detail.putExtra("MOVIE", m);
            startActivity(detail);
        }
        else
        {
            MovieDetailFragment detailFragment = new MovieDetailFragment();
            Bundle extras = new Bundle();
            extras.putParcelable("MOVIE", m);
            detailFragment.setArguments(extras);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_pane, detailFragment)
                    .commit();
        }
    }
}
