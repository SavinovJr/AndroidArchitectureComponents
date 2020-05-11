package com.example.mvvmretrofitdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmretrofitdemo.MovieDetailsActivity;
import com.example.mvvmretrofitdemo.R;
import com.example.mvvmretrofitdemo.databinding.PopularMovieListItemBinding;
import com.example.mvvmretrofitdemo.model.Result;

public class ResultAdapter extends PagedListAdapter<Result, ResultAdapter.PopularMovieViewHolder> {

    private Context context;

    public ResultAdapter(Context context) {
        super(Result.CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public PopularMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PopularMovieListItemBinding popularMovieListItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                        R.layout.popular_movie_list_item, parent,
                                        false);
        return new PopularMovieViewHolder(popularMovieListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMovieViewHolder holder, int position) {
        Result result = getItem(position);
        holder.listItemBinding.setResult(result);

    }

    class PopularMovieViewHolder extends RecyclerView.ViewHolder {

        private PopularMovieListItemBinding listItemBinding;

        public PopularMovieViewHolder(@NonNull PopularMovieListItemBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Result result = getItem(position);
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    intent.putExtra("movieData", result);
                    context.startActivity(intent);
                }
            });
        }
    }
}
