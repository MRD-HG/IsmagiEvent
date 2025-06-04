package com.boushib.eventsismagi.fragments.adminfragments;

import android.annotation.SuppressLint;
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
import com.boushib.eventsismagi.model.Event;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.Map;

public class AddEventsFragment extends Fragment {

    // UI Components
    private TextInputEditText titre, description, lieu, prix;
    private CheckBox estPrive;
    private Button btnAddEvent, btnSelectDate, btnSelectTime, buttonSelectImage;
    private TextView tvSelectedDateTime;
    private ImageView imageViewNote;

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // Date/Time
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private int durationHours = 1;

    // Image
    private Uri imageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    public AddEventsFragment() {
        // Required empty constructor
    }

    public static AddEventsFragment newInstance() {
        return new AddEventsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        imageViewNote.setImageURI(imageUri);
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        showToast("Erreur lors de la sélection de l'image");
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_events, container, false);

        initializeViews(view);
        setupFirebase();
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
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
    }

    private void setupFirebase() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupClickListeners() {
        buttonSelectImage.setOnClickListener(v -> launchImagePicker());
        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());
        btnSelectTime.setOnClickListener(v -> showTimePickerDialog());
        btnAddEvent.setOnClickListener(v -> addEvent());
    }

    private void launchImagePicker() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    imagePickerLauncher.launch(intent);
                    return null;
                });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                requireContext(),
                (view, year, month, day) -> {
                    selectedYear = year;
                    selectedMonth = month;
                    selectedDay = day;
                    updateSelectedDateTime();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(
                requireContext(),
                (view, hour, minute) -> {
                    selectedHour = hour;
                    selectedMinute = minute;
                    updateSelectedDateTime();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    @SuppressLint("StringFormatInvalid")
    private void updateSelectedDateTime() {
        String dateTime = String.format(
                "%02d/%02d/%04d %02d:%02d",
                selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute
        );
        tvSelectedDateTime.setText(getString(R.string.selected_date_time, dateTime));
    }

    private void addEvent() {
        // Validate required fields
        if (!validateFields()) {
            return;
        }

        // Create Event object
        Event event = createEventFromInputs();

        if (imageUri != null) {
            uploadImageAndAddEvent(event);
        } else {
            saveEventToFirestore(event);
        }
    }

    private boolean validateFields() {
        if (titre.getText().toString().trim().isEmpty()) {
            showToast("Veuillez entrer un titre");
            return false;
        }
        if (description.getText().toString().trim().isEmpty()) {
            showToast("Veuillez entrer une description");
            return false;
        }
        if (lieu.getText().toString().trim().isEmpty()) {
            showToast("Veuillez entrer un lieu");
            return false;
        }
        if (prix.getText().toString().trim().isEmpty()) {
            showToast("Veuillez entrer un prix");
            return false;
        }
        if (selectedYear == 0) { // Date not selected
            showToast("Veuillez sélectionner une date");
            return false;
        }
        return true;
    }

    private Event createEventFromInputs() {
        return new Event(
                mAuth.getCurrentUser().getUid(),
                titre.getText().toString().trim(),
                description.getText().toString().trim(),
                lieu.getText().toString().trim(),
                String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay),
                String.format("%02d:%02d", selectedHour, selectedMinute),
                durationHours,
                parsePrice(prix.getText().toString().trim()),
                estPrive.isChecked(),
                null // imageUrl will be set after upload
        );
    }

    private double parsePrice(String priceStr) {
        try {
            return Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void uploadImageAndAddEvent(Event event) {
        MediaManager.get().upload(imageUri)
                .option("folder", "event_images")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        showToast("Téléchargement de l'image...");
                    }

                    @Override
                    public void onProgress(String s, long l, long l1) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        event.setImageUrl((String) resultData.get("url"));
                        saveEventToFirestore(event);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo errorInfo) {
                        showToast("Échec du téléchargement de l'image");
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        showToast("Nouvelle tentative de téléchargement...");
                    }
                })
                .dispatch();
    }

    private void saveEventToFirestore(Event event) {
        db.collection("evenements")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    event.setId(documentReference.getId());
                    showToast("Événement créé avec succès");
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    showToast("Erreur: " + e.getMessage());
                });
    }

    private void clearFields() {
        titre.setText("");
        description.setText("");
        lieu.setText("");
        prix.setText("");
        estPrive.setChecked(false);
        tvSelectedDateTime.setText(R.string.default_date_time_text);
        imageViewNote.setImageResource(R.drawable.placeholder);
        imageUri = null;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}