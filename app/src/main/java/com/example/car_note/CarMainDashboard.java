package com.example.car_note;

import Class.Event;
import Class.Car;
import Class.User;
import Adapter.CarMainDashboardAdapter;
import Database.Database;
import Manager.CarManager;
import Manager.UserManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CarMainDashboard extends AppCompatActivity {
    Database db = new Database(this);
    ArrayList<Event> events;
    CarMainDashboardAdapter carMainDashboardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

        User currentUser = UserManager.getInstance().getCurrentUser();
        Car currentCar = CarManager.getInstance().getCurrentCar();

        ListView list = findViewById(R.id.mainDashboardListOfEvents);

        Button create = findViewById(R.id.mainDashboardButtonCreateEvent);
        Button edit = findViewById(R.id.mainDashboardButtonEditEvent);
        Button delete = findViewById(R.id.mainDashboardButtonDeleteEvent);

        Spinner spinner = findViewById(R.id.mainDashboardSpinner);
        Button sort = findViewById(R.id.mainDashboardButtonSortEvents);

        events = getListOfEvents(currentUser.getId(), currentCar.getId());

        if (events == null || events.isEmpty()) {
            Toast.makeText(this, "Try to add your first event !!!", Toast.LENGTH_LONG).show();
        } else {
            carMainDashboardAdapter = new CarMainDashboardAdapter(getApplicationContext(), events);
            list.setAdapter(carMainDashboardAdapter);
        }

        create.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EventAdd.class);
            startActivity(intent);
        });
    }

    private ArrayList<Event> getListOfEvents(int userId, int carId)
    {
        try {
            return db.getListOfEvents(userId, carId);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}