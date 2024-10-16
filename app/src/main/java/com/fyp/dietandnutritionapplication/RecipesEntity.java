package com.fyp.dietandnutritionapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipesEntity {

    // Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RecipesEntity() {
        // Default constructor
    }

    // Method to fetch recipes from the folder
    public void fetchRecipesFromFolder(String folderNameInput, OnRecipesFetchedListener listener) {
        db.collection("RecipesFoldersStoring")  // Replace with your collection name
                .whereEqualTo("folderName", folderNameInput)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Recipe> recipeList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Extract the recipes array from the document
                                List<Map<String, Object>> recipes = (List<Map<String, Object>>) document.get("recipes");

                                // Iterate through the array and convert each map into a Recipe object
                                if (recipes != null) {
                                    for (Map<String, Object> recipeMap : recipes) {
                                        Recipe recipe = new Recipe();
                                        recipe.setLabel((String) recipeMap.get("label"));
                                        recipe.setImage((String) recipeMap.get("image"));
                                        recipe.setMealType((List<String>) recipeMap.get("mealType"));
                                        recipe.setCuisineType((List<String>) recipeMap.get("cuisineType"));
                                        recipe.setDishType((List<String>) recipeMap.get("dishType"));
                                        recipe.setDietLabels((List<String>) recipeMap.get("dietLabels"));
                                        recipe.setHealthLabels((List<String>) recipeMap.get("healthLabels"));
                                        recipe.setIngredientLines((List<String>) recipeMap.get("ingredientLines"));
                                        recipe.setCalories(getDoubleValue(recipeMap.get("calories")));
                                        recipe.setTotalWeight(getDoubleValue(recipeMap.get("totalWeight")));
                                        recipe.setTotal_Time(getIntValue(recipeMap.get("total_time")));
                                        recipe.setCaloriesPer100g(getDoubleValue(recipeMap.get("caloriesPer100g")));
                                        recipe.setImage((String) recipeMap.get("url"));
                                        recipe.setUserId((String) recipeMap.get("userId"));

                                        // Add the Recipe object to the list
                                        recipeList.add(recipe);
                                    }
                                }
                            }

                            // Pass the fetched recipes to the listener
                            listener.onRecipesFetched(recipeList);
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // Helper method to handle potential null values for Double fields
    private double getDoubleValue(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else {
            return 0.0;  // Default value
        }
    }

    // Helper method to handle potential null values for Integer fields
    private int getIntValue(Object value) {
        if (value instanceof Long) {
            return ((Long) value).intValue();
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return 0;  // Default value
        }
    }

    // Callback interface for fetching recipes
    public interface OnRecipesFetchedListener {
        void onRecipesFetched(ArrayList<Recipe> recipeList);
    }
}
