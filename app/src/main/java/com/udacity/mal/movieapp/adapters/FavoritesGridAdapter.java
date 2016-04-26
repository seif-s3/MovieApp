package com.udacity.mal.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.mal.movieapp.DetailActivity;
import com.udacity.mal.movieapp.R;
import com.udacity.mal.movieapp.data.Movie;
import com.udacity.mal.movieapp.provider.MoviesContract;
import com.udacity.mal.movieapp.utilities.ApiParams;
import com.udacity.mal.movieapp.utilities.Utilities;

/**
 * Created by Seif3 on 3/25/2016.
 */
public class FavoritesGridAdapter extends CursorRecyclerViewAdapter<FavoritesGridAdapter.PosterViewHolder>
{

    public FavoritesGridAdapter(Context context, Cursor cursor)
    {
        super(context, cursor);
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

//    @Override
//    public void onBindViewHolder(PosterViewHolder holder, final int position)
//    {
//        if (mMovies.size() <= 0)
//        {
//            return;
//        }
//        else
//        {
//            CardView posterCardView = holder.cardViewHolder;
//            ImageView poster = holder.posterHolder;
//            TextView movieTitle = holder.titleHolder;
//            TextView date = holder.dateHolder;
//
//            // TODO: Handle Tablet mode from here
//            posterCardView.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    //Toast.makeText(mContext, mMovies.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//
//                    // Save clicked item position
//                    SharedPreferences shp = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = shp.edit();
//                    editor.putInt(mContext.getString(R.string.poster_grid_position_shared_pref_key), position);
//                    editor.commit();
//
//                    Intent detail = new Intent(mContext, DetailActivity.class);
//                    detail.putExtra("MOVIE", mMovies.get(position));
//                    mContext.startActivity(detail);
//
//                }
//            });
//            movieTitle.setText(mMovies.get(position).getTitle());
//            date.setText(mMovies.get(position).getRelease_date().split("-")[0]);
//            Picasso.with(mContext).load(ApiParams.BASE_IMG_URL + mMovies.get(position).getPoster_path()).into(poster);
//        }
//    }


    @Override
    public void onBindViewHolder(PosterViewHolder holder, final Cursor movieCursor)
    {
        CardView posterCardView = holder.cardViewHolder;
        ImageView poster = holder.posterHolder;
        TextView movieTitle = holder.titleHolder;
        TextView date = holder.dateHolder;
        final int id_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_ID);
        final int title_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_TITLE);
        final int overview_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_OVERVIEW);
        final int popularity_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_POPULARITY);
        final int vote_count_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_VOTE_COUNT);
        final int release_date_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_RELEASE_DATE);
        final int favored_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_FAVORED);
        final int poster_path_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_POSTER_PATH);
        final int backdrop_path_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_BACKDROP_PATH);
        final int original_language_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_ORIGINAL_LANGUAGE);
        final int original_title_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_ORIGINAL_TITLE);
        final int adult_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_ADULT);
        final int video_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_VIDEO);
        final int genre_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_GENRE_IDS);
        final int vote_avg_col = movieCursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_VOTE_AVERAGE);

        posterCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(mContext, mMovies.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                // Save clicked item position
                RecyclerView parentRecyclerView = (RecyclerView) v.getParent();
                int position = parentRecyclerView.getChildLayoutPosition(v);
                if (position != RecyclerView.NO_POSITION)
                {
                    movieCursor.moveToPosition(position);
                    SharedPreferences shp = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shp.edit();
                    editor.putInt(mContext.getString(R.string.poster_grid_position_shared_pref_key), position);
                    editor.commit();

                }

                Intent detail = new Intent(mContext, DetailActivity.class);
                Movie temp = new Movie();
                temp.setId(movieCursor.getInt(id_col));
                temp.setTitle(movieCursor.getString(title_col));
                temp.setOverview(movieCursor.getString(overview_col));
                temp.setPopularity(movieCursor.getDouble(popularity_col));
                temp.setVote_count(movieCursor.getInt(vote_count_col));
                temp.setRelease_date(movieCursor.getString(release_date_col));
                temp.setPoster_path(movieCursor.getString(poster_path_col));
                temp.setBackdrop_path(movieCursor.getString(backdrop_path_col));
                temp.setOriginal_language(movieCursor.getString(original_language_col));
                temp.setOriginal_title(movieCursor.getString(original_title_col));
                temp.setAdult(movieCursor.getInt(adult_col) == 1 ? true : false);
                temp.setVideo(movieCursor.getInt(video_col) == 1 ? true : false);
                temp.setGenre_ids(Utilities.parseGenres(movieCursor.getString(genre_col)));
                temp.setVote_average(movieCursor.getDouble(vote_avg_col));


                detail.putExtra("MOVIE", temp);
                mContext.startActivity(detail);

            }
        });

        movieTitle.setText(movieCursor.getString(title_col));
        date.setText(movieCursor.getString(release_date_col).split("-")[0]);
        Picasso.with(mContext).load(ApiParams.BASE_IMG_URL + movieCursor.getString(poster_path_col)).into(poster);

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
