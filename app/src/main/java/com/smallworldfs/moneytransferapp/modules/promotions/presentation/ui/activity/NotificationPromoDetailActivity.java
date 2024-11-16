package com.smallworldfs.moneytransferapp.modules.promotions.presentation.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.promotions.presentation.presenters.NotificationPromoDetailPresenter;
import com.smallworldfs.moneytransferapp.modules.promotions.presentation.presenters.implementation.NotificationPromoDetailPresenterImpl;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by pedro del castillo on 7/9/17
 */
@AndroidEntryPoint
public class NotificationPromoDetailActivity extends GenericActivity implements NotificationPromoDetailPresenter.View {

    Unbinder mUnbinder;
    private NotificationPromoDetailPresenterImpl mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.subtitle)
    TextView mSubtitle;
    @BindView(R.id.greeting)
    TextView mGreeting;
    @BindView(R.id.body)
    TextView mBody;
    @BindView(R.id.time_period)
    TextView mTimePeriod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_promo_detail);
        mUnbinder = ButterKnife.bind(this);

        mPresenter = new NotificationPromoDetailPresenterImpl(AndroidSchedulers.mainThread(), this, this, this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Configure View Methods
     */
    @Override
    public void configureView() {

        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.notification_promo_detail_title));

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_toolbar_white));
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
