package com.boushib.eventsismagi.adapter;

/*public class EventAdapterCopy extends RecyclerView.Adapter<EventAdapterCopy.EventViewHolder>{


    private List<Event> eventList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public EventAdapterCopy(List<Event> eventList, OnItemClickListener listener) {

        this.eventList = eventList;

        this.listener =listener;

    }



    @NonNull
    @NotNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EventAdapterCopy.EventViewHolder holder, int position) {
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
            titre = itemView.findViewById(R.id.titre);
            description = itemView.findViewById(R.id.description);
            lieu = itemView.findViewById(R.id.lieu);
            dateEvent = itemView.findViewById(R.id.dateEvent);
            heureEvent = itemView.findViewById(R.id.heureEvent);
            prix = itemView.findViewById(R.id.prix);
            imageEvent = itemView.findViewById(R.id.imageEvent);
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


            /*if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
                if (imageUrl != null && imageUrl.startsWith("http://")) {
                    imageUrl = imageUrl.replace("http://", "https://");
                }
                Glide.with(itemView.getContext())
                        .load(event.getImageUrl())
                        .into(imageEvent);
            } else {
                imageEvent.setImageResource(R.drawable.placeholder); // Image par défaut
            }*/

