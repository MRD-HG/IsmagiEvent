package com.boushib.eventsismagi.fragments.userfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.adapter.EventAdapter;
import com.boushib.eventsismagi.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements EventAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private NavController navController;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialisation de Firestore
        db = FirebaseFirestore.getInstance();

        // Récupérer les événements
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, this); // Passer le listener ici
        recyclerView.setAdapter(eventAdapter);

        loadEvents();

        return view;
    }

    private void loadEvents() {
        db.collection("evenements")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            event.setId(document.getId());
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Erreur lors du chargement des événements", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClick(Event event) {
        // Gérer le clic sur un événement
        Toast.makeText(getContext(), "Événement cliqué: " + event.getTitre(), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.getId());

        // Naviguer vers EventsDetails avec l'ID de l'événement
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_home_to_event_details, bundle);
    }
}