package com.example.dietandnutritionapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class userHomePageFragment extends Fragment {

    private FirebaseFirestore firestore;
    private double userCalorieGoal;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;

    private final Random random = new Random();

    private List<String> simpleFoodSearches = Arrays.asList(
            "chicken", "beef", "steak", "fish","lamb", "pasta","burger", "curry", "shrimp", "bacon", "fried", "grilled", "smoked", "salmon"
    );

    private List<String> mealtype = Arrays.asList(
            "lunch","dinner"
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.user_homepage, container, false);
        firestore = FirebaseFirestore.getInstance();
        fetchUserCalorieGoal();

        ImageView reviewIcon = view.findViewById(R.id.reviewIcon);
        ImageView logoutIcon = view.findViewById(R.id.logout_icon);

        // Initialize buttons using view.findViewById
        Button button_recipes = view.findViewById(R.id.button_recipes);
        Button button_mealLog = view.findViewById(R.id.button_MealLog);
        Button button_diary = view.findViewById(R.id.diary);
        Button button_bmiCalculator = view.findViewById(R.id.bmiCalculator);
        Button button_consultation = view.findViewById(R.id.consultation);
        Button button_healthReport = view.findViewById(R.id.healthReport);
        Button button_faq = view.findViewById(R.id.FAQ);
        Button button_profile = view.findViewById(R.id.profile);
        Button button_mealLog1 = view.findViewById(R.id.button_MealLog1);

        recyclerView = view.findViewById(R.id.recipeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the recipe list and adapter
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(recipeList, this::openRecipeDetailFragment, false);
        recyclerView.setAdapter(recipeAdapter);

        // Fetch recipes
        fetchRecipes(getRandomSimpleFoodSearch(), getRandomSMealType(), null);

        // Set up button click listeners to navigate between fragments
        button_recipes.setOnClickListener(v -> {
            // Replace current fragment with NavAllRecipesFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new navCreateFolderFragment())
                    .addToBackStack(null)  // Add to back stack to enable back navigation
                    .commit();
        });
        reviewIcon.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new userReviewAppFragment());
            }
        });
        logoutIcon.setOnClickListener(v -> {

            if (getActivity() instanceof MainActivity) {
                Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).switchToGuestMode();
                ((MainActivity) getActivity()).replaceFragment(new LandingFragment());
            }
        });

        // Define the OnClickListener
        View.OnClickListener buttonClickListener = v -> {
            // Replace current fragment with MealLogPreviewFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new MealLogUFragment())
                    .addToBackStack(null)
                    .commit();
        };

// Assign the OnClickListener to both buttons
        button_mealLog.setOnClickListener(buttonClickListener);
        button_mealLog1.setOnClickListener(buttonClickListener);

        button_diary.setOnClickListener(v -> {
           // Replace current fragment with NavFavouriteRecipesFragment
           requireActivity().getSupportFragmentManager().beginTransaction()
                   .replace(R.id.frame_layout, new UserDiaryFragment())
                    .addToBackStack(null)
                   .commit();
       });

        button_bmiCalculator.setOnClickListener(v -> {
            // Replace current fragment with NavRecipesStatusFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new BMICalculatorFragment())
                    .addToBackStack(null)
                    .commit();
        });

        button_consultation.setOnClickListener(v -> {
           // Replace current fragment with NavConsultationFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new ConsultationsUFragment())
                .addToBackStack(null)
                .commit();
        });

        button_healthReport.setOnClickListener(v -> {
            // Replace current fragment with NavHealthReportFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new HealthReportFragment())
                    .addToBackStack(null)
                    .commit();
        });

        button_faq.setOnClickListener(v -> {
            // Replace current fragment with NavFAQFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new userViewFAQFragment())
                    .addToBackStack(null)
                    .commit();
        });

        button_profile.setOnClickListener(v -> {
            // Replace current fragment with NavProfileFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new ProfileUFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void fetchUserCalorieGoal() {
        String userId = getUserId(); // Assume you have a method to retrieve the current user's ID
        firestore.collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userCalorieGoal = documentSnapshot.getDouble("calorieLimit");
                        Log.d("Calorie Goal", "User's calorie goal: " + userCalorieGoal);
                    } else {
                        Log.w("Calorie Goal", "User document not found");
                    }
                })
                .addOnFailureListener(e -> Log.e("Calorie Goal", "Failed to retrieve calorie goal", e));
    }

    private String getUserId() {
        // Return the current user's ID, possibly from FirebaseAuth or another source
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void openRecipeDetailFragment(Recipe recipe) {
        // From NavAllRecipesFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("selected_recipe", recipe);  // Assuming selectedRecipe is the clicked recipe object
        bundle.putString("source", "recommended");  // Pass "all" as the source


        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, recipeDetailFragment)
                .addToBackStack(null)
                .commit();

    }

    private void fetchRecipes(String query, String mealType, String dishType) {
        String app_id = "2c7710ea"; // Your Edamam API app ID
        String app_key = "97f5e9187c865600f74e2baa358a9efb";
        String type = "public";

        EdamamApi api = ApiClient.getRetrofitInstance().create(EdamamApi.class);

        Call<RecipeResponse> call = api.searchRecipes(query, app_id, app_key, type, null, mealType, dishType, null);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RecipeResponse.Hit> hits = response.body().getHits();

                    Log.d("Fetched Recipes", "Number of recipes fetched: " + hits.size());

                    // Clear previous recipes
                    recipeList.clear();

                    // Limit to 3 recipes
                    int count = 0;

                    for (RecipeResponse.Hit hit : hits) {
                        if (count >= 3) {
                            break; // Exit the loop after fetching 3 recipes
                        }

                        Recipe recipe = hit.getRecipe();
                        double caloriesPer100g = recipe.getCaloriesPer100g();

                        if (recipe.getTotalWeight() > 0) {
                            caloriesPer100g = (recipe.getCalories() / recipe.getTotalWeight()) * 100;
                        }

                        recipe.setCaloriesPer100g(caloriesPer100g);

                        // Filter recipes based on user's calorie goal
                        if (recipe.getCalories() <= userCalorieGoal) {
                            recipeList.add(recipe); // Add only recipes that meet the calorie goal
                            count++; // Increment the count of recipes added
                        }
                    }

                    recipeAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Fetch Recipes", "Response was not successful or body is null. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.e("Fetch Recipes", "Error: " + t.getMessage());
            }
        });
    }

    private String getRandomSimpleFoodSearch() {
        if (!simpleFoodSearches.isEmpty()) {
            return simpleFoodSearches.get(random.nextInt(simpleFoodSearches.size()));
        } else {
            Log.w("Random Search", "Simple food searches list is empty!");
            return ""; // Return an empty string or handle it appropriately
        }
    }

    private String getRandomSMealType() {
        if (!mealtype.isEmpty()) {
            return mealtype.get(random.nextInt(mealtype.size()));
        } else {
            Log.w("Random Search", "Simple food searches list is empty!");
            return ""; // Return an empty string or handle it appropriately
        }
    }
}
