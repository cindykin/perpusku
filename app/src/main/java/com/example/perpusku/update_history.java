package com.example.perpusku;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class update_history extends AppCompatActivity {
    private String id_history, borrowDate, returnDate, bookId, userId;

    private ImageView bookImage;
    private Button saveBtn, chooseImageBtn;
    private EditText borrowDateEditText, returnDateEditText, bookIdEditText, userIdEditText;
    private DatabaseHelper db;
    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_history);
        db = new DatabaseHelper(this);

        bookImage = findViewById(R.id.bookImage);  // Update with the actual ID
        borrowDateEditText = findViewById(R.id.tanggalPinjam);  // Update with the actual ID
        returnDateEditText = findViewById(R.id.tanggalKembali);  // Update with the actual ID
        bookIdEditText = findViewById(R.id.id_book);  // Update with the actual ID
        userIdEditText = findViewById(R.id.id_user);  // Update with the actual ID
        saveBtn = findViewById(R.id.buttonSave);  // Update with the actual ID

        getSetIntentData();

        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHistory();
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
            borrowDateEditText.setText(borrowDate);
            returnDateEditText.setText(returnDate);
            bookIdEditText.setText(bookId);
            userIdEditText.setText(userId);

            // Load the image from imgPath and set it to the ImageView
            // Note: You need to have a mechanism to store and retrieve image paths for history items.
            // For simplicity, let's assume you have it in the database, and you can fetch it here.

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

    private void updateHistory() {
        String updatedBorrowDate = borrowDateEditText.getText().toString().trim();
        String updatedReturnDate = returnDateEditText.getText().toString().trim();
        String updatedBookId = bookIdEditText.getText().toString().trim();
        String updatedUserId = userIdEditText.getText().toString().trim();

        if (!updatedBorrowDate.isEmpty() && !updatedReturnDate.isEmpty() && !updatedBookId.isEmpty() && !updatedUserId.isEmpty() && imageUri != null) {
            // Save the updated image to internal storage
            String imagePath = saveImageToInternalStorage(imageUri, "history_" + id_history + ".png");

            if (imagePath != null) {
                boolean isUpdated = db.updateHistoryRecord(Integer.parseInt(id_history), updatedBorrowDate, updatedReturnDate, updatedBookId, updatedUserId, imagePath);

                if (isUpdated) {
                    Toast.makeText(update_history.this, "History Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(update_history.this, list_history.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(update_history.this, "Failed to update history", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(update_history.this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(update_history.this, "Please fill all fields and choose an image", Toast.LENGTH_SHORT).show();
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
