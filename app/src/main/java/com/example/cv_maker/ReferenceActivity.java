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

public class ReferenceActivity extends AppCompatActivity {

    private Button saveBtn, cancelBtn;
    private EditText nameField, designationField, organizationField, emailField, phoneField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reference);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nameField = findViewById(R.id.nameField);
        designationField = findViewById(R.id.designationField);
        organizationField = findViewById(R.id.organizationField);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        saveBtn.setOnClickListener(v -> {
            String referenceName = nameField.getText().toString();
            String designation = designationField.getText().toString();
            String organization = organizationField.getText().toString();
            String referenceEmail = emailField.getText().toString();
            String referencePhone = phoneField.getText().toString();

            if (referenceName.isEmpty() || designation.isEmpty() || organization.isEmpty() || referenceEmail.isEmpty() || referencePhone.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save data in SharedPreferences
            saveReferenceData(referenceName, designation, organization, referenceEmail, referencePhone);

            Toast.makeText(this, "Details saved successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to MainActivity
            startActivity(new Intent(ReferenceActivity.this, MainActivity.class));
            finish();
        });

        cancelBtn.setOnClickListener(v -> {
            startActivity(new Intent(ReferenceActivity.this, MainActivity.class));
            finish();
        });
    }

    private void saveReferenceData(String name, String designation, String organization, String email, String phone) {
        SharedPreferences sharedPreferences = getSharedPreferences("ReferenceData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("referenceName", name);
        editor.putString("designation", designation);
        editor.putString("organization", organization);
        editor.putString("referenceEmail", email);
        editor.putString("referencePhone", phone);
        editor.apply();
    }
}