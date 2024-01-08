package com.example.perpusku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private Button loginButton, registerButton;
    private EditText usernameEditText, passwordEditText;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loginButton = findViewById(R.id.loginButton);
        usernameEditText = findViewById(R.id.usernameText);
        passwordEditText = findViewById(R.id.passwordText);
        registerButton = findViewById(R.id.registerButton);
        db = new DatabaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, register_user.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Masukan username dan password", Toast.LENGTH_SHORT).show();
                    } else{
                    boolean checkuser = db.checkUser(username, password);
                    if(checkuser){
                        Toast.makeText(MainActivity.this, "Login Sukses", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, home.class);
                        startActivity(intent);
                    } else{
                        Toast.makeText(MainActivity.this, "Login Gagal, username atau password tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                }
                }
            }
        );

    }
}
