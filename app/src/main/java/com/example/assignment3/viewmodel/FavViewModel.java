package com.example.assignment3.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment3.model.FavMovieModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavViewModel extends ViewModel {
    private final MutableLiveData<List<FavMovieModel>> moviesLiveData;
    private FirebaseFirestore db;


    public FavViewModel() {
        moviesLiveData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<FavMovieModel>> getMovies() {
        return moviesLiveData;
    }

    public void fetchFavourites(String id) {
        db.collection(id)
                .orderBy("title")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<FavMovieModel> movieList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String poster = document.getString("poster");
                            String description = document.getString("description");
                            movieList.add(new FavMovieModel(title, poster, description));
                        }
                        moviesLiveData.postValue(movieList);
                    } else {
                        Log.w("tag", "Error getting movies: ", task.getException());
                    }
                });
    }
}