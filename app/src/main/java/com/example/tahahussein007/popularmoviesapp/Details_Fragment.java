package com.example.tahahussein007.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahahussein007.popularmoviesapp.DatabaseUtils.DbHelper;
import com.example.tahahussein007.popularmoviesapp.Models.MovieMODEL;
import com.example.tahahussein007.popularmoviesapp.Models.ReviewsMODEL;
import com.example.tahahussein007.popularmoviesapp.Models.TrailersMODEL;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class Details_Fragment extends Fragment {
    public static final String TAG = Details_Fragment.class.getSimpleName();

    MovieMODEL model = new MovieMODEL();
    ImageView coverImg , posterView;
    TextView title , storyView , yearView , rating;
    private RecyclerView trailersView;
    private RecyclerView revRecycler;
    private ImageButton favorite;
    private ImageButton un_favourite;
    TrailerFetchDATA trailerFetchDATA;
    ReviewFetchDATA reviewFetchDATA;
    DbHelper dbHelperObj;
    Boolean globalCheck;


    public static Details_Fragment getDetailInstance(MovieMODEL movie)
    {
        Details_Fragment detailsFragment = new Details_Fragment();
        Bundle args = new Bundle();
        args.putSerializable("movie",movie);
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    public Details_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_, container, false);


        coverImg = (ImageView) view.findViewById(R.id.coverIMG);
        posterView = (ImageView) view.findViewById(R.id.titleimg);
        title = (TextView) view.findViewById(R.id.titleText);
        rating = (TextView) view.findViewById(R.id.rating);
        storyView = (TextView) view.findViewById(R.id.txtOverview);
        yearView = (TextView) view.findViewById(R.id.txtDate);
        trailersView = (RecyclerView) view.findViewById(R.id.vidRecycler);
        revRecycler = (RecyclerView) view.findViewById(R.id.revRecycler);
        favorite = (ImageButton) view.findViewById(R.id.favBtn);
        un_favourite = (ImageButton) view.findViewById(R.id.delete);


        model = (MovieMODEL) getArguments().getSerializable("movie");

        // setting data to view
        title.setText(model.getTitle());
        title.setSelected(true);
         rating.setText(model.getRating() + " / 10");
          yearView.setText(model.getRelease_date());
         storyView.setText(model.getOverview());
        Picasso.with(getActivity()).load(model.getBackdrop_path()).placeholder(R.drawable.placeholder).into(coverImg);
        Picasso.with(getActivity()).load( model.getPoster_path()).placeholder(R.drawable.placeholder).into(posterView);

        trailerFetchDATA = (TrailerFetchDATA) new TrailerFetchDATA() {
            @Override
            protected void onPostExecute(List<TrailersMODEL> result) {
        if(null != result && !result.isEmpty()) {
            trailAdapter = new trailersAdapter((ArrayList<TrailersMODEL>) result,getActivity());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),1);
            trailersView.setLayoutManager(mLayoutManager);
            trailersView.setItemAnimator(new DefaultItemAnimator());
            trailersView.setAdapter(trailAdapter);

        } else {
            Toast.makeText(getActivity(), "Not able to fetch Trailers from server, please check Website.", Toast.LENGTH_SHORT).show();
        }
            }
        }.execute(model.getId());

        reviewFetchDATA = (ReviewFetchDATA) new ReviewFetchDATA() {
            @Override
            protected void onPostExecute(List<ReviewsMODEL> result) {
                if(null != result && !result.isEmpty()) {
                    revAdapter = new reviewAdapter((ArrayList<ReviewsMODEL>) result,getActivity());
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),1);
                    revRecycler.setLayoutManager(mLayoutManager);
                    revRecycler.setItemAnimator(new DefaultItemAnimator());
                    revRecycler.setAdapter(revAdapter);

                } else {
                    Toast.makeText(getActivity(), "Not able to fetch Reviews from server, please check Website.", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(model.getId());


        dbHelperObj = new DbHelper(getActivity());

        if (dbHelperObj.checkFavourite(model.getId()) == true){
            favorite.setVisibility(View.GONE);
            un_favourite.setVisibility(View.VISIBLE);
        }
        else {
            favorite.setVisibility(View.VISIBLE);
            un_favourite.setVisibility(View.GONE);
        }


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelperObj.ifExist(model.getId()))
                {
                    Toast.makeText(getActivity(),model.getTitle()+" is Already added to favorite !",Toast.LENGTH_LONG).show();
                }
               else {
                    globalCheck = dbHelperObj.insertRow(model);
                    if(globalCheck){
                        Toast.makeText(getActivity(),model.getTitle()+" is added to favorite !",Toast.LENGTH_LONG).show();
                        favorite.setVisibility(View.GONE);
                        un_favourite.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(getActivity(),model.getTitle()+" can not be added to favorite !",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        un_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelperObj.deleteData(model);
                Toast.makeText(getActivity(),model.getTitle()+" is removed from favorite !",Toast.LENGTH_LONG).show();
                un_favourite.setVisibility(View.GONE);
                favorite.setVisibility(View.VISIBLE);

            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home){
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
