package com.udacity.mal.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null)
        {
            MovieDetailFragment mf = new MovieDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, new MovieDetailFragment())
                    .commit();
        }
    }

}