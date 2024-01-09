package com.example.perpusku;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Arrays;

public class view_history extends AppCompatActivity {
    private String id_history, borrowDate, returnDate, bookId, userId;

    private ImageView bookImage;
    private Button editBtn, deleteBtn;
    private TextView txtBorrowDate, txtReturnDate, txtBookId, txtUserId;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        db = new DatabaseHelper(this);
        txtBorrowDate = findViewById(R.id.tanggalPinjam);  // Update with the actual ID
        txtReturnDate = findViewById(R.id.tanggalKembali);  // Update with the actual ID
        txtBookId = findViewById(R.id.id_book);  // Update with the actual ID
        txtUserId = findViewById(R.id.id_user);  // Update with the actual ID
        bookImage = findViewById(R.id.bookImage);  // Update with the actual ID
        deleteBtn = findViewById(R.id.btnDelete);  // Update with the actual ID
        editBtn = findViewById(R.id.btnEdit);  // Update with the actual ID
        getSetIntentData(); // Call the method to retrieve intent data

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSetIntentData();
                Intent intent = new Intent(view_history.this, update_history.class);
                intent.putExtra("id_history", id_history);
                intent.putExtra("borrow_date", borrowDate);
                intent.putExtra("return_date", returnDate);
                intent.putExtra("book_id", bookId);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_history != null && !id_history.isEmpty()) {
                    try {
                        int historyId = Integer.parseInt(id_history); // Convert id_history to int
                        boolean recordDeleteBw = db.deleteHistoryRecord(historyId);
                        if (recordDeleteBw) {
                            Toast.makeText(getApplicationContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(view_history.this, list_history.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Data Gagal Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Invalid History ID", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "History ID is Null or Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getSetIntentData() {
        if (getIntent().hasExtra("id_history")
                && getIntent().hasExtra("borrow_date")
                && getIntent().hasExtra("return_date")
                && getIntent().hasExtra("book_id")
                && getIntent().hasExtra("user_id")) {

            id_history = getIntent().getStringExtra("id_history");
            borrowDate = getIntent().getStringExtra("borrow_date");
            returnDate = getIntent().getStringExtra("return_date");
            bookId = getIntent().getStringExtra("book_id");
            userId = getIntent().getStringExtra("user_id");

            // Setting data to TextViews, ImageView, etc.
            txtBorrowDate.setText(borrowDate);
            txtReturnDate.setText(returnDate);
            txtBookId.setText(bookId);
            txtUserId.setText(userId);

            // Set the image using imgPath (load it into ImageView)
            // Note: You need to have a mechanism to store and retrieve image paths for history items.
            // For simplicity, let's assume you have it in the database, and you can fetch it here.

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }
}
