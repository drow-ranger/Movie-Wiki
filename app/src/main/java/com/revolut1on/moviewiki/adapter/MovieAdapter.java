package com.revolut1on.moviewiki.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.revolut1on.moviewiki.DetailActivity;
import com.revolut1on.moviewiki.R;
import com.revolut1on.moviewiki.model.movies.MoviesItem;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Deo Fibrianico on 03,December,2019
 * Visit https://revolut1on.com
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context mContext;
    private List<MoviesItem> movieList;
    String movie_title, movie_release_date;

    public MovieAdapter(Context mContext, List<MoviesItem> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_repo, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        DateTime dateTime = DateTime.parse(movieList.get(i).getReleaseDate(), DateTimeFormat.forPattern("yyyy-MM-dd"));

        movie_title = movieList.get(i).getOriginalTitle();
        movie_release_date = dateTime.toString("dd MMMM yyyy");
        ;

        viewHolder.tv_movie_title.setText(movie_title);
        viewHolder.tv_movie_release_date.setText(movie_release_date);

        String poster = "https://image.tmdb.org/t/p/original" + movieList.get(i).getPosterPath();

        Glide.with(mContext)
                .load(poster)
                .error(R.drawable.ic_load_image)
                .into(viewHolder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_movie_title)
        TextView tv_movie_title;
        @BindView(R.id.tv_movie_release_date)
        TextView tv_movie_release_date;
        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        int clickedDataItem = movieList.get(pos).getId();
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("movieId", clickedDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}