package com.fyp.dietandnutritionapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewFavouriteRecipesFragment extends Fragment implements NavFavouriteRecipesController.OnFavoriteRecipesRetrievedListener {
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private RecipeAdapter recipeNewAdapter;
    private List<Recipe> recipeList;
    private List<Recipe> recipeNewList;
    private List<Recipe> APIRecipeList;
    private EditText searchEditText;
    private Spinner mealTypeSpinner;
    private Spinner dishTypeSpinner;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    List<Recipe> filteredList = new ArrayList<>();

    private TextView notificationBadgeTextView;
    private NotificationUController notificationUController;
    private final Random random = new Random();

    // Define your meal types and dish types
    private final String[] mealTypes = {"--Select Meal Type--", "Breakfast", "Lunch", "Dinner", "Snack", "Teatime"};
    private final String[] dishTypes = {"--Select Dish Type--", "Starter", "Main course", "Side dish", "Soup", "Condiments and sauces", "Desserts", "Drinks", "Salad"};

    private List<String> simpleFoodSearches = Arrays.asList(
            "chicken", "beef", "steak", "fish", "soup", "lamb", "pasta", "potato", "burger", "curry", "shrimp", "bacon", "fried", "grilled", "smoked", "salmon"
    );


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_favourite_recipes, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            notificationBadgeTextView = view.findViewById(R.id.notificationBadgeTextView);

            notificationUController = new NotificationUController();
            notificationUController.fetchNotifications(userId, new Notification.OnNotificationsFetchedListener() {
                @Override
                public void onNotificationsFetched(List<Notification> notifications) {
                    // Notifications can be processed if needed

                    // After fetching notifications, count them
                    notificationUController.countNotifications(userId, new Notification.OnNotificationCountFetchedListener() {
                        @Override
                        public void onCountFetched(int count) {
                            if (count > 0) {
                                notificationBadgeTextView.setText(String.valueOf(count));
                                notificationBadgeTextView.setVisibility(View.VISIBLE);
                            } else {
                                notificationBadgeTextView.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });

        }

        ImageView notiImage = view.findViewById(R.id.noti_icon);
        notiImage.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new NotificationUFragment())
                    .addToBackStack(null)
                    .commit();

        });

        Button button_all_recipes = view.findViewById(R.id.button_all_recipes);
        Button button_vegetarian = view.findViewById(R.id.button_vegetarian);
        Button button_favourite = view.findViewById(R.id.button_favourite);
        Button button_personalise_recipes = view.findViewById(R.id.button_personalise);
        Button button_recipes_status = view.findViewById(R.id.button_recipes_status);
        Button button_recommendedRecipes = view.findViewById(R.id.button_recommendRecipes);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recipe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the recipe list and adapter
        recipeList = new ArrayList<>();
        APIRecipeList = new ArrayList<>();
        recipeNewList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(recipeList, this::openRecipeDetailFragment, false);
        recyclerView.setAdapter(recipeAdapter);

//        fetchRecipes(getRandomSimpleFoodSearch(), null, null);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Start fetching recipes after the delay
            fetchRecipesFromFavourite();
        }, 1000);

        searchEditText = view.findViewById(R.id.search_recipe);
        // Setup spinners
        mealTypeSpinner = view.findViewById(R.id.spinner_meal_type);
        dishTypeSpinner = view.findViewById(R.id.spinner_dish_type);
        setupSpinners(); // Call to setup spinners

        // Call the setup methods for listeners
        setupSpinnerListeners(); // Call to setup spinner listeners
        setupSearchBar(); // Call to setup search bar listenersv

        if (getArguments() != null) {
            String savedSearchQuery = getArguments().getString("search_query", "");
            int savedMealTypePos = getArguments().getInt("spinner1_value", 0);
            int savedDishTypePos = getArguments().getInt("spinner2_value", 0);

            // Restore the saved search query and spinner selections
            searchEditText.setText(savedSearchQuery);
            mealTypeSpinner.setSelection(savedMealTypePos);
            dishTypeSpinner.setSelection(savedDishTypePos);

            // Apply the filters with the restored values
            filterRecipes();

        } else {
            fetchFavoriteRecipes("", null,null);
        }

        // Set up button click listeners
        button_all_recipes.setOnClickListener(v -> navigateToFragment(new NavAllRecipesFragment()));
        button_vegetarian.setOnClickListener(v -> navigateToFragment(new NavVegetarianRecipesFragment()));
        button_favourite.setOnClickListener(v -> navigateToFragment(new ViewFavouriteRecipesFragment()));
        button_personalise_recipes.setOnClickListener(v -> navigateToFragment(new NavCommunityRecipesFragment()));
        button_recipes_status.setOnClickListener(v -> navigateToFragment(new NavPendingRecipesFragment()));
        button_recommendedRecipes.setOnClickListener(v -> navigateToFragment(new NavRecommendedRecipesFragment()));

        // Clear filters button logic
        Button clearFiltersButton = view.findViewById(R.id.clear_filters_button);
        clearFiltersButton.setOnClickListener(v -> clearFiltersAndFetchRandomRecipes());

        return view;
    }

    private void setupSpinners() {
        // Set up meal type spinner
        ArrayAdapter<String> mealTypeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, mealTypes);
        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(mealTypeAdapter);

        // Set up dish type spinner
        ArrayAdapter<String> dishTypeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, dishTypes);
        dishTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dishTypeSpinner.setAdapter(dishTypeAdapter);
    }

    private void filterRecipes() {
        Log.d("FilterRecipes", "filterRecipes called");

        filteredList.clear();

        String searchQuery = searchEditText.getText().toString().trim().toLowerCase();
        String selectedMealType = mealTypeSpinner.getSelectedItem() != null ? mealTypeSpinner.getSelectedItem().toString() : "--Select Meal Type--";
        String selectedDishType = dishTypeSpinner.getSelectedItem() != null ? dishTypeSpinner.getSelectedItem().toString() : "--Select Dish Type--";

        Log.d("FilterRecipes", "Search Query: " + searchQuery);
        Log.d("FilterRecipes", "Selected Meal Type: " + selectedMealType);
        Log.d("FilterRecipes", "Selected Dish Type: " + selectedDishType);

        if (searchQuery.isEmpty()) {
            // If the query is empty, reset to the original list
            filteredList.addAll(recipeList);
        }
        else{
            for (Recipe recipe : recipeList) {
                boolean matchesSearchQuery = searchQuery.isEmpty() || recipe.getLabel().toLowerCase().contains(searchQuery);
                boolean matchesMealType = selectedMealType.equals("--Select Meal Type--") || recipe.getMealType().equals(selectedMealType);
                boolean matchesDishType = selectedDishType.equals("--Select Dish Type--") || recipe.getDishType().equals(selectedDishType);

                Log.d("FilterRecipes", "Recipe: " + recipe.getLabel() + ", Matches: " + matchesSearchQuery + ", " + matchesMealType + ", " + matchesDishType);

                if (matchesSearchQuery && matchesMealType && matchesDishType) {
                    filteredList.add(recipe);
                }
            }
        }

        Log.d("FilterRecipes", "Filtered List Size: " + filteredList.size());

        recipeAdapter.updateRecipeList(filteredList);
        recipeAdapter.notifyDataSetChanged();
    }



    private void setupSpinnerListeners() {
        mealTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Call filterRecipes when meal type changes
                filterRecipes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        dishTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Call filterRecipes when dish type changes
                filterRecipes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSearchBar() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecipes(); // Call filterRecipes whenever the text changes
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void clearFiltersAndFetchRandomRecipes() {
        // Reset the spinners to default selections
        mealTypeSpinner.setSelection(0); // Assuming the first position is the default
        dishTypeSpinner.setSelection(0); // Assuming the first position is the default

        // Clear the search bar
        searchEditText.setText(""); // This will clear the search bar

        fetchFavoriteRecipes("", null, null);
    }

    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRecipeDetailFragment(Recipe recipe) {
        // From NavAllRecipesFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("selected_recipe", recipe);  // Assuming selectedRecipe is the clicked recipe object
        bundle.putString("source", "fav");  // Pass "all" as the source
        bundle.putString("search_query", searchEditText.getText().toString());  // Pass the search query
        bundle.putInt("spinner1_value", mealTypeSpinner.getSelectedItemPosition());  // Pass the selected position of spinner1
        bundle.putInt("spinner2_value", dishTypeSpinner.getSelectedItemPosition());  // Pass the selected position of spinner2

        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, recipeDetailFragment)
                .addToBackStack(null)
                .commit();

    }

    private void fetchFavoriteRecipes(String query, String mealType, String dishType) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            NavFavouriteRecipesController controller = new NavFavouriteRecipesController();
            controller.retrieveFavoriteRecipes(userId, getContext(), this);
            // Pass 'this' to the controller
        } else {
            Toast.makeText(getContext(), "User is not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFavoriteRecipesRetrieved(ArrayList<Recipe> recipes) {
        recipeList.clear(); // Clear previous recipes
        recipeList.addAll(recipes); // Add new recipes

        // Notify the adapter about data changes
//        recipeAdapter.notifyDataSetChanged();

//        Toast.makeText(getContext(), "Recipes retrieved successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Exception e) {
        // Handle error
        Toast.makeText(getContext(), "Failed to retrieve recipes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void fetchRecipesFromFavourite() {
        // Clear previous recipe lists
        APIRecipeList.clear();
        recipeNewList.clear();

        String app_id = "2c7710ea"; // Your Edamam API app ID
        String app_key = "97f5e9187c865600f74e2baa358a9efb";
        String type = "public";

        // Fetch favorite recipes
//        fetchFavoriteRecipes(null, null, null);

        // Initialize AtomicInteger to track completed requests
        AtomicInteger completedRequests = new AtomicInteger(0);
        int totalRecipes = recipeList.size(); // Total number of recipes to fetch

        // Log the number of recipes being fetched
        Log.d("Fetch Recipes", "Total recipes to fetch: " + totalRecipes);


        // Iterate through each recipe label
        for (Recipe recipe : recipeList) {
            String labelQuery = recipe.getLabel(); // Use the label from the current recipe
            Log.d("API Call", "Fetching recipes for: " + labelQuery);

            EdamamApi api = ApiClient.getRetrofitInstance().create(EdamamApi.class);

            // Call the API to fetch recipes based on the label
            Call<RecipeResponse> call = api.searchRecipes(labelQuery, app_id, app_key, type, null, null, null, null);

            call.enqueue(new Callback<RecipeResponse>() {
                @Override
                public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<RecipeResponse.Hit> hits = response.body().getHits(); // Get hits from response

                        // Log the number of recipes fetched
                        Log.d("Fetched Recipes", "Number of recipes fetched for " + labelQuery + ": " + hits.size());

                        for (RecipeResponse.Hit hit : hits) {
                            Recipe apiRecipe = hit.getRecipe(); // Extract the Recipe from Hit

                            // Calculate calories per 100g
                            double caloriesPer100g = apiRecipe.getCaloriesPer100g();
                            if (apiRecipe.getTotalWeight() > 0) {
                                caloriesPer100g = (apiRecipe.getCalories() / apiRecipe.getTotalWeight()) * 100;
                            }
                            apiRecipe.setCaloriesPer100g(caloriesPer100g); // Update recipe object

                            APIRecipeList.add(apiRecipe); // Add to the APIRecipeList
                        }

                        // Compare the retrieved recipes with the user's favorite recipes
                        for (Recipe favoriteRecipe : recipeList) {
                            for (Recipe apiRecipe : APIRecipeList) {
                                // Compare by label
                                if (favoriteRecipe.getLabel().equals(apiRecipe.getLabel())) {
                                    Log.d("Matching Recipe:", favoriteRecipe.getLabel());
                                    recipeNewList.add(apiRecipe); // Add matching recipe to the new list
                                    break; // Exit inner loop once a match is found
                                } else {
                                    Log.d("No Match Found:", favoriteRecipe.getLabel() + " vs " + apiRecipe.getLabel());
                                }
                            }
                        }
                    } else {
                        Log.d("Fetch Recipes", "Response was not successful or body is null. Code: " + response.code());
                    }

                    // Increment completed requests
                    if (completedRequests.incrementAndGet() == totalRecipes) {

                        // Update the UI after all requests have completed
                        recipeList.clear();
                        recipeList.addAll(recipeNewList);
                        HashSet<Recipe> recipeSet = new HashSet<>(recipeList);
                        recipeList.clear();
                        recipeList.addAll(recipeSet);
                        Log.d("Recipe List Size", "Size before notify: " + recipeList.size());
                        recipeAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<RecipeResponse> call, Throwable t) {
                    Log.e("Fetch Recipes", "Error: " + t.getMessage());
                    // Increment completed requests even if there was a failure
                    if (completedRequests.incrementAndGet() == totalRecipes) {
                        // Update the UI in case of failure as well
                        recipeList.clear();
                        recipeList.addAll(recipeNewList);
                        Log.d("Recipe List Size", "Size before notify: " + recipeList.size());
                        recipeAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public void displayFavouriteRecipes() {
        // Ensure recipeNewList is initialized
        if (recipeNewList == null) {
            recipeNewList = new ArrayList<>(); // Initialize if null
        } else {
            recipeNewList.clear(); // Clear if already initialized
        }

        for (Recipe apiRecipe : APIRecipeList) {
            if (apiRecipe.getImage() != null && !apiRecipe.getImage().isEmpty()) {
                Log.d(TAG, "Image URL: " + apiRecipe.getImage());
            } else {
                Log.d(TAG, "No image for recipe: " + apiRecipe.getLabel());
            }
        }
        recipeList.clear();

        // Compare the two recipe lists (recipeList and APIRecipeList)
        for (Recipe recipe1 : recipeList) {
            for (Recipe recipe2 : APIRecipeList) {
                // Compare by both label and calories
                if (recipe1.getLabel().equals(recipe2.getLabel())) {
                    recipeList.add(recipe2); // Add matching recipe to the new list
                    break;  // Exit inner loop once a match is found
                }
            }
        }

        // Notify the adapter that the data has changed
        recipeAdapter.notifyDataSetChanged();
    }


    private String getRandomSimpleFoodSearch() {
        if (!simpleFoodSearches.isEmpty()) {
            return simpleFoodSearches.get(random.nextInt(simpleFoodSearches.size()));
        } else {
            Log.w("Random Search", "Simple food searches list is empty!");
            return ""; // Return an empty string or handle it appropriately
        }
    }

}