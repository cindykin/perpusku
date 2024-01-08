package com.example.perpusku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.File;
import java.util.Arrays;

public class view_book extends AppCompatActivity {
    private String id_book, title, author, imgPath, sinopsis;

    private ImageView bookImage;
    private Button editBtn, deleteBtn;
    private TextView txtTitle, txtAuthor, txtSinopsis;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        db = new DatabaseHelper(this);
        txtTitle = findViewById(R.id.bookName);
        txtAuthor = findViewById(R.id.authorName);
        txtSinopsis = findViewById(R.id.bookDescription);
        bookImage = findViewById(R.id.bookImage);
        deleteBtn = findViewById(R.id.btnDelete);
        editBtn = findViewById(R.id.btnEdit);
        getSetIntentData(); // Call the method to retrieve intent data

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSetIntentData();
                Intent intent = new Intent(view_book.this, update_book.class);
                intent.putExtra("id_book", id_book);
                intent.putExtra("title", title);
                intent.putExtra("author", author);
                intent.putExtra("imgPath", imgPath);
                intent.putExtra("sinopsis", sinopsis);
                startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_book != null && !id_book.isEmpty()) {
                    try {
                        int bookId = Integer.parseInt(id_book); // Convert id_book to int
                        boolean recordDeleteBw = db.deleteBook(bookId);
                        if (recordDeleteBw) {
                            Toast.makeText(getApplicationContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(view_book.this, list_book.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Data Gagal Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Invalid Book ID", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Book ID is Null or Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    void getSetIntentData() {
        if (getIntent().hasExtra("id_book")
                && getIntent().hasExtra("title")
                && getIntent().hasExtra("author")
                && getIntent().hasExtra("imgPath")
                && getIntent().hasExtra("sinopsis")) {

            id_book = getIntent().getStringExtra("id_book");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            imgPath = getIntent().getStringExtra("imgPath");
            sinopsis = getIntent().getStringExtra("sinopsis");

            // Setting data to TextViews, ImageView, etc.
            txtTitle.setText(title);
            txtAuthor.setText(author);
            txtSinopsis.setText(sinopsis);

            // Set the image using imgPath (load it into ImageView)
            // Load the image from imgPath and set it to the ImageView
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            bookImage.setImageBitmap(bitmap);

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }


}
