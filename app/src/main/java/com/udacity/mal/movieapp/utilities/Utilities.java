package com.udacity.mal.movieapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.mal.movieapp.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Seif3 on 3/26/2016.
 */

/*
Class containing helper functions
 */
public class Utilities
{
    public static int[] parseGenres(String genres)
    {
        Log.d("genres", genres);
        return new int[10];
    }

    public static boolean isInternetAvailable(Context ctx)
    {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static String imageCacheFolder = "/MovieApp Cache";

    public static boolean imageIsCached(String name)
    {
        File dir = new File(Environment.getExternalStorageDirectory(), Utilities.imageCacheFolder);
        if (!dir.exists())
        {
            Log.i("DIRECTORY", "Creating directory");
            dir.mkdirs();
        }
        File file = new File(dir + name);
        Log.i("imageIsCached", file.getPath());

        return file.exists();
    }

    public static Target getLocalTarget(final String name)
    {
        return new Target()
        {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from)
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Log.i("LOADING_IMAGE", "Started Loading");
                        File dir = new File(Environment.getExternalStorageDirectory(), Utilities.imageCacheFolder);
                        if (!dir.exists())
                        {
                            Log.i("DIRECTORY", "Creating directory");
                            dir.mkdirs();
                        }
                        File file = new File(dir + name);
                        Log.i("File Path", file.getPath());
                        if (file.exists())
                        {
                            // Abort download if file already exists in cache.
                            Log.d("IMAGE_CACHE", "File already exists in cache");
                            return;
                        }
                        try
                        {
                            file.createNewFile();
                            FileOutputStream outStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                            outStream.close();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable)
            {
                Log.i("LOADING_IMAGE", "Bitmap Failed");
                // Possible failed due to internet connection unavailable
                File dir = new File(Environment.getExternalStorageDirectory(), Utilities.imageCacheFolder);
                if (!dir.exists())
                {
                    Log.i("DIRECTORY", "Creating directory");
                    dir.mkdirs();
                }
                File file = new File(name);
                Log.i("Failed File Path", file.getPath());
                if (file.exists())
                {
                    // Abort download if file already exists in cache.
                    Log.d("IMAGE_CACHE", "File already exists in cache");
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable)
            {
                Log.i("LOADING_IMAGE", "Prepare Load");
            }
        };
    }


    public static String getGridPositionKey(Context ctx)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(
                ctx.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        );

        String sort_order = sharedPreferences.getString(ctx.getString(R.string.sort_order_shared_pref_key), "most_popular");
        if (sort_order.equals(ctx.getString(R.string.most_popular_key)))
        {
            return ctx.getString(R.string.popular_grid_position_shared_pref_key);
        }

        if (sort_order.equals(ctx.getString(R.string.top_rated_key)))
        {
            return ctx.getString(R.string.top_rated_grid_position_shared_pref_key);
        }

        if (sort_order.equals(ctx.getString(R.string.fav_only_key)))
        {
            return ctx.getString(R.string.fav_grid_position_shared_pref_key);
        }

        return "most_popular";
    }

    public static void clearCache(Context ctx)
    {
        File dir = new File(Environment.getExternalStorageDirectory(), Utilities.imageCacheFolder);
        dir.delete();
        Toast.makeText(ctx, "Cleared!", Toast.LENGTH_SHORT).show();
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (String aChildren : children)
            {
                new File(dir, aChildren).delete();
            }
        }
    }
}
