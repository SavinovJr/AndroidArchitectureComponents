package com.example.mvvmretrofitdemo.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.mvvmretrofitdemo.R;
import com.example.mvvmretrofitdemo.service.MovieApiService;
import com.example.mvvmretrofitdemo.service.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Long, Result> {

    private MovieApiService movieApiService;
    private Application application;

    public MovieDataSource(MovieApiService movieApiService, Application application) {
        this.movieApiService = movieApiService;
        this.application = application;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
            @NonNull LoadInitialCallback<Long, Result> callback) {
        movieApiService = RetrofitInstance.getService();
        Call<MovieApiResponse> call = movieApiService
                .getPopularMoviesWithPaging(application.getApplicationContext().getString(R.string.api_key),
                                            1);
        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {
                MovieApiResponse movieApiResponse = response.body();
                if (movieApiResponse != null && movieApiResponse.getResults() != null) {
                    List<Result> results = movieApiResponse.getResults();
                    callback.onResult(results, null, 2L);
                }
            }

            @Override
            public void onFailure(Call<MovieApiResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Result> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull final LoadCallback<Long, Result> callback) {
        movieApiService = RetrofitInstance.getService();
        Call<MovieApiResponse> call = movieApiService
                .getPopularMoviesWithPaging(application.getApplicationContext().getString(R.string.api_key),
                                            params.key);
        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {
                MovieApiResponse movieApiResponse = response.body();
                if (movieApiResponse != null && movieApiResponse.getResults() != null) {
                    List<Result> results = movieApiResponse.getResults();
                    callback.onResult(results, params.key + 1);
                }
            }

            @Override
            public void onFailure(Call<MovieApiResponse> call, Throwable t) {

            }
        });
    }
}
