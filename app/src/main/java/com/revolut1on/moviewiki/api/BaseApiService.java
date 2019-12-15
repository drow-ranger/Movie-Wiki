package com.revolut1on.moviewiki.api;

import com.revolut1on.moviewiki.model.movie.MovieResponse;
import com.revolut1on.moviewiki.model.movies.MoviesResponse;
import com.revolut1on.moviewiki.model.tvshows.TvShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Deo Fibrianico on 03,December,2019
 * Visit https://revolut1on.com
 */

public interface BaseApiService {
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("tv/popular")
    Call<TvShowsResponse> getPopularTvShows(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<MovieResponse> getMovieDetail(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
