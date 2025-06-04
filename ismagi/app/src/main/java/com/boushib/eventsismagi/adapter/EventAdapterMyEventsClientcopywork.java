package com.boushib.eventsismagi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.model.MyEventsReservation;
import com.bumptech.glide.Glide;

import java.util.List;

/*public class EventAdapterMyEventsClientcopywork extends RecyclerView.Adapter<EventAdapterMyEventsClientcopywork.EventViewHolder> {
    private List<MyEventsReservation> reservationList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MyEventsReservation reservation);
    }

    public EventAdapterMyEventsClientcopywork(List<MyEventsReservation> reservationList, OnItemClickListener listener) {
        this.reservationList = reservationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_myevents_client, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        MyEventsReservation reservation = reservationList.get(position);
        holder.bind(reservation);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(reservation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView titre, lieu, dateEvent, prix, confirmation;
        private final ImageView imageEvent;
        private final Button btnGetTicket;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.titreMyEventsClient);
            lieu = itemView.findViewById(R.id.lieuEventsClients);
            dateEvent = itemView.findViewById(R.id.dateDeEvenmentClietns);
            prix = itemView.findViewById(R.id.prixEventsCleint);
            confirmation = itemView.findViewById(R.id.confirmationCleint);
            imageEvent = itemView.findViewById(R.id.imageEventMyeventsClients);
            btnGetTicket = itemView.findViewById(R.id.btngetticketClients);
        }

        public void bind(MyEventsReservation reservation) {
            titre.setText(reservation.getTitre());
            lieu.setText("Lieu: " + reservation.getLieu());
            dateEvent.setText("Date: " + reservation.getDateEvent());
            prix.setText(String.format("Prix: %.2f €", reservation.getPrix()));

            // Afficher le statut de confirmation
            String status = reservation.isPaymentValid() ? "Confirmé" : "En attente";
            confirmation.setText("Statut: " + status);

            btnGetTicket.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Ticket pour: " + reservation.getTitre(),
                        Toast.LENGTH_SHORT).show();
                // Ici vous pouvez implémenter la logique du ticket
            });

            if (reservation.getImageUrl() != null && !reservation.getImageUrl().isEmpty()) {
                String imageUrl = reservation.getImageUrl();

                if (imageUrl.startsWith("http://")) {
                    imageUrl = imageUrl.replace("http://", "https://");
                }

                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .into(imageEvent);
            } else {
                imageEvent.setImageResource(R.drawable.placeholder);
            }
        }
    }
}*/