package com.example.infs3605.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.infs3605.HomeActivity;
import com.example.infs3605.R;
import com.example.infs3605.databinding.ActivityRegisterBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(v -> checkRegisterDetails());

    }

    private void updateUI(boolean isRegistering) {
        binding.btnRegister.setEnabled(!isRegistering);
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear all previous activities
        startActivity(intent);
        finish();
    }

    private void checkRegisterDetails() {
        String name = Objects.requireNonNull(binding.edtName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString().trim();

        if (name.isEmpty()) {
            binding.edtNameLayout.setError(getString(R.string.err_blank, getString(R.string.hint_name)));
        } else if (email.isEmpty()) {
            binding.edtEmailLayout.setError(getString(R.string.err_blank, getString(R.string.hint_email)));
        } else if (password.isEmpty()) {
            binding.edtPasswordLayout.setError(getString(R.string.err_blank, getString(R.string.hint_password)));
        } else if (password.length() < 6) {
            binding.edtPassword.setError(getString(R.string.err_less_pw_limit));
        } else {
            registerNow(name, email, password);
        }
    }

    private void registerNow(String name, String email, String password) {
        updateUI(true);

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        updateUserProfile(name);
                    } else {
                        updateUI(false);
                        Snackbar.make(binding.getRoot(), task.getException() + "", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserProfile(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            user.updateProfile(changeRequest).addOnCompleteListener(task -> {
                updateUI(false);
                if (task.isSuccessful()) {
                    launchHomeActivity();
                } else {
                    Snackbar.make(binding.getRoot(), task.getException() + "", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }
}