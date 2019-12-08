package com.revolut1on.moviewiki;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.revolut1on.moviewiki.adapter.MovieAdapter;
import com.revolut1on.moviewiki.api.BaseApiService;
import com.revolut1on.moviewiki.api.UtilsApi;
import com.revolut1on.moviewiki.model.movies.MoviesItem;
import com.revolut1on.moviewiki.model.movies.MoviesResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.rvMovie)
    RecyclerView rvMovie;
    @BindView(R.id.main_content)
    SwipeRefreshLayout swipeContainer;

    BaseApiService mApiService;
    MovieAdapter mMovieAdapter;
    ProgressDialog mProgressDialog;

    private List<MoviesItem> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mApiService = UtilsApi.getAPIService();

        initViews();
        loadMovie();
    }

    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void initViews() {
        mMovieAdapter = new MovieAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvMovie.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else {
            rvMovie.setLayoutManager(new GridLayoutManager(this, 2));
        }

        rvMovie.setItemAnimator(new DefaultItemAnimator());
        rvMovie.setHasFixedSize(true);
        rvMovie.setAdapter(mMovieAdapter);

        swipeContainer.setColorSchemeResources(android.R.color.holo_red_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMovie();
                Toast.makeText(MainActivity.this, R.string.refresh, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMovie() {
        pbLoading.setVisibility(View.VISIBLE);

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.noApi, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                return;
            }

            Call<MoviesResponse> call = mApiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<MoviesItem> movies = response.body().getResults();
                    Collections.sort(movies, MoviesItem.BY_NAME_ALPHABETICAL);
                    rvMovie.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                    rvMovie.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                    mMovieAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d(String.valueOf(R.string.error), t.getMessage());
                    Toast.makeText(getApplicationContext(), R.string.errorFetchingData, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d(String.valueOf(R.string.error), e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        pbLoading.setVisibility(View.GONE);
    }
}
