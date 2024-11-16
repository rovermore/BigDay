package com.smallworldfs.moneytransferapp.modules.qr.presentation.presenter;

import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;

public interface QrPresenter {

    interface Presenter {
        void setView(QrPresenter.View view, GenericActivity activity);
    }

    interface View {
        void configureView();

        void startCameraSource();

        void stopCamera();

        void showGeneralLoadingView();

        void hideGeneralLoadingView();

        void showTransactionProgress(String url);
    }
}
