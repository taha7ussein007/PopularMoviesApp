package com.example.tahahussein007.popularmoviesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahahussein007.popularmoviesapp.Models.MovieMODEL;
import com.example.tahahussein007.popularmoviesapp.Models.ReviewsMODEL;
import com.example.tahahussein007.popularmoviesapp.Models.TrailersMODEL;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

/**
 * Created by tahahussein007 on 10/18/16.
 */

public class MovieDetails extends AppCompatActivity {

    MovieMODEL model ;
    ImageView coverImg , posterView;
    TextView title , reviewUSR , reviewTXT , txtDate;
    RatingBar ratingBar;
    final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final String API_KEY = "?api_key=858a0b80b5b758a974600daa98070f2c";
    SharedPreferences sharedPreferences;
    reviewAdapter revAdapter;
    trailersAdapter trailAdapter;
    private RecyclerView reviewView;
    private RecyclerView trailersView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        model= (MovieMODEL) getIntent().getExtras().get("movie");  // if i work with activity without fragment

        coverImg = (ImageView) findViewById(R.id.coverIMG);
        posterView = (ImageView) findViewById(R.id.titleimg);

        // initialize  view
        title = (TextView) findViewById(R.id.titleText);
        reviewTXT = (TextView)findViewById(R.id.txtOverview);
        txtDate = (TextView)findViewById(R.id.txtDate);


        title.setText(model.getTitle());

        float ratingValue = (float) ((model.getRating()*5)/10);
        ratingBar.setRating(ratingValue);
        ratingBar.setStepSize(ratingValue);

        trailersView = (RecyclerView) findViewById(R.id.vidRecycler);
        reviewView = (RecyclerView) findViewById(R.id.revRecycler);

//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                rating = rating%10;
//                model.setRating(rating);
//                ratingBar.setRating(rating);
//                ratingBar.setStepSize(rating);
//            }
//        });
        reviewTXT.setText(model.getOverview());
        txtDate.setText(model.getRelease_date());


        Picasso.with(getApplicationContext()).load(model.getBackdrop_path()).placeholder(R.drawable.placeholder).into(coverImg);
        Picasso.with(getApplicationContext()).load( model.getPoster_path()).placeholder(R.drawable.placeholder).into(posterView);

        sharedPreferences = getApplicationContext().getSharedPreferences("state", Context.MODE_APPEND);
        String type = sharedPreferences.getString("key","popular");

        new TrailerJsonTask().execute(type);
        new ReviewJsonTask().execute();

    }

    /////////////////////////////////   Trailers    //////////////////////////////////////////

    public class TrailerJsonTask extends AsyncTask<String ,Void, List<TrailersMODEL> > {



        @Override
        protected List<TrailersMODEL> doInBackground(String... strings) {

            List<TrailersMODEL> trailerModelList = new ArrayList<>();

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            String type = strings[0];


            try {

                URL url = new URL(BASE_URL + model.getId() +  "/videos" +API_KEY);
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

                JSONObject object;

                try {
                    object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("results");

                    Gson gson = new Gson();

                    for(int i=0; i<array.length(); i++){

                        JSONObject finalObject = array.getJSONObject(i);

                        String name = finalObject.getString("name");
                        String key = finalObject.getString("key");


                        TrailersMODEL trailer = new TrailersMODEL(name,key);

                        trailerModelList.add(trailer);


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


            return trailerModelList;

        }

        @Override
        protected void onPostExecute(final List<TrailersMODEL> result ) {
            super.onPostExecute(result);



            if(null != result && !result.isEmpty()) {
                trailAdapter = new trailersAdapter((ArrayList<TrailersMODEL>) result,getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1);
                trailersView.setLayoutManager(mLayoutManager);
                trailersView.setItemAnimator(new DefaultItemAnimator());
                trailersView.setAdapter(trailAdapter);



            } else {
                Toast.makeText(getApplicationContext(), "Not able to fetch Trailers from server, please check url.", Toast.LENGTH_SHORT).show();
            }


        }
    }

   /////////////////////////////////   Reviews    //////////////////////////////////////////
    public class ReviewJsonTask extends AsyncTask<String ,Void, List<ReviewsMODEL> > {



        @Override
        protected List<ReviewsMODEL> doInBackground(String... strings) {

            List<ReviewsMODEL> reviewsModelList = new ArrayList<>();

            HttpURLConnection connection = null;
            BufferedReader reader = null;



            try {

                URL url = new URL(BASE_URL + model.getId() +  "/reviews" +API_KEY);
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

                JSONObject object;

                try {
                    object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("results");

                    Gson gson = new Gson();

                    for(int i=0; i<array.length(); i++){

                        JSONObject finalObject = array.getJSONObject(i);

                        String author = finalObject.getString("author");
                        String content = finalObject.getString("content");


                        ReviewsMODEL trailer = new ReviewsMODEL(author,content);

                        reviewsModelList.add(trailer);


//                        for (ReviewsMODEL reviewsMODEL1 :  reviewsModelList){
//                            System.out.println("author"+author);
//
//                        }
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


            return reviewsModelList;

        }

        @Override
        protected void onPostExecute(final List<ReviewsMODEL> result ) {
            super.onPostExecute(result);



            if(null != result && !result.isEmpty()) {
                revAdapter = new reviewAdapter((ArrayList<ReviewsMODEL>) result,getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1);
                reviewView.setLayoutManager(mLayoutManager);
                reviewView.setItemAnimator(new DefaultItemAnimator());
                reviewView.setAdapter(revAdapter);



            } else {
                Toast.makeText(getApplicationContext(), "Not able to fetch Review from server, please check url.", Toast.LENGTH_SHORT).show();
            }


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



}
