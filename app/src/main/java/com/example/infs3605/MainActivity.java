package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infs3605.constants.FirestoreCollections;
import com.example.infs3605.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> launchLoginUI());

        // hide Login button while checking auth
        binding.btnLogin.setVisibility(View.GONE);
        checkAuth();
    }

    private void launchLoginUI() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(), result -> checkAuth()
    );

    private void checkAuth() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            checkWelcomeSurvey(user.getUid());
        } else {
            binding.btnLogin.setVisibility(View.VISIBLE);
        }
    }

    private void checkWelcomeSurvey(String userId) {
        FirebaseFirestore.getInstance()
                .collection(FirestoreCollections.SURVEYS)
                .document(userId)
                .collection(FirestoreCollections.WELCOME_SURVEYS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        launchHomeActivity();
                    } else {
                        launchScanActivity();
                    }
                });
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchSurveyActivity() {
        Intent intent = new Intent(this, SurveyActivity.class);
        startActivity(intent);
        finish();
    }
    private void launchScanActivity() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        finish();
    }
}