package com.udacity.mal.movieapp.data;

import android.util.Log;

import com.udacity.mal.movieapp.utilities.ApiParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Seif3 on 4/26/2016.
 * This project was created for the
 * Udacity Developing Android Apps
 * course project.
 */
public class Review
{
    private static final String LOG_TAG = "REVIEW";
    private String id;
    private String content;
    private String author;
    private String url;

    public Review(JSONObject json)
    {
        try
        {
            this.setId(json.getString(ApiParams.REVIEW_ID_KEY));
            this.setAuthor(json.getString(ApiParams.REVIEW_AUTHOR_KEY));
            this.setContent(json.getString(ApiParams.REVIEW_CONTENT_KEY));
            this.setUrl(json.getString(ApiParams.REVIEW_URL_KEY));
        } catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(LOG_TAG, "ERROR Parsing Review JSON Object");
        }
    }


    private void setId(String id)
    {
        this.id = id;
    }

    public String getContent()
    {
        return content;
    }

    private void setContent(String content)
    {
        this.content = content;
    }

    public String getAuthor()
    {
        return author;
    }

    private void setAuthor(String author)
    {
        this.author = author;
    }

    public String getUrl()
    {
        return url;
    }

    private void setUrl(String url)
    {
        this.url = url;
    }
}
