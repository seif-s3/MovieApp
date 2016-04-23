package com.udacity.mal.movieapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Seif3 on 4/23/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";
    private Context mContext;

    public MovieDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + MoviesContract.MovieEntry.TABLE_NAME + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY,"
                        + MoviesContract.MoviesColumns.MOVIE_ID + " TEXT NOT NULL,"
                        + MoviesContract.MoviesColumns.MOVIE_TITLE + " TEXT NOT NULL,"
                        + MoviesContract.MoviesColumns.MOVIE_OVERVIEW + " TEXT,"
                        + MoviesContract.MoviesColumns.MOVIE_GENRE_IDS + " TEXT,"
                        + MoviesContract.MoviesColumns.MOVIE_POPULARITY + " REAL,"
                        + MoviesContract.MoviesColumns.MOVIE_VOTE_AVERAGE + " REAL,"
                        + MoviesContract.MoviesColumns.MOVIE_VOTE_COUNT + " INTEGER,"
                        + MoviesContract.MoviesColumns.MOVIE_BACKDROP_PATH + " TEXT,"
                        + MoviesContract.MoviesColumns.MOVIE_POSTER_PATH + " TEXT,"
                        + MoviesContract.MoviesColumns.MOVIE_RELEASE_DATE + " TEXT,"
                        + MoviesContract.MoviesColumns.MOVIE_FAVORED + " INTEGER NOT NULL DEFAULT 0,"
                        + "UNIQUE (" + MoviesContract.MoviesColumns.MOVIE_ID + ") ON CONFLICT REPLACE)";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
