package com.revolut1on.moviewiki;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.revolut1on.moviewiki.api.BaseApiService;
import com.revolut1on.moviewiki.api.UtilsApi;
import com.revolut1on.moviewiki.model.movie.GenresItem;
import com.revolut1on.moviewiki.model.movie.ProductionCompaniesItem;
import com.revolut1on.moviewiki.model.movie.MovieResponse;
import com.willy.ratingbar.ScaleRatingBar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.rb_movie_vote_average)
    ScaleRatingBar rb_movie_vote_average;
    @BindView(R.id.tv_movie_title)
    TextView tv_movie_title;
    @BindView(R.id.tv_movie_popularity)
    TextView tv_movie_popularity;
    @BindView(R.id.tv_movie_release_date)
    TextView tv_movie_release_date;
    @BindView(R.id.tv_movie_vote_count)
    TextView tv_movie_vote_count;
    @BindView(R.id.tv_movie_overview)
    TextView tv_movie_overview;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_genre)
    TextView tv_genre;
    @BindView(R.id.tv_production_companies)
    TextView tv_production_companies;
    @BindView(R.id.iv_movie_backdrop)
    ImageView iv_movie_backdrop;
    @BindView(R.id.iv_movie_poster)
    ImageView iv_movie_poster;

    BaseApiService mApiService;
    ProgressDialog mProgressDialog;

    private final AppCompatActivity activity = DetailActivity.this;

    String movie_id, movie_title, movie_backdrop_path, movie_poster_path,
            movie_popularity, movie_release_date, movie_vote_count, movie_overview,
            movie_status, movie_genre, movie_production_companies;

    List<GenresItem> genreList = new ArrayList<>();
    List<ProductionCompaniesItem> productionCompaniesList = new ArrayList<>();

    int movieId;
    float movie_vote_average;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        ButterKnife.bind(this);
        mApiService = UtilsApi.getAPIService();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initCollapsingToolbar();

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movieId")) {

            movieId = getIntent().getIntExtra("movieId", 0);
            tv_movie_title.setText(Integer.toString(movieId));
            loadMovie(movieId);
        } else {
            Toast.makeText(this, "No Data Receive", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMovie(int movieId) {
            try {
                if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.noApi, Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    return;
                }

                Call<MovieResponse> call = mApiService.getMovieDetail(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN);
                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        final MovieResponse movie = response.body();

                        String base_image_url = "https://image.tmdb.org/t/p/original";
                        DateTime dateTime = DateTime.parse(movie.getReleaseDate(), DateTimeFormat.forPattern("yyyy-MM-dd"));
                        genreList = movie.getGenres();
                        productionCompaniesList = movie.getProductionCompanies();

                        movie_id = Integer.toString(movie.getId());
                        movie_title = movie.getOriginalTitle();
                        movie_backdrop_path = movie.getBackdropPath();
                        movie_poster_path = movie.getPosterPath();
                        movie_popularity = Double.toString(movie.getPopularity());
                        movie_release_date = dateTime.toString("dd MMMM yyyy");
                        movie_vote_count = Integer.toString(movie.getVoteCount());
                        movie_overview = movie.getOverview();
                        movie_vote_average = (float) movie.getVoteAverage();
                        movie_status = movie.getStatus();

                        movie_genre = " ";
                        for (int i = 0; i < genreList.size(); i++) {
                            movie_genre = movie_genre + ", " + genreList.get(i).getName();
                        }

                        movie_production_companies = " ";
                        for (int i = 0; i < productionCompaniesList.size() ; i++) {
                            movie_production_companies = movie_production_companies + ", " + productionCompaniesList.get(i).getName();
                        }

                        Glide.with(DetailActivity.this)
                                .load(base_image_url + movie_backdrop_path)
                                .error(R.drawable.ic_load_image)
                                .into(iv_movie_backdrop);

                        Glide.with(DetailActivity.this)
                                .load(base_image_url + movie_poster_path)
                                .error(R.drawable.ic_load_image)
                                .into(iv_movie_poster);

                        rb_movie_vote_average.setRating(movie_vote_average);
                        tv_movie_title.setText(movie_title);
                        tv_movie_vote_count.setText(movie_vote_count + " Total Vote Count");
                        tv_movie_popularity.setText(movie_popularity + " Popularity");
                        tv_movie_release_date.setText("Release " + movie_release_date);
                        tv_movie_overview.setText(movie_overview);
                        tv_status.setText(movie_status);
                        tv_genre.setText(movie_genre.substring(3));
                        tv_production_companies.setText(movie_production_companies.substring(3));
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.d(String.valueOf(R.string.error), t.getMessage());
                        Toast.makeText(getApplicationContext(), R.string.errorFetchingData, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.d(String.valueOf(R.string.error), e.getMessage());
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(movie_title);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
