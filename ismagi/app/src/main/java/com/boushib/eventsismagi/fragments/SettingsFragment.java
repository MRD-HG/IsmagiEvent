package com.boushib.eventsismagi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class SettingsFragment extends Fragment {

    private Button btnQuitte;
    private TextView tvUserName, tvUserEmail;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialisation des composants
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btnQuitte = view.findViewById(R.id.btnQuitte);
        tvUserName = view.findViewById(R.id.profilename);
        tvUserEmail = view.findViewById(R.id.profileemail);

        // Charger les infos utilisateur
        loadUserData();

        // Gestion de la déconnexion
        btnQuitte.setOnClickListener(v -> signOut());

        return view;
    }

    private void loadUserData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // Afficher l'email directement depuis Firebase Auth
            tvUserEmail.setText(currentUser.getEmail());

            // Récupérer les autres infos depuis Firestore
            db.collection("utilisateurs")
                    .document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Convertir le document en AdminModel
                                AdminModel admin = document.toObject(AdminModel.class);
                                if (admin != null) {
                                    admin.setId(document.getId());
                                    tvUserName.setText(admin.getNom());
                                    tvUserEmail.setText(admin.getEmail());
                                }
                            } else {
                                tvUserName.setText(currentUser.getDisplayName() != null ?
                                        currentUser.getDisplayName() : "Utilisateur");
                            }
                        } else {
                            Toast.makeText(getContext(), "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Si aucun utilisateur connecté, rediriger vers le login
            Navigation.findNavController(requireView()).navigate(R.id.tologin);
        }
    }

    private void signOut() {
        auth.signOut();
        Toast.makeText(getContext(), "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).navigate(R.id.tologin);
    }
}