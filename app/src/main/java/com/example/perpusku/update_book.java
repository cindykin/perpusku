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

public class update_book extends AppCompatActivity {
    private String id_book, title, author, imgPath, sinopsis;

    private ImageView bookImage;
    private Button saveBtn, chooseImageBtn;
    private EditText bookName, bookAuthor, bookDescription;
    private DatabaseHelper db;
    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);
        db = new DatabaseHelper(this);

        bookImage = findViewById(R.id.bookImage);
        bookName = findViewById(R.id.bookName);
        bookAuthor = findViewById(R.id.authorName);
        bookDescription = findViewById(R.id.bookDescription);
        saveBtn = findViewById(R.id.buttonSave);

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
                updateBook();
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
            bookName.setText(title);
            bookAuthor.setText(author);
            bookDescription.setText(sinopsis);

            // Load the image from imgPath and set it to the ImageView
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
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

    private void updateBook() {
        String updatedTitle = bookName.getText().toString().trim();
        String updatedAuthor = bookAuthor.getText().toString().trim();
        String updatedDescription = bookDescription.getText().toString().trim();

        if (!updatedTitle.isEmpty() && !updatedAuthor.isEmpty() && !updatedDescription.isEmpty() && imageUri != null) {
            // Save the updated image to internal storage
            String imagePath = saveImageToInternalStorage(imageUri, "book_" + id_book + ".png");

            if (imagePath != null) {
                boolean isUpdated = db.updateBook(Integer.parseInt(id_book), updatedTitle, updatedAuthor, imagePath, updatedDescription);

                if (isUpdated) {
                    Toast.makeText(update_book.this, "Book Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(update_book.this, list_book.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(update_book.this, "Failed to update book", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(update_book.this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(update_book.this, "Please fill all fields and choose an image", Toast.LENGTH_SHORT).show();
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
