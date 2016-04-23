package com.udacity.mal.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.mal.movieapp.data.Movie;
import com.udacity.mal.movieapp.utilities.ApiParams;

public class MovieDetailFragment extends Fragment
{

    private Movie movieDetails;
    private ImageView mBackdropView;
    private ImageView mPosterView;
    private TextView mTitleView;
    private TextView mRatingView;
    private TextView mRuntimeView;

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

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mBackdropView = (ImageView) rootView.findViewById(R.id.movie_detail_backdrop);
        mPosterView = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
        mTitleView = (TextView) rootView.findViewById(R.id.movie_detail_title);
        mRatingView = (TextView) rootView.findViewById(R.id.movie_detail_rating);
        mRuntimeView = (TextView) rootView.findViewById(R.id.movie_detail_runtime);

        Picasso.with(getContext()).load(ApiParams.BASE_IMG_URL + movieDetails.getBackdrop_path()).into(mBackdropView);
        Picasso.with(getContext()).load(ApiParams.BASE_IMG_URL + movieDetails.getPoster_path()).into(mPosterView);
        mTitleView.setText(movieDetails.getTitle());
        mRatingView.setText(movieDetails.getVote_average().toString());

        return rootView;
    }

}
