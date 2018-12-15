package com.example.rohi.boostcamp_android;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rohi.boostcamp_android.model.Movie;
import com.example.rohi.boostcamp_android.view.recycler.MoviesViewHolder;

import java.util.ArrayList;

public interface MVP_Main {
    interface RequiredViewOps{
        Context getAppContext();
        Context getActivityContext();
        void showToast(Toast toast);
        void notifyItemInserted(int layoutPosition);
        void notifyItemRangeChanged(int positionStart, int itemCount);
        void notifyItemRemoved(int position);
        void notifyDataSetChanged();
        void clearEditText();
        void addOnclickListener(String link);
    }

    interface ProvidedPresenterOps{
        int getMovieCount();
        void loadData();
            MoviesViewHolder createViewHolder(ViewGroup parent, int viewType);
        void bindViewHolder(MoviesViewHolder holder, int position);
        void searchNewMovie(EditText editText);
        void setView(RequiredViewOps view);
        void onDestroy(boolean isChangingConfiguration);
    }

    interface RequiredPresenterOps{
        Context getAppContext();
        Context getActivityContext();
    }

    interface ProvidedModelOps{
        void onDestroy(boolean isChangingConfiguration);
        void insertMovie(Movie movie);
        void insertAllMovie(ArrayList<Movie> movies);
        void removeMovieList();
        int getMoviesCount();
        ArrayList<Movie> loadData();
        Movie getMovie(int position);
    }
}
