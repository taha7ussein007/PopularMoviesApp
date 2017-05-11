package com.example.tahahussein007.popularmoviesapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tahahussein007 on 10/7/16.
 */

public class
MovieMODEL implements Serializable {


    private String poster_path;
    private String backdrop_path;
    private String overview;
    private String title;
    private String id;
    private String release_date;
    private float vote_average;
    final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w185";


    public MovieMODEL(JSONObject movie) throws JSONException {
        this.id = movie.getString("id");
        this.title = movie.getString("original_title");
        this.poster_path = movie.getString("poster_path");
        this.backdrop_path = movie.getString("backdrop_path");
        this.overview = movie.getString("overview");
        this.vote_average = movie.getInt("vote_average");
        this.release_date = movie.getString("release_date");
    }

    public MovieMODEL(){}

    public MovieMODEL(String poster_path, String backdrop_path, String overview, String title, String id, String date, float rate_average) {
        this.poster_path = BASE_URL_IMAGE.concat(poster_path);
        this.backdrop_path = BASE_URL_IMAGE.concat(backdrop_path);
        this.overview = overview;
        this.title = title;
        this.id = id;
        this.release_date = date;
        this.vote_average = rate_average;
    }

    public float getRating() {
        return vote_average;
    }

    public void setRating(float rating) {
        this.vote_average = rating;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path){
        if(!poster_path.contains(BASE_URL_IMAGE))
            this.poster_path = BASE_URL_IMAGE.concat(poster_path);
        else
            this.poster_path = poster_path;
    }
    public String getBackdrop_path(){
        return backdrop_path;
    }
    public void setBackdrop_path(String backdrop_path)
    {
        if(!backdrop_path.contains(BASE_URL_IMAGE))
            this.backdrop_path = BASE_URL_IMAGE.concat(backdrop_path);
        else
            this.backdrop_path = backdrop_path;
    }

    public String getOverview(){
        return overview;
    }
    public void setOverview(String overview){
        this.overview = overview;
    }

    public String getRelease_date(){
        return release_date;
    }
    public void setRelease_date(String release_date){
        this.release_date = release_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
