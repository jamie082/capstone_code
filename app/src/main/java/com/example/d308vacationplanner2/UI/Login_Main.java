package com.example.d308vacationplanner2.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308vacationplanner2.R;

public class Login_Main extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;

    private final String CORRECT_USERNAME = "admin";
    private final String CORRECT_PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        loginButton = findViewById(R.id.my_button);


        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();


            if (username.equals(CORRECT_USERNAME) && password.equals(CORRECT_PASSWORD)) {
                Toast.makeText(Login_Main.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login_Main.this, MainActivity.class);
                startActivity(intent);
                // Optionally navigate to next screen
            } else {
                Toast.makeText(Login_Main.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}