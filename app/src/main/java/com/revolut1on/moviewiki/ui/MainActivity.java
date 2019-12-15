package com.revolut1on.moviewiki.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.revolut1on.moviewiki.R;
import com.revolut1on.moviewiki.adapter.TabPagerAdapter;
import com.revolut1on.moviewiki.model.movies.MoviesItem;
import com.revolut1on.moviewiki.ui.movies.MoviesFragment;
import com.revolut1on.moviewiki.ui.tvshow.TvShowFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main)
    Toolbar toolbar_main;
    @BindView(R.id.tabs)
    TabLayout tablayout;
    @BindView(R.id.container)
    ViewPager viewPager;

    private List<MoviesItem> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_main);
        setUpViewpager(viewPager);
        tablayout.setupWithViewPager(viewPager);
    }

    void setUpViewpager(ViewPager viewPager){
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        adapter.populateFragment(new TvShowFragment(), getString(R.string.tvshow));
        adapter.populateFragment(new MoviesFragment(), getString(R.string.movies));
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.settings){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);

    }
}
