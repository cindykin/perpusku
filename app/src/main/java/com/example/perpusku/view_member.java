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

public class view_member extends AppCompatActivity {
    private String id_user, username, email, userImage;

    private ImageView imageUser;
    private Button editBtn, deleteBtn;
    private TextView txtUsername, txtEmail;

    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_view_member);
        txtUsername = findViewById(R.id.usernameText);
        txtEmail = findViewById(R.id.emailText);
        imageUser = findViewById(R.id.user_Image);
        editBtn = findViewById(R.id.btnEdit);
        deleteBtn = findViewById(R.id.btnDelete);
        getSetIntentData();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSetIntentData();
                Intent intent = new Intent(view_member.this, update_member.class);
                intent.putExtra("userId", id_user);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("imgPath", userImage);

                startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_user != null && !id_user.isEmpty()) {
                    try {
                        int bookId = Integer.parseInt(id_user); // Convert id_book to int
                        boolean recordDeleteBw = db.deleteUser(bookId);
                        if (recordDeleteBw) {
                            Toast.makeText(getApplicationContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(view_member.this, list_member.class);
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

    private Bitmap loadImageFromStorage(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return BitmapFactory.decodeFile(path);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    void getSetIntentData() {
        if (getIntent().hasExtra("userId")
                && getIntent().hasExtra("username")
                && getIntent().hasExtra("email")
                && getIntent().hasExtra("imgPath")) {

            id_user = getIntent().getStringExtra("userId");
            username = getIntent().getStringExtra("username");
            email = getIntent().getStringExtra("email");
            userImage = getIntent().getStringExtra("imgPath");

            txtUsername.setText(username);
            txtEmail.setText(email);

            try {
                InputStream inputStream = new FileInputStream(userImage);
                Bitmap bitmap = loadImageFromStorage(userImage);
                if (bitmap != null) {
                    imageUser.setImageBitmap(bitmap);
                } else {
                    // Handle the case where the file does not exist
                    Toast.makeText(getApplicationContext(), "Image file not found", Toast.LENGTH_SHORT).show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }

}