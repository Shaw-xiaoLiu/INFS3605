package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.infs3605.constants.FirestoreCollections;
import com.example.infs3605.databinding.ActivityMainBinding;
import com.example.infs3605.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkAuth();
    }

    private void checkAuth() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            checkWelcomeSurvey(user.getUid());
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void checkWelcomeSurvey(String userId) {
        FirebaseFirestore.getInstance()
                .collection(FirestoreCollections.SURVEYS)
                .document(userId)
                .collection(FirestoreCollections.WELCOME_SURVEYS)
                .get()
                .addOnCompleteListener(task -> {
                    binding.progress.setVisibility(View.GONE);
                    boolean isSurveyAnswered = task.isSuccessful() && !task.getResult().isEmpty();
                    launchHomeActivity(isSurveyAnswered);
                });
    }

    private void launchHomeActivity(boolean isSurveyAnswered) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.IS_SURVEY_ANSWERED, isSurveyAnswered);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}