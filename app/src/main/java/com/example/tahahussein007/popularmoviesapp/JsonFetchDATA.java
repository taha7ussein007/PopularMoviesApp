package com.example.tahahussein007.popularmoviesapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.tahahussein007.popularmoviesapp.Models.MovieMODEL;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tahahussein007 on 10/19/16.
 */



abstract public class JsonFetchDATA extends AsyncTask<String ,Void, List<MovieMODEL> > {


//    Main_Fragment mainFragment ;
//
//    private Dialog dialog = mainFragment.dialog ;


    // "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=858a0b80b5b758a974600daa98070f2c&page=1"
    private final int PAGE_NUM =  1;  /*new Random().nextInt(5) +*/ //50 is the maximum and the 1 is our minimum ;
    private final String stnrd_wrd_sort = "&sort_by=";
    private final String stnrd_wrd_page = "&page=";
    private String MOVIES_URL = "";
    final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final String API_KEY = "?api_key=858a0b80b5b758a974600daa98070f2c";

    private RecyclerView recyclerView_movies ;
    private RecyclerViewAdapter adapter;
    SharedPreferences sharedPreferences;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//       dialog.show();
    }

    @Override
    protected List<MovieMODEL> doInBackground(String... strings) {

        List<MovieMODEL> movieModelList = new ArrayList<>();

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        String type = strings[0];

        try {
            // http://api.themoviedb.org/3/movie/popular?api_key=858a0b80b5b758a974600daa98070f2c
            URL url = new URL(BASE_URL + type + API_KEY +stnrd_wrd_page+PAGE_NUM );
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader stream = new InputStreamReader(connection.getInputStream());
            reader = new BufferedReader(stream);

            final StringBuilder txtBuilder = new StringBuilder();
            String line;
            String result;

            while ((line=reader.readLine()) != null){
                txtBuilder.append(line);
            }

            result = txtBuilder.toString();

            JSONObject  object;

            try {
                object = new JSONObject(result);
                JSONArray array = object.getJSONArray("results");

                Gson gson = new Gson();

                for(int i=0; i<array.length(); i++){

                    JSONObject finalObject = array.getJSONObject(i);
                    //    MovieMODEL movieModel = gson.fromJson(finalObject.toString(), MovieMODEL.class);
                    // worked right but i need the another way becs image came from another link and it is just string in api

                    String title = finalObject.getString("original_title");
                    String image = finalObject.getString("poster_path");
                    String overview = finalObject.getString("overview");
                    String date = finalObject.getString("release_date");
                    float vote = (float)finalObject.getDouble("vote_average");
                    String backImage = finalObject.getString("backdrop_path");
                    String id = finalObject.getString("id");

                    MovieMODEL movieModel = new MovieMODEL(image,backImage,overview,title,id,date,vote);

                    movieModelList.add(movieModel);
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                connection.disconnect();

            try {
                if (reader != null)
                    reader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }


        return movieModelList;
    }

    @Override
    abstract protected void onPostExecute(final List<MovieMODEL> result );
}

