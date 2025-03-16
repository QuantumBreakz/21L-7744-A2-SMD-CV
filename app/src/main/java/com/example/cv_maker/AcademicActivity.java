package com.example.cv_maker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class AcademicActivity extends AppCompatActivity {

    private Button saveButton;
    private Button cancelButton;
    private EditText schoolInput;
    private EditText startDateInput;
    private EditText endDateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_academic);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        schoolInput = findViewById(R.id.schoolInput);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Attach DatePicker to Start Date and End Date fields
        startDateInput.setOnClickListener(v -> showDatePicker(startDateInput));
        endDateInput.setOnClickListener(v -> showDatePicker(endDateInput));

        saveButton.setOnClickListener(v -> {
            String school = schoolInput.getText().toString();
            String startDate = startDateInput.getText().toString();
            String endDate = endDateInput.getText().toString();

            if (school.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save data in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("EducationData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("school", school);
            editor.putString("startDate", startDate);
            editor.putString("endDate", endDate);
            editor.apply();

            Toast.makeText(this, "Details saved successfully", Toast.LENGTH_SHORT).show();

            // Navigate to MainActivity
            startActivity(new Intent(AcademicActivity.this, MainActivity.class));
            finish();
        });

        cancelButton.setOnClickListener(v -> {
            startActivity(new Intent(AcademicActivity.this, MainActivity.class));
            finish();
        });
    }

    // Method to show a Date Picker
    private void showDatePicker(EditText dateField) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Format the date as DD/MM/YYYY
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateField.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }
}