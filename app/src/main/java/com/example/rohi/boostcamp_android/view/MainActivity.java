package com.example.rohi.boostcamp_android.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rohi.boostcamp_android.MVP_Main;
import com.example.rohi.boostcamp_android.R;
import com.example.rohi.boostcamp_android.common.StateMaintainer;
import com.example.rohi.boostcamp_android.model.MainModel;
import com.example.rohi.boostcamp_android.presenter.MainPresenter;
import com.example.rohi.boostcamp_android.view.recycler.EndlessRecyclerViewScrollListener;
import com.example.rohi.boostcamp_android.view.recycler.MoviesViewHolder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MVP_Main.RequiredViewOps{
    private EditText mTextNewMovie;
    private ListMovies mListAdapter;

    private final StateMaintainer mStateMaintainer =
            new StateMaintainer(getFragmentManager(), MainActivity.class.getName());
    private MVP_Main.ProvidedPresenterOps mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupMVP();
    }

    private void setupViews(){
        mTextNewMovie = (EditText)findViewById(R.id.edit_text);
        mListAdapter = new ListMovies();

        RecyclerView mList = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList.setLayoutManager(linearLayoutManager);
        mList.setAdapter(mListAdapter);
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                mPresenter.loadData();
            }
        });
    }

    private void setupMVP(){
        if(mStateMaintainer.firstTimeIn()) {
            MainPresenter presenter = new MainPresenter(this);
            MainModel model = new MainModel(presenter);
            presenter.setModel(model);
            mStateMaintainer.put(presenter);
            mStateMaintainer.put(model);

            mPresenter = presenter;
        }
        else{
            mPresenter = mStateMaintainer.get(MainPresenter.class.getName());
            mPresenter.setView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy(isChangingConfigurations());
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }


    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void showToast(Toast toast) {
        toast.show();
    }

    @Override
    public void notifyItemInserted(int layoutPosition) {
        mListAdapter.notifyItemInserted(layoutPosition);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        mListAdapter.notifyItemRangeChanged(positionStart,itemCount);
    }

    @Override
    public void notifyItemRemoved(int position) {
        mListAdapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyDataSetChanged() {
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearEditText() {
        mTextNewMovie.setText("");
    }

    @Override
    public void addOnclickListener(String link){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        mPresenter.searchNewMovie(mTextNewMovie);
    }

    private class ListMovies extends RecyclerView.Adapter<MoviesViewHolder>{

        @Override
        public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return mPresenter.createViewHolder(parent,viewType);
        }

        @Override
        public void onBindViewHolder(MoviesViewHolder holder, int position) {
            mPresenter.bindViewHolder(holder,position);
        }

        @Override
        public int getItemCount() {
            return mPresenter.getMovieCount();
        }
    }
}
