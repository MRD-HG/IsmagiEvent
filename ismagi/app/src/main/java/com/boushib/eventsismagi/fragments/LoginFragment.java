package com.boushib.eventsismagi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.boushib.eventsismagi.DashboardActivity;
import com.boushib.eventsismagi.MainActivity;
import com.boushib.eventsismagi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {
    private EditText emailEditText, passwordEditText;
    private Button redirectToRegisterButton; // Changed from TextView to Button
    private Button loginButton;
    private FirebaseAuth auth;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        auth = FirebaseAuth.getInstance();

        emailEditText = view.findViewById(R.id.loginEmail);
        passwordEditText = view.findViewById(R.id.passwordEmail);
        loginButton = view.findViewById(R.id.btnConnexion);
        redirectToRegisterButton = view.findViewById(R.id.btncree); // Correctly cast as Button

        loginButton.setOnClickListener(v -> loginUser());

        redirectToRegisterButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.toregister);
        });

        return view;
    }
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Auth login
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Fetch user role from Firestore
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("utilisateurs").document(userId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String role = documentSnapshot.getString("role");
                                            redirectBasedOnRole(role); // Redirect based on role
                                        } else {
                                            Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Failed to fetch user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(getContext(), "Erreur de connexion: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectBasedOnRole(String role) {
        Intent intent;
        if (role != null) {
            switch (role) {
                case "student":
                    intent = new Intent(getActivity(), MainActivity.class);

                    break;
                case "admin":
                    intent = new Intent(getActivity(), DashboardActivity.class);

                    break;
                default:
                    Toast.makeText(getContext(), "Role not recognized", Toast.LENGTH_SHORT).show();
                    return;
            }
            startActivity(intent);
            getActivity().finish(); // Close the current activity
        } else {
            Toast.makeText(getContext(), "Role not found", Toast.LENGTH_SHORT).show();
        }
    }

   /* private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }*/

        // Firebase Auth login
     /*   auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Redirect to home fragment after successful login
                       *//* Navigation.findNavController(requireView()).navigate(R.id.to_home_fragment);*//*

                        FirebaseUser user = auth.getCurrentUser();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(getContext(), "error de connexion "+errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });*/
    }
