package com.smallworldfs.moneytransferapp.modules.home.presentation.presentation;

import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderMessage;

import java.util.ArrayList;

/**
 * Created by luismiguel on 13/6/17.
 */

public interface HomePresenter extends BasePresenter {

    interface View {
        void configureView();

        void showTabIndicator(int position, int count);

        void hideTabIndicator(int position);

        void showDialogCheckout(CreateTransactionResponse mTransactionResponse);

        int getSelectedTabPosition();

        void showGdprBlockingPopup(String title, ArrayList<QuickReminderMessage> list, int type, String popupReceived, String positiveText,
                                   String negativeText);

        void showHomeDialog();

        void showSendEmailValidationDialog(String email);

        void showEmailValidatedDialog();

        void registerBrazeEvent(String eventName);

        void showAppRatingDialog();
    }
}
