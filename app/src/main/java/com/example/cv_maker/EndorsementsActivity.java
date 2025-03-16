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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EndorsementsActivity extends AppCompatActivity {

    private EditText qualificationTitle;
    private EditText issuanceDate;
    private Button btnSubmit;
    private Button btnBack;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_endorsements);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize preferences
        preferences = getSharedPreferences("ResumeData", MODE_PRIVATE);

        // Initialize views
        qualificationTitle = findViewById(R.id.inputQualificationTitle);
        issuanceDate = findViewById(R.id.qualificationDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        // Load saved data if exists
        loadSavedData();

        // Set up date picker listener
        issuanceDate.setOnClickListener(v -> showDatePickerDialog());

        btnSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                saveQualificationData();
                Toast.makeText(this, "Qualification information saved", Toast.LENGTH_SHORT).show();
                navigateToDashboard();
            }
        });

        btnBack.setOnClickListener(v -> {
            navigateToDashboard();
        });
    }

    private void loadSavedData() {
        String savedTitle = preferences.getString("qualification_title", "");
        String savedDate = preferences.getString("qualification_date", "");

        if (!savedTitle.isEmpty()) {
            qualificationTitle.setText(savedTitle);
        }

        if (!savedDate.isEmpty()) {
            issuanceDate.setText(savedDate);
        }
    }

    private boolean validateInputs() {
        String title = qualificationTitle.getText().toString().trim();
        String date = issuanceDate.getText().toString().trim();

        if (title.isEmpty()) {
            qualificationTitle.setError("Please enter qualification title");
            return false;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select issuance date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveQualificationData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("qualification_title", qualificationTitle.getText().toString().trim());
        editor.putString("qualification_date", issuanceDate.getText().toString().trim());
        editor.apply();
    }

    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Format date using SimpleDateFormat
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    String formattedDate = dateFormat.format(selectedCalendar.getTime());
                    issuanceDate.setText(formattedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void navigateToDashboard() {
        startActivity(new Intent(EndorsementsActivity.this, MainActivity.class));
        finish();
    }
}