package com.example.perpusku;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class create_member extends AppCompatActivity {
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ImageView bookImage;
    private DatabaseHelper db;
    private Button btnTambah;
    private Uri imageUri;

    private EditText username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member);

        db = new DatabaseHelper(this);
        btnTambah = findViewById(R.id.buttonSave);
        bookImage = findViewById(R.id.bookImage);

        // Register ActivityResultLauncher
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        imageUri = result.getData().getData();
                        bookImage.setImageURI(imageUri);
                    }
                });

        bookImage.setOnClickListener(v -> openImageChooser());

        btnTambah.setOnClickListener(view -> {
            username = findViewById(R.id.usernameText);
            email = findViewById(R.id.emailText);
            password = findViewById(R.id.passwordText);

            String namaUser = username.getText().toString();
            String emailUser = email.getText().toString();
            String passwordUser = password.getText().toString();

            if (imageUri != null) {
                long insertedId = db.addUser(namaUser, emailUser, "", passwordUser);

                if (insertedId != -1) {
                    String imagePath = saveImageToInternalStorage(imageUri, "user_" + insertedId + ".png");

                    if (imagePath != null) {
                        boolean updated = db.updateUserImage(insertedId, imagePath);

                        if (updated) {
                            Toast.makeText(create_member.this, "Book Added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(create_member.this, list_member.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(create_member.this, "Failed to update image path", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(create_member.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(create_member.this, "Can't Added Data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(create_member.this, "Can't add image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private String saveImageToInternalStorage(Uri uri, String fileName) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                File directory = getDir("images", MODE_PRIVATE);
                File file = new File(directory, fileName);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
                return file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
