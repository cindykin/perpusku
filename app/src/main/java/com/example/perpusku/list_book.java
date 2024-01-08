package com.example.perpusku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class list_book extends AppCompatActivity {

    private Button btnTambah;
    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private ArrayList<String> id_buku, title, author, sinopsis, imgPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);
        btnTambah = findViewById(R.id.btnTambah);
        recyclerView = findViewById(R.id.recycler_view);
        db = new DatabaseHelper(this);

        id_buku = new ArrayList<>();
        title = new ArrayList<>();
        author = new ArrayList<>();
        sinopsis = new ArrayList<>();
        imgPaths = new ArrayList<>();

        bookAdapter bk = new bookAdapter(
                list_book.this,
                id_buku,
                title,
                author,
                imgPaths,
                sinopsis
        );

        recyclerView.setAdapter(bk);
        recyclerView.setLayoutManager(new LinearLayoutManager(list_book.this));
        storeDataToArray();
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(list_book.this, create_book.class);
                startActivity(intent);
            }
        });
    }

    void storeDataToArray() {
        try {
            Cursor cursor = db.getAllBooks();
            if (cursor.getCount() == 0) {
                Toast.makeText(list_book.this, "Data belum ada", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    id_buku.add(cursor.getString(0));
                    title.add(cursor.getString(1));
                    author.add(cursor.getString(2));
                    imgPaths.add(cursor.getString(3)); // Fetch image paths from the database
                    sinopsis.add(cursor.getString(4));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(list_book.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}