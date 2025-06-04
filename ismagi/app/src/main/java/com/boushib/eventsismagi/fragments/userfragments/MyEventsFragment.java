package com.boushib.eventsismagi.fragments.userfragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
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

public class MyEventsFragment extends Fragment implements EventAdapterMyEventsClient.OnItemClickListener {

    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private EventAdapterMyEventsClient adapter;
    private List<MyEventsReservation> reservationList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        recyclerView = view.findViewById(R.id.recyclerEventsClients);
        emptyView = view.findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reservationList = new ArrayList<>();

        // Pass three parameters: reservationList, listener (this), and activity
        adapter = new EventAdapterMyEventsClient(
                reservationList,
                this,
                requireActivity());

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadMyReservations();

        return view;
    }

    private void loadMyReservations() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            showEmptyView(true);
            return;
        }

        db.collection("reservations")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reservationList.clear();

                        if (task.getResult().isEmpty()) {
                            showEmptyView(true);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            MyEventsReservation reservation = doc.toObject(MyEventsReservation.class);
                            fetchEventDetails(reservation);
                        }
                    } else {
                        showEmptyView(true);
                        Toast.makeText(getContext(), "Erreur lors du chargement des réservations", Toast.LENGTH_SHORT).show();
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
                        reservation.setImageUrl(doc.getString("imageUrl"));

                        // Gestion sécurisée du prix
                        Object prixValue = doc.get("prix");
                        if (prixValue instanceof Number) {
                            reservation.setPrix(((Number) prixValue).doubleValue());
                        } else {
                            reservation.setPrix(0.0);
                        }

                        reservationList.add(reservation);
                        adapter.notifyItemInserted(reservationList.size() - 1);
                        showEmptyView(reservationList.isEmpty());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Erreur lors du chargement des réservations", Toast.LENGTH_SHORT).show();
                    showEmptyView(reservationList.isEmpty());
                });
    }

    private void showEmptyView(boolean show) {
        if (getView() == null) return;

        if (show) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(MyEventsReservation reservation) {
        // Handle item click
    }

    @Override
    public void onRequestPermission() {
        // Handle permission request
        if (getActivity() != null) {
           // ((Activity)getActivity()).requestPermissions;
        }
    }
}