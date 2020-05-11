package com.example.mvvmretrofitdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mvvmretrofitdemo.MovieDetailsActivity;
import com.example.mvvmretrofitdemo.R;
import com.example.mvvmretrofitdemo.databinding.PopularMovieListItemBinding;
import com.example.mvvmretrofitdemo.model.Result;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.PopularMovieViewHolder> {

    private List<Result> popularList;
    private Context context;

    public ResultAdapter(Context context, List<Result> popularList) {
        this.context = context;
        this.popularList = popularList;
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
        Result result = popularList.get(position);
        holder.listItemBinding.setResult(result);

    }

    @Override
    public int getItemCount() {
        return popularList.size();
    }

    class PopularMovieViewHolder extends RecyclerView.ViewHolder {

        private PopularMovieListItemBinding listItemBinding;

        public PopularMovieViewHolder(@NonNull PopularMovieListItemBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Result result = popularList.get(position);
                        Intent intent = new Intent(context, MovieDetailsActivity.class);
                        intent.putExtra("movieData", result);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
