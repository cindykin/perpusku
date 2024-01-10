package com.example.perpusku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class list_member extends AppCompatActivity {
    private Button btnTambah;
    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private ArrayList<String> id_user, username, email,password,imgPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_member);
        btnTambah = findViewById(R.id.btnTambah);
        recyclerView = findViewById(R.id.recycler_view);
        db = new DatabaseHelper(this);

        id_user = new ArrayList<>();
        username = new ArrayList<>();
        email = new ArrayList<>();
        password = new ArrayList<>();
        imgPaths = new ArrayList<>();

        userAdapter uk = new userAdapter(
                list_member.this,
                id_user,
                username,
                email,
                password,
                imgPaths
        );

        // make default member
//        ArrayList<ArrayList<Object>> memb = new ArrayList<>();
//        ArrayList<Object> membs = new ArrayList<>();
//        membs.add("admin");
//        membs.add("adm@johny.com");
//        membs.add("admin");
//        membs.add(R.drawable.default_book_image);

        recyclerView.setAdapter(uk);
        recyclerView.setLayoutManager(new LinearLayoutManager(list_member.this));
        storeDataToArray();
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(list_member.this, create_member.class);
                startActivity(intent);

            }
        });
    }
    void storeDataToArray() {
        try {
            Cursor cursor = db.getAllUser();
            if (cursor.getCount() == 0) {
                Toast.makeText(list_member.this, "Data belum ada", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    id_user.add(cursor.getString(0));
                    username.add(cursor.getString(1));
                    password.add(cursor.getString(2));
                    email.add(cursor.getString(3));
                    imgPaths.add(cursor.getString(4));
                }

                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(list_member.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}