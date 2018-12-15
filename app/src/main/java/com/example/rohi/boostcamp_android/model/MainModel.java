package com.example.rohi.boostcamp_android.model;

import com.example.rohi.boostcamp_android.MVP_Main;

import java.util.ArrayList;

public class MainModel implements MVP_Main.ProvidedModelOps {

    private MVP_Main.RequiredPresenterOps mPresenter;
    private ArrayList<Movie> mMovies;

    public MainModel(MVP_Main.RequiredPresenterOps presenter){
        this.mPresenter = presenter;
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if(!isChangingConfiguration){
            mPresenter = null;
            //mDao = null;
            mMovies = null;
        }
    }

    @Override
    public void insertMovie(Movie movie) {
        if(mMovies == null)
            mMovies = new ArrayList<Movie>();

        mMovies.add(movie);
    }

    @Override
    public void insertAllMovie(ArrayList<Movie> movies) {
        mMovies.addAll(movies);
    }

    @Override
    public void removeMovieList() {
        if(mMovies != null)
            mMovies.clear();
    }

    @Override
    public int getMoviesCount() {
        if(mMovies!=null){
            return mMovies.size();
        }
        return 0;
    }

    @Override
    public ArrayList<Movie> loadData() {
        return mMovies;
    }


    @Override
    public Movie getMovie(int position) {
        return mMovies.get(position);
    }
}
