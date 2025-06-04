package com.boushib.eventsismagi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.model.Reservation;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReservationAdapterDashboard extends RecyclerView.Adapter<ReservationAdapterDashboard.DashboardReservationHolder> {

    private List<Reservation> reservationList;
    private final OnItemClickListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OnItemClickListener {
        void onItemClick(Reservation reservation);
    }

    public ReservationAdapterDashboard(List<Reservation> reservationList, OnItemClickListener listener) {
        this.reservationList = reservationList != null ? reservationList : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public DashboardReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_item, parent, false);
        return new DashboardReservationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardReservationHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.bind(reservation);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(reservation);
                Toast.makeText(v.getContext(),
                        "Réservation pour " + reservation.getPlaces() + " place(s)\n" +
                                "Email: " + reservation.getEmail(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public void updateReservations(List<Reservation> newReservations) {
        this.reservationList = newReservations != null ? newReservations : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class DashboardReservationHolder extends RecyclerView.ViewHolder {
        private final TextView tvEventTitle, tvAmount, tvDate, tvPaymentMethod, tvStatus, tvPlaces, tvEmail;

        public DashboardReservationHolder(@NonNull View itemView) {
            super(itemView);
            tvEventTitle = itemView.findViewById(R.id.evenmtservervation);
            tvAmount = itemView.findViewById(R.id.prixreservations);
            tvDate = itemView.findViewById(R.id.datereservation);
            tvPaymentMethod = itemView.findViewById(R.id.typedepamentsRevervation);
            tvStatus = itemView.findViewById(R.id.statusRevervation);
            tvPlaces = itemView.findViewById(R.id.placesreservation);
            tvEmail = itemView.findViewById(R.id.emailreservation);
        }

        public void bind(Reservation reservation) {
            // Vérification des null pour éviter les crashes
            tvEventTitle.setText(reservation.getEventId() != null ?
                    "Événement ID: " + reservation.getEventId() : "Événement non spécifié");

            tvPlaces.setText("Places: " + reservation.getPlaces());
            tvEmail.setText(reservation.getEmail() != null ?
                    "Email: " + reservation.getEmail() : "Email non spécifié");

            // Gestion du paiement
            String paymentType = reservation.getPaymentType() != null ?
                    reservation.getPaymentType() : "non spécifié";

            String paymentInfo = "Type: " + paymentType;
            if ("carte".equals(paymentType) && reservation.getCardLast4() != null) {
                paymentInfo += " (****" + reservation.getCardLast4() + ")";
            }
            tvPaymentMethod.setText(paymentInfo);

            // Statut du paiement
            String status;
            if ("especes".equals(paymentType)) {
                status = "À payer sur place";
            } else {
                status = reservation.isPaymentValid() ? "Payé" : "En attente";
            }
            tvStatus.setText("Statut: " + status);

            // Date de réservation
            if (reservation.getReservationDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                tvDate.setText("Date: " + dateFormat.format(reservation.getReservationDate()));
            } else {
                tvDate.setText("Date: Non spécifiée");
            }
            tvAmount.setText("Prix: Non spécifié");
            // Prix - ajout de la vérification
          /*  if (reservation.getAmount() > 0) {
                tvAmount.setText("Prix: " + reservation.getAmount());
            } else {
                tvAmount.setText("Prix: Non spécifié");
            }*/
        }
    }
}