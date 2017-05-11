package com.example.tahahussein007.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tahahussein007.popularmoviesapp.Models.TrailersMODEL;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tahahussein007 on 11/23/16.
 */
public class trailersAdapter extends RecyclerView.Adapter<trailersAdapter.videosHolder> {


    ArrayList<TrailersMODEL> videoList;
    Context context;
    String JpgDefault = "/mqdefault.jpg";


    public trailersAdapter(ArrayList<TrailersMODEL> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }




    @Override
    public videosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_row,parent,false);
        videosHolder moviesHolder = new videosHolder(row);
        return moviesHolder;
    }

    @Override
    public void onBindViewHolder(videosHolder holder, int position) {


        final TrailersMODEL movie =  videoList.get(position);

        holder.videoName.setText(movie.getVideoName());
     //   holder.link.setText("https://www.youtube.com/watch?v="+movie.getVideoLink());
        Picasso.with(context).load("http://img.youtube.com/vi/"+movie.getVideoLink()+JpgDefault).placeholder(R.drawable.placeholder).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+movie.getVideoLink()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }


    public class videosHolder extends RecyclerView.ViewHolder{

        private TextView videoName;
  //      private TextView link;

        private ImageView thumbnail;

        public videosHolder(View itemView) {
            super(itemView);

            videoName = (TextView) itemView.findViewById(R.id.videoName);
          //  link = (TextView) itemView.findViewById(R.id.link);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

        }
    }
}
