package com.example.rohi.boostcamp_android.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rohi.boostcamp_android.MVP_Main;
import com.example.rohi.boostcamp_android.R;
import com.example.rohi.boostcamp_android.model.Movie;
import com.example.rohi.boostcamp_android.view.recycler.MoviesViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainPresenter implements MVP_Main.ProvidedPresenterOps, MVP_Main.RequiredPresenterOps{

    private WeakReference<MVP_Main.RequiredViewOps> mView;
    private MVP_Main.ProvidedModelOps mModel;
    public static StringBuilder res;
    public static Bitmap bm;

    public MainPresenter(MVP_Main.RequiredViewOps view){
        mView = new WeakReference<MVP_Main.RequiredViewOps>(view);
    }

    private MVP_Main.RequiredViewOps getView() throws NullPointerException{
        if(mView !=null){
            return  mView.get(); //WeakReference는 .get() 메서드로 객체를 얻을 수 있다.
        }else{
            throw new NullPointerException("View is unavailable");
        }
    }

    public void setModel(MVP_Main.ProvidedModelOps model){
        mModel = model;
    }

    private Toast makeToast(String msg){
        return Toast.makeText(getView().getAppContext(),msg,Toast.LENGTH_SHORT);
    }

    public Movie makeMovie(int id, String image, String title, String link, double grade, String year, String director, String actors){
        Movie movie = new Movie(id, image, title, link, grade, year, director, actors);
        return movie;
    }

    @Override
    public void loadData(){
        try{
            new AsyncTask<Void,Void,ArrayList<Movie>>(){

                @Override
                protected ArrayList<Movie> doInBackground(Void... voids) {
                    return mModel.loadData();
                }

                @Override
                protected void onPostExecute(ArrayList<Movie> result) {
                    try {
                        if(result == null){
                            getView().showToast(makeToast("Error Loading"));
                        }else{
                            mModel.insertAllMovie(result);
                            getView().notifyDataSetChanged();
                        }
                    }catch (NullPointerException e){

                    }
                }
            }.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getMovieCount() {
        return mModel.getMoviesCount();
    }

    @Override
    public MoviesViewHolder createViewHolder(ViewGroup parent, int viewType) {
        MoviesViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewTaskRow = layoutInflater.inflate(R.layout.recycler_view_row,parent,false);
        viewHolder = new MoviesViewHolder(viewTaskRow);

        return viewHolder;
    }

    @Override
    public void bindViewHolder(final MoviesViewHolder holder, final int position) {
        final Movie movie = mModel.getMovie(position);

        new AsyncTask<Void,Void,Integer>(){

            @Override
            protected Integer doInBackground(Void... voids) {
                int isImage = movie.getImage().length();
                if(isImage > 1){
                    try{
                        URL url = new URL(movie.getImage());
                        URLConnection conn = url.openConnection();
                        conn.connect();
                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                        bm = BitmapFactory.decodeStream(bis);
                        bis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return isImage;
            }

            @Override
            protected void onPostExecute(Integer isImage) {
                if(isImage > 1)
                    holder.viewImage.setImageBitmap(bm);
                holder.title.setText(Html.fromHtml(movie.getTitle()));
                holder.grade.setText(Double.toString(movie.getGrade()));
                holder.year.setText(movie.getYear());
                holder.director.setText(movie.getDirector());
                holder.actors.setText(movie.getActors());
            }
        }.execute();

       holder.container.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
               getView().addOnclickListener(movie.getLink());
           }
       });
    }

    @Override
    public void searchNewMovie(EditText editText) {
        final String clientId = "auwJ19_GlTs5QUxUWI4S";
        final String clientSecret = "boZepMnQkO";
        final String movieText = editText.getText().toString();

        if(!movieText.isEmpty()){
            mModel.removeMovieList();
            getView().notifyDataSetChanged();

            new AsyncTask<Void,Void,Integer>(){

                @Override
                protected Integer doInBackground(Void... voids) {
                    try{
                        String query = URLEncoder.encode(movieText, "utf-8");
                        String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + query + "&display=" + 100 + "&";
                        URL url = new URL(apiURL);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("X-Naver-Client-Id", clientId);
                        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                        int responseCode = con.getResponseCode();
                        BufferedReader br;

                        if (responseCode == 200) {
                            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        } else {
                            switch (responseCode){
                                case 404:
                                    getView().showToast(makeToast("Invalid search api (존재하지 않는 검색 api 입니다)"));
                                    break;
                                case 500:
                                    getView().showToast(makeToast("System Error (시스템 에러)"));
                                    break;
                                case 400:
                                    getView().showToast(makeToast("Invalid value(부적절한 값입니다)"));
                                    break;
                            }
                            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        }

                        String line;
                        res = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            res.append(line + "\n");
                        }
                        br.close();
                        con.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                }

                @Override
                protected void onPostExecute(Integer adapterPosition) {
                    try {
                        JSONArray jarray = new JSONObject(res.toString()).getJSONArray("items");   // JSONArray 생성
                        if(jarray.length() != 0){
                            mModel.removeMovieList();
                            for(int i=0; i < jarray.length(); i++){
                                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                                int id = i;
                                String image = jObject.getString("image");
                                String title = jObject.getString("title");
                                //String title = Html.fromHtml(jObject.getString("title")).toString();
                                String link = jObject.getString("link");
                                double grade = jObject.getDouble("userRating");
                                String year = jObject.getString("pubDate");
                                String director = jObject.getString("director");
                                String actors = jObject.getString("actor");

                                mModel.insertMovie(makeMovie(id, image, title, link, grade, year, director, actors));
                                getView().notifyItemInserted(i);
                            }
                            getView().notifyItemRangeChanged(0,mModel.getMoviesCount());
                        }
                        else{
                            getView().showToast(makeToast("검색결과가 없습니다"));
                        }
                        getView().clearEditText();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
        else{
            getView().showToast(makeToast("검색어를 입력해주세요"));
        }
    }

    @Override
    public void setView(MVP_Main.RequiredViewOps view) {
        mView = new WeakReference<MVP_Main.RequiredViewOps>(view);
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        mView = null;
        mModel.onDestroy(isChangingConfiguration);
        if(!isChangingConfiguration){
            mModel = null;
        }

    }

    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        }catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        } catch (NullPointerException e){
            return null;
        }
    }
}