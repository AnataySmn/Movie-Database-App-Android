package com.example.assignment3.view.Favourite;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment3.databinding.ActivityFavouriteHolderBinding;
import com.example.assignment3.model.FavMovieModel;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteHolder> {
    private List<FavMovieModel> movieList;


    public FavouriteAdapter(List<FavMovieModel> movieList) {
        this.movieList = movieList;
    }

    public void updateFavourites(List<FavMovieModel> newMovies) {
        this.movieList = newMovies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityFavouriteHolderBinding binding = ActivityFavouriteHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FavouriteHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, int position) {
        FavMovieModel favMovieModel = movieList.get(position);
        holder.binding.movieName.setText(favMovieModel.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(favMovieModel.getPoster())
                .into(holder.binding.imageview);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FavDetailsActivity.class);
                intent.putExtra("title", favMovieModel.getTitle());
                intent.putExtra("poster", favMovieModel.getPoster());
                intent.putExtra("description", favMovieModel.getDescription());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
