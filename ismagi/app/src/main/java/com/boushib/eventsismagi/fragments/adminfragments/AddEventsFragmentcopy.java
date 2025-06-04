package com.boushib.eventsismagi.fragments.adminfragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import com.boushib.eventsismagi.R;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/*public class AddEventsFragmentcopy extends Fragment {

    private TextInputEditText titre, description, lieu, prix;
    private CheckBox estPrive;
    private Button btnAddEvent, btnSelectDate, btnSelectTime,buttonSelectImage;
    private TextView tvSelectedDateTime;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private Uri imageUri;
    private ImageView imageViewNote;
    private FirebaseStorage storage;
    private int durationHours = 1;

    public AddEventsFragmentcopy() {
        // Required empty public constructor
    }



    public static AddEventsFragmentcopy newInstance() {
        AddEventsFragmentcopy fragment = new AddEventsFragmentcopy();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "di36kbm3p");
        config.put("api_key", "177629361138436");
        config.put("api_secret", "ZTB6il43K9lHyWnfok4OUMj8OtE");
       // MediaManager.init(requireContext(), config);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_events, container, false);

        titre = view.findViewById(R.id.titre);
        description = view.findViewById(R.id.description);
        lieu = view.findViewById(R.id.lieu);
        prix = view.findViewById(R.id.prix);
        estPrive = view.findViewById(R.id.estPrive);
        btnAddEvent = view.findViewById(R.id.btn_add_event);
        btnSelectDate = view.findViewById(R.id.btn_select_date);
        btnSelectTime = view.findViewById(R.id.btn_select_time);
        tvSelectedDateTime = view.findViewById(R.id.tv_selected_date_time);
        buttonSelectImage = view.findViewById(R.id.btn_select_image);
        imageViewNote = view.findViewById(R.id.imageViewNote);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();


          ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        // Récupérer l'URI de l'image sélectionnée
                        imageUri = result.getData().getData();
                        // Afficher l'image dans l'ImageView
                        imageViewNote.setImageURI(imageUri);
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(getContext(), "Erreur lors de la sélection de l'image", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        buttonSelectImage.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop() // Recadrer l'image
                    .compress(1024) // Compresser l'image
                    .maxResultSize(1080, 1080) // Dimensions maximales
                    .createIntent(intent -> {
                        imagePickerLauncher.launch(intent);
                        return null;
                    });
        });


        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());

        btnSelectTime.setOnClickListener(v -> showTimePickerDialog());





        btnAddEvent.setOnClickListener(v -> addEvent());

        return view;
    }

    private void showDatePickerDialog() {
        // Obtenir la date actuelle
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Afficher le DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, yearSelected, monthOfYear, dayOfMonth) -> {
                    selectedYear = yearSelected;
                    selectedMonth = monthOfYear;
                    selectedDay = dayOfMonth;
                    updateSelectedDateTime();
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        // Obtenir l'heure actuelle
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Afficher le TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minuteSelected) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minuteSelected;
                    updateSelectedDateTime();
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    private void showDurationPickerDialog() {

        Toast.makeText(getContext(), "Sélectionnez la durée (en heures)", Toast.LENGTH_SHORT).show();

    }

    private void updateSelectedDateTime() {
        // Mettre à jour le TextView avec la date et l'heure sélectionnées
        String dateTime = String.format(
                "%02d/%02d/%04d %02d:%02d",
                selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute, durationHours
        );
        tvSelectedDateTime.setText("Date et heure : " + dateTime);
    }

    private void addEvent() {

        String createurId = mAuth.getCurrentUser().getUid();
        String eventTitre = titre.getText().toString().trim();
        String eventDescription = description.getText().toString().trim();
        String eventLieu = lieu.getText().toString().trim();

        double eventPrix = Double.parseDouble(prix.getText().toString().trim());
        boolean eventEstPrive = estPrive.isChecked();

        // Vérifier que tous les champs sont remplis
        if (eventTitre.isEmpty() || eventDescription.isEmpty() || eventLieu.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer un objet événement
        Map<String, Object> event = new HashMap<>();
        event.put("createurId", createurId);
        event.put("titre", eventTitre);
        event.put("description", eventDescription);
        event.put("lieu", eventLieu);
        event.put("date_event", String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay));
        event.put("heure_event", String.format("%02d:%02d", selectedHour, selectedMinute));
        event.put("duree", durationHours);
        event.put("prix", eventPrix);
        event.put("estPrive", eventEstPrive);

        if (imageUri != null) {
            uploadImageAndAddEvent(event);
        } else {
            // Ajouter l'événement sans image
            saveEventToFirestore(event);
        }

        // Ajouter l'événement à Firestore
      /*  db.collection("evenements")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Événement ajouté avec succès", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Erreur lors de l'ajout de l'événement: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveEventToFirestore(Map<String, Object> event) {
        // Add the event to Firestore
        db.collection("evenements")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Événement ajouté avec succès", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Erreur lors de l'ajout de l'événement: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImageAndAddEvent(Map<String, Object> event) {
        // Upload image to Cloudinary
        MediaManager.get().upload(imageUri)
                .option("folder", "event_images") // Optional: Organize images in a folder
                .callback(new UploadCallback() {

                    @Override
                    public void onStart(String requestId) {
                        // Upload started
                        Toast.makeText(getContext(), "Uploading image...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Upload in progress
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        // Upload successful
                        String imageUrl = (String) resultData.get("url"); // Get the image URL

                        // Add the image URL to the event data
                        event.put("imageUrl", imageUrl);

                        // Save the event to Firestore
                        saveEventToFirestore(event);
                    }

                    @Override
                    public void onError(String s, ErrorInfo errorInfo) {

                    }


                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Upload rescheduled
                    }
                })
                .dispatch();
    }

    private void clearFields() {
        titre.setText("");
        description.setText("");
        lieu.setText("");
        prix.setText("");
        estPrive.setChecked(false);
        tvSelectedDateTime.setText("Date et heure : Non sélectionnées");
    }


}*/