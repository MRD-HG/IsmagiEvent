package com.boushib.eventsismagi.fragments.userfragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.adapter.EventAdapterMyEventsClient;
import com.boushib.eventsismagi.model.MyEventsReservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


/*public class MyEventsFragmentwork extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapterMyEventsClient adapter;
    private List<MyEventsReservation> reservationList;
    private FirebaseFirestore db;

    private static final String TAG = "MyEventsFragment";
    // ... autres déclarations

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        // Initialisation Firebase
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), "Veuillez vous connecter", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recyclerEventsClients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reservationList = new ArrayList<>();
        adapter = new EventAdapterMyEventsClient(reservationList, this::onReservationClicked);
        recyclerView.setAdapter(adapter);

        // Charger les données
        loadMyReservations(user.getUid());

        return view;
    }

    private void loadMyReservations(String userId) {
        db.collection("reservations")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            Log.d(TAG, "Aucune réservation trouvée");
                            return;
                        }

                        reservationList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            MyEventsReservation reservation = doc.toObject(MyEventsReservation.class);
                            fetchEventDetails(reservation);
                        }
                    } else {
                        Log.e(TAG, "Erreur Firestore", task.getException());
                    }
                });
    }

    private void fetchEventDetails(MyEventsReservation reservation) {
        db.collection("evenements").document(reservation.getEventId())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        reservation.setTitre(doc.getString("titre"));
                        reservation.setLieu(doc.getString("lieu"));
                        reservation.setDateEvent(doc.getString("dateEvent"));
                      //  reservation.setPrix(doc.getDouble("prix"));
                        reservation.setImageUrl(doc.getString("imageUrl"));

                        reservationList.add(reservation);
                        adapter.notifyItemInserted(reservationList.size() - 1);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Échec du chargement de l'événement", e);
                    e.printStackTrace(); // Affiche la stack trace complète
                    Toast.makeText(getContext(), "Erreur: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }



                );
    }

    private void onReservationClicked(MyEventsReservation reservation) {
        // Gestion du clic
    }
}*/