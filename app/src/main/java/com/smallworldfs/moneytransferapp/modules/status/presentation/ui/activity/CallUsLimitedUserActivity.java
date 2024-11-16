package com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.CallUsLimitedUserPresenter.FIELD_MAX;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.CallUsLimitedUserPresenter.FIELD_NAME;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.CallUsLimitedUserPresenter.FIELD_PHONE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.country.presentation.ui.fragment.SelectCodeCountryDialogFragment;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ContactSupportInfo;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.CallUsLimitedUserPresenter;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation.CallUsLimitedUserPresenterImpl;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by ccasanova on 16/04/2018
 */
@AndroidEntryPoint
public class CallUsLimitedUserActivity extends GenericActivity implements CallUsLimitedUserPresenter.View, SelectCodeCountryDialogFragment.CountrySelectedInterface {
	private static final String SEPARATOR_TAG = "separator_line";
	private static final String ERROR_FIELD_TEXT_TAG = "error_text";
	public static final String CONTACT_SUPPORT_INFO_EXTRA = "CONTACT_SUPPORT_INFO_EXTRA";

	private CallUsLimitedUserPresenterImpl mPresenter;
	private Dialog mProgressDialog;
	private ContactSupportInfo mContactSupportInfo;

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

	@BindView(R.id.lay_name)
	View mLayName;
	@BindView(R.id.lay_phone)
	View mLayPhone;

	@BindView(R.id.txt_name)
	EditText mTxtName;
	@Override
	public String getName() { return mTxtName.getText().toString(); }

	@BindView(R.id.txt_phone)
	EditText mTxtPhone;
	@Override
	public String getPhone() { return mTxtPhone.getText().toString(); }

	@BindView(R.id.img_country_phone)
	ImageView mImgCountryPhone;

	@BindView(R.id.lay_txt_phone)
	TextInputLayout mLayTxtPhone;

	//----------------------------------------------------------------------------------------------
	@OnClick({R.id.sel_country_phone, R.id.img_country_phone})
	public void onCountryPhone() {
		//showCountryPhoneSelector();
		mPresenter.onCountryPhone();
	}
	@Override
	public void updateCountryPhone(Pair<String, String> country) {
		if(country != null && country.first != null)
			ImageViewExtKt.loadCircularImage(
					mImgCountryPhone,
					this,
					R.drawable.placeholder_country_adapter,
					Constants.COUNTRY.FLAG_IMAGE_ASSETS + country.first + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
			);
	}

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_us_limited_user);
		mUnbinder = ButterKnife.bind(this);
		if (getIntent().getExtras() != null)
			mContactSupportInfo = getIntent().getParcelableExtra(CONTACT_SUPPORT_INFO_EXTRA);
		mPresenter = new CallUsLimitedUserPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, mContactSupportInfo);
		mPresenter.create();
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
		if (mPresenter != null) {
			mPresenter.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
		mPresenter.destroy();
		mUnbinder.unbind();
	}

	@Override
	public void configureView() {
		mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
		mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);
		((StyledTextView)mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.call_limited_receive_call));
		mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back_toolbar_white));
		setSupportActionBar(mToolbar);
		if(getSupportActionBar() != null)
			getSupportActionBar().setDisplayShowHomeEnabled(true);

		mErrorView.findViewById(R.id.status_bar_padding).setVisibility(View.GONE);
		mLoadingView.setVisibility(View.VISIBLE);
		mNestedScrollview.setVisibility(View.GONE);

		mTxtName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (!charSequence.toString().isEmpty()) {
					((TextInputLayout) mLayName).setErrorEnabled(false);
					((TextInputLayout) mLayName).setError(null);
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {}
		});

		mTxtPhone.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (!charSequence.toString().isEmpty()) {
					mLayTxtPhone.setErrorEnabled(false);
					mLayTxtPhone.setError(null);
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {}
		});
	}


	@OnClick(R.id.submit_email_button_limited)
	public void onSubmitEmail() {
		mPresenter.onSendEmail();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				super.onOptionsItemSelected(item);
				return true;
			case R.id.send_email:
				onSubmitEmail();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.send_email, menu);
		return true;
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		CallUsLimitedUserActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
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
		switch(code) {
			case FIELD_NAME:
				return mLayName;
			case FIELD_PHONE:
				return mLayPhone;
			default:
				return null;
		}
	}
	@Override
	public void onFormErrors(ArrayList<Pair<Integer, String>> listErrors) {
		for (Pair<Integer, String> pairError : listErrors) {
			if(pairError.first != null) {
				View field = getFieldByCode(pairError.first);
				if(field != null) {
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
		for(int i=0; i < FIELD_MAX; i++) {
			View view = getFieldByCode(i);
			if(view != null) {
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
							getString(R.string.loading_text),
							true);
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
	public void onCallUsFinish() {
		new DialogExt().showSingleActionInfoDialog(this, getString(R.string.action_done_transactional_calculator),
				getString(R.string.email_sent_successfully),null, new DialogExt.OnPositiveClick() {
					@Override
					public void onClick() {
						setResult(Activity.RESULT_OK);
						finish();
						CallUsLimitedUserActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
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
	}

	@Override
	public void showCountryPhoneSelector() {
		final SelectCodeCountryDialogFragment fragment = new SelectCodeCountryDialogFragment();
		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		final Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
		if(fragment1 == null) {
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

	@Override
	public void showTextFieldErrors() {
		if (mTxtName.getText().toString().isEmpty()) {
			((TextInputLayout) mLayName).setError(getString(R.string.empty_field));
			((TextInputLayout) mLayName).setErrorEnabled(true);
		}
		if (mTxtPhone.getText().toString().isEmpty()) {
			mLayTxtPhone.setError(getString(R.string.empty_field));
			mLayTxtPhone.setErrorEnabled(true);
		}
	}
}
