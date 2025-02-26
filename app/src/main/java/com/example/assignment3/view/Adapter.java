package com.example.assignment3.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment3.databinding.MovieLayoutBinding;
import com.example.assignment3.model.MovieModel;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<MovieHolder> {

    private final List<MovieModel> movies;
    private MovieClickListener clickListener;

    public Adapter(List<MovieModel> movies, MovieClickListener clickListener) {
        this.movies = movies;
        this.clickListener = clickListener;
    }

    public interface MovieClickListener {
        void onMovieClick(MovieModel movie);
    }

    public void refreshMovies(List<MovieModel> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieLayoutBinding binding = MovieLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        MovieModel movie = movies.get(position);
        holder.binding.movieName.setText(movie.getTitle());
        holder.binding.movieYear.setText(movie.getYear());

        Glide.with(holder.itemView.getContext())
                .load(movie.getPoster())
                .into(holder.binding.imageview);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("poster", movie.getPoster());
                intent.putExtra("imdbID", movie.getImdbID());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
