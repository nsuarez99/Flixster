package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    Context context;
    List<Movie> movies;
    OnClickListener listener;

    public MovieAdapter(Context context, List<Movie> movies, OnClickListener clickListener) {
        this.context = context;
        this.movies = movies;
        listener = clickListener;
    }

    //Usually involves inflating a layout from the XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //Populates data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        //Get the movie at the position
        Movie movie = movies.get(position);
        //Bind the movie data into the viewholder
        holder.bind(movie);
    }

    //return total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String urlImage;
            int placeholder;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                urlImage = movie.getBackdropPath();
                placeholder = R.drawable.backdrop_placeholder;
            }
            else{
                urlImage = movie.getPosterPath();
                placeholder = R.drawable.portrait_placeholder;
            }
            Glide.with(context).load(urlImage).placeholder(placeholder).into(ivPoster);

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //listener is binded to the view (this is the interface we created)
                    listener.onItemClicked(getAdapterPosition());
                }
            });

            tvOverview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });

            ivPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });

        }
    }
}
