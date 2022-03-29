package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.infs3605.constants.FirestoreCollections;
import com.example.infs3605.databinding.ActivitySurveyBinding;
import com.example.infs3605.dto.SurveyItem;
import com.example.infs3605.dto.SurveyType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class SurveyActivity extends AppCompatActivity {

    private ActivitySurveyBinding binding;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySurveyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get user id to link with answer
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        userId = user.getUid();

        binding.cbSelectAllNo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.rBtnQuestion1No.setChecked(true);
                binding.rBtnQuestion2No.setChecked(true);
                binding.rBtnQuestion3No.setChecked(true);
            }
        });

        binding.btnSubmit.setOnClickListener(v -> checkForm());
    }

    private void checkForm() {
        int answer1Id = binding.rGroupQuestion1.getCheckedRadioButtonId();
        int answer2Id = binding.rGroupQuestion2.getCheckedRadioButtonId();
        int answer3Id = binding.rGroupQuestion3.getCheckedRadioButtonId();

        if (answer1Id == -1 || answer2Id == -1 || answer3Id == -1) {
            Snackbar.make(binding.getRoot(), R.string.msg_fill_all_fields, Snackbar.LENGTH_SHORT).show();
        } else {

            // Get selected answers
            String answer1 = ((MaterialRadioButton) findViewById(answer1Id)).getText().toString();
            String answer2 = ((MaterialRadioButton) findViewById(answer2Id)).getText().toString();
            String answer3 = ((MaterialRadioButton) findViewById(answer3Id)).getText().toString();

            // Create empty answer list
            ArrayList<SurveyItem> surveyItems = new ArrayList<>();

            // Add answers
            surveyItems.add(new SurveyItem(SurveyType.MULTIPLE_CHOICE, getString(R.string.survey_question_1), answer1, null));
            surveyItems.add(new SurveyItem(SurveyType.MULTIPLE_CHOICE, getString(R.string.survey_question_2), answer2, null));
            surveyItems.add(new SurveyItem(SurveyType.MULTIPLE_CHOICE, getString(R.string.survey_question_3), answer3, null));
            // We don't have checkbox question type yet, so "multipleAnswers" value is set to null

            submitForm(surveyItems);
        }
    }

    private void submitForm(ArrayList<SurveyItem> surveyItems) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        CollectionReference ref = db.collection(FirestoreCollections.SURVEYS)
                .document(userId)
                .collection(FirestoreCollections.WELCOME_SURVEYS);

        for (SurveyItem surveyItem : surveyItems) {
            batch.set(ref.document(), surveyItem);
        }

        batch.commit().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(HomeActivity.EXTRA_SURVEY_ITEMS, surveyItems);
                startActivity(intent);
                finish();
            } else {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.title_error)
                        .setMessage("" + task.getException())
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });
    }

}



