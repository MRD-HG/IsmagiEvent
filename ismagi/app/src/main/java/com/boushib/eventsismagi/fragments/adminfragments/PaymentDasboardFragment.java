package com.boushib.eventsismagi.fragments.adminfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.adapter.ReservationAdapterDashboard;
import com.boushib.eventsismagi.model.Reservation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentDasboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReservationAdapterDashboard adapter;
    private List<Reservation> reservationList;
    private FirebaseFirestore db;

    public PaymentDasboardFragment() {
        // Required empty public constructor
    }

    public static PaymentDasboardFragment newInstance() {
        return new PaymentDasboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        reservationList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_dasboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewreservation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ReservationAdapterDashboard(reservationList, reservation -> {
            // Gestion du clic sur une réservation
            String paymentStatus = reservation.isPaymentValid() ? "validé" : "non validé";
            if ("especes".equals(reservation.getPaymentType())) {
                paymentStatus = "à payer sur place";
            }

            Toast.makeText(getContext(),
                    "Détails de la réservation:\n" +
                            "Événement: " + reservation.getEventId() + "\n" +
                            "Places: " + reservation.getPlaces() + "\n" +
                            "Email: " + reservation.getEmail() + "\n" +
                            "Paiement: " + reservation.getPaymentType() + " (" + paymentStatus + ")\n" +
                            "Date: " + (reservation.getReservationDate() != null ?
                            new SimpleDateFormat("dd/MM/yyyy HH:mm").format(reservation.getReservationDate()) : "N/A"),
                    Toast.LENGTH_LONG).show();
        });

        recyclerView.setAdapter(adapter);
        loadReservations();

        return view;
    }

    private void loadReservations() {
        db.collection("reservations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reservationList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reservation reservation = document.toObject(Reservation.class);
                            reservationList.add(reservation);
                        }
                        adapter.updateReservations(reservationList);
                    } else {
                        Toast.makeText(getContext(),
                                "Erreur lors du chargement des réservations: " +
                                        task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}