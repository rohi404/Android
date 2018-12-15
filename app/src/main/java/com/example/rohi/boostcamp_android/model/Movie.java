package com.example.rohi.boostcamp_android.model;

public class Movie {

    private int id = -1;
    private String mImage;
    private String mTitle;
    private String mLink;
    private double mGrade;
    private String mYear;
    private String mDirector;
    private String mActors;

    public Movie(int id, String mImage, String mTitle, String mLink, double mGrade, String mYear, String mDirector, String mActors){
        this.id = id;
        this.mImage = mImage;
        this.mTitle = mTitle;
        this.mLink = mLink;
        this.mGrade = mGrade;
        this.mYear = mYear;
        this.mDirector = mDirector;
        this.mActors = mActors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }

    public double getGrade() {
        return mGrade;
    }

    public void setGrade(double mGrade) {
        this.mGrade = mGrade;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String mYear) {
        this.mYear = mYear;
    }

    public String getDirector() {
        return mDirector;
    }

    public void setDirector(String mDirector) {
        this.mDirector = mDirector;
    }

    public String getActors() {
        return mActors;
    }

    public void setActors(String mActors) {
        this.mActors = mActors;
    }
}