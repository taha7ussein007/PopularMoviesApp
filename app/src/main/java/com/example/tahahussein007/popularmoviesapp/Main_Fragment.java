package com.example.tahahussein007.popularmoviesapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tahahussein007.popularmoviesapp.DatabaseUtils.DbHelper;
import com.example.tahahussein007.popularmoviesapp.JsonFetchDATA;

import com.example.tahahussein007.popularmoviesapp.Models.MovieMODEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main_Fragment extends Fragment {


    private RecyclerView recyclerView_movies;
    private RecyclerViewAdapter adapter;
    SharedPreferences sharedPreferences;
    DbHelper dbObj;
    boolean twoPane = false;


    public MovieMODEL MovieListener;

    private void jsonParsing(String x)
    {
        JsonFetchDATA movieParser =  new JsonFetchDATA() {
            @Override
            protected void onPostExecute(List<MovieMODEL> result) {
                adapter = new RecyclerViewAdapter(getActivity(),result,twoPane);
                recyclerView_movies.setAdapter(adapter);

            }
        };
        movieParser.execute(x);
    }

    void setMovieListener(MovieMODEL MovieListener) {
        this.MovieListener = MovieListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_, container, false);
        dbObj = new DbHelper(getActivity());


        sharedPreferences = getActivity().getSharedPreferences("state", Context.MODE_APPEND);
        String type = sharedPreferences.getString("key","popular");

        recyclerView_movies = (RecyclerView) view.findViewById(R.id.recycleView);
//
//        jsonTask = (JsonFetchDATA) new JsonFetchDATA() {
//            @Override
//            protected void onPostExecute(List<MovieMODEL> result) {
//
//                if (null != result && !result.isEmpty()) {
//                adapter = new RecyclerViewAdapter(getActivity(), result);
//                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
//                recyclerView_movies.setLayoutManager(mLayoutManager);
//                recyclerView_movies.setItemAnimator(new DefaultItemAnimator());
//                recyclerView_movies.setAdapter(adapter);
//
//        } else {
//            Toast.makeText(getActivity(), "Main_Fragment Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
//        }
//
//            }
//        }.execute(type);


        if (type.equals("Fav_DB")){
            RecyclerViewAdapter dbAdapter = new RecyclerViewAdapter(getActivity(),dbObj.getAllData(),twoPane);
            recyclerView_movies.setAdapter(dbAdapter);
        }
        else{
            jsonParsing(type);
        }
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView_movies.setLayoutManager(mLayoutManager);

//        if (null != result && !result.isEmpty()) {
//            adapter = new RecyclerViewAdapter(getActivity(), result);
//            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
//            recyclerView_movies.setLayoutManager(mLayoutManager);
//            recyclerView_movies.setItemAnimator(new DefaultItemAnimator());
//            recyclerView_movies.setAdapter(adapter);
//
//        } else {
//            Toast.makeText(getActivity(), "Main_Fragment Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
//        }


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sharedPreferences = getActivity().getSharedPreferences("state", Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.linearViewHorizontal:
                LinearLayoutManager mLinearLayoutManagerHorizontal = new LinearLayoutManager(getActivity()); // (Context context)
                mLinearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView_movies.setLayoutManager(mLinearLayoutManagerHorizontal);
                break;

            case R.id.linearViewVertical:
                LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
                mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView_movies.setLayoutManager(mLinearLayoutManagerVertical);
                break;
            case R.id.gridView:
                GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3); // (Context context, int spanCount)
                recyclerView_movies.setLayoutManager(mGridLayoutManager);
                break;
            case R.id.staggeredViewHorizontal:
                StaggeredGridLayoutManager mStaggeredHorizontalLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL); // (int spanCount, int orientation)
                recyclerView_movies.setLayoutManager(mStaggeredHorizontalLayoutManager);
                break;
            case R.id.staggeredViewVertical:
                StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
                recyclerView_movies.setLayoutManager(mStaggeredVerticalLayoutManager);
                break;
        }

        if (id == R.id.popular) {
            editor.putString("key","popular");
            editor.commit();
            jsonParsing("popular");
            return true;
        }

        if (id == R.id.mostRated) {
            editor.putString("key","top_rated");
            editor.commit();
            jsonParsing("top_rated");

            return true;
        }

        if (id == R.id.MyFav) {
            if (dbObj.checkData() == false){
                Toast.makeText(getActivity(),"No favorite movies !",Toast.LENGTH_SHORT).show();
            }
            else {
                editor.putString("key","Fav_DB");
                editor.commit();
                RecyclerViewAdapter dbAdapter = new RecyclerViewAdapter(getActivity(),dbObj.getAllData(),twoPane);
                recyclerView_movies.setAdapter(dbAdapter);
                return true;

            }

        }

        if (id == R.id.action_refresh) {
            sharedPreferences = getActivity().getSharedPreferences("state", Context.MODE_APPEND);
            String type = "popular";
           // PAGE_NUM = new Random().nextInt(800) + 1;
           jsonParsing(type);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}