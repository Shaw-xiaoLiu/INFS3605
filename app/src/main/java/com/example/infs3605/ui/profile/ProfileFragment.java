package com.example.infs3605.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.infs3605.EditProfileActivity;
import com.example.infs3605.R;
import com.example.infs3605.constants.FirestoreCollections;
import com.example.infs3605.databinding.FragmentProfileBinding;
import com.example.infs3605.dto.Gender;
import com.example.infs3605.dto.UserDetail;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private UserDetail userDetail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        getUserDetails();
        binding.btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            intent.putExtra(EditProfileActivity.EXTRA_USER_DETAIL, userDetail);
            resultLauncher.launch(intent);
        });

        binding.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            requireActivity().finish();
        });
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) getUserDetails();
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
                        new MaterialAlertDialogBuilder(requireContext())
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}