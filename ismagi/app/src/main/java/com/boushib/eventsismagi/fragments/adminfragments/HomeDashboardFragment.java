package com.boushib.eventsismagi.fragments.adminfragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.adapter.EventAdapter;
import com.boushib.eventsismagi.adapter.EventDashboardAdapter;
import com.boushib.eventsismagi.model.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeDashboardFragment extends Fragment implements EventDashboardAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EventDashboardAdapter eventAdapter;
    private List<Event> eventList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_dashboard, container, false);

        // Initialisation des vues
        recyclerView = view.findViewById(R.id.recyclerViewDashboard);
        progressBar = view.findViewById(R.id.progressBar);

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        // Initialisation de la liste et de l'adapter
        eventList = new ArrayList<>();
        eventAdapter = new EventDashboardAdapter(eventList, this);
        recyclerView.setAdapter(eventAdapter);

        // Initialisation Firestore
        db = FirebaseFirestore.getInstance();

        // Chargement des événements
        loadEvents();

        return view;
    }

    private void loadEvents() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("evenements")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        List<Event> tempList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            event.setId(document.getId());
                            tempList.add(event);
                        }
                        eventAdapter.updateEvents(tempList);
                    } else {
                        Toast.makeText(getContext(),
                                "Erreur de chargement: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClick(Event event) {
        // Navigation vers le détail de l'événement
        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.getId());

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.to_event_details, bundle);
    }

    @Override
    public void onDeleteClick(Event event) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmer la suppression")
                .setMessage("Êtes-vous sûr de vouloir supprimer cet événement?")
                .setPositiveButton("Supprimer", (dialog, which) -> deleteEvent(event))
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void deleteEvent(Event event) {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("evenements").document(event.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Événement supprimé avec succès", Toast.LENGTH_SHORT).show();
                        // Recharger la liste après suppression
                        loadEvents();
                    } else {
                        Toast.makeText(getContext(),
                                "Erreur lors de la suppression: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}