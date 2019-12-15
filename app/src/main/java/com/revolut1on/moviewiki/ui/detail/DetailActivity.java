package com.revolut1on.moviewiki.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.revolut1on.moviewiki.R;
import com.revolut1on.moviewiki.model.movies.MoviesItem;
import com.revolut1on.moviewiki.model.tvshows.TvShowsItem;
import com.willy.ratingbar.ScaleRatingBar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.iv_movie_backdrop)
    ImageView iv_movie_backdrop;
    @BindView(R.id.iv_movie_poster)
    ImageView iv_movie_poster;

    private final AppCompatActivity activity = DetailActivity.this;

    String movie_id, movie_title, movie_backdrop_path, movie_poster_path,
            movie_popularity, movie_release_date, movie_vote_count, movie_overview;

    String tvshow_id, tvshow_title, tvshow_backdrop_path, tvshow_poster_path,
            tvshow_popularity, tvshow_release_date, tvshow_vote_count, tvshow_overview;

    float movie_vote_average, tvshow_vote_average;

    MoviesItem movie;
    TvShowsItem tvshow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initCollapsingToolbar();

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movies")) {

            movie = getIntent().getParcelableExtra("movies");
            String base_image_url = "https://image.tmdb.org/t/p/original";
            DateTime dateTime = DateTime.parse(movie.getReleaseDate(), DateTimeFormat.forPattern("yyyy-MM-dd"));

            movie_id = Integer.toString(movie.getId());
            movie_title = movie.getOriginalTitle();
            movie_backdrop_path = movie.getBackdropPath();
            movie_poster_path = movie.getPosterPath();
            movie_popularity = Double.toString(movie.getPopularity());
            movie_release_date = dateTime.toString("dd MMMM yyyy");
            movie_vote_count = Integer.toString(movie.getVoteCount());
            movie_overview = movie.getOverview();
            movie_vote_average = (float) movie.getVoteAverage();

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
            tv_movie_vote_count.setText(movie_vote_count + " " + getText(R.string.total_vote_count));
            tv_movie_popularity.setText(movie_popularity + " " + getText(R.string.popularity));
            tv_movie_release_date.setText(getText(R.string.release_date) + " " + movie_release_date);
            tv_movie_overview.setText(movie_overview);

        } else if (intentThatStartedThisActivity.hasExtra("tvshows")) {
            tvshow = getIntent().getParcelableExtra("tvshows");
            String base_image_url = "https://image.tmdb.org/t/p/original";
            DateTime dateTime = DateTime.parse(tvshow.getFirstAirDate(), DateTimeFormat.forPattern("yyyy-MM-dd"));

            tvshow_id = Integer.toString(tvshow.getId());
            tvshow_title = tvshow.getOriginalName();
            tvshow_backdrop_path = tvshow.getBackdropPath();
            tvshow_poster_path = tvshow.getPosterPath();
            tvshow_popularity = Double.toString(tvshow.getPopularity());
            tvshow_release_date = dateTime.toString("dd MMMM yyyy");
            tvshow_vote_count = Integer.toString(tvshow.getVoteCount());
            tvshow_overview = tvshow.getOverview();
            tvshow_vote_average = (float) tvshow.getVoteAverage();

            Glide.with(DetailActivity.this)
                    .load(base_image_url + tvshow_backdrop_path)
                    .error(R.drawable.ic_load_image)
                    .into(iv_movie_backdrop);

            Glide.with(DetailActivity.this)
                    .load(base_image_url + tvshow_poster_path)
                    .error(R.drawable.ic_load_image)
                    .into(iv_movie_poster);

            rb_movie_vote_average.setRating(tvshow_vote_average);
            tv_movie_title.setText(tvshow_title);
            tv_movie_vote_count.setText(tvshow_vote_count + " " + getText(R.string.total_vote_count));
            tv_movie_popularity.setText(tvshow_popularity + " " + getText(R.string.popularity));
            tv_movie_release_date.setText(getText(R.string.release_date) + " " + tvshow_release_date);
            tv_movie_overview.setText(tvshow_overview);
        } else {
            Toast.makeText(this, R.string.no_data_receive, Toast.LENGTH_SHORT).show();
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
                    if (movie_title != null){
                        collapsingToolbarLayout.setTitle(movie_title);
                    } else if (tvshow_title != null) {
                        collapsingToolbarLayout.setTitle(tvshow_title);
                    }
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
