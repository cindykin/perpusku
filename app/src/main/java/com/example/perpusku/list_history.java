package com.example.perpusku;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class list_history extends AppCompatActivity {

    private Button btnTambah;
    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private ArrayList<String> id_history, borrowDate, returnDate, bookId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history);

        btnTambah = findViewById(R.id.btnTambah);
        recyclerView = findViewById(R.id.recycler_view);
        db = new DatabaseHelper(this);

        id_history = new ArrayList<>();
        borrowDate = new ArrayList<>();
        returnDate = new ArrayList<>();
        bookId = new ArrayList<>();
        userId = new ArrayList<>();

        historyAdapter historyAdapter = new historyAdapter(
                list_history.this,
                id_history,
                borrowDate,
                returnDate,
                bookId,
                userId
        );

        recyclerView.setAdapter(historyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(list_history.this));

        storeHistoryDataToArray();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(list_history.this, create_history.class);
                startActivity(intent);
            }
        });
    }

    void storeHistoryDataToArray() {
        try {
            Cursor cursor = db.getAllHistoryRecords();
            if (cursor.getCount() == 0) {
                Toast.makeText(list_history.this, "History data is empty", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    id_history.add(cursor.getString(0));
                    borrowDate.add(cursor.getString(1));
                    returnDate.add(cursor.getString(2));
                    bookId.add(cursor.getString(3));
                    userId.add(cursor.getString(4));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(list_history.this, "Error retrieving history data", Toast.LENGTH_SHORT).show();
        }
    }
}
