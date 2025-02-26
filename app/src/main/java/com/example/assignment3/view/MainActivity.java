package com.example.assignment3.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment3.databinding.ActivityMainBinding;
import com.example.assignment3.view.Favourite.FavouritesActivity;
import com.example.assignment3.viewmodel.MovieViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Adapter adapter;
    private MovieViewModel viewModel;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new Adapter(new ArrayList<>(), movie -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("poster", movie.getPoster());
            intent.putExtra("imdbID", movie.getImdbID());
            startActivity(intent);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        viewModel.getMovies().observe(this, movieList -> {
            if (movieList != null && !movieList.isEmpty()) {
                adapter.refreshMovies(movieList);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, no movies found", Toast.LENGTH_SHORT).show();
            }
        });
        binding.searchButton.setOnClickListener(v -> {
            String searchEnter = binding.enterMovie.getText().toString().trim();
            if (searchEnter.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a movie name!", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.movieSearchAPI(searchEnter);
        });

        binding.favButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
            startActivity(intent);
        });

        binding.searchBtn.setOnClickListener(v -> {
            Toast.makeText(this, "It's search page", Toast.LENGTH_SHORT).show();
        });
    }
}