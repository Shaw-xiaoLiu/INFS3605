package com.example.infs3605.ui.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.infs3605.R;
import com.example.infs3605.SurveyActivity;
import com.example.infs3605.constants.FirestoreCollections;
import com.example.infs3605.databinding.FragmentScannerBinding;
import com.example.infs3605.dto.SurveyItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.ArrayList;
import java.util.Collections;

public class ScannerFragment extends Fragment implements DecoratedBarcodeView.TorchListener, BarcodeCallback {

    private static final int REQ_CAMERA_PERMISSION = 100;
    private boolean askedPermission = false;

    private FragmentScannerBinding binding;
    private MenuItem menuFlash;
    private BeepManager beepManager;

    private FirebaseUser user;
    private ArrayList<SurveyItem> surveyItems;
    private final ArrayList<String> surveyIds = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentScannerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.barcodeScanner.setTorchListener(this);
        binding.barcodeScanner.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(Collections.singletonList(BarcodeFormat.QR_CODE)));
        binding.barcodeScanner.decodeSingle(this);

        beepManager = new BeepManager(requireActivity());
        binding.btnLaunchSurvey.setOnClickListener(v -> launchSurveyActivity());

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) binding.btnLaunchSurvey.setEnabled(true);
        else getAnsweredSurveys();
    }

    private void getAnsweredSurveys() {
        FirebaseFirestore.getInstance().collection(FirestoreCollections.SURVEYS)
                .document(user.getUid())
                .collection(FirestoreCollections.WELCOME_SURVEYS)
                .get()
                .addOnCompleteListener(task -> {
                    binding.btnLaunchSurvey.setEnabled(true);
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            surveyIds.add(snapshot.getId());
                        }
                        surveyItems = (ArrayList<SurveyItem>) task.getResult().toObjects(SurveyItem.class);
                    } else {
                        binding.tvResult.setText(String.format("%s", task.getException()));
                    }
                });
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) navigateToHistoryFragment();
    });

    private void navigateToHistoryFragment() {
        ((BottomNavigationView) requireActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_history);
    }

    private void launchSurveyActivity() {
        Intent intent = new Intent(requireContext(), SurveyActivity.class);
        if (surveyItems != null) {
            intent.putExtra(SurveyActivity.EXTRA_SURVEY_ITEMS, surveyItems);
            intent.putExtra(SurveyActivity.EXTRA_SURVEY_IDS, surveyIds);
        }
        resultLauncher.launch(intent);
    }

    private boolean hasFlash() {
        return requireContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.scanner_menu, menu);
        menuFlash = menu.findItem(R.id.menu_flash);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_flash && hasFlash()) {
            if (item.getTitle().equals(getString(R.string.menu_flash_on))) {
                binding.barcodeScanner.setTorchOff();
            } else {
                binding.barcodeScanner.setTorchOn();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onTorchOn() {
        menuFlash.setIcon(R.drawable.ic_baseline_flashlight_on_24);
        menuFlash.setTitle(R.string.menu_flash_on);
    }

    @Override
    public void onTorchOff() {
        menuFlash.setIcon(R.drawable.ic_baseline_flashlight_off_24);
        menuFlash.setTitle(R.string.menu_flash_off);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQ_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.barcodeScanner.resume();
            } else {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Required camera permission")
                        .setMessage("App requires camera permission to scan QR code.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        openCameraWithPermission();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.barcodeScanner.pauseAndWait();
    }

    private void openCameraWithPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                binding.barcodeScanner.resume();
            } else if (!askedPermission) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQ_CAMERA_PERMISSION);
                askedPermission = true;
            }
        } else {
            binding.barcodeScanner.resume();
        }
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        beepManager.playBeepSoundAndVibrate();
        binding.barcodeScanner.pause();
        binding.barcodeScanner.setVisibility(View.GONE);
        binding.welcomeLayout.setVisibility(View.VISIBLE);
        binding.tvResult.setText(result.getText());
    }
}