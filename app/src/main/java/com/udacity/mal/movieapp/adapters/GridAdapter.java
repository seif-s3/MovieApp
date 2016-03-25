package com.udacity.mal.movieapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.mal.movieapp.R;
import com.udacity.mal.movieapp.data.Movie;
import com.udacity.mal.movieapp.utilities.ApiParams;

import java.util.ArrayList;

/**
 * Created by Seif3 on 3/25/2016.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.PosterViewHolder>
{
    private Context mContext;
    public ArrayList<Movie> mMovies;

    public GridAdapter(Context ctx, ArrayList<Movie> movies)
    {
        mContext = ctx;
        mMovies = movies;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_layout, parent, false);
        PosterViewHolder pvh = new PosterViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, final int position)
    {
        ImageView poster = holder.posterHolder;
        poster.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO: Launch Detail Activity from here
                Toast.makeText(mContext, mMovies.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        Picasso.with(mContext).load(ApiParams.BASE_IMG_URL + mMovies.get(position).getPoster_path()).into(poster);
    }

    @Override
    public int getItemCount()
    {
        return mMovies.size();
    }

    public static class PosterViewHolder extends RecyclerView.ViewHolder
    {

        ImageView posterHolder;

        public PosterViewHolder(View itemView)
        {
            super(itemView);
            posterHolder = (ImageView) itemView.findViewById(R.id.poster_holder);
        }
    }
}
