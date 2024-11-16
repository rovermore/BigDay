package com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TipInfo;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.TipsTransferPresenter;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation.TipsTransferPresenterImpl;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by luismiguel on 27/10/17.
 */
@AndroidEntryPoint
public class TipsTransferActivity extends GenericActivity implements TipsTransferPresenter.View {

    public static final String PAYMENT_METHOD_EXTRA = "PAYMENT_METHOD_EXTRA";

    Unbinder mUnbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.scrollView)
    ScrollView mScrollview;
    @BindView(R.id.general_loading_view)
    View mLoadingView;
    @BindView(R.id.mainContainer)
    LinearLayout mMainContainer;

    private TipsTransferPresenterImpl mPresenter;
    private String mPaymentMethod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_transfer);

        mUnbinder = ButterKnife.bind(this);

        mPaymentMethod = getIntent().getExtras().getString(PAYMENT_METHOD_EXTRA, "");

        mPresenter = new TipsTransferPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, mPaymentMethod);
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
    public void configureView() {
        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        ((TextView) mToolbar.findViewById(R.id.genericToolbarTitle)).setText(getString(R.string.transfer_details_title));
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.app_transfer_tips_title));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back_toolbar_white));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showHideLoadingView(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TipsTransferActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
    }

    @Override
    public void showHideLoadingView(boolean show) {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void drawAdditionalTips(ArrayList<TipInfo> listTip) {
        CardView additionalNotesCardView = (CardView) getLayoutInflater().inflate(R.layout.additional_tips_layout, null, false);
        if (additionalNotesCardView != null) {
            LinearLayout tipsContainer = (LinearLayout) additionalNotesCardView.findViewById(R.id.additional_notes_tip_container);
            if (tipsContainer != null) {
                int position = 0;
                for (TipInfo tip : listTip) {
                    LinearLayout tipLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.tip_item_layout, null, false);
                    if (tipLayout != null) {
                        ((StyledTextView) tipLayout.findViewById(R.id.tip_title)).setText(tip.getTitle());
                        ((StyledTextView) tipLayout.findViewById(R.id.tip_content)).setText(tip.getDescription());
                        if (position == listTip.size() - 1) {
                            tipLayout.findViewById(R.id.separator).setVisibility(View.GONE);
                        }
                        position++;
                        tipsContainer.addView(tipLayout);
                    }
                }
                mMainContainer.addView(additionalNotesCardView);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        mUnbinder.unbind();
    }
}
