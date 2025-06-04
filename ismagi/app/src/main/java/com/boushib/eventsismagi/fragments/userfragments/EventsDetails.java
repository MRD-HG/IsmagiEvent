package com.boushib.eventsismagi.fragments.userfragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.model.Event;
import com.boushib.eventsismagi.model.Reservation;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class EventsDetails extends Fragment {


    String[] item={"Payer avec carte", "Payer avec especes"};
    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String>  arrayAdapter;

    private String selectedPaymentMethod;
    private String eventId;

    public EventsDetails() {
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

        Button reserveButton = view.findViewById(R.id.btnreserver);
        reserveButton.setOnClickListener(v -> {
            // Afficher le pop-up de réservation
            showReservationDialog();
        });






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
        TextView lieuDeEvenments = view.findViewById(R.id.lieudetials);
        TextView dateDeEvenment = view.findViewById(R.id.dateEventdetials);
        TextView heruredeEvenment = view.findViewById(R.id.heureEventdetials);
        TextView prixDeEvents = view.findViewById(R.id.prixdetials);
        ImageView image = view.findViewById(R.id.imageEventdetials);

        // Afficher les détails de l'événement
        titre.setText(event.getTitre());
        description.setText(event.getDescription());
        lieuDeEvenments.setText(event.getLieu());
       dateDeEvenment.setText(event.getDateEvent());
        heruredeEvenment.setText(event.getHeureEvent());
        //  dateDeEvenment.setText(String.valueOf(event.getDateEvent()));
        // heruredeEvenment.setText(String.valueOf(event.getHeureEvent()));
        prixDeEvents.setText(String.valueOf(event.getPrix()));

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


    private void showReservationDialog() {
        // Créer une boîte de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reserve, null);
        builder.setView(dialogView);

        AutoCompleteTextView paymentDropdown = dialogView.findViewById(R.id.paymentDropdown);
        String[] paymentMethods = {"Payer avec carte", "Payer avec espèces"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                paymentMethods
        );

        paymentDropdown.setAdapter(adapter);

        paymentDropdown.setOnItemClickListener((parent, view, position, id) -> {
            selectedPaymentMethod = paymentMethods[position];
            Log.d("Payment", "Méthode sélectionnée: " + selectedPaymentMethod);

        });

        // Récupérer les champs de saisie
        EditText inputName = dialogView.findViewById(R.id.inputName);
        EditText inputEmail = dialogView.findViewById(R.id.inputEmail);
        EditText inputPlaces = dialogView.findViewById(R.id.inputPlaces);

        // Créer la boîte de dialogue
        AlertDialog dialog = builder.create();

        // Gérer le clic sur le bouton "Confirmer"
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            // Récupérer les valeurs saisies
            String name = inputName.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String places = inputPlaces.getText().toString().trim();

            // Valider les champs
            if (name.isEmpty() || email.isEmpty() || places.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enregistrer la réservation (exemple : enregistrer dans Firestore)
            saveReservation(name, email, places);

            // Fermer la boîte de dialogue
            dialog.dismiss();
        });

        // Gérer le clic sur le bouton "Annuler"
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Afficher la boîte de dialogue
        dialog.show();
    }

    private void saveReservation(String name, String email, String places) {
        // Exemple : Enregistrer la réservation dans Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (selectedPaymentMethod == null) {
            Toast.makeText(requireContext(), "Veuillez sélectionner un mode de paiement", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isCardPayment = selectedPaymentMethod.equals("Payer avec carte");

        Reservation reservation = new Reservation(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                eventId,
                isCardPayment ? "carte" : "especes",
                isCardPayment,
                "1234", // sera rempli après paiement réussi
                new Date(),
                email,
                Integer.parseInt(places)
        );

        db.collection("reservations")
                .add(reservation)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(requireContext(), "Réservation réussie !", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Erreur lors de la réservation", Toast.LENGTH_SHORT).show();
                });
    }
}