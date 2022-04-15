package com.example.infs3605;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SurveyActivity extends AppCompatActivity implements SurveyAdapter.OnAnswerCheckedListener {

    public static final String EXTRA_SURVEY_ITEMS = "EXTRA_SURVEY_ITEMS";
    public static final String EXTRA_SURVEY_IDS = "EXTRA_SURVEY_IDS";
    private ActivitySurveyBinding binding;
    private String userId;

    private final HashMap<Integer, String> answers = new HashMap<>();
    private int currentQuestionIndex = 0;
    private SurveyLayoutManager layoutManager;

    private ArrayList<String> surveyIds;
    private boolean isUpdateForm = false;

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

        // check whether user create or update survey
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_SURVEY_ITEMS)) {
            isUpdateForm = true;
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

            ArrayList<SurveyItem> surveyItems = intent.getParcelableArrayListExtra(EXTRA_SURVEY_ITEMS);
            surveyIds = intent.getStringArrayListExtra(EXTRA_SURVEY_IDS);
            ArrayList<String> userAnswers = new ArrayList<>();

            for (int i = 0; i < surveyItems.size(); i++) {
                String answer = surveyItems.get(i).getChoiceAnswer();
                userAnswers.add(answer);
                answers.put(i, answer);
            }

            SurveyAdapter adapter = new SurveyAdapter(this, Surveys.surveyImages, Surveys.surveyQuestions, Surveys.getChoices(), userAnswers, this);
            binding.recycler.setAdapter(adapter);
            checkAnswers();
        } else {
            SurveyAdapter adapter = new SurveyAdapter(this, Surveys.surveyImages, Surveys.surveyQuestions, Surveys.getChoices(), null, this);
            binding.recycler.setAdapter(adapter);
        }

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

        if (answers.containsKey(currentQuestionIndex)) {
            if (currentQuestionIndex == questionCount - 1) {
                prepareForm();
            } else if (currentQuestionIndex == questionCount - 2) {
                binding.btnNext.setText(isUpdateForm ? R.string.btn_update : R.string.btn_submit);
                binding.btnNext.setIconResource(R.drawable.ic_baseline_send_24);
            }

            if (currentQuestionIndex < questionCount - 1) currentQuestionIndex++;
            binding.btnPrevious.setEnabled(true);
        } else {
            Snackbar.make(binding.getRoot(), R.string.msg_fill_all_fields, 500)
                    .setAction(android.R.string.ok, v -> {
                    }).show();
        }

        layoutManager.setScrollEnabled(true);
        binding.recycler.smoothScrollToPosition(currentQuestionIndex);
    }

    private void performPreviousAction() {
        if (currentQuestionIndex == 1) {
            binding.btnPrevious.setEnabled(false);
        }

        currentQuestionIndex--;
        binding.btnNext.setText(R.string.btn_next);
        binding.btnNext.setIconResource(R.drawable.ic_baseline_navigate_next_24);
        layoutManager.setScrollEnabled(true);
        binding.recycler.smoothScrollToPosition(currentQuestionIndex);
    }

    private boolean hasDeniedAnswer(int position) {
        return answers.containsKey(position) && TextUtils.equals(answers.get(position), getString(R.string.btn_no));
    }

    private void checkAnswers() {
        if (hasDeniedAnswer(0) && hasDeniedAnswer(1) && hasDeniedAnswer(2)) {
            binding.warningLayout.setVisibility(View.VISIBLE);
            binding.tvWarning.setText(R.string.warn_survey_denied_all);
            binding.btnLearnMore.setOnClickListener(v -> launchUrl(R.string.url_survey_denied_all));
        } else if (hasDeniedAnswer(0) && hasDeniedAnswer(1)) {
            binding.warningLayout.setVisibility(View.VISIBLE);
            binding.tvWarning.setText(R.string.warn_survey_denied_2);
            binding.btnLearnMore.setOnClickListener(v -> launchUrl(R.string.url_survey_denied_2));
        } else {
            binding.warningLayout.setVisibility(View.GONE);
        }
    }

    private void launchUrl(@StringRes int url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(url)));
            startActivity(intent);
        } catch (ActivityNotFoundException ignored) {
            Snackbar.make(binding.getRoot(), R.string.err_no_browsers, Snackbar.LENGTH_SHORT).show();
        }
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

        for (int i = 0; i < surveyItems.size(); i++) {
            DocumentReference reference = isUpdateForm ? ref.document(surveyIds.get(i)) : ref.document();
            batch.set(reference, surveyItems.get(i));
        }

        batch.commit().addOnCompleteListener(task -> {
            binding.btnNext.setEnabled(true);

            if (task.isSuccessful()) {
                if (isUpdateForm) {
                    setResult(RESULT_OK);
                } else {
                    Intent intent = new Intent(this, SubmittedActivity.class);
                    startActivity(intent);
                }
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAnswerChecked(int position, String answer) {
        answers.put(position, answer);

        if (answers.containsKey(0) && answers.containsKey(1)) {
            checkAnswers();
        }
    }
}



