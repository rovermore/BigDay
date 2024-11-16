package com.smallworldfs.moneytransferapp.modules.common.presentation.presenter;

import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;

import rx.Scheduler;

/**
 * Created by luismiguel on 21/9/17
 */
public class FormAbstracPresenter extends GenericPresenterImpl {

    public FormAbstracPresenter(Scheduler observeOn, Context context) {
        super(observeOn, context);
    }

    public void onCountryPhoneClickFormEvent(Field field, int position) {
    }

    public void showRangeDateSelector(Field field, int position, String type, String value) {
    }

    public void showRangeDateSelector(String stepId, Field field, int position, String type, String value) {
    }

    public void onComboOwnDataSelected(Field field, int position) {
    }

    public void onComboOwnDataSelected(Field field, int position, Step step) {
    }

    public void onComboApiDataSelected(Field field, int position) {
    }

    public void onComboApiDataSelected(Field field, int position, Step step) {
    }

    public void onAttachSendFileButtonSelected(Field field, int position) {
    }

    public void onRadioSwitchButtonSelected(Field field, int position) {
    }

}
