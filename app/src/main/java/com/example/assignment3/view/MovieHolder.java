package com.example.assignment3.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.databinding.MovieLayoutBinding;
public class MovieHolder extends RecyclerView.ViewHolder {

    public final MovieLayoutBinding binding;

    public MovieHolder(@NonNull MovieLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
