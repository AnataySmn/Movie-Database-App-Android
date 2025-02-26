package com.example.assignment3.view.Favourite;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.databinding.ActivityFavouriteHolderBinding;

public class FavouriteHolder extends RecyclerView.ViewHolder {

    public final ActivityFavouriteHolderBinding binding;

    public FavouriteHolder(@NonNull ActivityFavouriteHolderBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}