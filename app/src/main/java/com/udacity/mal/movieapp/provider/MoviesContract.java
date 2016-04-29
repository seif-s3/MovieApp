package com.udacity.mal.movieapp.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Seif3 on 4/23/2016.
 */
public class MoviesContract
{
    public static final String CONTENT_AUTHORITY = "com.udacity.mal.movieapp.provider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public interface MoviesColumns
    {
        String MOVIE_ID = "movie_id";
        String MOVIE_TITLE = "movie_title";
        String MOVIE_OVERVIEW = "movie_overview";
        String MOVIE_POPULARITY = "movie_popularity";
        String MOVIE_GENRE_IDS = "movie_genre_ids"; //This is a comma-separated list of genre ids.
        String MOVIE_VOTE_COUNT = "movie_vote_count";
        String MOVIE_VOTE_AVERAGE = "movie_vote_average";
        String MOVIE_RELEASE_DATE = "movie_release_date";
        String MOVIE_FAVORED = "movie_favored";
        String MOVIE_POSTER_PATH = "movie_poster_path";
        String MOVIE_BACKDROP_PATH = "movie_backdrop_path";
        String MOVIE_ORIGINAL_TITLE = "movie_original_title";
        String MOVIE_ORIGINAL_LANGUAGE = "movie_original_language";
        String MOVIE_ADULT = "movie_adult";
        String MOVIE_VIDEO = "movie_video";
    }


    public static class MovieEntry implements BaseColumns, MoviesColumns
    {
        public static final String TABLE_NAME = "movies";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;


        // Build a URI that references all movies
        public static Uri buildMovieUri()
        {
            return CONTENT_URI;
        }

        public static Uri buildMovieUri(String movieId)
        {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

    }

}
