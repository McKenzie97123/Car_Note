package com.example.car_note;

import Class.User;
import Database.Database;
import Service.LoginRegisterValidator;
import Service.PasswordHasher;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    Database db = new Database(this);
    PasswordHasher passwordHasher = new PasswordHasher();
    LoginRegisterValidator validator = new LoginRegisterValidator();

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        EditText email = findViewById(R.id.registerEmail);
        EditText name = findViewById(R.id.registerName);
        EditText lastName = findViewById(R.id.registerLastName);
        EditText password = findViewById(R.id.registerPasswordFirst);
        EditText repeatPassword = findViewById(R.id.registerPasswordSecond);

        Button signup = findViewById(R.id.registerButtonRegister);
        Button login = findViewById(R.id.registerButtonLogin);

        signup.setOnClickListener(v -> {
            String userEmail = email.getText().toString();
            String userName = name.getText().toString();
            String userLastName = lastName.getText().toString();
            String userPassword = password.getText().toString();
            String userRepeatPassword = repeatPassword.getText().toString();

           registerUser(userEmail, userName, userLastName, userPassword, userRepeatPassword);
        });

        login.setOnClickListener(v -> loginLayout());
    }

    private void insertUser(User user) {
        try {
            db.insertUser(user);

            Intent intent = new Intent(getApplicationContext(), CarPick.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser(
            String email,
            String name,
            String lastName,
            String password,
            String repeatedPassword
    ) {
        if (email.isEmpty() || name.isEmpty() || lastName.isEmpty()
                || password.isEmpty() || repeatedPassword.isEmpty()) {
            Toast.makeText(this, "To register new user you need to fill " +
                    "up all presented text fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validator.emailValidate(email)) {
            Toast.makeText(this, "To register new user you need to fill" +
                    " up this form with valid email", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validator.passwordValidate(password)) {
            Toast.makeText(this, "Passwords needs to have minimum 8 chars" +
                    ", and one Uppercase char", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(repeatedPassword)) {
            Toast.makeText(this, "Passwords are different", Toast.LENGTH_LONG).show();
            return;
        }

        String hashedUserPassword = passwordHasher.hashPassword(password);
        User user = new User(null ,email, name, lastName, hashedUserPassword);
        insertUser(user);
        Toast.makeText(this, "User has been added, login again now !", Toast.LENGTH_LONG).show();
        loginLayout();
    }

    private void loginLayout() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }
}
