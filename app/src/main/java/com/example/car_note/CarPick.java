package com.example.car_note;

import Adapter.CarPickAdapter;
import Class.Car;
import Class.User;
import Database.Database;
import Manager.CarManager;
import Manager.UserManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CarPick extends AppCompatActivity {
    Database db = new Database(this);
    CarPickAdapter carPickAdapter;
    ArrayList<Car> cars;
    int pickedCarId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_pick);

        User currentUser = UserManager.getInstance().getCurrentUser();

        ListView list = findViewById(R.id.carPickListOfCars);
        Button pick = findViewById(R.id.carPickButtonPickCar);
        Button add = findViewById(R.id.carPickButtonAddCar);

        cars = getListOfCars(currentUser.getId());
        if (cars == null || cars.isEmpty()) {
            Toast.makeText(this, "Try to add your first car !!!", Toast.LENGTH_LONG).show();
        } else {
            carPickAdapter = new CarPickAdapter(getApplicationContext(), cars);
            list.setAdapter(carPickAdapter);
        }

        list.setOnItemClickListener((adapterView, view, position, id) -> pickedCarId = position);

        pick.setOnClickListener(v -> {
            if (pickedCarId < 0) {
                Toast.makeText(this, "Pick car firstly !", Toast.LENGTH_LONG).show();
            } else {
                Car pickedCar = cars.get(pickedCarId);
                CarManager.getInstance().setCurrentCar(pickedCar);
                carMainDashboardLayout();
            }
        });

        add.setOnClickListener(v -> addCarLayout());
    }

    private ArrayList<Car> getListOfCars(Integer userId) {
        try {
            return db.getListOfCars(userId);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void addCarLayout() {
        Intent intent = new Intent(getApplicationContext(), CarAdd.class);
        startActivity(intent);
    }

    private void carMainDashboardLayout() {
        Intent intent = new Intent(getApplicationContext(), CarMainDashboard.class);
        startActivity(intent);
    }
}
