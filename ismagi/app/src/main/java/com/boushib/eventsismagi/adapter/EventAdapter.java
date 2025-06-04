package com.boushib.eventsismagi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.model.Event;
import com.bumptech.glide.Glide;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public EventAdapter(List<Event> eventList, OnItemClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView titre, description, lieu, dateEvent, heureEvent, prix;
        private final ImageView imageEvent;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.titre1);
            description = itemView.findViewById(R.id.description1);
            lieu = itemView.findViewById(R.id.lieu1);
            dateEvent = itemView.findViewById(R.id.dateEvent1);
            heureEvent = itemView.findViewById(R.id.heureEvent1);
            prix = itemView.findViewById(R.id.prix1);
            imageEvent = itemView.findViewById(R.id.imageEvent1);
        }

        public void bind(Event event) {
            titre.setText(event.getTitre());
            description.setText(event.getDescription());
            lieu.setText(event.getLieu());
            dateEvent.setText(event.getDateEvent());
            heureEvent.setText(event.getHeureEvent());
            prix.setText(String.format("Prix : %.2f €", event.getPrix()));

            if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
                String imageUrl = event.getImageUrl();

                if (imageUrl.startsWith("http://")) {
                    imageUrl = imageUrl.replace("http://", "https://");
                }

                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .into(imageEvent);
            } else {
                imageEvent.setImageResource(R.drawable.placeholder); // Image par défaut
            }
        }
    }
}