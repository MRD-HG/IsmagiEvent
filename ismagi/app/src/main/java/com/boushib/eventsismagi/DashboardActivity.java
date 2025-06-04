package com.boushib.eventsismagi;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.boushib.eventsismagi.databinding.ActivityDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize BottomNavigationView
        bottomNavigationView = binding.bottomNavigationDashboard;

        // Get the NavHostFragment and NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container_dashboard);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // Set up BottomNavigationView with NavController
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Add a listener to update BottomNavigationView visibility based on the destination
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                // Hide BottomNavigationView for LoginFragment and RegisterFragment
                if (destination.getId() == R.id.loginFragment2 || destination.getId() == R.id.registerFragment2) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    // Show BottomNavigationView for other fragments
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            });

            // Check user authentication state
            if (mAuth.getCurrentUser() != null) {
                // User is logged in, navigate to Home
                navController.navigate(R.id.navigation_home_dashboard);
            } else {
                // User is not logged in, navigate to Login
                navController.navigate(R.id.loginFragment2);
            }
        } else {
            // Handle the case where NavHostFragment is not found
            throw new IllegalStateException("NavHostFragment not found in the layout.");
        }
    }
}