package com.example.car_note;

import Adapter.CarAddAdapter;
import Class.Car;
import Class.User;
import Database.DBHelper;
import Service.CarAddValidator;
import Service.UserManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class CarAdd extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String[] VALID_TYPES = Car.BODY_TYPE;
    private String type = "";
    CarAddValidator validator = new CarAddValidator();
    DBHelper db = new DBHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_add);

        User currentUser = UserManager.getInstance().getCurrentUser();

        EditText brand = findViewById(R.id.carAddBrand);
        EditText model = findViewById(R.id.carAddModel);
        EditText color = findViewById(R.id.carAddColor);
        EditText plateNumber = findViewById(R.id.carAddPlateNumber);

        Spinner spinner = findViewById(R.id.carAddSpinner);
        spinner.setOnItemSelectedListener(this);

        CarAddAdapter carAddAdapter = new CarAddAdapter(
                this,
                android.R.layout.simple_spinner_item,
                VALID_TYPES
        );
        spinner.setAdapter(carAddAdapter);

        Button addCar = findViewById(R.id.carAddButtonAddNewCar);
        Button back = findViewById(R.id.carAddButtonBack);

        addCar.setOnClickListener(v -> {
            String carBrand = brand.getText().toString();
            String carModel = model.getText().toString();
            String carColor = color.getText().toString();
            String carPlateNumber = plateNumber.getText().toString();

            addCar(currentUser.getId(), carBrand, carModel, carColor, carPlateNumber, type);
        });

        back.setOnClickListener(v -> returnToCarPick());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = VALID_TYPES[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Auto-generated method stub
    }

    private void addCar(
            int userId,
            String brand,
            String model,
            String color,
            String plateNumber,
            String type
    ) {
        if (brand.isEmpty() || model.isEmpty() || color.isEmpty()
                || plateNumber.isEmpty()) {
            Toast.makeText(this, "To add new car you need to fill " +
                    "up all presented text fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (type.isEmpty()) {
            Toast.makeText(this, "Need to choose body type of your car", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validator.plateNumberValidate(plateNumber)) {
            String message = "Persisted plate number is invalid. The first character must be a letter," +
                    " and the maximum length of the plate is 7 characters";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }
        Car car = new Car(null, userId, brand, model, color, plateNumber, type);
        insertCar(car, userId);
    }

    private void insertCar(Car car, int userId) {
        try {
            db.insertCar(car, userId);

            Intent intent = new Intent(getApplicationContext(), CarPick.class);
            startActivity(intent);
            Toast.makeText(this, "car has been added", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void returnToCarPick() {
        Intent intent = new Intent(getApplicationContext(), CarPick.class);
        startActivity(intent);
    }
}
