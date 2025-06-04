package com.boushib.eventsismagi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.model.CalendarDay;
import com.boushib.eventsismagi.model.Event;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.security.AccessController.getContext;

public class CalendarAdapter extends BaseAdapter {
    private final Context context;
    private final List<CalendarDay> days;
    private final int currentMonth;
    private final int currentYear;
    private final List<Event> events;
    private final OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(CalendarDay day, boolean hasEvents);
    }

    public CalendarAdapter(Context context, List<CalendarDay> days, int month, int year,
                           List<Event> events, OnDateClickListener listener) {
        this.context = context;
        this.days = days;
        this.currentMonth = month;
        this.currentYear = year;
        this.events = events;
        this.listener = listener;
    }

    @Override public int getCount() { return days.size(); }
    @Override public Object getItem(int position) { return days.get(position); }
    @Override public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_day_item, parent, false);
        }

        TextView dayTextView = convertView.findViewById(R.id.dayTextView);
        CalendarDay day = days.get(position);

        // Reset appearance
        dayTextView.setText(String.valueOf(day.getDay()));
        convertView.setBackgroundResource(0);
        dayTextView.setTextColor(ContextCompat.getColor(context, R.color.black));

        if (day.isOtherMonth()) {
            dayTextView.setTextColor(ContextCompat.getColor(context, R.color.gray));
        } else {
            // Create calendar instance for this day
            Calendar dayCalendar = Calendar.getInstance();
            dayCalendar.set(currentYear, currentMonth, day.getDay());

            // Check for events
            boolean hasEvents = hasEventOnDate(dayCalendar);
            if (hasEvents) {
                convertView.setBackgroundResource(R.drawable.event_day_bg);
                dayTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
            }

            // Highlight current day
            Calendar today = Calendar.getInstance();
            if (currentMonth == today.get(Calendar.MONTH) &&
                    currentYear == today.get(Calendar.YEAR) &&
                    day.getDay() == today.get(Calendar.DAY_OF_MONTH)) {
                convertView.setBackgroundResource(R.drawable.current_day_bg);
            }

            // Set click listener
            convertView.setOnClickListener(v -> listener.onDateClick(day, hasEvents));
        }

        return convertView;
    }

    private boolean hasEventOnDate(Calendar date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = sdf.format(date.getTime());

        for (Event event : events) {
            if (formattedDate.equals(event.getDateEvent())) {

                return true;
            }
        }
        return false;
    }
}