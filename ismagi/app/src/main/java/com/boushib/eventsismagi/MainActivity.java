package com.boushib.eventsismagi;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.boushib.eventsismagi.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Initialize BottomNavigationView
        bottomNavigationView = binding.bottomNavigation;

        // Get the NavHostFragment and NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // Set up BottomNavigationView with NavController
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Add a listener to update BottomNavigationView visibility based on the destination
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                // Hide BottomNavigationView for the LoginFragment
                if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.registerFragment){
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    // Show BottomNavigationView for other fragments
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            });

            // Check user authentication state
            if (mAuth.getCurrentUser() != null) {
                // User is logged in, navigate to Home
                navController.navigate(R.id.navigation_home);
               // checkUserRole(navController);
            } else {
                // User is not logged in, navigate to Login
                navController.navigate(R.id.loginFragment);
            }
        } else {
            // Handle the case where NavHostFragment is not found
            throw new IllegalStateException("NavHostFragment not found in the layout.");
        }
    }

    /*private void checkUserRole(NavController navController) {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("utilisateurs").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Check if user is admin
                            String isAdmin = document.getString("role");

                            if (Objects.equals(isAdmin, "admin")) {
                                // User is admin - navigate to dashboard
                                navController.navigate(R.id.tohomedahboard);
                            } else {
                                // Regular user - navigate to home
                                navController.navigate(R.id.navigation_home);
                            }
                        } else {
                            // User document doesn't exist - treat as regular user
                            navController.navigate(R.id.navigation_home);
                        }
                    } else {
                        // Error checking role - treat as regular user
                        navController.navigate(R.id.navigation_home);
                    }
                });
    }*/
}