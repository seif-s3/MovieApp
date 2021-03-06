package com.udacity.mal.movieapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.mal.movieapp.R;
import com.udacity.mal.movieapp.data.Movie;
import com.udacity.mal.movieapp.interfaces.MovieChosenListener;
import com.udacity.mal.movieapp.utilities.ApiParams;
import com.udacity.mal.movieapp.utilities.Utilities;

import java.util.ArrayList;

/**
 * Created by Seif3 on 3/25/2016.
 * This project was created for the
 * Udacity Developing Android Apps
 * course project.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.PosterViewHolder>
{
    private Context mContext;
    private ArrayList<Movie> mMovies;

    private MovieChosenListener mMovieListener;


    public void setMovieListener(MovieChosenListener mMovieListener)
    {
        this.mMovieListener = mMovieListener;
    }

    public GridAdapter(Context ctx, ArrayList<Movie> movies)
    {
        mContext = ctx;
        mMovies = movies;
    }


    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_layout, parent, false);
        return new PosterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, final int position)
    {
        if (mMovies.size() <= 0)
        {
            return;
        }
        else
        {
            CardView posterCardView = holder.cardViewHolder;
            ImageView poster = holder.posterHolder;
            final TextView movieTitle = holder.titleHolder;
            TextView date = holder.dateHolder;

            // TODO: Handle Tablet mode from here
            posterCardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Toast.makeText(mContext, mMovies.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                    // Save clicked item position
                    SharedPreferences shp = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shp.edit();


                    editor.putInt(Utilities.getGridPositionKey(mContext), position);
                    editor.commit();
                    Log.d("SetGridPosition", Utilities.getGridPositionKey(mContext) + " " + String.valueOf(shp.getInt(Utilities.getGridPositionKey(mContext), -1)));


                    mMovieListener.paneHandleItemClick(mMovies.get(position));
                    /*
                    Intent detail = new Intent(mContext, DetailActivity.class);
                    detail.putExtra("MOVIE", mMovies.get(position));
                    mContext.startActivity(detail);
*/
                }
            });
            movieTitle.setText(mMovies.get(position).getTitle());
            date.setText(mMovies.get(position).getRelease_date().split("-")[0]);
            Picasso.with(mContext).load(ApiParams.BASE_IMG_URL + mMovies.get(position).getPoster_path()).into(poster);
        }
    }

    @Override
    public int getItemCount()
    {
        return mMovies.size();
    }

    public static class PosterViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardViewHolder;
        ImageView posterHolder;
        TextView titleHolder;
        TextView dateHolder;

        public PosterViewHolder(View itemView)
        {
            super(itemView);
            cardViewHolder = (CardView) itemView.findViewById(R.id.poster_card_view);
            posterHolder = (ImageView) itemView.findViewById(R.id.poster_holder);
            titleHolder = (TextView) itemView.findViewById(R.id.movie_item_title);
            dateHolder = (TextView) itemView.findViewById(R.id.movie_item_genres);
        }
    }
}