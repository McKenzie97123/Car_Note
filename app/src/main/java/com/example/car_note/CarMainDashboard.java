package com.example.car_note;

import Adapter.CarMainDashboardAdapter;
import Class.Car;
import Class.Event;
import Class.User;
import Database.Database;
import Manager.CarManager;
import Manager.EventManager;
import Manager.UserManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static Class.Event.EVENT_TYPES;


public class CarMainDashboard extends AppCompatActivity {
    private static ArrayList<String> SORT_TYPES = new ArrayList<>(Arrays.asList(EVENT_TYPES));
    Database db = new Database(this);
    ArrayList<Event> events;
    CarMainDashboardAdapter carMainDashboardAdapter;
    private int pickedEventId = -1;
    User currentUser;
    Car currentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

        currentUser = UserManager.getInstance().getCurrentUser();
        currentCar = CarManager.getInstance().getCurrentCar();

        ListView list = findViewById(R.id.mainDashboardListOfEvents);

        Button create = findViewById(R.id.mainDashboardButtonCreateEvent);
        Button edit = findViewById(R.id.mainDashboardButtonEditEvent);
        Button delete = findViewById(R.id.mainDashboardButtonDeleteEvent);

        Spinner spinner = findViewById(R.id.mainDashboardSpinner);
        SORT_TYPES.add(0, "all");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SORT_TYPES);
        spinner.setAdapter(adapter);

        Button sort = findViewById(R.id.mainDashboardButtonSortEvents);
        Button back = findViewById(R.id.mainDashboardButtonBackToCarPick);

        events = getListOfEvents(currentUser.getId(), currentCar.getId());
        if (events == null || events.isEmpty()) {
            Toast.makeText(this, "Try to add your first event !!!", Toast.LENGTH_LONG).show();
        } else {
            carMainDashboardAdapter = new CarMainDashboardAdapter(getApplicationContext(), events);
            list.setAdapter(carMainDashboardAdapter);
        }

        list.setOnItemClickListener((adapterView, view, position, id) -> pickedEventId = position);

        create.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EventAdd.class);
            startActivity(intent);
        });

        edit.setOnClickListener(v -> {
            if (pickedEventId < 0) {
                Toast.makeText(this, "Pick event to edit firstly !", Toast.LENGTH_LONG).show();
            } else {
                Event event = events.get(pickedEventId);
                EventManager.getInstance().setCurrentEvent(event);

                Intent intent = new Intent(getApplicationContext(), EventEdit.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(v -> {
            if (pickedEventId < 0) {
                Toast.makeText(this, "Pick event to delete it !", Toast.LENGTH_LONG).show();
            } else {
                Event event = events.get(pickedEventId);
                deleteEvent(event.getId());
                events.remove(pickedEventId);

                carMainDashboardAdapter = new CarMainDashboardAdapter(getApplicationContext(), events);
                list.setAdapter(carMainDashboardAdapter);
            }
        });

        sort.setOnClickListener(v -> {
            events = sortEventsByCategory(spinner.getSelectedItem().toString());
            carMainDashboardAdapter = new CarMainDashboardAdapter(getApplicationContext(), events);
            list.setAdapter(carMainDashboardAdapter);
        });

        back.setOnClickListener(v -> returnToCarPick());
    }

    private void deleteEvent(int pickedEventId) {
        try {
            db.deleteEvent(pickedEventId);
            Toast.makeText(this, "picked event has been deleted successfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Event> getListOfEvents(int userId, int carId) {
        try {
            return db.getListOfEvents(userId, carId);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private ArrayList<Event> sortEventsByCategory(String type) {
        events = getListOfEvents(currentUser.getId(), currentCar.getId());

        if (events == null || events.isEmpty()) {
            Toast.makeText(this, "Event list is empty, try to add first event", Toast.LENGTH_LONG).show();
            return events;
        }

        if (Objects.equals(type, "all")) {
            return events;
        }

        for (Event event: events) {
            if (!event.getType().equals(type)) {
                events.remove(event);
            }
        }
        return events;
    }

    private void returnToCarPick() {
        Intent intent = new Intent(getApplicationContext(), CarPick.class);
        startActivity(intent);
    }
}