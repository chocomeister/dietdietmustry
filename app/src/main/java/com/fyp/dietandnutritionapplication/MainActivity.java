package com.fyp.dietandnutritionapplication;


import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.fyp.dietandnutritionapplication.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean isAdminMode = false;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ViewUserProfileController viewUserProfileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        viewUserProfileController = new ViewUserProfileController(this);

        // Example of fetching user role
        FirebaseUser user = mAuth.getCurrentUser();
        String userRole = "guest"; // Default role


        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scheduleWorkManager();

        // Navigate based on user role
        switch (userRole) {
            case "admin":
                hideBottomNavigationView();
                replaceFragment(new LoginFragment());
                break;

            default: // guest or undefined role
                setupGuestNavigation();
                replaceFragment(new LoginFragment());
                break;
        }
    }

    public void switchToAdminMode() {
        isAdminMode = true;
        setupAdminNavigation();
        showBottomNavigationView();
        replaceFragment(new AdminHomeFragment());
    }

    public void switchToGuestMode() {
        isAdminMode = false;
        setupGuestNavigation();
        replaceFragment(new LandingFragment());
    }

    public void switchToUserMode() {
        isAdminMode = false;
        setupUserNavigation();
        replaceFragment(new userHomePageFragment());
    }

    public void switchToNutriMode() {
        isAdminMode = false;
        setupNutriNavigation();
        replaceFragment(new NutriHomeFragment());
    }

    private void setupAdminNavigation() {
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().clear();
        binding.bottomNavigationView.inflateMenu(R.menu.admin_bottom_menu);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.adminhome:
                    replaceFragment(new AdminHomeFragment());
                    break;
                case R.id.viewallaccounts:
                    replaceFragment(new viewAccountsFragment());
                    break;
                case R.id.addfaqpage:
                    replaceFragment(new FAQFragment());
                    break;
                case R.id.adminviewprofile:
                    replaceFragment(new ProfileAFragment());
                    break;
            }
            return true;
        });
    }

    private void setupUserNavigation() {
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().clear();
        binding.bottomNavigationView.inflateMenu(R.menu.user_bottom_menu);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.userHomepage:
                    replaceFragment(new userHomePageFragment());
                    break;
                case R.id.recipe:
                    replaceFragment(new navCreateFolderFragment());
                    break;
                case R.id.consultations:
                    replaceFragment(new ConsultationsUFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileUFragment());
                    break;
            }
            return true;
        });
    }

    public void hideBottomNavigationView() {
        binding.bottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomNavigationView() {
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }


    private void setupGuestNavigation() {
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().clear();
        binding.bottomNavigationView.inflateMenu(R.menu.guest_bottom_menu);


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.landing:
                    replaceFragment(new LoginFragment());
                    break;
                case R.id.recipe:
                    replaceFragment(new navGuestRecipesFolderFragment());
                    break;
                case R.id.meallog:
                    replaceFragment(new MealLogPreviewFragment());
                    break;
                case R.id.reviews:
                    replaceFragment(new AppReviewsFragment());
                    break;
            }
            return true;
        });
    }

    private void setupNutriNavigation() {
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().clear();
        binding.bottomNavigationView.inflateMenu(R.menu.nutri_bottom_menu);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nutriHome:
                    replaceFragment(new NutriHomeFragment());
                    break;
                case R.id.recipe:
                    replaceFragment(new NutriAllRecipesFragment());
                    break;
//                case R.id.addRecipe: // Link API
//                    replaceFragment(new AddRecipeFragment());
//                    break;
                case R.id.bookingPage:
                    replaceFragment(new PendingConsultationFragment());
                    break;
                case R.id.NutriViewProfile:
                    replaceFragment(new NutriViewProfileFragment());
                    break;
            }
            return true;
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void scheduleWorkManager() {
        // Schedule WorkManager to run every hour
        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.Builder(UpdateConsultationStatusWorker.class, 1, TimeUnit.HOURS)
                        .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
    }
    
}