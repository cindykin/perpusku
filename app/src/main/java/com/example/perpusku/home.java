package com.example.perpusku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity {

    private Button btnMember, btnHistory, btnBook;

    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnMember = findViewById(R.id.btnToMember);
        btnHistory = findViewById(R.id.btnToHistory);
        btnBook = findViewById(R.id.btnToBook);

        btnMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, list_member.class);
                startActivity(intent);
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, list_book.class);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, list_history.class);
                startActivity(intent);
            }
        });
    }
}