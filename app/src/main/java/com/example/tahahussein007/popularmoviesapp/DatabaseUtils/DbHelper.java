package com.example.tahahussein007.popularmoviesapp.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.tahahussein007.popularmoviesapp.Models.MovieContract;
import com.example.tahahussein007.popularmoviesapp.Models.MovieMODEL;

import java.io.Console;
import java.util.ArrayList;

/**
 * Created by tahahussein007 on 26/11/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final String DB_NAME = "fav_movies.db";
    private static final int DB_VERSION = 1;


    MovieContract movieDbObj = new MovieContract();

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(movieDbObj.getStreamTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + movieDbObj.getTableName());
        onCreate(db);
    }

    public boolean insertRow(MovieMODEL movieMODEL) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(movieDbObj.getColId(),movieMODEL.getId());
        contentValues.put(movieDbObj.getColMovName(),movieMODEL.getTitle());
        contentValues.put(movieDbObj.getColDate(),movieMODEL.getRelease_date());
        contentValues.put(movieDbObj.getColVote(),movieMODEL.getRating());
        contentValues.put(movieDbObj.getColOverview(), movieMODEL.getOverview());
        contentValues.put(movieDbObj.getColPoster(),movieMODEL.getPoster_path());
        contentValues.put(movieDbObj.getColBackdrop(),movieMODEL.getBackdrop_path());
        long num = db.insert(movieDbObj.getTableName(),null,contentValues);
        if (num == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteData(MovieMODEL movie){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(movieDbObj.getTableName(),movieDbObj.getColId()+ " = ? ",new String[] { String.valueOf(movie.getId()) });
        closeDB();
    }

    public boolean ifExist(String movId) {
        db = getReadableDatabase();

        Cursor cursor = db.query(movieDbObj.getTableName(), new String[]{movieDbObj.getColMovId()}, movieDbObj.getColMovId() + " =?",
                new String[]{movId}, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getData() {
        db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + movieDbObj.getTableName(), null);
    }


    public Boolean checkFavourite(String id){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = " select * from "+movieDbObj.getTableName()+" where " +movieDbObj.getColId()+ "=" +id;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if (cursor.moveToFirst()) {

            return true;
        }
        else{
            return false;
        }

    }

    public ArrayList<MovieMODEL> getAllData(){

        ArrayList<MovieMODEL> movies = new ArrayList<MovieMODEL>();
        String selectQuery = "SELECT  * FROM " + movieDbObj.getTableName();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MovieMODEL movieModel = new MovieMODEL();
                movieModel.setId(cursor.getString(cursor.getColumnIndex(movieDbObj.getColId())));
                movieModel.setTitle(cursor.getString(cursor.getColumnIndex(movieDbObj.getColMovName() )));
                movieModel.setRelease_date(cursor.getString(cursor.getColumnIndex(movieDbObj.getColDate() )));
                movieModel.setRating(cursor.getFloat(cursor.getColumnIndex(movieDbObj.getColVote() )));
                movieModel.setOverview(cursor.getString(cursor.getColumnIndex(movieDbObj.getColOverview() )));
                movieModel.setPoster_path(cursor.getString(cursor.getColumnIndex(movieDbObj.getColPoster() )));
                movieModel.setBackdrop_path(cursor.getString(cursor.getColumnIndex(movieDbObj.getColBackdrop() )));

                movies.add(movieModel);
            } while (cursor.moveToNext());
        }
        closeDB();
        return movies;
    }

    public Boolean checkData(){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = " select * from "+movieDbObj.getTableName() ;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            closeDB();
            return true;
        }
        else{
            closeDB();
            return false;
        }


    }

    // closing database
    public void closeDB() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
            sqLiteDatabase.close();
    }
}
