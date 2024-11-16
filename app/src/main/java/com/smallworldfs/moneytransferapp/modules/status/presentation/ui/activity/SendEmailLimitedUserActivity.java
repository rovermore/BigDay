package com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter.CODE_HOW_CAN_WE_HELP;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter.CODE_STATE;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter.FIELD_COUNTRY;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter.FIELD_EMAIL;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter.FIELD_HOW;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter.FIELD_MAX;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter.FIELD_NAME;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter.FIELD_PHONE;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter.FIELD_STATE;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SendEmailActivity.MESSAGE;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SendEmailActivity.SUBJECT;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.RESULT_DATA;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.country.presentation.ui.fragment.SelectCodeCountryDialogFragment;
import com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.fragment.ChooseCountryFromFragment;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ContactSupportInfo;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation.SendEmailLimitedUserPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.support.model.ContactSupportInfoUIModel;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
@AndroidEntryPoint
public class SendEmailLimitedUserActivity extends GenericActivity implements SendEmailLimitedUserPresenter.View, SelectCodeCountryDialogFragment.CountrySelectedInterface {

    public static final String TAG = SendEmailLimitedUserActivity.class.getSimpleName();

    private static final String SEPARATOR_TAG = "separator_line";
    private static final String ERROR_FIELD_TEXT_TAG = "error_text";
    public static final String CONTACT_SUPPORT_INFO_EXTRA = "CONTACT_SUPPORT_INFO_EXTRA";

    private SendEmailLimitedUserPresenterImpl mPresenter;
    private Dialog mProgressDialog;
    private ContactSupportInfoUIModel mContactSupportInfo;

    Unbinder mUnbinder;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.general_loading_view)
    View mLoadingView;

    @BindView(R.id.global_error_view)
    View mGeneralErrorView;

    @BindView(R.id.error_view)
    View mErrorView;

    @BindView(R.id.nested_scrollview)
    View mNestedScrollview;

    @BindView(R.id.lay_how)
    View mLayHow;

    @BindView(R.id.lay_country)
    View mLayCountry;

    @BindView(R.id.lay_state)
    View mLayState;

    @BindView(R.id.lay_name)
    View mLayName;

    @BindView(R.id.lay_email)
    View mLayEmail;

    @BindView(R.id.lay_phone)
    View mLayPhone;

    @BindView(R.id.txt_how_can_we_help_you)
    EditText mTxtHowCanWeHelpYou;

    @BindView(R.id.txt_country)
    EditText mTxtCountrty;

    @BindView(R.id.txt_state)
    EditText mTxtState;

    @BindView(R.id.txt_name)
    EditText mTxtName;

    @Override
    public String getName() {
        return mTxtName.getText().toString();
    }

    @BindView(R.id.txt_email)
    EditText mTxtEmail;

    @Override
    public String getEmail() {
        return mTxtEmail.getText().toString();
    }

    @BindView(R.id.txt_phone)
    EditText mTxtPhone;

    @Override
    public String getPhone() {
        return mTxtPhone.getText().toString();
    }

    @BindView(R.id.img_country_phone)
    ImageView mImgCountryPhone;

    @BindView(R.id.img_country_from)
    ImageView mImgCountryFrom;

    @OnClick(R.id.txt_how_can_we_help_you)
    public void onHowCanWeHelpYou() {
        mPresenter.onHowCanWeHelpYou();
    }

    @Override
    public void updateHowCanHelpYou(String subject) {
        mTxtHowCanWeHelpYou.setText(subject);
    }

    @OnClick({R.id.txt_country, R.id.img_country_from})
    public void onCountryFrom() {
        mPresenter.onCountryFrom();
    }

    @Override
    public void updateCountryFrom(Pair<String, String> country) {
        if (country == null || country.second == null || country.first == null) {
            Log.e(TAG, "updateCountryFrom:e:--------------------------------------------------" + country);
            return;
        }
        mTxtCountrty.setText(country.second);

        ImageViewExtKt.loadCircularImage(
                mImgCountryFrom,
                this,
                R.drawable.placeholder_country_adapter,
                Constants.COUNTRY.FLAG_IMAGE_ASSETS + country.first + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
        );
    }

    @OnClick({R.id.sel_country_phone, R.id.img_country_phone})
    public void onCountryPhone() {
        //showCountryPhoneSelector();
        mPresenter.onCountryPhone();
    }

    @Override
    public void updateCountryPhone(Pair<String, String> country) {
        if (country == null) return;

        ImageViewExtKt.loadCircularImage(
                mImgCountryPhone,
                this,
                R.drawable.placeholder_country_adapter,
                Constants.COUNTRY.FLAG_IMAGE_ASSETS + country.first + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
        );
    }


    @OnClick({R.id.txt_state})
    public void onState() {
        mPresenter.onState();
    }

    @Override
    public void updateState(String state) {
        mTxtState.setText(state);
    }

    @Override
    public void setStateEnabled(boolean enabled) {
        mLayState.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email_limited_user);
        mUnbinder = ButterKnife.bind(this);
        if (getIntent().getExtras() != null)
            mContactSupportInfo = getIntent().getParcelableExtra(CONTACT_SUPPORT_INFO_EXTRA);
        mPresenter = new SendEmailLimitedUserPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, mContactSupportInfo);
        mPresenter.create();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SendEmailLimitedUserActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if (mPresenter != null)
            mPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        if (mPresenter != null)
            mPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
        mPresenter.destroy();
        mUnbinder.unbind();
    }

    @Override
    public void configureView() {
        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.email_limited_title));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back_toolbar_white));

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        mErrorView.findViewById(R.id.status_bar_padding).setVisibility(View.GONE);

        mLoadingView.setVisibility(View.VISIBLE);
        mNestedScrollview.setVisibility(View.GONE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onOptionsItemSelected(item);
                return true;
            case R.id.send_email:
                mPresenter.onSendEmail();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send_email_limited_user, menu);
        return true;
    }

    @Override
    public void showHideLoadingView(boolean show) {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showHideGeneralErrorView(boolean show) {
        mGeneralErrorView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private View getFieldByCode(int code) {
        switch (code) {
            case FIELD_HOW:
                return mLayHow;
            case FIELD_COUNTRY:
                return mLayCountry;
            case FIELD_STATE:
                return mLayState;
            case FIELD_NAME:
                return mLayName;
            case FIELD_EMAIL:
                return mLayEmail;
            case FIELD_PHONE:
                return mLayPhone;
            default:
                return null;
        }
    }

    @Override
    public void onFormErrors(ArrayList<Pair<Integer, String>> listErrors) {
        for (Pair<Integer, String> pairError : listErrors) {
            if (pairError.first != null) {
                View field = getFieldByCode(pairError.first);
                if (field != null) {
                    //Log.e(TAG, "onFormErrors------------------------------------"+pairError.first);
                    field.findViewWithTag(SEPARATOR_TAG).setBackgroundColor(getResources().getColor(R.color.colorRedError));
                    ((StyledTextView) field.findViewWithTag(ERROR_FIELD_TEXT_TAG)).setText(pairError.second);
                    (field.findViewWithTag(ERROR_FIELD_TEXT_TAG)).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void clearErrorIndicatorInForm() {
        for (int i = 0; i < FIELD_MAX; i++) {
            View view = getFieldByCode(i);
            if (view != null) {
                view.findViewWithTag(SEPARATOR_TAG).setBackgroundColor(getResources().getColor(R.color.default_grey_control));
                view.findViewWithTag(ERROR_FIELD_TEXT_TAG).setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showLoadingDialog(boolean show) {
        if (show && this.mProgressDialog == null) {
            this.mProgressDialog =
                    new DialogExt().showLoadingDialog(this,
                            getString(R.string.sending_email_text),
                            getString(R.string.loading_text), true);
        } else if (!show && this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    @Override
    public void showTopErrorView() {
        showErrorView(getString(R.string.email_sent_error), getString(R.string.generic_subtitle_top_error_view), mErrorView);
    }

    @Override
    public void hideTopErrorView() {
        hideErrorView(mErrorView);
    }

    @Override
    public void showContentLayout() {
        mNestedScrollview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEmailSent() {
        new DialogExt().showSingleActionInfoDialog(this, getString(R.string.action_done_transactional_calculator),
                getString(R.string.email_sent_successfully),null,  new DialogExt.OnPositiveClick() {
                    @Override
                    public void onClick() {
                        setResult(Activity.RESULT_OK);
                        finish();
                        SendEmailLimitedUserActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
                    }
                },
                "");
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.from_right_to_left, R.anim.from_position_to_left);
        startActivityForResult(intent, requestCode, compat.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_HOW_CAN_WE_HELP && data != null) {
            mPresenter.setHowCanWeHelpYou(data.getStringExtra(SUBJECT), data.getStringExtra(MESSAGE));
        } else if (requestCode == CODE_STATE && resultCode == RESULT_OK) {
            KeyValueData keyValueSelected = data.getParcelableExtra(RESULT_DATA);
            mPresenter.setState(keyValueSelected);
        }
    }

    @Override
    public void showCountryFromSelector() {
        ChooseCountryFromFragment fragment = new ChooseCountryFromFragment();
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        final Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
        if (fragment1 == null) {
            transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            transaction.add(android.R.id.content, fragment, fragment.getClass().getSimpleName());
            transaction.addToBackStack(fragment.getClass().getSimpleName());
            transaction.commit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChooseCountryFromFragment.EventRefreshCountry event) {
        mPresenter.onCountryFromSelected(event.country);
    }

    @Override
    public void showCountryPhoneSelector() {
        final SelectCodeCountryDialogFragment fragment = new SelectCodeCountryDialogFragment();
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        final Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
        if (fragment1 == null) {
            transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            transaction.add(android.R.id.content, fragment, fragment.getClass().getSimpleName());
            transaction.addToBackStack(fragment.getClass().getSimpleName());
            transaction.commit();
        }
    }

    @Override
    public void onCountryCodeSelected(Pair<String, String> selectedCountry) {
        mPresenter.onCountryPhoneSelected(selectedCountry);
    }

}
