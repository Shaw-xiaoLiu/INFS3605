package com.example.infs3605.ui.history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.infs3605.R;
import com.example.infs3605.SurveyActivity;
import com.example.infs3605.adapters.SurveyResultAdapter;
import com.example.infs3605.constants.FirestoreCollections;
import com.example.infs3605.constants.Surveys;
import com.example.infs3605.databinding.FragmentHistoryBinding;
import com.example.infs3605.dto.SurveyItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private final ArrayList<SurveyItem> surveyItems = new ArrayList<>();
    private final ArrayList<String> surveyIds = new ArrayList<>();
    private SurveyResultAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        setupViews();
        getAnsweredSurveys();
    }

    private void setupViews() {
        adapter = new SurveyResultAdapter(requireContext(), Surveys.surveyImages, Surveys.getChoices(), surveyItems);
        binding.recycler.setAdapter(adapter);
        binding.swipe.setOnRefreshListener(this::getAnsweredSurveys);
    }

    private void getAnsweredSurveys() {
        binding.swipe.setRefreshing(true);

        firestore.collection(FirestoreCollections.SURVEYS)
                .document(user.getUid())
                .collection(FirestoreCollections.WELCOME_SURVEYS)
                .get()
                .addOnCompleteListener(task -> {
                    binding.swipe.setRefreshing(false);

                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            surveyIds.add(snapshot.getId());
                        }

                        ArrayList<SurveyItem> surveyList = (ArrayList<SurveyItem>) task.getResult().toObjects(SurveyItem.class);
                        updateSurveyItems(surveyList);
                    }
                });
    }

    private void updateSurveyItems(ArrayList<SurveyItem> surveyList) {
        int oldSize = surveyItems.size();
        surveyItems.clear();
        adapter.notifyItemRangeRemoved(0, oldSize);

        surveyItems.addAll(surveyList);
        adapter.notifyItemRangeInserted(0, surveyList.size());
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) getAnsweredSurveys();
    });

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.history_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_edit_result) launchSurveyActivity();
        return super.onOptionsItemSelected(item);
    }

    private void launchSurveyActivity() {
        Intent intent = new Intent(requireContext(), SurveyActivity.class);
        intent.putExtra(SurveyActivity.EXTRA_SURVEY_ITEMS, surveyItems);
        intent.putExtra(SurveyActivity.EXTRA_SURVEY_IDS, surveyIds);
        resultLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}