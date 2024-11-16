package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation;

import static com.smallworldfs.moneytransferapp.api.Api.BASE_URL;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.ACTIVITY_NAME_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_FIELD_POSITION;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_LIST_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.FIELD_STEP_TYPE;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.SEARCHABLE_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.SELECTED_VALUE;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.TYPE_CELL;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.URL_EXTRA_DATA;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.US_COUNTRY_VALUE;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.SendEmailInteractor;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.implementation.SendEmailInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailLimitedUserPresenter;
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SendEmailActivity;
import com.smallworldfs.moneytransferapp.modules.support.model.ContactSupportInfoUIModel;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity;
import com.smallworldfs.moneytransferapp.utils.Utils;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import rx.Scheduler;

@Singleton
public class SendEmailLimitedUserPresenterImpl extends GenericPresenterImpl implements SendEmailLimitedUserPresenter, SendEmailInteractor.Callback {

    //private static final String TAG = SendEmailLimitedUserPresenterImpl.class.getSimpleName();

    private View mView;
    private SendEmailInteractorImpl mInteractor;
    private ContactSupportInfoUIModel mContactSupportInfo;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        LoginRepository provideLoginRepository();
    }

    public LoginRepository loginRepository;

    private Pair<String, String> originCountryLogin;

    @Inject
    public SendEmailLimitedUserPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, View view, ContactSupportInfoUIModel contactSupportInfo) {
        super(observeOn, context);
        this.mView = view;
        this.mContactSupportInfo = contactSupportInfo;
        this.mInteractor = new SendEmailInteractorImpl(observeOn, susbscribeOn, this);

        SendEmailLimitedUserPresenterImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        SendEmailLimitedUserPresenterImpl.DaggerHiltEntryPoint.class);
        loginRepository = hiltEntryPoint.provideLoginRepository();
        originCountryLogin = loginRepository.getOriginLoginCountry();
    }

    @Override
    public void create() {
        super.create();
        mView.configureView();
        mView.showHideLoadingView(false);
        mView.showContentLayout();

        //
        User mUser = loginRepository.getUser();
        Pair<String, String> country = new Pair<>(mUser.getCountry().firstEntry().getKey(), mUser.getCountry().firstEntry().getValue());

        if(country != null) {
            originCountryLogin = country;
            mView.updateCountryFrom(country);
            mView.updateCountryPhone(originCountryLogin);
            mView.setStateEnabled(isStateEnabled(country.first));
        }
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void destroy() {
        mContactSupportInfo = null;
        mInteractor.destroy();
        super.destroy();
    }

    @Override
    public void onError(String message) { }

    @Override
    public void refresh() { }

    /*@Override
    public void onCustomerSupportInfoReceived() {
        mView.showHideLoadingView(false);
        mView.showContentLayout();
    }

    @Override
    public void onCustomerSupportInfoError() {
        mView.showHideLoadingView(false);
        mView.showHideGeneralErrorView(true);
    }*/

    @Override
    public void onSendEmailError() {
        mView.showLoadingDialog(false);
        mView.showTopErrorView();

    }

    @Override
    public void onSendEmailSusccessfull() {
        mView.showLoadingDialog(false);
        mView.onEmailSent();
    }



    private ArrayList<Pair<Integer, String>> checkFields() {
        ArrayList<Pair<Integer, String>> listErrors = new ArrayList<>();
        // Subject
        if(TextUtils.isEmpty(subject)) {
            listErrors.add(new Pair<>(FIELD_HOW, SmallWorldApplication.getStr(R.string.empty_field)));
        }
        // Country
        if(originCountryLogin == null || TextUtils.isEmpty(originCountryLogin.first)) {
            listErrors.add(new Pair<>(FIELD_COUNTRY, SmallWorldApplication.getStr(R.string.empty_field)));
        }
        // State
        else if(originCountryLogin.first.equals(US_COUNTRY_VALUE) && (state == null || TextUtils.isEmpty(state.getKey()))) {
            listErrors.add(new Pair<>(FIELD_STATE, SmallWorldApplication.getStr(R.string.empty_field)));
        }
        // Name
        if(TextUtils.isEmpty(mView.getName())) {
            listErrors.add(new Pair<>(FIELD_NAME, SmallWorldApplication.getStr(R.string.empty_field)));
        }
        // Email
        if(TextUtils.isEmpty(mView.getEmail())) {
            listErrors.add(new Pair<>(FIELD_EMAIL, SmallWorldApplication.getStr(R.string.empty_field)));
        }
        else if( ! Utils.isValidEmail(mView.getEmail())) {
            listErrors.add(new Pair<>(FIELD_EMAIL, SmallWorldApplication.getStr(R.string.invalid_email)));
        }
        // Phone
        if(TextUtils.isEmpty(mView.getPhone())) {
            listErrors.add(new Pair<>(FIELD_PHONE, SmallWorldApplication.getStr(R.string.empty_field)));
        }
        else if (!android.util.Patterns.PHONE.matcher(mView.getPhone()).matches()) {
            listErrors.add(new Pair<>(FIELD_PHONE, SmallWorldApplication.getStr(R.string.invalid_mobile_phone)));
        }
        return listErrors;
    }

    /*public boolean onFieldsChange() {
        ArrayList<Pair<Integer, String>> listErrors = checkFields();
        boolean enabled = listErrors.size() == 0;
        mView.setSendEmailEnabled(enabled);
        Log.e(TAG, "onFieldsChange:-----------------------------------------------"+enabled);
        return enabled;
    }*/
    public void onSendEmail() {
        mView.clearErrorIndicatorInForm();
        mView.hideTopErrorView();

        ArrayList<Pair<Integer, String>> listErrors = checkFields();

        if(listErrors.size() > 0) {
            mView.onFormErrors(listErrors);
        }
        else {
            sendEmail();
        }
    }
    private void sendEmail() {
        if (mContactSupportInfo != null) {
            mView.showLoadingDialog(true);
            String userId = mInteractor.getUserId();
            if (!TextUtils.isEmpty(userId)) {
                String strState = "-";
                if (originCountryLogin.first != null && originCountryLogin.first.equals(US_COUNTRY_VALUE) && state != null && state.getKey() != null)
                    strState = state.getKey();
                message += String.format(SmallWorldApplication.getStr(R.string.customer_support_contact_user_by_email_msg_limited),
                        mView.getName(), userId, originCountryLogin.first, mView.getPhone(), mView.getEmail(), originCountryLogin.first, strState);
                //\n\n-------------------\nMessage sent by user %s (id: %s), \nPhone: %s%s, \nEmail: %s, \nCountry: %s, \nState: %s, through the Android app</string>
                //Log.e(TAG, "sendEmail:-------"+message);
            }

            mInteractor.sendEmail(subject, message, mContactSupportInfo.getEmail());
        }
    }



    //----------------------------------------------------------------------------------------------
    private String subject = "";
    private String message = "";
    public void onHowCanWeHelpYou() {
        Intent intent = new Intent(mContext, SendEmailActivity.class);
        intent.putExtra(SendEmailActivity.LIMITED, true);
        intent.putExtra(SendEmailActivity.SUBJECT, subject);
        intent.putExtra(SendEmailActivity.MESSAGE, message);
        mView.startActivityForResult(intent, CODE_HOW_CAN_WE_HELP);
    }
    public void setHowCanWeHelpYou(String subject, String message) {
        this.subject = subject;
        this.message = message;
        mView.updateHowCanHelpYou(subject);
    }

    //----------------------------------------------------------------------------------------------
    private boolean isStateEnabled(String country) { return country != null && country.equals(US_COUNTRY_VALUE); }
    public void onCountryFromSelected(Pair<String, String> country) {
        originCountryLogin = country;
        mView.updateCountryFrom(country);
        mView.setStateEnabled(isStateEnabled(country.first));
    }
    public void onCountryFrom() {
        mView.showCountryFromSelector();
    }
    //----------------------------------------------------------------------------------------------
    public void onCountryPhoneSelected(Pair<String, String> country) {
        this.originCountryLogin = country;
        mView.updateCountryPhone(country);
    }
    public void onCountryPhone() {
        mView.showCountryPhoneSelector();
    }

    //----------------------------------------------------------------------------------------------
    private KeyValueData state = null;
    public void onState() {
        String name = SmallWorldApplication.getStr(R.string.select_state_activity_title);
        String typeCell = "";

        Intent intent = new Intent(mContext, GenericSelectDropContentActivity.class);
        intent.putParcelableArrayListExtra(CONTENT_LIST_EXTRA, null);
        intent.putExtra(CONTENT_FIELD_POSITION, -1);
        intent.putExtra(FIELD_STEP_TYPE, "");
        intent.putExtra(SELECTED_VALUE, state == null ? "" : state.getKey());
        intent.putExtra(SEARCHABLE_EXTRA, true);
        intent.putExtra(URL_EXTRA_DATA, BASE_URL + EndPoint.SOFT_REGISTER_STATES);
        intent.putExtra(ACTIVITY_NAME_EXTRA, name);
        intent.putExtra(TYPE_CELL, typeCell);

        mView.startActivityForResult(intent, CODE_STATE);
    }
    public void setState(KeyValueData state) {
        this.state = state;
        mView.updateState(state.getValue());
    }

}
