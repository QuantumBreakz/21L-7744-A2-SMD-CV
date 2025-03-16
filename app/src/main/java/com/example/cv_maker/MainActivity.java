package com.example.cv_maker;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView avatarSection, bioSection, objectiveSection, academicSection, workSection, qualificationsSection, endorsementsSection;
    private MaterialButton previewResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        avatarSection = findViewById(R.id.openAvatarSection);
        bioSection = findViewById(R.id.openBioSection);
        objectiveSection = findViewById(R.id.openObjectiveSection);
        academicSection = findViewById(R.id.openAcademicSection);
        workSection = findViewById(R.id.openWorkSection);
        qualificationsSection = findViewById(R.id.openQualificationsSection);
        endorsementsSection = findViewById(R.id.openEndorsementsSection);
        previewResume = findViewById(R.id.openPreview);

        avatarSection.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AvatarActivity.class));
        });
        bioSection.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BioActivity.class));
        });
        objectiveSection.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ObjectiveActivity.class));
        });
        academicSection.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AcademicActivity.class));
        });
        workSection.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, WorkActivity.class));
        });
        qualificationsSection.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ReferenceActivity.class));
        });
        endorsementsSection.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, EndorsementsActivity.class));
        });
        previewResume.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ResumePreviewActivity.class));
        });
    }
}