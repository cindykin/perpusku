package com.example.perpusku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register_user extends AppCompatActivity {
    private Button loginButton, registerButton;
    private EditText usernameEditText, passwordEditText, emailEditText;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        registerButton = findViewById(R.id.registerButton);
        usernameEditText = findViewById(R.id.usernameText);
        passwordEditText = findViewById(R.id.passwordText);
        emailEditText = findViewById(R.id.emailText);
        loginButton = findViewById(R.id.loginButton);

        db = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register_user.this, MainActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(register_user.this, "Masukan email, username, dan password", Toast.LENGTH_SHORT).show();
                } else {
                    long result = db.addUser(username, password,"", email);
                    if (result != -1) {
                        Toast.makeText(register_user.this, "Registrasi Sukses", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(register_user.this, home.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(register_user.this, "Registrasi Gagal", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}