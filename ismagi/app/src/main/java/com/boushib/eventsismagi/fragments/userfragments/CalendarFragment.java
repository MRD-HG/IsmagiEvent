package com.boushib.eventsismagi.fragments.userfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.adapter.CalendarAdapter;
import com.boushib.eventsismagi.adapter.EventAdapter;
import com.boushib.eventsismagi.model.CalendarDay;
import com.boushib.eventsismagi.model.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    private TextView monthYearTextView;
    private GridView calendarGridView;
    private Calendar currentCalendar;
    private CalendarAdapter calendarAdapter;
    private LinearLayout eventsContainer;
    private RecyclerView eventsRecyclerView;
    private List<Event> allEvents = new ArrayList<>();
    private Button btnPrevYear, btnPrevMonth, btnNextMonth, btnNextYear;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar, container, false);

        // Initialize views
        monthYearTextView = view.findViewById(R.id.monthYearTextView);
        calendarGridView = view.findViewById(R.id.calendarGridView);
        eventsContainer = view.findViewById(R.id.eventsContainer);
        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        btnPrevYear = view.findViewById(R.id.btnPrevYear);
        btnPrevMonth = view.findViewById(R.id.btnPrevMonth);
        btnNextMonth = view.findViewById(R.id.btnNextMonth);
        btnNextYear = view.findViewById(R.id.btnNextYear);

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set current date
        currentCalendar = Calendar.getInstance();

        // Setup navigation
        setupNavigationButtons();

        // Load events
        loadEventsFromFirestore();

        return view;
    }

    private void setupNavigationButtons() {
        btnPrevYear.setOnClickListener(v -> navigateYear(-1));
        btnPrevMonth.setOnClickListener(v -> navigateMonth(-1));
        btnNextMonth.setOnClickListener(v -> navigateMonth(1));
        btnNextYear.setOnClickListener(v -> navigateYear(1));
    }

    private void navigateMonth(int direction) {
        currentCalendar.add(Calendar.MONTH, direction);
        renderCalendar();
    }

    private void navigateYear(int direction) {
        currentCalendar.add(Calendar.YEAR, direction);
        checkYearBounds();
        renderCalendar();
    }

    private void checkYearBounds() {
        int year = currentCalendar.get(Calendar.YEAR);
        if (year < 2010) currentCalendar.set(Calendar.YEAR, 2010);
        if (year > 2100) currentCalendar.set(Calendar.YEAR, 2100);
    }

    private void loadEventsFromFirestore() {
        FirebaseFirestore.getInstance().collection("evenements")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        allEvents.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            event.setId(document.getId());
                            allEvents.add(event);
                        }
                        renderCalendar();
                    }
                });
    }

    private void renderCalendar() {
        int monthIndex = currentCalendar.get(Calendar.MONTH);
        int yearIndex = currentCalendar.get(Calendar.YEAR);

        // Update month/year display
        monthYearTextView.setText(getMonthName(monthIndex) + " " + yearIndex);

        // Calculate days to show
        List<CalendarDay> days = calculateDaysToShow(monthIndex, yearIndex);

        // Setup adapter
        calendarAdapter = new CalendarAdapter(
                getContext(),
                days,
                monthIndex,
                yearIndex,
                allEvents,
                this::handleDayClick
        );

        calendarGridView.setAdapter(calendarAdapter);
    }

    private List<CalendarDay> calculateDaysToShow(int month, int year) {
        List<CalendarDay> days = new ArrayList<>();
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(year, month, 1);

        int daysInMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK);

        // Previous month days
        tempCalendar.add(Calendar.MONTH, -1);
        int daysInPrevMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int prevMonthDaysToShow = firstDayOfWeek - 1;

        for (int i = 0; i < prevMonthDaysToShow; i++) {
            days.add(new CalendarDay(daysInPrevMonth - prevMonthDaysToShow + i + 1, true));
        }

        // Current month days
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(new CalendarDay(i, false));
        }

        // Next month days (to complete grid)
        int totalCells = 42; // 6 weeks
        int nextMonthDaysToShow = totalCells - (prevMonthDaysToShow + daysInMonth);
        for (int i = 1; i <= nextMonthDaysToShow; i++) {
            days.add(new CalendarDay(i, true));
        }

        return days;
    }

    private void handleDayClick(CalendarDay day, boolean hasEvents) {
        if (!day.isOtherMonth()) {
            if (hasEvents) {
                Toast.makeText(getContext(), "Événements disponibles", Toast.LENGTH_SHORT).show();
                showEventsForDate(day.getDay(),
                        currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.YEAR));
            } else {
                Toast.makeText(getContext(), "Aucun événement ce jour", Toast.LENGTH_SHORT).show();
                eventsContainer.setVisibility(View.GONE);
            }
        }
    }

    private void showEventsForDate(int day, int month, int year) {
        List<Event> dailyEvents = getEventsForDate(day, month, year);

        if (!dailyEvents.isEmpty()) {
            eventsContainer.setVisibility(View.VISIBLE);
            eventsRecyclerView.setAdapter(new EventAdapter(dailyEvents, this::handleEventClick));
        } else {
            eventsContainer.setVisibility(View.GONE);
        }
    }

    private List<Event> getEventsForDate(int day, int month, int year) {
        List<Event> dailyEvents = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        String formattedDate = sdf.format(date.getTime());

        for (Event event : allEvents) {
            if (formattedDate.equals(event.getDateEvent())) {
                dailyEvents.add(event);
            }
        }
        return dailyEvents;
    }

    private void handleEventClick(Event event) {
        Toast.makeText(getContext(), event.getTitre(), Toast.LENGTH_SHORT).show();
        // Add navigation to event details if needed
    }

    private String getMonthName(int monthIndex) {
        String[] months = getResources().getStringArray(R.array.months_array);
        return months[monthIndex];
    }
}