package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infs3605.constants.FirestoreCollections;
import com.example.infs3605.databinding.ActivityEditProfileBinding;
import com.example.infs3605.dto.Gender;
import com.example.infs3605.dto.UserDetail;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    public static final String EXTRA_USER_DETAIL = "EXTRA_USER_DETAIL";
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_USER_DETAIL)) {
            UserDetail userDetail = intent.getParcelableExtra(EXTRA_USER_DETAIL);
            userDetail = userDetail == null ? new UserDetail() : userDetail;
            setupUserDetail(userDetail);
        } else {
            setupUserDetail(new UserDetail());
        }

        binding.btnUpdate.setOnClickListener(v -> checkForm());

    }

    private void setupUserDetail(UserDetail userDetail) {
        if (userDetail.getName() != null) {
            binding.edtName.setText(userDetail.getName());
        }
        if (userDetail.getEmail() != null) {
            binding.edtEmail.setText(userDetail.getEmail());
        }
        if (userDetail.getPhoneNumber() != null) {
            binding.edtPhone.setText(userDetail.getPhoneNumber());
        }
        if (userDetail.getAddress() != null) {
            binding.edtAddress.setText(userDetail.getAddress());
        }
        if (userDetail.getAge() != 0) {
            binding.edtAge.setText(String.format(Locale.ENGLISH, "%d", userDetail.getAge()));
        }

        Gender gender = userDetail.getGender() == null ? Gender.Unknown : userDetail.getGender();
        ArrayAdapter<Gender> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Gender.values());
        binding.tvGender.setAdapter(adapter);
        binding.tvGender.setText(gender.name(), false);
    }

    private void checkForm() {
        String name = binding.edtName.getEditableText().toString().trim();
        String email = binding.edtEmail.getEditableText().toString().trim();
        String phone = binding.edtPhone.getEditableText().toString().trim();
        String address = binding.edtAddress.getEditableText().toString().trim();
        String ageStr = binding.edtAge.getEditableText().toString().trim();
        int age = ageStr.isEmpty() ? 0 : Integer.parseInt(ageStr);
        Gender gender = Gender.valueOf(binding.tvGender.getText().toString());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || age <= 0) {
            Snackbar.make(binding.getRoot(), R.string.msg_fill_all_fields, Snackbar.LENGTH_SHORT).show();
        } else {
            UserDetail detail = new UserDetail();
            detail.setName(name);
            detail.setEmail(email);
            detail.setPhoneNumber(phone);
            detail.setAddress(address);
            detail.setAge(age);
            detail.setGender(gender);
            updateUserDetail(detail);
        }
    }

    private void updateUserDetail(UserDetail detail) {
        Snackbar.make(binding.getRoot(), R.string.msg_updating_info, Snackbar.LENGTH_INDEFINITE).show();

        firestore.collection(FirestoreCollections.USER_DETAILS)
                .document(user.getUid())
                .set(detail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Snackbar.make(binding.getRoot(), task.getException() + "", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}