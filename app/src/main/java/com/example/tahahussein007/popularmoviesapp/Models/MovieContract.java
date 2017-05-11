package com.example.tahahussein007.popularmoviesapp.Models;

import android.content.Context;

/**
 * Created by tahahussein007 on 11/30/16.
 */

public class MovieContract {


        private static final String TABLE_NAME = "movies";
        private static final String COL_ID = "id";
        private static final String COL_POSTER = "poster";
        private static final String COL_BACKDROP = "backdrop";
        private static final String COL_MOV_ID = "movie_id";
        private static final String COL_MOV_NAME = "mov_name";
        private static final String COL_VOTE = "vote_avr";
        public static final String COL_DATE = "year";
        private static final String COL_OVERVIEW = "overview";

        public static String getStreamTable() {
            return STREAM_TABLE;
        }

        private static final String STREAM_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MOV_ID + " TEXT, " + COL_POSTER + " TEXT, "
                + COL_BACKDROP + " TEXT, " + COL_MOV_NAME + " TEXT, "
                + COL_DATE + " TEXT, " + COL_VOTE + " TEXT, "
                + COL_OVERVIEW + " TEXT" + ")";



        public  String getColOverview() {
            return COL_OVERVIEW;
        }

        public  String getColDate() {
            return COL_DATE;
        }

        public  String getColVote() {
            return COL_VOTE;
        }

        public  String getColMovName() {
            return COL_MOV_NAME;
        }

        public  String getColMovId() {
            return COL_MOV_ID;
        }

        public  String getTableName() {
            return TABLE_NAME;
        }

        public  String getColId() {
            return COL_ID;
        }

        public  String getColBackdrop() {
            return COL_BACKDROP;
        }

        public  String getColPoster() {
            return COL_POSTER;
        }




}
