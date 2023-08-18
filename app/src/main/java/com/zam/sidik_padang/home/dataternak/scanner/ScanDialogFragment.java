package com.zam.sidik_padang.home.dataternak.scanner;

import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.databinding.FragmentScanDialogBinding;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     ScanDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class ScanDialogFragment extends BottomSheetDialogFragment implements DecoratedBarcodeView.TorchListener {

    private FragmentScanDialogBinding binding;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private boolean flashOn = false;
    private ImageView btnFlash;
    private ScanListener listener;
    private List<String> permisions = new ArrayList<>();
    private final int PERMISSION_CODE = 12345;

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            setResult(result);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    public ScanDialogFragment(ScanListener listener) {
        this.listener = listener;
    }

    public static ScanDialogFragment newInstance(ScanListener listener) {
        return new ScanDialogFragment(listener);
    }

    private void setResult(BarcodeResult result) {
        listener.scanCallback(result.getText());
        this.dismiss();
    }
    private final ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        Log.e("activityResultLauncher", ""+result.toString());
        boolean areAllGranted = true;
        for(Boolean b : result.values()) {
            areAllGranted = areAllGranted && b;
        }

        if(areAllGranted) {
            openScanner();
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentScanDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //String[] appPerms;
        //appPerms = new String[]{Manifest.permission.CAMERA};
        //this.activityResultLauncher.launch(appPerms);
        openScanner();
    }

    private void openScanner() {
        barcodeView = binding.barcodeScanner;
        btnFlash = binding.flash;
        btnFlash.setOnClickListener(v -> switchFlashlight());
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39, BarcodeFormat.CODE_128);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(requireActivity().getIntent());
        barcodeView.decodeContinuous(callback);
        barcodeView.setStatusText("Arahkan kamera ke barcode");
        barcodeView.setTorchListener(this);
        beepManager = new BeepManager(requireActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onTorchOn() {
        btnFlash.setImageResource(R.drawable.ic_flash_on_black_24dp);
    }

    @Override
    public void onTorchOff() {
        btnFlash.setImageResource(R.drawable.ic_flash_off_black_24dp);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding != null && barcodeView != null) barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (binding != null && barcodeView != null) barcodeView.pauseAndWait();
    }

    public void switchFlashlight() {
        if (!flashOn) {
            flashOn = true;
            barcodeView.setTorchOn();
        } else {
            flashOn = false;
            barcodeView.setTorchOff();
        }
    }

    public interface ScanListener {
        void scanCallback(String result);
    }
}