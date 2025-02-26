package com.example.assignment3.view.Favourite;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment3.databinding.ActivityFavouritesBinding;
import com.example.assignment3.view.MainActivity;
import com.example.assignment3.viewmodel.FavViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {
    private ActivityFavouritesBinding binding;
    private FavouriteAdapter adapter;
    private FavViewModel viewModel;
    FirebaseFirestore db;



    FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FavouriteAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(FavViewModel.class);

        viewModel.getMovies().observe(this, movieList -> {
                adapter.updateFavourites(movieList);
        });

        viewModel.fetchFavourites(currentUser.getUid());


        binding.searchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(FavouritesActivity.this, MainActivity.class);
            startActivity(intent);
        });

        binding.favButton.setOnClickListener(v -> {
            Toast.makeText(FavouritesActivity.this, "It's favourite page", Toast.LENGTH_SHORT).show();
        });

    }

    protected void onResume() {
        super.onResume();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                viewModel.fetchFavourites(currentUser.getUid());
            }
        }, 500);
    }

}