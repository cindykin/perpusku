package com.example.perpusku;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.google.android.material.textfield.TextInputEditText;


public class create_history extends AppCompatActivity {

    private DatabaseHelper db;
    private TextInputEditText tanggalPinjam;
    private TextInputEditText tanggalKembali;
    private SimpleDateFormat dateFormatter;
    private Button btnTambah;

    // Your existing code...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_history);

        db = new DatabaseHelper(this);
        btnTambah = findViewById(R.id.buttonSave);

        tanggalPinjam = findViewById(R.id.tanggalPinjam);
        tanggalKembali = findViewById(R.id.tanggalKembali);

        // Initialize date picker
        initializeDatePicker();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve other input fields if needed
                String bookId = ((TextInputEditText) findViewById(R.id.id_book)).getText().toString();
                String userId = ((TextInputEditText) findViewById(R.id.id_user)).getText().toString();
                String borrowDate = tanggalPinjam.getText().toString();
                String returnDate = tanggalKembali.getText().toString();

                // Add your logic to save the history data to the database
                long insertedId = db.addHistory(bookId, userId, borrowDate, returnDate);

                if (insertedId != -1) {
                    Toast.makeText(create_history.this, "History Added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(create_history.this, list_history.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(create_history.this, "Failed to add history", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Add the following methods

    private void initializeDatePicker() {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tanggalPinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(tanggalPinjam);
            }
        });
        tanggalKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(tanggalKembali);
            }
        });
    }

    private void showDatePickerDialog(final TextInputEditText editText) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                create_history.this,
                (DatePicker datePicker, int year, int month, int day) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, day);
                    editText.setText(dateFormatter.format(selectedDate.getTime()));
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
