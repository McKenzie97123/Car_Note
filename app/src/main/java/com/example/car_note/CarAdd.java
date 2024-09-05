package com.example.car_note;

import Adapter.CarAddAdapter;
import Class.Car;
import Class.User;
import Database.Database;
import Manager.UserManager;
import Service.CarAddValidator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class CarAdd extends AppCompatActivity {
    private static final String[] VALID_TYPES = Car.BODY_TYPES;
    CarAddValidator validator = new CarAddValidator();
    Database db = new Database(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_add);

        User currentUser = UserManager.getInstance().getCurrentUser();

        EditText brand = findViewById(R.id.carAddBrand);
        EditText model = findViewById(R.id.carAddModel);
        EditText color = findViewById(R.id.carAddColor);
        EditText plateNumber = findViewById(R.id.carAddPlateNumber);

        Spinner spinner = findViewById(R.id.carAddSpinner);
        CarAddAdapter carAddAdapter = new CarAddAdapter(
                this,
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
            int carTypeId = (int) spinner.getSelectedItemId();
            String carType = VALID_TYPES[carTypeId];

            addCar(currentUser.getId(), carBrand, carModel, carColor, carPlateNumber, carType);
        });

        back.setOnClickListener(v -> returnToCarPick());
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

            Toast.makeText(this, "car has been added", Toast.LENGTH_LONG).show();
            returnToCarPick();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void returnToCarPick() {
        Intent intent = new Intent(getApplicationContext(), CarPick.class);
        startActivity(intent);
    }
}
