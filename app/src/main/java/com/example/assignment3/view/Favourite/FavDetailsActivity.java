package com.example.assignment3.view.Favourite;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.assignment3.databinding.ActivityFavDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FavDetailsActivity extends AppCompatActivity {

    private ActivityFavDetailsBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra("title");
        String poster = getIntent().getStringExtra("poster");
        String description = getIntent().getStringExtra("description");

        binding.detailsTitle.setText(title);
        binding.detailsDescription.setText(description);

        if (!poster.isEmpty()) {
            Glide.with(this)
                    .load(poster)
                    .into(binding.detailsImage);
        }

        binding.returnBtn.setOnClickListener(v -> {
            finish();
        });

        binding.updateBtn.setOnClickListener(v -> {
            String newDescription = binding.detailsDescription.getText().toString();
            updateMovie(title, newDescription, currentUser.getUid());
            binding.detailsDescription.setText(newDescription);
            Toast.makeText(this, title + " desc updated", Toast.LENGTH_SHORT).show();
        });

        binding.deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this item?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteMovie(title, currentUser.getUid());
                    dialog.dismiss();
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        });
    }

    private void updateMovie(String title, String newDescription, String id) {
        db.collection(id)
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference()
                                    .update("description", newDescription)
                                    .addOnSuccessListener(aVoid -> Log.d("tag", "Movie updated successfully!"))
                                    .addOnFailureListener(e -> Log.w("tag", "Error updating movie: ", e));
                        }
                    }
                });
    }

    private void deleteMovie(String title, String id) {
        db.collection(id)
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        Toast.makeText(this, "Movie deleted", Toast.LENGTH_SHORT).show();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference()
                                    .delete()
                                    .addOnSuccessListener(aVoid -> Log.d("tag", "Movie deleted successfully!"))
                                    .addOnFailureListener(e -> Log.w("tag", "Error deleting movie: ", e));
                        }
                    }
                });
    }
}