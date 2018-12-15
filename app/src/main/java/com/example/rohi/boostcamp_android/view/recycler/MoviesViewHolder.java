package com.example.rohi.boostcamp_android.view.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rohi.boostcamp_android.R;

public class MoviesViewHolder extends RecyclerView.ViewHolder{
    public LinearLayout container;
    public TextView title, grade, year, director, actors;
    public ImageView viewImage;

    public MoviesViewHolder(View itemView) {
        super(itemView);
        setupViews(itemView);
    }

    private void setupViews(View view){
        container = (LinearLayout)view.findViewById(R.id.container);
        title = (TextView)view.findViewById(R.id.title);
        grade = (TextView)view.findViewById(R.id.grade);
        year = (TextView)view.findViewById(R.id.year);
        director = (TextView)view.findViewById(R.id.director);
        actors = (TextView)view.findViewById(R.id.actors);
        viewImage = (ImageView)view.findViewById(R.id.image);
    }
}
