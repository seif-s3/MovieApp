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
    public static final String LOG_TAG = "TRAILER";

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


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSite()
    {
        return site;
    }

    public void setSite(String site)
    {
        this.site = site;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public String getIso_3166_1()
    {
        return locale;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }
}
