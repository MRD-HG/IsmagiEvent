package com.boushib.eventsismagi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.model.Event;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EventDashboardAdapter extends RecyclerView.Adapter<EventDashboardAdapter.DashboardViewHolder> {

    private final List<Event> eventList;
    private final OnItemClickListener listener;
    private final FirebaseFirestore db;

    public interface OnItemClickListener {
        void onItemClick(Event event);
        void onDeleteClick(Event event);
    }

    public EventDashboardAdapter(List<Event> eventList, OnItemClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item_dasboard, parent, false);
        return new DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event, listener);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(eventList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateEvents(List<Event> newEvents) {
        eventList.clear();
        eventList.addAll(newEvents);
        notifyDataSetChanged();
    }

    static class DashboardViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle, tvDate, tvPrice;
        private final ImageView ivEvent;
        private final Button btnDelete;

        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.titredasboarditem);
            tvDate = itemView.findViewById(R.id.dateEventdasboard);
            tvPrice = itemView.findViewById(R.id.prixboarditem);
            ivEvent = itemView.findViewById(R.id.imageEventboarditem);
            btnDelete = itemView.findViewById(R.id.btnsupprimer);

            // Safety check
            if (btnDelete == null) {
                throw new RuntimeException("Delete button not found in layout. Check your XML file.");
            }
        }

        public void bind(Event event, OnItemClickListener listener) {
            tvTitle.setText(event.getTitre());
            tvDate.setText(String.format("%s • %s", event.getDateEvent(), event.getHeureEvent()));
            tvPrice.setText(String.format("%.2f €", event.getPrix()));

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(event);
                }
            });

            // Load image with Glide
            if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
                String imageUrl = event.getImageUrl();
                if (imageUrl.startsWith("http://")) {
                    imageUrl = imageUrl.replace("http://", "https://");
                }
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.picture_bg)
                        .into(ivEvent);
            } else {
                ivEvent.setImageResource(R.drawable.placeholder);
            }
        }
    }
}