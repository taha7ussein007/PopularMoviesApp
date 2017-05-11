package com.example.tahahussein007.popularmoviesapp.Models;

/**
 * Created by tahahussein007 on 11/20/16.
 */

public class ReviewsMODEL {

    String author;
    String content;

    public ReviewsMODEL(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}