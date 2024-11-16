package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Locations;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Payout;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.LocationMapPresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation.LocationMapPresenterImpl;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by luismiguel on 28/8/17.
 */
@AndroidEntryPoint
public class LocationMapActivity extends GenericActivity implements OnMapReadyCallback, LocationMapPresenter.View {

    public static final String PAYOUT_LOCATIONS_EXTRA = "PAYOUT_LOCATIONS_EXTRA";
    public static final String ACTIVITY_TITLE = "ACTIVITY_TITLE";

    Unbinder mUnbinder;

    private GoogleMap mMap;
    private Payout mPayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.general_loading_view)
    View mLoadingView;

    private LocationMapPresenterImpl mPresenter;
    private String mTitle;
    private Marker mLastMarkerClicked = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);
        mUnbinder = ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            mPayout = getIntent().getParcelableExtra(PAYOUT_LOCATIONS_EXTRA);
            mTitle = getIntent().getStringExtra(ACTIVITY_TITLE);
        }

        mPresenter = new LocationMapPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, mPayout);
        mPresenter.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.destroy();
        }

        mUnbinder.unbind();
    }


    @Override
    public void configureView() {
        mLoadingView.setVisibility(View.VISIBLE);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.default_grey_control));

        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(mTitle);
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setTextColor(getResources().getColor(R.color.dark_grey_text));;

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_arrow_back_grey));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        mPresenter.mapReady();
    }

    @Override
    public void hideLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void drawPoisInMap(Payout payout) {
        if (mPayout != null && mPayout.getLocations() != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (Locations location : mPayout.getLocations()) {
                if (!TextUtils.isEmpty(location.getLatitude()) && !TextUtils.isEmpty(location.getLongitude())) {

                    try {
                        LatLng position = new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()));

                        mMap.addMarker(new MarkerOptions()
                                .position(position)
                                .title(location.getLocationName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_off)));

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                if (mLastMarkerClicked != null) {
                                    mLastMarkerClicked.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_off));
                                }

                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on));
                                mLastMarkerClicked = marker;
                                return false;
                            }
                        });

                        builder.include(position);
                    } catch (Exception e) {
                        Log.e("STACK", "----------------------",e);
                    }
                }
            }

            try {

                int padding = 60; // offset from edges of the map in pixels
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                animateCamera(cu);

            } catch (Exception e) {
                Log.e("STACK", "----------------------",e);
            }
        }
    }

    @Override
    public void showMessageError() {
        if ((this).hasWindowFocus()) {
            (new DialogExt()).showSingleActionErrorDialog(this, getString(R.string.location_map_error_title), getString(R.string.location_map_error_description),
                    new DialogExt.OnPositiveClick() {
                        @Override
                        public void onClick() {
                            onBackPressed();
                        }
                    });
        }
    }

    private void animateCamera(final CameraUpdate cu) {
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cu);
            }
        });
    }

}
