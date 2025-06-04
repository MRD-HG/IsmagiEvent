package com.boushib.eventsismagi.fragments.userfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.model.Event;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

/*public class EventsDetailscopi extends Fragment {
    private String eventId;

    public EventsDetailscopi() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupérer l'ID de l'événement depuis les arguments
        if (getArguments() != null) {
            this.eventId = getArguments().getString("eventId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater le layout du fragment
        return inflater.inflate(R.layout.fragment_events_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);







        Button backButton = view.findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            // Naviguer vers HomeFragment
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.navigation_home);
        });


        if (eventId != null) {
            loadEventDetails(eventId);
        } else {
            Toast.makeText(requireContext(), "ID de l'événement non trouvé", Toast.LENGTH_SHORT).show();
        }

        if (eventId != null) {
            loadEventDetails(eventId);
        } else {
            Toast.makeText(requireContext(), "ID de l'événement non trouvé", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadEventDetails(String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("evenements").document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        if (event != null) {
                            // Afficher les détails de l'événement dans l'UI
                            displayEventDetails(event);
                        }
                    } else {
                        Toast.makeText(requireContext(), "Événement non trouvé", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Erreur lors du chargement des détails", Toast.LENGTH_SHORT).show();
                });
    }

    private void displayEventDetails(Event event) {
        // Initialiser les vues
        View view = requireView();
        TextView titre = view.findViewById(R.id.titredetials);
        TextView description = view.findViewById(R.id.descriptiondetials);
        ImageView image = view.findViewById(R.id.imageEventdetials);

        // Afficher les détails de l'événement
        titre.setText(event.getTitre());
        description.setText(event.getDescription());

        // Charger l'image avec Glide
        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            String imageUrl = event.getImageUrl();
            if (imageUrl.startsWith("http://")) {
                imageUrl = imageUrl.replace("http://", "https://");
            }



            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(image);
        } else {
            image.setImageResource(R.drawable.placeholder); // Image par défaut
        }
    }
}*/