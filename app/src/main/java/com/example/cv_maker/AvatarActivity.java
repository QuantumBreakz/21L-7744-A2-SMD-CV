package com.example.cv_maker;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AvatarActivity extends AppCompatActivity {

    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private ImageView avatarImageView;
    private Uri userImageUri; // Store selected image URI
    private SharedPreferences userPreferences;

    // ActivityResultLauncher for image selection
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            try {
                                userImageUri = result.getData().getData();
                                if (userImageUri != null) {
                                    // Take persistent permission for the URI
                                    final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                                    getContentResolver().takePersistableUriPermission(
                                            userImageUri, takeFlags);

                                    avatarImageView.setImageURI(userImageUri);
                                } else {
                                    Toast.makeText(this, "Error selecting image", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e("AvatarActivity", "Error setting image URI", e);
                                Toast.makeText(this, "Failed to set profile picture", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avatar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        avatarImageView = findViewById(R.id.avatarImageView);
        Button saveButton = findViewById(R.id.saveButton);
        userPreferences = getSharedPreferences("ProfileData", MODE_PRIVATE);

        // Load saved profile picture if available
        loadUserAvatar();

        // Set click listeners
        avatarImageView.setOnClickListener(v -> checkStoragePermission());
        saveButton.setOnClickListener(v -> saveAndNavigate());
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            openGallery(); // No need for storage permissions in Android 13+
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);
            } else {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied. Cannot access gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveAndNavigate() {
        if (userImageUri == null) {
            Toast.makeText(this, "Please select a profile picture first", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Save the URI directly to SharedPreferences
            SharedPreferences.Editor editor = userPreferences.edit();
            editor.putString("profileImageUri", userImageUri.toString());
            editor.apply();

            Toast.makeText(this, "Profile picture saved successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to MainActivity
            startActivity(new Intent(AvatarActivity.this, MainActivity.class));
            finish();
        } catch (Exception e) {
            Log.e("AvatarActivity", "Error saving image URI", e);
            Toast.makeText(this, "Failed to save profile picture", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserAvatar() {
        String savedImageUri = userPreferences.getString("profileImageUri", null);
        if (savedImageUri != null) {
            try {
                userImageUri = Uri.parse(savedImageUri);
                avatarImageView.setImageURI(userImageUri);
            } catch (Exception e) {
                Log.e("AvatarActivity", "Error loading saved image", e);
            }
        }
    }
}