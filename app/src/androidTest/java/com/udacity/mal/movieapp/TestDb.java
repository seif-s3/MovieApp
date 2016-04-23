package com.udacity.mal.movieapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.udacity.mal.movieapp.provider.MovieDbHelper;


/**
 * Created by Seif3 on 4/23/2016.
 */
public class TestDb extends AndroidTestCase
{

    void deleteTheDatabase()
    {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    @Override
    protected void setUp() throws Exception
    {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Exception
    {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);

        SQLiteDatabase db = new MovieDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());
        c.moveToNext();
        assertEquals("movies", c.getString(0));
        db.close();
    }

}
