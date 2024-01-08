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
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ImageView bookImage;

    // Your existing code...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_history);

        db = new DatabaseHelper(this);
        btnTambah = findViewById(R.id.buttonSave);
        bookImage = findViewById(R.id.bookImage);

        tanggalPinjam = findViewById(R.id.tanggalPinjam);
        tanggalKembali = findViewById(R.id.tanggalKembali);

        // Initialize date picker
        initializeDatePicker();

        // Your existing code...
    }

    // Add the following methods

    private void initializeDatePicker() {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tanggalPinjam.setOnClickListener(view -> showDatePickerDialog(tanggalPinjam));
        tanggalKembali.setOnClickListener(view -> showDatePickerDialog(tanggalKembali));
    }

    private void showDatePickerDialog(final TextInputEditText editText) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                create_history.this, // Use the correct reference to your activity
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
