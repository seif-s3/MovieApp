package com.udacity.mal.movieapp.data;

import android.util.Log;

import com.udacity.mal.movieapp.utilities.ApiParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Seif3 on 3/25/2016.
 */
public class Movie
{
    public static final String LOG_TAG = "MOVIE_CLASS";
    private Integer id;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private String release_date;
    private Double vote_average;
    private Integer[] genre_ids;
    private String title;
    private String original_title;
    private String original_language;
    private Double popularity;
    private Boolean video;
    private Boolean adult;
    private Integer vote_count;

    public Movie(JSONObject m)
    {
        try
        {
            this.setPoster_path(m.getString(ApiParams.POSTER_PATH_KEY));
            this.setAdult(m.getBoolean(ApiParams.ADULT_KEY));
            this.setOverview(m.getString(ApiParams.OVERVIEW_KEY));
            this.setRelease_date(m.getString(ApiParams.RELEASE_DATE_KEY));
            // Converting Genre IDs JSONArray to Integer Array
            JSONArray genreTemp = m.getJSONArray(ApiParams.GENRE_IDS_KEY);
            this.genre_ids = new Integer[genreTemp.length()];
            for(int i=0;i<genreTemp.length(); i++)
            {
                genre_ids[i] = genreTemp.getInt(i);
            }
            this.setId(m.getInt(ApiParams.ID_KEY));
            this.setTitle(m.getString(ApiParams.TITLE_KEY));
            this.setOriginal_language(m.getString(ApiParams.ORIGINAL_LANG_KEY));
            this.setOriginal_title(m.getString(ApiParams.ORIGINAL_TITLE_KEY));
            this.setBackdrop_path(m.getString(ApiParams.BACKDROP_PATH_KEY));
            this.setPopularity(m.getDouble(ApiParams.POPULARITY_KEY));
            this.setVote_count(m.getInt(ApiParams.VOTE_COUNT_KEY));
            this.setVideo(m.getBoolean(ApiParams.VIDEO_KEY));
            this.setVote_average(m.getDouble(ApiParams.VOTE_AVG_KEY));

        }
        catch(JSONException e)
        {
            Log.e(LOG_TAG, "ERROR Parsing JSON Object");
            e.printStackTrace();
        }
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getOverview()
    {
        return overview;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public String getPoster_path()
    {
        return poster_path;
    }

    public void setPoster_path(String poster_path)
    {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path()
    {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path)
    {
        this.backdrop_path = backdrop_path;
    }

    public String getRelease_date()
    {
        return release_date;
    }

    public void setRelease_date(String release_date)
    {
        this.release_date = release_date;
    }

    public Double getVote_average()
    {
        return vote_average;
    }

    public void setVote_average(Double vote_verage)
    {
        this.vote_average = vote_verage;
    }

    public Integer[] getGenre_ids()
    {
        return genre_ids;
    }

    public void setGenre_ids(Integer[] genre_ids)
    {
        this.genre_ids = genre_ids;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getOriginal_title()
    {
        return original_title;
    }

    public void setOriginal_title(String original_title)
    {
        this.original_title = original_title;
    }

    public String getOriginal_language()
    {
        return original_language;
    }

    public void setOriginal_language(String original_language)
    {
        this.original_language = original_language;
    }

    public Double getPopularity()
    {
        return popularity;
    }

    public void setPopularity(Double popularity)
    {
        this.popularity = popularity;
    }

    public Boolean getVideo()
    {
        return video;
    }

    public void setVideo(Boolean video)
    {
        this.video = video;
    }

    public Boolean getAdult()
    {
        return adult;
    }

    public void setAdult(Boolean adult)
    {
        this.adult = adult;
    }

    public Integer getVote_count()
    {
        return vote_count;
    }

    public void setVote_count(Integer vote_count)
    {
        this.vote_count = vote_count;
    }
}
