package com.boushib.eventsismagi.fragments.adminfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.model.AdminModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingDashboardFragment extends Fragment {

    private TextView  profileAdminDashboard, profileAdminEmail;
    private ImageView imageViewAdmin;
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_dashboard, container, false);

        // Initialisation des vues
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        profileAdminDashboard = view.findViewById(R.id.profileadmindasborad);
        profileAdminEmail = view.findViewById(R.id.profileadmin);
        imageViewAdmin = view.findViewById(R.id.imageEventadmin);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Charger les données de l'admin
        loadAdminData();

        // Gestion de la déconnexion
        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void loadAdminData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Récupérer les données depuis Firestore
            db.collection("utilisateurs")
                    .document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                AdminModel admin = document.toObject(AdminModel.class);
                                if (admin != null) {

                                    profileAdminDashboard.setText(admin.getNom());
                                    profileAdminEmail.setText(admin.getEmail());

                                }
                            } else {
                                showError("Document admin non trouvé");
                            }
                        } else {
                            showError("Erreur de chargement des données");
                        }
                    });
        } else {
            redirectToLogin();
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        redirectToLogin();
    }

    private void redirectToLogin() {
        if (getView() != null) {
            Navigation.findNavController(requireView()).navigate(R.id.action_settingsFragment_to_loginFragment);
        }
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }
}