package com.example.youtube_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login_screen extends AppCompatActivity {
    private EditText usernameField;
    private EditText passwordField;
    private TextView showPassword;
    private TextView errorMessage;
    private Users users;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button login = findViewById(R.id.login);
        usernameField = findViewById(R.id.Username);
        passwordField = findViewById(R.id.Password);
        showPassword = findViewById(R.id.showPassword);
        errorMessage = findViewById(R.id.errorMessage);

        // Initialize user manager
        users = new Users();

        login.setOnClickListener(v -> {
            if (validateUsername() && validatePassword()) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (users.validateUser(username, password)) {
                    Intent i = new Intent(login_screen.this, Home_screen.class);
                    startActivity(i);
                } else {
                    showErrorMessage("Error: You have entered incorrect username or password.");
                }
            }
        });


        showPassword.setOnClickListener(v -> {
            if (passwordField.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                // Show Password
                passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showPassword.setText("Hide");
            } else {
                // Hide Password
                passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                showPassword.setText("Show");
            }
        });
    }

    private boolean validateUsername() {
        String username = usernameField.getText().toString().trim();

        if (username.length() < 5) {
            showErrorMessage("Username must be at least 5 characters long");
            return false;
        }

        if (!username.matches(".*\\d.*")) {
            showErrorMessage("Username must contain at least one number");
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        String password = passwordField.getText().toString().trim();

        if (password.length() < 8) {
            showErrorMessage("Password must be at least 8 characters long");
            return false;
        }

        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            showErrorMessage("Password must contain both letters and numbers");
            return false;
        }

        return true;
    }

    private void showErrorMessage(String message) {
        errorMessage.setText(message);
        errorMessage.setVisibility(View.VISIBLE);
    }
}
