package com.fyp.dietandnutritionapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

public class Recipe implements Parcelable {
    private String name; // Name of the recipe
    private String folder; // Folder to which the recipe belongs
    private String recipe_id;
    private String userId;
    private String label; // The title of the recipe
    private String status;
    private String image; // URL of the recipe image
    private List<String> mealType; // Meal type (e.g., lunch, dinner)
    private List<String> cuisineType; // Cuisine type (e.g., Italian, Asian)
    private List<String> dishType; // Dish type (e.g., main course, appetizer)
    private List<String> dietLabels; // Diet labels (e.g., gluten-free, vegetarian)
    private List<String> healthLabels; // Health labels (e.g., low-fat, high-protein)
    private String url; // Link to the full recipe
    private List<String> ingredientLines; // List of ingredients
    private List<String> recipeStepsLines;
    private double calories; // Calorie count
    private double totalWeight; // Total weight of the recipe in grams
    private int total_time; // Total cooking time in minutes
    private double caloriesPer100g; // New field for calories per 100g
    private String instructions;

    public Recipe() {
    }

    // Getters for the fields
    public String getRecipe_id(){ return  recipe_id;}

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;}

    public String getuserId(){ return userId;}

    public void setUserId(String userId) {
        this.userId = userId;}

    public String getStatus(){ return status;}

    public void setStatus(String status){ this.status = status;}

    public String getLabel() {
        return label;
    }

    public String getImage() {
        return image;
    }

    public List<String> getMealType() {
        return mealType;
    }

    public List<String> getCuisineType() {
        return cuisineType;
    }

    public List<String> getDishType() {
        return dishType;
    }

    public List<String> getDietLabels() {
        return dietLabels;
    }

    public List<String> getHealthLabels() {
        return healthLabels;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    public List<String> getRecipeStepsLines() {
        return recipeStepsLines;
    }

    public double getCalories() {
        return calories;
    }

    public double getTotalWeight() {return totalWeight;}

    public double getCaloriesPer100g() { return caloriesPer100g;}

    public void setCaloriesPer100g(double caloriesPer100g) {
        this.caloriesPer100g = caloriesPer100g;}

    public int getTotal_Time() {
        return total_time;
    }

    public void setTotal_Time(int total_time) {
        this.total_time = total_time;
    }

    public String getInstructions() {
        return instructions;
    }


    // Optionally, add a constructor
    public Recipe(String recipe_id, String userId, String label, String status, String image, List<String> mealType, List<String> cuisineType, List<String> dishType, List<String> dietLabels,
                  List<String> healthLabels, String url, List<String> ingredientLines, List<String> recipeStepsLines, double calories, double totalWeight, int total_Time, String instructions) {
        this.recipe_id = recipe_id;
        this.userId = userId;
        this.label = label;
        this.status = status;
        this.image = image;
        this.mealType = mealType;
        this.cuisineType = cuisineType;
        this.dishType = dishType;
        this.dietLabels = dietLabels;
        this.healthLabels = healthLabels;
        this.url = url;
        this.ingredientLines = ingredientLines;
        this.recipeStepsLines = recipeStepsLines;
        this.calories = calories;
        this.totalWeight = totalWeight;
        this.total_time = total_Time;
        this.name = name;
        this.folder = folder;
        this.instructions = instructions;
    }

    // Getter for the name
    public String getName() {
        return name;
    }

    // Getter for the folder
    public String getFolder() {
        return folder;
    }


    // Parcelable implementation for passing Recipe between fragments
    protected Recipe(Parcel in) {
        recipe_id = in.readString();
        userId = in.readString();
        label = in.readString();
        status = in.readString();
        image = in.readString();
        mealType = in.createStringArrayList();
        cuisineType = in.createStringArrayList();
        dishType = in.createStringArrayList();
        dietLabels = in.createStringArrayList();
        healthLabels = in.createStringArrayList();
        url = in.readString();
        ingredientLines = in.createStringArrayList();
        recipeStepsLines = in.createStringArrayList();
        calories = in.readDouble();
        totalWeight = in.readDouble();
        total_time = in.readInt();
        name = in.readString();
        folder = in.readString();
        instructions = in.readString();
            // Read other fields from the Parcel

    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipe_id);
        dest.writeString(userId);
        dest.writeString(label);
        dest.writeString(status);
        dest.writeString(image);
        dest.writeStringList(mealType);
        dest.writeStringList(cuisineType);
        dest.writeStringList(dishType);
        dest.writeStringList(dietLabels);
        dest.writeStringList(healthLabels);
        dest.writeString(url);
        dest.writeStringList(ingredientLines);
        dest.writeStringList(recipeStepsLines);
        dest.writeDouble(calories);
        dest.writeDouble(totalWeight);
        dest.writeInt(total_time);
        dest.writeString(name);
        dest.writeString(folder);
        dest.writeString(instructions);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void setMealType(List<String> mealType) {
        this.mealType = mealType;
    }

    public void setCuisineType(List<String> cuisineType) {
        this.cuisineType = cuisineType;
    }

    public void setDishType(List<String> dishType) {
        this.dishType = dishType;
    }

    public void setDietLabels(List<String> dietLabels) {
        this.dietLabels = dietLabels;
    }

    public void setHealthLabels(List<String> healthLabels) {
        this.healthLabels = healthLabels;
    }

    public void setImage(String imageUrl) {
        this.image = imageUrl;
    }

    public void setIngredientLines(List<String> ingredients) {
        this.ingredientLines = ingredients;
    }

    public void setRecipeStepsLines(List<String> recipeSteps) {this.recipeStepsLines = recipeSteps;}

    public void setInstructions(String instructions) {this.instructions = instructions; }
}
