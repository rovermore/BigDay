package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation;

import android.content.Context;
import android.text.TextUtils;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.SendEmailInteractor;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.implementation.SendEmailInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ContactSupportInfo;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.CallUsLimitedUserPresenter;
import com.smallworldfs.moneytransferapp.utils.Log;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import rx.Scheduler;

@Singleton
public class CallUsLimitedUserPresenterImpl extends GenericPresenterImpl implements CallUsLimitedUserPresenter, SendEmailInteractor.Callback {

    private static final String TAG = CallUsLimitedUserPresenterImpl.class.getSimpleName();

    private View mView;
    private SendEmailInteractorImpl mInteractor;
    private ContactSupportInfo mContactSupportInfo;


    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        LoginRepository provideLoginRepository();
    }

    public LoginRepository loginRepository;

    @Inject
    public CallUsLimitedUserPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, View view, ContactSupportInfo contactSupportInfo) {
        super(observeOn, context);
        this.mView = view;
        this.mContactSupportInfo = contactSupportInfo;
        this.mInteractor = new SendEmailInteractorImpl(observeOn, susbscribeOn, this);

        CallUsLimitedUserPresenterImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        CallUsLimitedUserPresenterImpl.DaggerHiltEntryPoint.class);
        loginRepository = hiltEntryPoint.provideLoginRepository();
    }

    @Override
    public void create() {
        super.create();
        mView.configureView();
        mView.showHideLoadingView(false);
        mView.showContentLayout();
        //
        //
        User mUser = loginRepository.getUser();
        Pair<String, String> country = new Pair<>(mUser.getCountry().firstEntry().getKey(), mUser.getCountry().firstEntry().getValue());

        if(country != null) {
            this.countryPhone = country;
            mView.updateCountryPhone(countryPhone);
        }
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void destroy() {
        mContactSupportInfo = null;
        mInteractor.destroy();
        super.destroy();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onSendEmailError() {
        mView.showLoadingDialog(false);
        mView.showTopErrorView();

    }

    @Override
    public void onSendEmailSusccessfull() {
        mView.showLoadingDialog(false);
        mView.onCallUsFinish();
    }



    private ArrayList<Pair<Integer, String>> checkFields() {
        ArrayList<Pair<Integer, String>> listErrors = new ArrayList<>();
        // Name
        if(TextUtils.isEmpty(mView.getName())) {
            listErrors.add(new Pair<>(FIELD_NAME, SmallWorldApplication.getStr(R.string.empty_field)));
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
        mView.showLoadingDialog(true);
        String message= "";
        String userId = mInteractor.getUserId();
        String subject = String.format(SmallWorldApplication.getStr(R.string.customer_support_contact_user_by_phone), mView.getName());
        String name = mView.getName();
        String phone = mView.getPhone();
        if (!name.isEmpty() && !phone.isEmpty()) {
            if (!TextUtils.isEmpty(userId)) {
                message = String.format(SmallWorldApplication.getStr(R.string.customer_support_contact_user_by_phone_msg_limited),
                        mView.getName(), userId, countryPhone.first, mView.getPhone());
                //%s (id: %s) has requested to be called by phone to %s %s, through the Android app
                Log.e(TAG, "sendEmail:-------" + message);
            }
            mInteractor.sendEmail(subject, message, mContactSupportInfo.getEmail());
        } else  {
            mView.showTextFieldErrors();
        }
    }

    //----------------------------------------------------------------------------------------------
    private Pair<String, String> countryPhone = loginRepository.getOriginLoginCountry();
    public void onCountryPhoneSelected(Pair<String, String> country) {
        this.countryPhone = country;
        mView.updateCountryPhone(country);
    }
    public void onCountryPhone() {
        mView.showCountryPhoneSelector();
    }

}
