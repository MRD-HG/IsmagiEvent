package com.boushib.eventsismagi.fragments.adminfragments;

import androidx.fragment.app.Fragment;


public class HomeDashboardFragmentcopy extends Fragment {

 /*   private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private FirebaseFirestore db;

    public HomeDashboardFragmentcopy() {
        // Required empty public constructor
    }


    public static HomeDashboardFragmentcopy newInstance() {
        HomeDashboardFragmentcopy fragment = new HomeDashboardFragmentcopy();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_dashboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialisation de Firestore
        db = FirebaseFirestore.getInstance();

        // Récupérer les événements
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
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
    }*/
}
