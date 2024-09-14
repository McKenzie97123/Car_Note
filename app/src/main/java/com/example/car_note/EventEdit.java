package com.example.car_note;

import Class.Car;
import Class.Event;
import Class.User;
import Database.Database;
import Manager.CarManager;
import Manager.UserManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class EventEdit extends AppCompatActivity {
    private static final String[] EVENT_TYPES = Event.EVENT_TYPES;
    Database db = new Database(this);
    ArrayList<String> types = new ArrayList<>(Arrays.asList(EVENT_TYPES));

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_edit);
        Intent intent = getIntent();

        User currentUser = UserManager.getInstance().getCurrentUser();
        Car currentCar = CarManager.getInstance().getCurrentCar();
        int pickedEventId = intent.getIntExtra("pickedEventId", -1);

        Event currentEvent = db.getSingleEvent(pickedEventId);

        EditText title = findViewById(R.id.eventEditTitle);
        title.setText(currentEvent.getTitle());

        EditText description = findViewById(R.id.eventEditDescription);
        description.setText(currentEvent.getDescription());

        Spinner spinner = findViewById(R.id.eventEditSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        spinner.setAdapter(adapter);

        Button editEvent = findViewById(R.id.eventEditButtonEditEvent);
        Button back = findViewById(R.id.eventEditButtonBack);

        editEvent.setOnClickListener(v -> {
            String eventTitle = title.getText().toString();
            String eventDescription = description.getText().toString();
            int eventTypeId = (int) spinner.getSelectedItemId();
            String eventType = EVENT_TYPES[eventTypeId];

            editEvent(currentUser.getId(), currentCar.getId(), eventTitle, eventDescription, eventType);
        });

        back.setOnClickListener(v -> returnToCarMainDashboard());
    }

    private void editEvent(
            Integer userId,
            Integer carId,
            String title,
            String description,
            String type
    ) {
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "To edit picked event you need to fill " +
                    "up all presented text fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (type.isEmpty()) {
            Toast.makeText(this, "Need to choose type of your modified event", Toast.LENGTH_LONG).show();
            return;
        }

        String datePattern = "dd-MM-yyyy";
        @SuppressLint("SimpleDateFormat") DateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        String eventDateInString = simpleDateFormat.format(now);

        Event event = new Event(null, userId, carId, title, description, eventDateInString, type, null);
        updateEvent(event);
    }

    private void updateEvent(Event event) {
        try {
            db.updateEvent(event);

            Toast.makeText(this, "event has been edited", Toast.LENGTH_LONG).show();
            returnToCarMainDashboard();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void returnToCarMainDashboard() {
        Intent intent = new Intent(getApplicationContext(), CarMainDashboard.class);
        startActivity(intent);
    }
}
