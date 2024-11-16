package com.smallworldfs.moneytransferapp.modules.status.domain.interactor;

import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;

/**
 * Created by luismiguel on 4/10/17
 */
public interface SendEmailInteractor extends Interactor {

    interface Callback {
        void onSendEmailError();
        void onSendEmailSusccessfull();
    }
}
