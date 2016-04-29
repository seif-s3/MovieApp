package com.udacity.mal.movieapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;

import com.udacity.mal.movieapp.data.Movie;
import com.udacity.mal.movieapp.interfaces.MovieChosenListener;

public class MainActivity extends AppCompatActivity implements MovieChosenListener
{
    private static String LOG_TAG = "MAIN";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

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
            invalidateOptionsMenu();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_pane, detailFragment)
                    .commit();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions()
    {
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs external storage access");
            builder.setMessage("Please grant access so this app can cache images to file.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @TargetApi(Build.VERSION_CODES.M)
                public void onDismiss(DialogInterface dialog)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            });
            builder.show();
        }
        if (this.checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs Internet access");
            builder.setMessage("Please grant access so this app can get movie details.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @TargetApi(Build.VERSION_CODES.M)
                public void onDismiss(DialogInterface dialog)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            });
            builder.show();
        }
        if (this.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs Network State access");
            builder.setMessage("Please grant access so this app can check whether your device is connected to the internet.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @TargetApi(Build.VERSION_CODES.M)
                public void onDismiss(DialogInterface dialog)
                {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                }
            });
            builder.show();
        }

    }
}
