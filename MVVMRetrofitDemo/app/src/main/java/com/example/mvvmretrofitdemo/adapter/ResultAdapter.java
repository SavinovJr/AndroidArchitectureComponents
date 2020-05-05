package com.example.mvvmretrofitdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mvvmretrofitdemo.MovieDetailsActivity;
import com.example.mvvmretrofitdemo.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movie_list_item, parent, false);
        return new PopularMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMovieViewHolder holder, int position) {

        holder.titleTextView.setText(popularList.get(position).getOriginalTitle());
        holder.popularityTextView.setText(String.valueOf(popularList.get(position).getPopularity()));

        String imagePath = "https://image.tmdb.org/t/p/w500/" + popularList.get(position).getPosterPath();
        Glide.with(context).load(imagePath).placeholder(context.getDrawable(R.drawable.original))
             .into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        return popularList.size();
    }

    class PopularMovieViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView popularityTextView;
        ImageView movieImageView;

        public PopularMovieViewHolder(@NonNull View itemView) {
            super(itemView);

            movieImageView = itemView.findViewById(R.id.movie_image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            popularityTextView = itemView.findViewById(R.id.popularity_text_view);

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
