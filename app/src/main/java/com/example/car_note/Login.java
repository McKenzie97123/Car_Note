package com.example.car_note;

import Database.Database;
import Class.User;
import Service.PasswordHasher;
import Manager.UserManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    PasswordHasher passwordHasher = new PasswordHasher();
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = new Database(this);

        EditText email = findViewById(R.id.loginUserEmail);
        EditText password = findViewById(R.id.loginUserPass);

        Button login = findViewById(R.id.loginButton);
        Button signUp = findViewById(R.id.loginSignUpButton);

        login.setOnClickListener(v -> {
            final String userEmail = email.getText().toString();
            final String userPassword = password.getText().toString();

            validateUser(userEmail, userPassword);
        });

        signUp.setOnClickListener(v -> registerLayout());
    }

    private void userLogin(String email, String hashedUserPassword) {
        try {
            String hashedLoginPassword = db.loginUser(email);
            if (hashedLoginPassword.equals(hashedUserPassword)) {
                Intent intent = new Intent(getApplicationContext(), CarPick.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        setCurrentUser(email);
    }

    private void validateUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "You need to fill up both fields to login", Toast.LENGTH_LONG).show();
        }
        String hashedUserPassword = passwordHasher.hashPassword(password);

        userLogin(email, hashedUserPassword);
    }

    private void registerLayout() {
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
    }

    private void setCurrentUser(String email) {
        try {
            User user = db.getUser(email);
            UserManager.getInstance().setCurrentUser(user);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}