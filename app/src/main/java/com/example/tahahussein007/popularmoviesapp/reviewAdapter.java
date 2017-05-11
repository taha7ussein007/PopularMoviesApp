package com.example.tahahussein007.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tahahussein007.popularmoviesapp.Models.ReviewsMODEL;
import com.example.tahahussein007.popularmoviesapp.Models.TrailersMODEL;

import java.util.ArrayList;

/**
 * Created by tahahussein007 on 11/23/16.
 */
public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.reviewHolder> {

    ArrayList<ReviewsMODEL> reviewList;
    Context context;

    public reviewAdapter(ArrayList<ReviewsMODEL> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }


    @Override
    public reviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row,parent,false);
        reviewHolder moviesHolder = new reviewHolder(row);
        return moviesHolder;
    }



    @Override
    public void onBindViewHolder(reviewHolder holder, int position) {

        final ReviewsMODEL movie =  reviewList.get(position);
        holder.author.setText(movie.getAuthor());
        holder.content.setText(movie.getContent());

    }




    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class reviewHolder extends RecyclerView.ViewHolder{

        private TextView author;
        private TextView content;

        public reviewHolder(View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
