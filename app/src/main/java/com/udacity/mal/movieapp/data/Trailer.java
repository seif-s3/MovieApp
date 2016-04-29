package com.udacity.mal.movieapp.data;

import android.util.Log;

import com.udacity.mal.movieapp.utilities.ApiParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Seif3 on 4/26/2016.
 */
public class Trailer
{
    private static final String LOG_TAG = "TRAILER";

    private String id;
    private String key;
    private String name;
    private String site;
    private String type;
    private int size;
    private String lang;
    private String locale;

    public Trailer(JSONObject json)
    {
        try
        {
            this.setId(json.getString(ApiParams.TRAILER_ID_KEY));
            this.setLang(json.getString(ApiParams.TRAILER_LANG_KEY));
            this.setLocale(json.getString(ApiParams.TRAILER_LOCALE_KEY));
            this.setKey(json.getString(ApiParams.TRAILER_KEY_KEY));
            this.setName(json.getString(ApiParams.TRAILER_NAME_KEY));
            this.setSite(json.getString(ApiParams.TRAILER_SITE_KEY));
            this.setSize(json.getInt(ApiParams.TRAILER_SIZE_KEY));
            this.setType(json.getString(ApiParams.TRAILER_TYPE_KEY));
        } catch (JSONException e)
        {
            Log.e(LOG_TAG, "ERROR Parsing Trailer JSON Object");
            e.printStackTrace();
        }
    }


    private void setId(String id)
    {
        this.id = id;
    }

    public String getKey()
    {
        return key;
    }

    private void setKey(String key)
    {
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    private void setName(String name)
    {
        this.name = name;
    }

    private void setSite(String site)
    {
        this.site = site;
    }

    private void setType(String type)
    {
        this.type = type;
    }

    private void setSize(int size)
    {
        this.size = size;
    }

    private void setLang(String lang)
    {
        this.lang = lang;
    }

    private void setLocale(String locale)
    {
        this.locale = locale;
    }
}
