package com.example.tahahussein007.popularmoviesapp.Models;

/**
 * Created by tahahussein007 on 11/20/16.
 */

public class TrailersMODEL {

    String videoName;
    String videoLink;


    public TrailersMODEL(String videoName, String videoLink) {
        this.videoName = videoName;
        this.videoLink = videoLink;

    }


    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }


}

