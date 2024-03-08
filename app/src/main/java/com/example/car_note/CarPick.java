package com.example.car_note;

import Adapter.CarAdapter;
import Class.Car;
import Class.User;
import Database.DBHelper;
import Service.UserManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CarPick extends AppCompatActivity {
    DBHelper db = new DBHelper(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_pick);

        User currentUser = UserManager.getInstance().getCurrentUser();

        ListView list = findViewById(R.id.carPickListOfCars);
        Button pick = findViewById(R.id.carPickButtonPickCar);
        Button add = findViewById(R.id.carPickButtonAddCar);

        List<Car> cars = getListOfCars(currentUser.getId());
        if (cars != null) {
            if (cars.isEmpty()) {
                Toast.makeText(this, "Try to add your first car !!!", Toast.LENGTH_LONG).show();
            } else {
                CarAdapter adapter = new CarAdapter(this, cars);
                list.setAdapter(adapter);
            }
        } else {
            Toast.makeText(this, "Error loading cars data", Toast.LENGTH_LONG).show();
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        add.setOnClickListener(v -> addCarLayout());
    }
    private List<Car> getListOfCars(int userId) {
        try {
            return db.getListOfCars(userId);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void addCarLayout(){
        Intent intent = new Intent(getApplicationContext(), CarAdd.class);
        startActivity(intent);
    }
}
