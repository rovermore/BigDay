package com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.google.android.material.textfield.TextInputLayout;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailPresenter;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation.SendEmailPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.support.model.ContactSupportInfoUIModel;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by luismiguel on 4/10/17
 */
@AndroidEntryPoint
public class SendEmailActivity extends GenericActivity implements SendEmailPresenter.View {

    private static final String TAG = SendEmailActivity.class.getSimpleName();

    private static final String SEPARATOR_TAG = "separator_line";
    private static final String ERROR_FIELD_TEXT_TAG = "error_text";
    public static final String CONTACT_SUPPORT_INFO_EXTRA = "CONTACT_SUPPORT_INFO_EXTRA";

    public static final String LIMITED = "limited";
    public static final String SUBJECT = "subject";
    public static final String MESSAGE = "message";

    Unbinder mUnbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.general_loading_view)
    View mLoadingView;
    @BindView(R.id.global_error_view)
    View mGeneralErrorView;
    @BindView(R.id.error_view)
    View mErrorView;
    @BindView(R.id.content_layout)
    View mContentLayout;

    @BindView(R.id.submit_email_button_limited)
    View mBtnSendLimited;
    @BindView(R.id.submit_email_button)
    View mBtnSend;

    @BindViews
            ({
                R.id.subject_edit_text,
                R.id.message_edit_text
            })
    List<EditText> mEditTextList;

    @BindViews
            ({
                R.id.subject_text_input_layout,
                R.id.message_text_input_layout,
            })
    List<TextInputLayout> mTextInputsLayoutList;

    private SendEmailPresenterImpl mPresenter;
    private Dialog mProgressDialog;
    private ContactSupportInfoUIModel mContactSupportInfo;

    private boolean mLitited = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        mUnbinder = ButterKnife.bind(this);

        if(getIntent().getExtras() != null) {
            mContactSupportInfo = getIntent().getParcelableExtra(CONTACT_SUPPORT_INFO_EXTRA);

            mLitited = getIntent().getBooleanExtra(LIMITED, false);
            if(mLitited) {
                String subject = getIntent().getStringExtra(SUBJECT);
                String message = getIntent().getStringExtra(MESSAGE);
                mEditTextList.get(0).setText(subject);
                mEditTextList.get(1).setText(message);
                mBtnSend.setVisibility(View.GONE);
                mBtnSendLimited.setVisibility(View.VISIBLE);
            }
        }

        mPresenter = new SendEmailPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, mContactSupportInfo);
        mPresenter.create();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                registerEvent("click_back");
                SendEmailActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mPresenter.destroy();
        mUnbinder.unbind();
    }

    @Override
    public void configureView() {
        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.contact_customer_support_transaction_status_button));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back_toolbar_white));
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        mErrorView.findViewById(R.id.status_bar_padding).setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send_email, menu);
        if( ! mLitited) {
            menu.setGroupVisible(0, false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.send_email:
                onSubmitEmailClickLimited();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showHideLoadingView(boolean show) {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showHideGeneralErrorView(boolean show) {
        mGeneralErrorView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFormErrors(ArrayList<Pair<Integer, String>> listErrors) {
        registerEvent("formKo");
        for (Pair<Integer, String> pairError : listErrors) {
            if(pairError.first != null) {
                mTextInputsLayoutList.get(pairError.first).findViewWithTag(SEPARATOR_TAG).setBackgroundColor(getResources().getColor(R.color.colorRedError));
                ((StyledTextView) mTextInputsLayoutList.get(pairError.first).findViewWithTag(ERROR_FIELD_TEXT_TAG)).setText(pairError.second);
                (mTextInputsLayoutList.get(pairError.first).findViewWithTag(ERROR_FIELD_TEXT_TAG)).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void clearErrorIndicatorInForm() {
        for (TextInputLayout textInputLayout : mTextInputsLayoutList) {
            textInputLayout.findViewWithTag(SEPARATOR_TAG).setBackgroundColor(getResources().getColor(R.color.default_grey_control));
            textInputLayout.findViewWithTag(ERROR_FIELD_TEXT_TAG).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showLoadingDialog(boolean show) {
        if (show && this.mProgressDialog == null) {
            this.mProgressDialog =
                    new DialogExt().showLoadingDialog(this,  getString(R.string.sending_email_text),
                            getString(R.string.loading_text), true);
        } else if (!show && this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    @Override
    public void showTopErrorView() {
        registerEvent("formKo");
        showErrorView(getString(R.string.email_sent_error), getString(R.string.generic_subtitle_top_error_view), mErrorView);
    }

    @Override
    public void hideTopErrorView() {
        hideErrorView(mErrorView);
    }

    @Override
    public void showContentLayout() {
        mContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEmailSent() {
        new DialogExt().showSingleActionInfoDialog(this, getString(R.string.action_done_transactional_calculator),
                getString(R.string.email_sent_successfully),null,  new DialogExt.OnPositiveClick() {
                    @Override
                    public void onClick() {
                        registerEvent("formOk");
                        setResult(Activity.RESULT_OK);
                        finish();
                        SendEmailActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
                    }
                },
                "");
    }

    @OnClick(R.id.submit_email_button)
    public void onSubmitEmailClick() {
        registerEvent("click_send_message");
        mPresenter.submitMail(mEditTextList.get(0).getText().toString(), mEditTextList.get(1).getText().toString());
    }
    @OnClick(R.id.submit_email_button_limited)
    public void onSubmitEmailClickLimited() {
        registerEvent("click_send_message");
        mPresenter.submitMailLimited(mEditTextList.get(0).getText().toString(), mEditTextList.get(1).getText().toString());
    }
    @Override
    public void getBack() {
        Intent intent = new Intent();
        intent.putExtra(SUBJECT, mEditTextList.get(0).getText().toString());
        intent.putExtra(MESSAGE, mEditTextList.get(1).getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void registerEvent(String eventAction) {
        trackEvent(
                new UserActionEvent(
                        ScreenCategory.CONTACT.getValue(),
                        eventAction,
                        "",
                        getHierarchy(""),
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                )
        );
    }

}
