package com.example.car_note;

import Class.Car;
import Database.DBHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

public class CarPick extends AppCompatActivity {
    DBHelper db = new DBHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_pick);

        ListView list = findViewById(R.id.userCarsListOfCars);

        try {
            Map<Integer, List<Car>> carsFromDb = db.getListOfCars(1);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        Button buttonChoose = findViewById(R.id.userCarsButtonChooseCar);
        Button buttonAdd = findViewById(R.id.userCarsButtonAddCar);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        buttonChoose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });

        buttonAdd.setOnClickListener(v -> addCarLayout());
    }

    private void addCarLayout(){
        Intent intent = new Intent(getApplicationContext(), CarAdd.class);
        startActivity(intent);
    }
}
