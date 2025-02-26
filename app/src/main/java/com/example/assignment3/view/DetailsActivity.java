package com.example.assignment3.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.assignment3.databinding.DetailsLayoutBinding;
import com.example.assignment3.utilities.ApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {
    private DetailsLayoutBinding binding;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();

    String title;
    String poster;
    String plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DetailsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        title = getIntent().getStringExtra("title");
        poster = getIntent().getStringExtra("poster");
        String imdbID = getIntent().getStringExtra("imdbID");

        binding.detailsTitle.setText(title);

        if (!poster.isEmpty()) {
            Glide.with(this)
                    .load(poster)
                    .into(binding.detailsImage);
        }

        getAdditionalDetails(imdbID);

        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(currentUser.getUid());
                Toast.makeText(DetailsActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendData(String id) {
        Map<String, Object> movieInfo = new HashMap<>();
        movieInfo.put("title", title);
        movieInfo.put("poster", poster);
        movieInfo.put("description", plot);

        db.collection(id)
                .add(movieInfo)
                .addOnSuccessListener(aVoid -> {
                    Log.d("tag", "Added movie");
                    Toast.makeText(DetailsActivity.this, "Added movie!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.w("tag", "Error!", e);
                    Toast.makeText(DetailsActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                });

    }

    private void getAdditionalDetails(String imdbID) {
        String url = "https://www.omdbapi.com/?apikey=8141545&i=" + imdbID;

        ApiClient.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Log.i("tag", "API call failed");
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseData = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        plot = jsonObject.getString("Plot");

                        runOnUiThread(() -> {
                            binding.detailsDescription.setText(plot);
                        });

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}