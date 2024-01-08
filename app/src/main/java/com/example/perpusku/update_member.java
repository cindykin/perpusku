package com.example.perpusku;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class update_member extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView bookImage;

    private String id_user, username, email, userImage;

    private EditText txtUsername, txtEmail, password;
    private DatabaseHelper db;
    private Button btnSave;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_member);
        db = new DatabaseHelper(this);
        bookImage = findViewById(R.id.bookImage);
        txtUsername = findViewById(R.id.usernameText);
        btnSave = findViewById(R.id.buttonSave);
        txtEmail = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        getSetIntentData();
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }
    void getSetIntentData() {
        if (getIntent().hasExtra("userId")
                && getIntent().hasExtra("username")
                && getIntent().hasExtra("email")
                && getIntent().hasExtra("imgPath")
        ) {

            id_user = getIntent().getStringExtra("userId");
            username = getIntent().getStringExtra("username");
            email = getIntent().getStringExtra("email");
            userImage = getIntent().getStringExtra("imgPath");

            txtUsername.setText(username);
            txtEmail.setText(email);

            Bitmap bitmap = BitmapFactory.decodeFile(userImage);
            bookImage.setImageBitmap(bitmap);

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            bookImage.setImageURI(imageUri);
        }
    }

    private void updateUser() {
        txtUsername = findViewById(R.id.usernameText);
        txtEmail = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);

        String namaUser = txtUsername.getText().toString();
        String emailUser = txtEmail.getText().toString();
        String passwordUser = password.getText().toString();

        if (!namaUser.isEmpty() && !emailUser.isEmpty() && !passwordUser.isEmpty() && imageUri != null) {
            // Save the updated image to internal storage
            String imagePath = saveImageToInternalStorage(imageUri, "user_" + id_user + ".png");

            if (imagePath != null) {
                boolean isUpdated = db.updateUser(Integer.parseInt(id_user), namaUser, passwordUser, imagePath, emailUser);

                if (isUpdated) {
                    Toast.makeText(update_member.this, "User Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(update_member.this, list_member.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(update_member.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(update_member.this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(update_member.this, "Please fill all fields and choose an image", Toast.LENGTH_SHORT).show();
        }

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