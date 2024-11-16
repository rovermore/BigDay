package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation;

import android.content.Context;
import android.text.TextUtils;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.SendEmailInteractor;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.implementation.SendEmailInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SendEmailPresenter;
import com.smallworldfs.moneytransferapp.modules.support.model.ContactSupportInfoUIModel;

import java.util.ArrayList;

import rx.Scheduler;

/**
 * Created by luismiguel on 4/10/17
 */
public class SendEmailPresenterImpl extends GenericPresenterImpl implements SendEmailPresenter, SendEmailInteractor.Callback {

    private SendEmailPresenter.View mView;
    private SendEmailInteractorImpl mInteractor;
    private ContactSupportInfoUIModel mContactSupportInfo;

    public SendEmailPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, View view, ContactSupportInfoUIModel contactSupportInfo) {
        super(observeOn, context);
        this.mView = view;
        this.mContactSupportInfo = contactSupportInfo;
        this.mInteractor = new SendEmailInteractorImpl(observeOn, susbscribeOn, this);
    }

    @Override
    public void create() {
        super.create();
        mView.configureView();
        mView.showHideLoadingView(false);
        mView.showContentLayout();
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

    public void submitMailLimited(String subject, String message) {
        mView.clearErrorIndicatorInForm();
        mView.hideTopErrorView();
        if(!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(message)) {
            mView.getBack();
        }
        else {
            ArrayList<Pair<Integer, String>> listErrors = new ArrayList<>();
            // Subject
            if (TextUtils.isEmpty(subject)) {
                listErrors.add(new Pair<>(0, SmallWorldApplication.getStr(R.string.empty_field)));
            }
            // Message
            if (TextUtils.isEmpty(message)) {
                listErrors.add(new Pair<>(1, SmallWorldApplication.getStr(R.string.empty_field)));
            }
            mView.onFormErrors(listErrors);
        }
    }
    public void submitMail(String subject, String message) {
        mView.clearErrorIndicatorInForm();
        mView.hideTopErrorView();

        if(!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(message)) {
            // Submit
            mView.showLoadingDialog(true);
            // Concat userId
            String userId = mInteractor.getUserId();
            String userName = mInteractor.getUserName();
            if (!TextUtils.isEmpty(userId)) {
                StringBuilder builder = new StringBuilder();
                builder.append("<br>");
                builder.append("<br>");
                builder.append("-------------------");
                builder.append("<br>");
                builder.append(message);
                builder.append(String.format(SmallWorldApplication.getStr(R.string.email_message_id), userName, userId));
                message = builder.toString();
            }
            mInteractor.sendEmail(subject, message, mContactSupportInfo.getEmail());
        }
        else {
            ArrayList<Pair<Integer, String>> listErrors = new ArrayList<>();

            // Subject
            if (TextUtils.isEmpty(subject)) {
                listErrors.add(new Pair<>(0, SmallWorldApplication.getStr(R.string.empty_field)));
            }

            // Message
            if (TextUtils.isEmpty(message)) {
                listErrors.add(new Pair<>(1, SmallWorldApplication.getStr(R.string.empty_field)));
            }

            mView.onFormErrors(listErrors);
        }
    }
}
