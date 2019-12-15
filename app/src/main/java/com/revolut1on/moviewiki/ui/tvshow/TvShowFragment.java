package com.revolut1on.moviewiki.ui.tvshow;

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
import com.revolut1on.moviewiki.adapter.TvShowAdapter;
import com.revolut1on.moviewiki.api.BaseApiService;
import com.revolut1on.moviewiki.api.UtilsApi;
import com.revolut1on.moviewiki.model.tvshows.TvShowsItem;
import com.revolut1on.moviewiki.model.tvshows.TvShowsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowFragment extends Fragment {

    private static final String TAG = "TV Show Fragment";

    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.rvTvShow)
    RecyclerView rvTvShow;

    BaseApiService mApiService;
    TvShowAdapter mTvShowAdapter;
    ProgressDialog mProgressDialog;

    private List<TvShowsItem> tvShowList = new ArrayList<>();

    public TvShowFragment() {
        // Required empty public constructor
    }

    public static TvShowFragment newInstance() {
        return new TvShowFragment();
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
        View root = inflater.inflate(R.layout.fragment_tv_show, container, false);
        ButterKnife.bind(this, root);
        mApiService = UtilsApi.getAPIService();
        initView();
        loadMovie();

        return root;
    }

    private void initView(){
        mTvShowAdapter = new TvShowAdapter(getActivity(), tvShowList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            rvTvShow.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }

        rvTvShow.setItemAnimator(new DefaultItemAnimator());
        rvTvShow.setHasFixedSize(true);
        rvTvShow.setAdapter(mTvShowAdapter);
    }

    private void loadMovie() {
        pbLoading.setVisibility(View.VISIBLE);

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getContext(), R.string.noApi, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                return;
            }

            Call<TvShowsResponse> call = mApiService.getPopularTvShows(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<TvShowsResponse>() {
                @Override
                public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                    List<TvShowsItem> movies = response.body().getResults();
                    Collections.sort(movies, TvShowsItem.BY_NAME_ALPHABETICAL);
                    rvTvShow.setAdapter(new TvShowAdapter(getActivity(), movies));
                    rvTvShow.smoothScrollToPosition(0);
                    mTvShowAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<TvShowsResponse> call, Throwable t) {
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
