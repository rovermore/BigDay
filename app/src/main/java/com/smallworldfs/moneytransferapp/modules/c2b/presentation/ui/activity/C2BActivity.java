package com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryActivity.EXTRA_KEY;
import static com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryActivity.EXTRA_VALUE;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.SHOW_CHECKOUT_DIALOG_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.TRANSACTION_DATA;
import static com.smallworldfs.moneytransferapp.utils.Constants.C2B_CONSTANTS.B2B_TAG;
import static com.smallworldfs.moneytransferapp.utils.Constants.C2B_CONSTANTS.B2C_TAG;
import static com.smallworldfs.moneytransferapp.utils.Constants.C2B_CONSTANTS.C2B_TAG;
import static com.smallworldfs.moneytransferapp.utils.Constants.C2B_CONSTANTS.C2C_TAG;
import static com.smallworldfs.moneytransferapp.utils.Constants.C2B_CONSTANTS.MYSELF_TAG;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_ASSETS;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_EXTENSION;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.c2b.C2BContract;
import com.smallworldfs.moneytransferapp.modules.c2b.presentation.navigator.C2BNavigator;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class C2BActivity extends GenericActivity implements C2BContract.View {

    public static final String BENEFICIARY_TYPE = "beneficiary_type";
    public static final String FROM_TRANSACTIONAL = "from_transactional";

    @Inject
    C2BContract.Presenter mPresenter;

    @Inject
    LoginRepository loginRepository;

    private Unbinder mUnbinder;

    private int listHeight;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.general_loading_view)
    RelativeLayout mLoadingView;
    @BindView(R.id.aux_loading_view)
    RelativeLayout mAuxLoadingView;
    @BindView(R.id.error_view)
    View mErrorView;
    @BindView(R.id.relative_container)
    LinearLayout mainLayout;
    @BindView(R.id.scroll_content)
    LinearLayout scrollContent;
    @BindView(R.id.send_to_error_view)
    RelativeLayout emptyErrorView;
    @BindView(R.id.genericToolbarTitle)
    ImageView toolbarImage;
    @BindView(R.id.title)
    StyledTextView toolbarTitle;

    private Pair<String, String> country;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_c2b);
        mUnbinder = ButterKnife.bind(this);

        String first = "";
        String second = "";
        boolean fromTransactional = false;
        if (getIntent().getExtras() != null) {
            first = getIntent().getExtras().getString(EXTRA_KEY);
            second = getIntent().getExtras().getString(EXTRA_VALUE);
            fromTransactional = getIntent().getBooleanExtra(FROM_TRANSACTIONAL, false);
        }
        country = new Pair<>(first, second);

        mPresenter.setView(this, this, country, fromTransactional);
        mPresenter.create();
    }

    private void registerBrazeEvent(String eventName, Map<String, String> eventProperties) {
        trackEvent(
                new BrazeEvent(eventName, eventProperties, BrazeEventType.ACTION)
        );
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
    public void configureView(Pair<String, String> country) {
        ImageViewExtKt.loadCircularImage(
                toolbarImage,
                this,
                R.drawable.placeholder_country_adapter,
                FLAG_IMAGE_ASSETS + country.first + FLAG_IMAGE_EXTENSION
        );

        mToolbar.setTitle("");

        toolbarTitle.setText(country.second);

        mToolbar.setNavigationIcon(getDrawable(R.drawable.ic_back_toolbar_white));
        mToolbar.setContentInsetsAbsolute(0, mToolbar.getContentInsetStartWithNavigation());
        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        mLoadingView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        mLoadingView.setVisibility(View.VISIBLE);
        mAuxLoadingView.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    @Override
    public void showGeneralLoadingView() {
        hideErrorView();
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
    public void showErrorView() {
        hideGeneralLoadingView();
        emptyErrorView.setVisibility(View.VISIBLE);
        showErrorView(getString(R.string.generic_title_error), getString(R.string.generic_subtitle_top_error_view), mErrorView);
    }

    @Override
    public void hideErrorView() {
        hideErrorView(mErrorView);
        emptyErrorView.setVisibility(View.GONE);
    }

    @Override
    public void hideAllViews() {
        hideGeneralLoadingView();
        hideErrorView();
    }

    @Override
    public void addButton(final String beneficiaryType, final String beneficiaryLabel, final int numberOfButtons) {
        if (listHeight == 0) {
            scrollContent.post(new Runnable() {
                @Override
                public void run() {
                    listHeight = scrollContent.getHeight();
                    addButtonView(beneficiaryType, beneficiaryLabel, numberOfButtons);
                }
            });
        } else {
            addButtonView(beneficiaryType, beneficiaryLabel, numberOfButtons);
        }
    }

    private void onButtonClicked(String beneficiaryType) {
        mPresenter.click(beneficiaryType);
        trackBeneficiaryType(beneficiaryType);
        TreeMap<String, String> properties = new TreeMap<>();
        properties.put(BrazeEventProperty.DESTINATION_COUNTRY.getValue(), country.first);
        properties.put(BrazeEventProperty.BENEFICIARY_TYPE.getValue(), beneficiaryType);
        registerBrazeEvent(
                BrazeEventName.BENEFICIARY_CREATION_STEP_3.getValue(),
                properties
        );
    }
    private void trackBeneficiaryType(String beneficiaryType) {
        if (!beneficiaryType.isEmpty()) {
            trackEvent(new UserActionEvent(
                    ScreenName.SEND_MONEY_TO_SCREEN.getValue(),
                    "click_choose_beneficiary_type",
                    beneficiaryType,
                    getHierarchy(""),
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
            ));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            trackEvent(new UserActionEvent(
                    ScreenName.SEND_MONEY_TO_SCREEN.getValue(),
                    "click_back",
                    "",
                    getHierarchy(""),
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
            ));
            super.onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void addButtonView(final String beneficiaryType, String beneficiaryLabel, int numberOfButtons) {
        int height = (listHeight - numberOfButtons * convertDpToPixel(2, this)) / numberOfButtons;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height);

        layoutParams.setMargins(0, convertDpToPixel(2, this), 0, 0);

        View genericButton = LayoutInflater.from(this).inflate(R.layout.generic_c2b_button, scrollContent, false);

        genericButton.setLayoutParams(layoutParams);

        genericButton.setOnClickListener(view -> onButtonClicked(beneficiaryType));

        ConstraintLayout myself = genericButton.findViewById(R.id.myself_icon);
        ImageView clientIcon = genericButton.findViewById(R.id.client_icon);

        switch (beneficiaryType) {
            case MYSELF_TAG:
                ImageViewExtKt.loadCircularImage(
                        clientIcon,
                        this,
                        R.drawable.placeholder_country_adapter,
                        R.drawable.beneficiary_icn_empty
                );

                User user = loginRepository.getUser();
                myself.setVisibility(View.VISIBLE);
                myself.setBackground(getDrawable(R.drawable.beneficiary_icn_empty));
                String firstChar = "";
                String secondChar = "";

                if (user != null && user.getName() != null && !TextUtils.isEmpty(user.getName())) {
                    firstChar = String.valueOf(user.getName().charAt(0));
                }

                if (user != null && user.getSurname() != null && !TextUtils.isEmpty(user.getSurname())) {
                    secondChar = String.valueOf(user.getSurname().charAt(0));
                }
                ((StyledTextView) genericButton.findViewById(R.id.user_name_letter_text)).setText(firstChar + secondChar);

                genericButton.setContentDescription("myself_button");

                break;
            case C2B_TAG:
            case B2B_TAG:
                ImageViewExtKt.loadCircularImage(
                        clientIcon,
                        this,
                        R.drawable.placeholder_country_adapter,
                        R.drawable.beneficiary_icn_business
                );

                myself.setVisibility(View.INVISIBLE);

                genericButton.setContentDescription("business_button");

                break;
            case C2C_TAG:
            case B2C_TAG:
                ImageViewExtKt.loadCircularImage(
                        clientIcon,
                        this,
                        R.drawable.placeholder_country_adapter,
                        R.drawable.beneficiary_icn_soemonelse
                );

                myself.setVisibility(View.INVISIBLE);

                genericButton.setContentDescription("someone_else_button");

                break;
            default:
                break;
        }

        ((StyledTextView) genericButton.findViewById(R.id.client_type)).setText(beneficiaryLabel);

        scrollContent.addView(genericButton);
    }

    @OnClick(R.id.general_retry_button)
    public void onGeneralRetryButtonClick() {
        mPresenter.retryC2BClick();
    }

    public static int convertDpToPixel(float dp, Context context) {
        return (int) dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODES.NEW_BENEFICIARY && resultCode == RESULT_OK) {
            mPresenter.backToPreviousScreenWithResultOK();
        } else if (requestCode == Constants.REQUEST_CODES.TRANSACTIONAL_REQUEST_CODE && resultCode == RESULT_OK) {
            CreateTransactionResponse transactionResponse = data.getParcelableExtra(TRANSACTION_DATA);
            boolean showCheckoutDialogExtra = data.getBooleanExtra(SHOW_CHECKOUT_DIALOG_EXTRA, false);

            C2BNavigator.navigateToHomeActivity(this, transactionResponse, showCheckoutDialogExtra);
        }
    }
}
