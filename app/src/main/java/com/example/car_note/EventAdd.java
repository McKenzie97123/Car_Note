package com.example.car_note;

import Class.Car;
import Class.Event;
import Class.User;
import Database.Database;
import Manager.CarManager;
import Manager.UserManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class EventAdd extends AppCompatActivity {
    private static final String[] EVENT_TYPES = Event.EVENT_TYPES;
    Database db = new Database(this);
    ArrayList<String> types = new ArrayList<>(Arrays.asList(EVENT_TYPES));

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add);

        User currentUser = UserManager.getInstance().getCurrentUser();
        Car currentCar = CarManager.getInstance().getCurrentCar();

        EditText title = findViewById(R.id.eventAddTitle);
        EditText description = findViewById(R.id.eventAddDescription);

        Spinner spinner = findViewById(R.id.eventAddSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        spinner.setAdapter(adapter);

        Button addEvent = findViewById(R.id.eventAddButtonAddNewEvent);
        Button back = findViewById(R.id.eventAddButtonBack);

        addEvent.setOnClickListener(v -> {
            String eventTitle = title.getText().toString();
            String eventDescription = description.getText().toString();
            int eventTypeId = (int) spinner.getSelectedItemId();
            String eventType = EVENT_TYPES[eventTypeId];

            addEvent(currentUser.getId(), currentCar.getId(), eventTitle, eventDescription, eventType);
        });

        back.setOnClickListener(v -> returnToCarMainDashboard());
    }

    private void addEvent(
            Integer userId,
            Integer carId,
            String title,
            String description,
            String type
    ) {
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "To add new event you need to fill " +
                    "up all presented text fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (type.isEmpty()) {
            Toast.makeText(this, "Need to choose type of your new event", Toast.LENGTH_LONG).show();
            return;
        }
        Date date = new Date();
        Event event = new Event(null, userId, carId, title, description, date, type);
        insertEvent(event);
    }

    private void insertEvent(Event event) {
        try {
            db.insertEvent(event);

            Toast.makeText(this, "event has been added", Toast.LENGTH_LONG).show();
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
