package com.example.tahahussein007.popularmoviesapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tahahussein007.popularmoviesapp.DatabaseUtils.DbHelper;
import com.example.tahahussein007.popularmoviesapp.Models.MovieMODEL;
import com.example.tahahussein007.popularmoviesapp.Utils.AnimationUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tahahussein007 on 10/12/16.
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ListRowHolder> {

    Context context;
    boolean twoPane;
    private List<MovieMODEL> feedItemList;
    private int previousPosition = 0;


    public RecyclerViewAdapter(Context context , List<MovieMODEL> result , boolean twoPane){
        this.context = context;
        feedItemList = result;
        this.twoPane = twoPane;
    }
    @Override
    public RecyclerViewAdapter.ListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_in_list, null);
        ListRowHolder mh = new ListRowHolder(v);

        return mh;
    }



    @Override
    public void onBindViewHolder(ListRowHolder feedListRowHolder, final int position) {
        final MovieMODEL feedItem = feedItemList.get(position);

        Picasso.with(context).load(feedItem.getPoster_path())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.thumbnail);

        feedListRowHolder.title.setText(Html.fromHtml(feedItem.getTitle()));
        feedListRowHolder.Date.setText(Html.fromHtml(feedItem.getRelease_date()));



        feedListRowHolder.thumbnail.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                Details_Fragment detailsFragment = Details_Fragment.getDetailInstance(feedItem);
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();




                // twoPane
                if (MainActivity.mainPane == true) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movie", feedItem);
                    detailsFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.detailPane, detailsFragment).commit();


                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movie", feedItem);
                    detailsFragment.setArguments(bundle);
//                for (MovieMODEL movieMODEL : feedItemList){
//                    System.out.println(feedItemList.get(position).getTitle());
//                }

                System.out.println(feedItem.getTitle());


                fragmentManager.beginTransaction()
                            .replace(R.id.main_activity, detailsFragment).addToBackStack("FRAGMENT#1").commit();
                }

            }
        });


        if(position > previousPosition){ // We are scrolling DOWN

            AnimationUtil.animate(feedListRowHolder, true);

        }else{ // We are scrolling UP

            AnimationUtil.animate(feedListRowHolder, false);
        }


    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }


    public class ListRowHolder extends RecyclerView.ViewHolder
    {
        private ImageView thumbnail;
        protected TextView title;
        protected TextView Date;

        public ListRowHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.title = (TextView) view.findViewById(R.id.title);
            this.title.setSelected(true);
            this.Date = (TextView) view.findViewById(R.id.posterYear);

        }
    }
}
