package com.boushib.eventsismagi.fragments;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;
import com.boushib.eventsismagi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private EditText emailEditText, passwordEditText, nomEditText;
    private Button registerButton,btnloginrediction;
    private FirebaseAuth auth;
    private FirebaseFirestore db;


    public static RegisterFragment newInstance() {

        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        emailEditText = view.findViewById(R.id.registerEmail);
        passwordEditText = view.findViewById(R.id.registerPassword);
        nomEditText = view.findViewById(R.id.noma);
        registerButton = view.findViewById(R.id.btnConnexion);
        btnloginrediction = view.findViewById(R.id.tologinrediction);

        registerButton.setOnClickListener(v -> registerUser());

        btnloginrediction.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.tologin);
        });

        return view;
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String nom = nomEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || nom.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création du compte avec Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Récupérer l'ID de l'utilisateur créé
                        String userId = auth.getCurrentUser().getUid();

                        // Ajouter les informations de l'utilisateur dans Firestore
                        Map<String, Object> utilisateur = new HashMap<>();
                        utilisateur.put("nom", nom);
                        utilisateur.put("email", email);
                        utilisateur.put("role", "student"); // Par défaut, l'utilisateur est un étudiant
                        utilisateur.put("dateInscription", new java.util.Date());

                        db.collection("utilisateurs").document(userId)
                                .set(utilisateur)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "Inscription réussie !", Toast.LENGTH_SHORT).show();
                                    // Redirection vers LoginFragment
                                    Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Erreur lors de l'ajout dans Firestore : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(getContext(), "Échec de l'inscription : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
