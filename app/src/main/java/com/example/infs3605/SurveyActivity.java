package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605.adapters.SurveyAdapter;
import com.example.infs3605.adapters.SurveyLayoutManager;
import com.example.infs3605.constants.FirestoreCollections;
import com.example.infs3605.constants.Surveys;
import com.example.infs3605.databinding.ActivitySurveyBinding;
import com.example.infs3605.dto.SurveyItem;
import com.example.infs3605.dto.SurveyType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;

public class SurveyActivity extends AppCompatActivity implements SurveyAdapter.OnAnswerCheckedListener {

    private ActivitySurveyBinding binding;
    private String userId;

    private final HashMap<Integer, String> answers = new HashMap<>();
    private int currentQuestion = 0;
    private SurveyLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySurveyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get user id to link with answer
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        userId = user.getUid();



        layoutManager = new SurveyLayoutManager(this, SurveyLayoutManager.HORIZONTAL, false);
        binding.recycler.setLayoutManager(layoutManager);

        SurveyAdapter adapter = new SurveyAdapter(this, Surveys.surveyImages, Surveys.surveyQuestions, Surveys.getChoices(), this);
        binding.recycler.setAdapter(adapter);

        binding.btnNext.setOnClickListener(v -> performNextAction());
        binding.btnPrevious.setOnClickListener(v -> performPreviousAction());

        binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                layoutManager.setScrollEnabled(false);
            }
        });
    }

    private void performNextAction() {
        int questionCount = Surveys.surveyQuestions.length;

        if (answers.containsKey(currentQuestion)) {
            if (currentQuestion == questionCount - 1) {
                prepareForm();
            } else if (currentQuestion == questionCount - 2) {
                binding.btnNext.setText(R.string.btn_submit);
                binding.btnNext.setIconResource(R.drawable.ic_baseline_send_24);
            }

            if (currentQuestion < questionCount - 1) currentQuestion++;
            binding.btnPrevious.setEnabled(true);
        } else {
            Snackbar.make(binding.getRoot(), R.string.msg_fill_all_fields, 500)
                    .setAction(android.R.string.ok, v -> {
                    }).show();
        }

        layoutManager.setScrollEnabled(true);
        binding.recycler.smoothScrollToPosition(currentQuestion);
    }

    private void performPreviousAction() {
        if (currentQuestion == 1) {
            binding.btnPrevious.setEnabled(false);
        }

        currentQuestion--;
        binding.btnNext.setText(R.string.btn_next);
        binding.btnNext.setIconResource(R.drawable.ic_baseline_navigate_next_24);
        layoutManager.setScrollEnabled(true);
        binding.recycler.smoothScrollToPosition(currentQuestion);
    }

    private void prepareForm() {
        ArrayList<SurveyItem> surveyItems = new ArrayList<>();

        for (int i = 0; i < Surveys.surveyQuestions.length; i++) {
            String surveyQuestion = getString(Surveys.surveyQuestions[i]);
            String surveyAnswer = answers.get(i);
            SurveyItem surveyItem = new SurveyItem(SurveyType.MULTIPLE_CHOICE, surveyQuestion, surveyAnswer, null);
            surveyItems.add(surveyItem);
        }

        submitForm(surveyItems);
    }

    private void submitForm(ArrayList<SurveyItem> surveyItems) {
        binding.btnNext.setEnabled(false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        CollectionReference ref = db.collection(FirestoreCollections.SURVEYS)
                .document(userId)
                .collection(FirestoreCollections.WELCOME_SURVEYS);

        for (SurveyItem surveyItem : surveyItems) {
            batch.set(ref.document(), surveyItem);
        }

        batch.commit().addOnCompleteListener(task -> {
            binding.btnNext.setEnabled(true);

            if (task.isSuccessful()) {
                Intent intent = new Intent(this, HomeActivity.class);
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

    @Override
    public void onAnswerChecked(int position, String answer) {
        answers.put(position, answer);
    }
}



