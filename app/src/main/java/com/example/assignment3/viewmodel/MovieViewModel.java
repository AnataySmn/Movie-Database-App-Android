package com.example.assignment3.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.compose.foundation.ExperimentalFoundationApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment3.model.MovieModel;
import com.example.assignment3.utilities.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {
    private final MutableLiveData<List<MovieModel>> movies = new MutableLiveData<>();

    public MutableLiveData<List<MovieModel>> getMovies() {
        return movies;
    }

    @OptIn(markerClass = ExperimentalFoundationApi.class)
    public void movieSearchAPI(String movieName) {
        String searchUrl = "https://www.omdbapi.com/?apikey=8141545&type=movie&s=" + movieName;

        ApiClient.get(searchUrl, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    if (jsonObject.has("Search")) {
                        JSONArray movieArray = jsonObject.getJSONArray("Search");
                        List<MovieModel> movieList = new ArrayList<>();
                        for (int i = 0; i < movieArray.length(); i++) {
                            JSONObject movieObject = movieArray.getJSONObject(i);
                            MovieModel movie = new MovieModel(movieObject.getString("Title"),
                                    movieObject.getString("Year"),
                                    movieObject.getString("imdbID"),
                                    movieObject.getString("Poster"));
                            movieList.add(movie);
                        }
                        movies.postValue(movieList);
                    } else {
                        movies.postValue(new ArrayList<>());
                    }
                } catch (JSONException e) {
                    movies.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                movies.postValue(new ArrayList<>());
            }
        });
    }
}
