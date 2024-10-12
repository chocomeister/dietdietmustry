package com.example.dietandnutritionapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddToFolderFragment extends Fragment {
    private Recipe recipe; // The recipe to be added
    private FirebaseFirestore db; // Firestore instance
    private static final String TAG = "AddToFolderFragment"; // Tag for logging

    // Constructor to initialize the fragment with a recipe
    public AddToFolderFragment(Recipe recipe) {
        this.recipe = recipe;
        this.db = FirebaseFirestore.getInstance();
    }

    // Method to show a dialog for folder name input
    public void showAddToFolderDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Folder Name");

        // Set up the input
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String folderName = input.getText().toString().trim();
                if (!folderName.isEmpty()) {
                    checkFolderExists(folderName, context);
                } else {
                    Toast.makeText(context, "Folder name cannot be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Method to check if the folder exists
    private void checkFolderExists(String folderName, @NonNull Context context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(context, "Error: User is not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log the folder name being checked
        Log.d(TAG, "Checking existence of folder: " + folderName);

        db.collection("RecipesFolders")
                .document(folderName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // If the folder exists, add the recipe to the folder
                            addRecipeToFolder(folderName, context);
                        } else {
                            Toast.makeText(context, "Folder does not exist.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Log the error message
                        Log.e(TAG, "Error checking folder existence: " + task.getException().getMessage());
                        Toast.makeText(context, "Error checking folder existence: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to add the recipe to the specified folder
    public void addRecipeToFolder(String folderName, @NonNull Context context) {
        // Get the current user's ID
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        // Check if userId is null (user is not logged in)
        if (userId == null) {
            Toast.makeText(context, "Error: User is not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the recipe is not null
        if (recipe == null) {
            Toast.makeText(context, "Error: Recipe is null.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a RecipeFolder object
        RecipeFolder folder = new RecipeFolder(userId, folderName, recipe);

        // Save the folder to Firestore
        db.collection("RecipesFolders")
                .document(folderName) // Use folder name as document ID
                .set(folder) // Set the folder object
                .addOnSuccessListener(aVoid -> {
                    // Show success message
                    Toast.makeText(context, "Recipe added to folder!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Show error message
                    Toast.makeText(context, "Error adding recipe to folder: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
