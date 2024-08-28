package com.example.youtube_project.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.youtube_project.R;
import com.example.youtube_project.home.HomeScreenActivity;
import com.example.youtube_project.user.User;
import com.example.youtube_project.user.UserManager;
import com.example.youtube_project.ApiHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField;
    private EditText passwordField;
    private TextView showPassword;
    private TextView errorMessage;
    private UserManager userManager;

    private Switch themeSwitch;

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
        Button signup = findViewById(R.id.sign);
        usernameField = findViewById(R.id.Username);
        passwordField = findViewById(R.id.Password);
        showPassword = findViewById(R.id.showPassword);
        errorMessage = findViewById(R.id.errorMessage);
        themeSwitch = findViewById(R.id.themeSwitch);

        // Initialize user manager
        userManager = UserManager.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        themeSwitch.setChecked(isDarkMode);


        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {


            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        login.setOnClickListener(v -> {
            if (validateUsername() && validatePassword()) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                performLogin(username, password);
            }
        });

        signup.setOnClickListener(v -> {
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
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

    private void performLogin(String username, String password) {
        new Thread(() -> {
            try {
                JsonObject payload = new JsonObject();
                payload.addProperty("username", username);
                payload.addProperty("password", password);

                Response response = ApiHandler.sendPost("/auth/login", payload);
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseReader(new StringReader(responseBody)).getAsJsonObject();

                    // Check if the "success" field exists and is not null
                    if (jsonResponse.has("message") && !jsonResponse.get("message").isJsonNull()) {


                            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("username", username);
                            editor.apply();

                            runOnUiThread(() -> {
                                userManager.setLoggedIn(true);
                                User userDetails = new Gson().fromJson(jsonResponse.getAsJsonObject("user"), User.class);
                                userManager.setCurrentUser(username, userDetails);

                                // Notify listeners about the user details change
                                userManager.notifyUserDetailsChanged(userDetails);

                                Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
                                startActivity(i);
                            });

                    } else {
                        runOnUiThread(() -> showErrorMessage(response.message()));
                    }
                } else {
                    runOnUiThread(() -> showErrorMessage(response.message()));
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }).start();
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