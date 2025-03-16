package com.example.cv_maker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.InputStream;

public class ResumePreviewActivity extends AppCompatActivity {
    private ImageView userImageView;
    private TextView userNameTextView, userEmailTextView, userPhoneTextView;
    private TextView userSummaryTextView;
    private TextView userCertificationTextView, certificationDateTextView;
    private TextView userSchoolTextView, eduStartDateTextView, eduEndDateTextView;
    private TextView userCompanyTextView, workStartDateTextView, workEndDateTextView;
    private TextView refNameTextView, refDesignationTextView, refOrganizationTextView, refEmailTextView, refPhoneTextView;

    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resume_preview);

        // Set window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        userImageView = findViewById(R.id.userImageView);
        userNameTextView = findViewById(R.id.userNameTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userPhoneTextView = findViewById(R.id.userPhoneTextView);
        userSummaryTextView = findViewById(R.id.userSummaryTextView);
        userCertificationTextView = findViewById(R.id.userCertificationTextView);
        certificationDateTextView = findViewById(R.id.certificationDateTextView);
        userSchoolTextView = findViewById(R.id.userSchoolTextView);
        eduStartDateTextView = findViewById(R.id.eduStartDateTextView);
        eduEndDateTextView = findViewById(R.id.eduEndDateTextView);
        userCompanyTextView = findViewById(R.id.userCompanyTextView);
        workStartDateTextView = findViewById(R.id.workStartDateTextView);
        workEndDateTextView = findViewById(R.id.workEndDateTextView);
        refNameTextView = findViewById(R.id.refNameTextView);
        refDesignationTextView = findViewById(R.id.refDesignationTextView);
        refOrganizationTextView = findViewById(R.id.refOrganizationTextView);
        refEmailTextView = findViewById(R.id.refEmailTextView);
        refPhoneTextView = findViewById(R.id.refPhoneTextView);

        // Load data from SharedPreferences
        loadData();

        // Image Picker Implementation
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                if (selectedImageUri != null) {
                    saveProfileImageUri(selectedImageUri);
                    setProfileImage(selectedImageUri);
                }
            }
        });

        userImageView.setOnClickListener(v -> openImagePicker());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImageLauncher.launch(intent);
    }

    private void saveProfileImageUri(Uri uri) {
        SharedPreferences profilePrefs = getSharedPreferences("ProfileData", MODE_PRIVATE);
        SharedPreferences.Editor editor = profilePrefs.edit();
        editor.putString("profileImageUri", uri.toString());
        editor.apply();
    }

    private void setProfileImage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            userImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            // Display a fallback image or message
            userImageView.setImageResource(R.drawable.ic_bio); // Add a placeholder image
            Toast.makeText(this, "Could not load profile image", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        SharedPreferences personalPrefs = getSharedPreferences("PersonalData", MODE_PRIVATE);
        SharedPreferences summaryPrefs = getSharedPreferences("SummaryData", MODE_PRIVATE);
        SharedPreferences certPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences eduPrefs = getSharedPreferences("EducationData", MODE_PRIVATE);
        SharedPreferences expPrefs = getSharedPreferences("ExperienceData", MODE_PRIVATE);
        SharedPreferences refPrefs = getSharedPreferences("ReferenceData", MODE_PRIVATE);
        SharedPreferences profilePrefs = getSharedPreferences("ProfileData", MODE_PRIVATE);

        // Personal Details
        userNameTextView.setText(personalPrefs.getString("name", "Not Provided"));
        userEmailTextView.setText(personalPrefs.getString("email", "Not Provided"));
        userPhoneTextView.setText(personalPrefs.getString("phone", "Not Provided"));

        // Load Profile Image
        String profileUriString = profilePrefs.getString("profileImageUri", null);
        if (profileUriString != null) {
            try {
                Uri imageUri = Uri.parse(profileUriString);
                // Try multiple methods to load the image
                try {
                    // Method 1: Direct setImageURI (simplest, but most likely to fail on MIUI)
                    userImageView.setImageURI(null); // Clear previous image
                    userImageView.setImageURI(imageUri);
                } catch (Exception e1) {
                    try {
                        // Method 2: Load via bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        userImageView.setImageBitmap(bitmap);
                    } catch (Exception e2) {
                        try {
                            // Method 3: Using input stream
                            InputStream is = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            is.close();
                            userImageView.setImageBitmap(bitmap);
                        } catch (Exception e3) {
                            // All methods failed, use default image
                            Log.e("ResumePreviewActivity", "All methods to load image failed", e3);
                            userImageView.setImageResource(R.drawable.ic_bio); // Use your default image
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("ResumePreviewActivity", "Error loading profile image URI", e);
                userImageView.setImageResource(R.drawable.ic_bio); // Use your default image
            }
        }

        // Summary
        userSummaryTextView.setText(summaryPrefs.getString("summary", "Not Provided"));

        // Certification
        userCertificationTextView.setText(certPrefs.getString("certification", "Not Provided"));
        certificationDateTextView.setText(certPrefs.getString("date", "Not Provided"));

        // Education
        userSchoolTextView.setText(eduPrefs.getString("school", "Not Provided"));
        eduStartDateTextView.setText(eduPrefs.getString("startDate", "Not Provided"));
        eduEndDateTextView.setText(eduPrefs.getString("endDate", "Not Provided"));

        // Experience
        userCompanyTextView.setText(expPrefs.getString("companyName", "Not Provided"));
        workStartDateTextView.setText(expPrefs.getString("startDate", "Not Provided"));
        workEndDateTextView.setText(expPrefs.getString("endDate", "Not Provided"));

        // References
        refNameTextView.setText(refPrefs.getString("referenceName", "Not Provided"));
        refDesignationTextView.setText(refPrefs.getString("designation", "Not Provided"));
        refOrganizationTextView.setText(refPrefs.getString("organization", "Not Provided"));
        refEmailTextView.setText(refPrefs.getString("referenceEmail", "Not Provided"));
        refPhoneTextView.setText(refPrefs.getString("referencePhone", "Not Provided"));
    }
}