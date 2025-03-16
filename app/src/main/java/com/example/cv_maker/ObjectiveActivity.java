package com.example.cv_maker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ObjectiveActivity extends AppCompatActivity {

    private Button saveBtn, cancelBtn;
    private EditText summaryField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_objective);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        summaryField = findViewById(R.id.summaryField);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);


        saveBtn.setOnClickListener(v -> {
            String summary = summaryField.getText().toString();

            if (summary.isEmpty()) {
                Toast.makeText(this, "Please enter a summary", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save data in SharedPreferences
            saveSummaryData(summary);

            Toast.makeText(this, "Summary saved successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to MainActivity
            startActivity(new Intent(ObjectiveActivity.this, MainActivity.class));
            finish();
        });

        cancelBtn.setOnClickListener(v -> {
            startActivity(new Intent(ObjectiveActivity.this, MainActivity.class));
            finish();
        });
    }

    private void saveSummaryData(String summary) {
        SharedPreferences sharedPreferences = getSharedPreferences("SummaryData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("summary", summary);
        editor.apply();

    }
}