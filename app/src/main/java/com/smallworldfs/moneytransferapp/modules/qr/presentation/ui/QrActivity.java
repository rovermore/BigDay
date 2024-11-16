package com.smallworldfs.moneytransferapp.modules.qr.presentation.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.qr.presentation.navigator.QrNavigator;
import com.smallworldfs.moneytransferapp.modules.qr.presentation.presenter.QrPresenter;
import com.smallworldfs.moneytransferapp.utils.camera.BarcodeGraphic;
import com.smallworldfs.moneytransferapp.utils.camera.CameraSource;
import com.smallworldfs.moneytransferapp.utils.camera.CameraSourcePreview;
import com.smallworldfs.moneytransferapp.utils.camera.GraphicOverlay;
import com.smallworldfs.moneytransferapp.utils.camera.QrScanningProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QrActivity extends GenericActivity implements QrPresenter.View, QrScanningProcessor.QrUpdateListener {

    @Inject
    QrPresenter.Presenter presenter;

    private static final int RC_HANDLE_GMS = 9001;

    private Unbinder unbinder;
    @BindView(R.id.general_loading_view)
    RelativeLayout mLoadingView;
    @BindView(R.id.aux_loading_view)
    RelativeLayout mAuxLoadingView;
    @BindView(R.id.qr_container)
    ConstraintLayout mainLayout;

    private CameraSource cameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    private boolean qrDetected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qr_tracker);
        unbinder = ButterKnife.bind(this);

        presenter.setView(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (areNeededPermissionsGranted()) {
            qrDetected = false;
            startCameraSource();
        }
    }

    @Override
    public void configureView() {
        mPreview = findViewById(R.id.preview);

        mGraphicOverlay = findViewById(R.id.graphicOverlay);

        Toolbar mToolbar = findViewById(R.id.toolbar);

        mToolbar.setNavigationIcon(getDrawable(R.drawable.ic_back_toolbar_white));
        mToolbar.setContentInsetsAbsolute(0, mToolbar.getContentInsetStartWithNavigation());
        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        mLoadingView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        showGeneralLoadingView();
    }

    @Override
    public void showTransactionProgress(String data) {
        if (this.hasWindowFocus()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showGeneralLoadingView();
                }
            });
        }
        QrNavigator.navigateToTransactionActivity(this, data);
        cameraSource.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCamera();
        unbinder.unbind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    @Override
    public void startCameraSource() throws SecurityException {
        cameraSource = new CameraSource(this, mGraphicOverlay);
        cameraSource.setMachineLearningFrameProcessor(new QrScanningProcessor(this));

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                this);
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (cameraSource != null) {
            try {
                mPreview.start(cameraSource, mGraphicOverlay);
                hideGeneralLoadingView();
            } catch (IOException e) {
                android.util.Log.e("", "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void stopCamera() {
        if (cameraSource != null) {
            if (mPreview != null) {
                mPreview.release();
            }
        }
    }

    @Override
    public void onBackPressed() {
        showGeneralLoadingView();
        super.onBackPressed();
    }

    @Override
    public void showGeneralLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
        mAuxLoadingView.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideGeneralLoadingView() {
        mLoadingView.setVisibility(View.GONE);
        mAuxLoadingView.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            finish();
        }
    }

    public boolean areNeededPermissionsGranted() {
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (permissions.size() == 0) {
                return true;
            } else {
                String[] requestPermissionsList = new String[permissions.size()];
                ActivityCompat.requestPermissions(this, permissions.toArray(requestPermissionsList), 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onQrDetected(FirebaseVisionBarcode qr) {
        if (qr.getRawValue() != null && !qrDetected) {
            qrDetected = true;
            showTransactionProgress(qr.getRawValue());
        }
    }
}
