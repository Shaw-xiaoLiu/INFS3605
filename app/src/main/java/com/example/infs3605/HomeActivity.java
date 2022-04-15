package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.infs3605.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    public static final String IS_SURVEY_ANSWERED = "IS_SURVEY_ANSWERED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        boolean isSurveyAnswered = intent != null && intent.getBooleanExtra(IS_SURVEY_ANSWERED, false);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_scanner, R.id.navigation_history, R.id.navigation_profile)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_home);
        if (navHostFragment == null) return;
        NavController navController = navHostFragment.getNavController();

        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);
        navGraph.setStartDestination(isSurveyAnswered ? R.id.navigation_dashboard : R.id.navigation_scanner);
        navController.setGraph(navGraph);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}