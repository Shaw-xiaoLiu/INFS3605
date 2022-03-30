package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infs3605.databinding.ActivityHomeBinding;
import com.example.infs3605.dto.SurveyItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    public static final String EXTRA_SURVEY_ITEMS = "EXTRA_SURVEY_ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        binding.tvWelcomeUser.setText(getString(R.string.title_welcome_user, user.getDisplayName()));

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_SURVEY_ITEMS)) {
            List<SurveyItem> surveys = intent.getParcelableArrayListExtra(EXTRA_SURVEY_ITEMS);
            setupViews(surveys);
        }

    }

    private void setupViews(List<SurveyItem> surveys) {
        // Currently we have 3 surveys
        if (surveys.size() < 3) return;

        if (TextUtils.equals(surveys.get(0).getChoiceAnswer(), "Yes")) {
            binding.rBtnQuestion1Yes.setChecked(true);
        } else {
            binding.rBtnQuestion1No.setChecked(true);
        }

        if (TextUtils.equals(surveys.get(1).getChoiceAnswer(), "Yes")) {
            binding.rBtnQuestion2Yes.setChecked(true);
        } else {
            binding.rBtnQuestion2No.setChecked(true);
        }

        if (TextUtils.equals(surveys.get(2).getChoiceAnswer(), "Yes")) {
            binding.rBtnQuestion3Yes.setChecked(true);
        } else {
            binding.rBtnQuestion3No.setChecked(true);
        }
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