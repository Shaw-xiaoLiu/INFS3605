package com.example.infs3605.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.infs3605.HomeActivity;
import com.example.infs3605.R;
import com.example.infs3605.constants.FirestoreCollections;
import com.example.infs3605.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> checkLoginDetails());
        binding.btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

    }

    private void updateUI(boolean isLoggingIn) {
        binding.btnLogin.setEnabled(!isLoggingIn);
    }

    private void launchHomeActivity(boolean isSurveyAnswered) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear all previous activities
        intent.putExtra(HomeActivity.IS_SURVEY_ANSWERED, isSurveyAnswered);
        startActivity(intent);
        finish();
    }

    private void checkLoginDetails() {
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString().trim();

        if (email.isEmpty()) {
            binding.edtEmailLayout.setError(getString(R.string.err_blank, getString(R.string.hint_email)));
        } else if (password.isEmpty()) {
            binding.edtPasswordLayout.setError(getString(R.string.err_blank, getString(R.string.hint_password)));
        } else if (password.length() < 6) {
            binding.edtPassword.setError(getString(R.string.err_less_pw_limit));
        } else {
            loginNow(email, password);
        }
    }

    private void loginNow(String email, String password) {
        updateUI(true);

        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        updateUI(false);
                        checkWelcomeSurvey();
                    } else {
                        Snackbar.make(binding.getRoot(), task.getException() + "", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkWelcomeSurvey() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore.getInstance()
                    .collection(FirestoreCollections.SURVEYS)
                    .document(user.getUid())
                    .collection(FirestoreCollections.WELCOME_SURVEYS)
                    .get()
                    .addOnCompleteListener(task -> {
                        updateUI(false);
                        boolean isSurveyAnswered = task.isSuccessful() && !task.getResult().isEmpty();
                        launchHomeActivity(isSurveyAnswered);
                    });
        }
    }
}