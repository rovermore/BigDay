package com.smallworldfs.moneytransferapp.modules.qr.presentation.presenter.implementation;

import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.qr.presentation.presenter.QrPresenter;

import rx.android.schedulers.AndroidSchedulers;

public class QrPresenterImpl extends GenericPresenterImpl implements QrPresenter.Presenter {

    private QrPresenter.View view;

    public QrPresenterImpl(Context context) {
        super(AndroidSchedulers.mainThread(), context);
    }

    public void setView(QrPresenter.View view, GenericActivity activity) {
        this.view = view;
        this.view.configureView();
        this.mActivity = activity;
    }

    @Override
    public void resume() {
        super.resume();
    }
}
