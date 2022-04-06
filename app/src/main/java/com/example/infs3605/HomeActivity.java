package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infs3605.adapters.SurveyResultAdapter;
import com.example.infs3605.constants.FirestoreCollections;
import com.example.infs3605.databinding.ActivityHomeBinding;
import com.example.infs3605.dto.Gender;
import com.example.infs3605.dto.SurveyItem;
import com.example.infs3605.dto.UserDetail;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private UserDetail userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        getUserDetails();
        getAnsweredSurveys();
        binding.btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra(EditProfileActivity.EXTRA_USER_DETAIL, userDetail);
            resultLauncher.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) getUserDetails();
    });

    private void getUserDetails() {
        firestore.collection(FirestoreCollections.USER_DETAILS)
                .document(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        userDetail = task.getResult().toObject(UserDetail.class);
                        if (userDetail == null) {
                            userDetail = new UserDetail();
                            userDetail.setAge(0);
                            userDetail.setEmail(user.getEmail());
                            userDetail.setGender(Gender.Unknown);
                            userDetail.setName(user.getDisplayName());
                            userDetail.setPhoneNumber(user.getPhoneNumber());
                        }
                        setupUserDetailView(userDetail);
                    } else {
                        new MaterialAlertDialogBuilder(this)
                                .setTitle(R.string.title_error)
                                .setMessage(task.getException() + "")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }
                });
    }

    private void setupUserDetailView(UserDetail detail) {
        binding.tvWelcomeUser.setText(getString(R.string.title_welcome_user, detail.getName()));
        binding.tvEmail.setText(String.format("%s", detail.getEmail()));
        binding.tvPhone.setText(String.format("%s", detail.getPhoneNumber()));
        binding.tvAddress.setText(String.format("%s", detail.getAddress()));
        binding.tvGender.setText(String.format("%s", detail.getGender().name()));
        binding.tvAge.setText(String.format(Locale.ENGLISH, "%d", detail.getAge()));
    }

    private void getAnsweredSurveys() {
        firestore.collection(FirestoreCollections.SURVEYS)
                .document(user.getUid())
                .collection(FirestoreCollections.WELCOME_SURVEYS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        ArrayList<SurveyItem> surveyItems = (ArrayList<SurveyItem>) task.getResult().toObjects(SurveyItem.class);
                        setupAnsweredSurveysView(surveyItems);
                    }
                });
    }

    private void setupAnsweredSurveysView(ArrayList<SurveyItem> surveys) {
        int[] surveyImages = {R.drawable.survey_question_1, R.drawable.survey_question_2, R.drawable.survey_question_3};
        // Yes or No choices for each question
        ArrayList<Pair<Integer, Integer>> choices = new ArrayList<>();
        choices.add(new Pair<>(R.string.btn_yes, R.string.btn_no));
        choices.add(new Pair<>(R.string.btn_yes, R.string.btn_no));
        choices.add(new Pair<>(R.string.btn_yes, R.string.btn_no));

        SurveyResultAdapter adapter = new SurveyResultAdapter(this, surveyImages, choices, surveys);
        binding.recycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}