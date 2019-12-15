package com.revolut1on.moviewiki.ui.movies;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.revolut1on.moviewiki.BuildConfig;
import com.revolut1on.moviewiki.R;
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

public class MoviesFragment extends Fragment {

    private static final String TAG = "Movies Fragment";

    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.rvTvShow)
    RecyclerView rvMovie;

    BaseApiService mApiService;
    MovieAdapter mMovieAdapter;
    ProgressDialog mProgressDialog;

    private List<MoviesItem> movieList = new ArrayList<>();

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, root);
        mApiService = UtilsApi.getAPIService();
        initView();
        loadMovie();

        return root;
    }

    private void initView(){
        mMovieAdapter = new MovieAdapter(getActivity(), movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            rvMovie.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }

        rvMovie.setItemAnimator(new DefaultItemAnimator());
        rvMovie.setHasFixedSize(true);
        rvMovie.setAdapter(mMovieAdapter);
    }

    private void loadMovie() {
        pbLoading.setVisibility(View.VISIBLE);

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getContext(), R.string.noApi, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                return;
            }

            Call<MoviesResponse> call = mApiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<MoviesItem> movies = response.body().getResults();
                    Collections.sort(movies, MoviesItem.BY_NAME_ALPHABETICAL);
                    rvMovie.setAdapter(new MovieAdapter(getActivity(), movies));
                    rvMovie.smoothScrollToPosition(0);
                    mMovieAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d(String.valueOf(R.string.error), t.getMessage());
                    Toast.makeText(getActivity(), R.string.errorFetchingData, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d(String.valueOf(R.string.error), e.getMessage());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        pbLoading.setVisibility(View.GONE);
    }
}
