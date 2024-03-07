package com.example.car_note;

import Class.Car;
import Class.User;
import Database.DBHelper;
import Service.CarAddValidator;
import Service.UserManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CarAdd extends AppCompatActivity {

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

        Button addCar = findViewById(R.id.carAddButtonAddNewCar);
        Button back = findViewById(R.id.carAddButtonBack);

        addCar.setOnClickListener(v -> {
            String carBrand = brand.getText().toString();
            String carModel = model.getText().toString();
            String carColor = color.getText().toString();
            String carPlateNumber = plateNumber.getText().toString();

            addCar(currentUser.getId() ,carBrand, carModel, carColor, carPlateNumber);
        });

        back.setOnClickListener(v -> returnToCarPick());
    }

    private void addCar(
            int userId,
            String brand,
            String model,
            String color,
            String plateNumber
    ) {
        if (brand.isEmpty() || model.isEmpty() || color.isEmpty()
                || plateNumber.isEmpty()) {
            Toast.makeText(this, "To add new car you need to fill " +
                    "up all presented text fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validator.plateNumberValidate(plateNumber)){
            String message = "Persisted plate number is invalid. The first character must be a letter," +
                    " and the maximum length of the plate is 7 characters";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }

        Car car = new Car(userId, brand, model, color, plateNumber);
        insertCar(car, userId);
    }

    private void insertCar(Car car, int userId) {
        try {
            System.out.println(car.getBrand());
            db.insertCar(car, userId);

            Intent intent = new Intent(getApplicationContext(), CarPick.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void returnToCarPick() {
        Intent intent = new Intent(getApplicationContext(), CarPick.class);
        startActivity(intent);
    }
}
