package com.smallworldfs.moneytransferapp.modules.tracking.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.tracking.presentation.TrackerPresenter;
import com.smallworldfs.moneytransferapp.modules.tracking.presentation.implementation.TrackerPresenterImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TrackingFragment extends Fragment implements TrackerPresenter.View {

    public static final String SCREEN_NAME = "TRACKING_FRAGMENT";

    private Unbinder mUnbinder;

    private TrackerPresenter.Presenter mPresenter;

    @BindView(R.id.aux_loading_view)
    RelativeLayout mAuxLoadingView;
    @BindView(R.id.general_loading_view)
    RelativeLayout mLoadingView;
    @BindView(R.id.main_content_tracker)
    ConstraintLayout mainLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_tracker, null, false);
        mUnbinder = ButterKnife.bind(this, view);

        mPresenter = new TrackerPresenterImpl(getContext(), getActivity());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideGeneralLoadingView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    //------------------
    // ON CLICKS
    //------------------

    @OnClick(R.id.track_order_qr)
    public void onQrTrackerClick() {
        if (areNeededPermissionsGranted()) {
            showGeneralLoadingView();
            mPresenter.openQrTracker();
        }
    }

    @OnClick({R.id.track_order_mtn})
    public void onMtnTrackerClick() {
        mPresenter.openMtnTracker();
    }

    public boolean areNeededPermissionsGranted() {
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (permissions.size() == 0) {
                return true;
            } else {
                String[] requestPermissionsList = new String[permissions.size()];
                requestPermissions(permissions.toArray(requestPermissionsList), 1);
                return false;
            }
        } else {
            return true;
        }
    }

    public void showGeneralLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
        mAuxLoadingView.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    public void hideGeneralLoadingView() {
        mLoadingView.setVisibility(View.GONE);
        mAuxLoadingView.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (getActivity() != null && permissions.length > 0) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                mPresenter.openQrTracker();
            }
        }
    }
}
