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
import com.revolut1on.moviewiki.R;
import com.revolut1on.moviewiki.model.tvshows.TvShowsItem;
import com.revolut1on.moviewiki.ui.detail.DetailActivity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Deo Fibrianico on 12,December,2019
 * Visit https://revolut1on.com
 */

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.MyViewHolder> {

    private Context mContext;
    private List<TvShowsItem> tvshowList;
    String tvshow_title, tvshow_first_air_date;

    public TvShowAdapter(Context mContext, List<TvShowsItem> tvshowList) {
        this.mContext = mContext;
        this.tvshowList = tvshowList;
    }

    @Override
    public TvShowAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_repo, viewGroup, false);

        return new TvShowAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TvShowAdapter.MyViewHolder viewHolder, int i) {
        DateTime dateTime = DateTime.parse(tvshowList.get(i).getFirstAirDate(), DateTimeFormat.forPattern("yyyy-MM-dd"));

        tvshow_title = tvshowList.get(i).getName();
        tvshow_first_air_date = dateTime.toString("dd MMMM yyyy");

        viewHolder.tv_movie_title.setText(tvshow_title);
        viewHolder.tv_movie_release_date.setText(tvshow_first_air_date);

        String poster = "https://image.tmdb.org/t/p/original" + tvshowList.get(i).getPosterPath();

        Glide.with(mContext)
                .load(poster)
                .error(R.drawable.ic_load_image)
                .into(viewHolder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return tvshowList.size();
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
                        TvShowsItem clickedDataItem = tvshowList.get(pos);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("tvshows", clickedDataItem );
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
