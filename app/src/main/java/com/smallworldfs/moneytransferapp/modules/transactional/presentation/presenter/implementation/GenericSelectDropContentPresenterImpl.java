package com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation;

import android.content.Context;
import android.text.TextUtils;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors.GenericSelectDropContentInteractor;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors.implementation.GenericSelectDropContentInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.GenericSelectDropContent;
import com.smallworldfs.moneytransferapp.utils.Utils;

import java.util.ArrayList;
import java.util.TreeMap;

import rx.Scheduler;

public class GenericSelectDropContentPresenterImpl extends GenericPresenterImpl implements GenericSelectDropContent, GenericSelectDropContentInteractor.Callback {

    private final ArrayList<KeyValueData> mPermanentData = new ArrayList<>();
    private final GenericSelectDropContent.View mView;
    private ArrayList<KeyValueData> mData;
    private final String mUrlApi;
    private GenericSelectDropContentInteractorImpl mInteractor;

    // Analytics


    public GenericSelectDropContentPresenterImpl(Scheduler observeOn, Scheduler subscribeOn, Context context, View view, String urlApi, String screenName, GenericActivity activity) {
        super(observeOn, context);
        mView = view;
        mUrlApi = urlApi;
        if (!TextUtils.isEmpty(mUrlApi)) {
            mInteractor = new GenericSelectDropContentInteractorImpl(observeOn, subscribeOn, this);
        }
        mActivity = activity;
    }


    @Override
    public void create() {
        super.create();
        mView.configureView();

        if (!TextUtils.isEmpty(mUrlApi)) {
            mView.showLoadingView();
            if (mInteractor != null)
                mInteractor.requestData(mUrlApi);
        } else {
            mView.setupAdapter(null);
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
        super.destroy();
        if (mInteractor != null) {
            mInteractor.destroy();
        }
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }


    public void performSearch(String text) {
        filterBy(text);
    }

    private void filterBy(String filterBy) {
        String normalizedText = Utils.removeAccents(filterBy, true);
        if (mData != null) {
            mData.clear();
            if (filterBy.equals("")) {
                mData.addAll(mPermanentData);
            } else {
                for (KeyValueData s : mPermanentData) {
                    String normalizedData = Utils.removeAccents(s.getValue(), true);
                    if ((normalizedData.toLowerCase().contains(normalizedText.toLowerCase()))) {
                        mData.add(s);
                    }
                }
            }

            mView.onFilterApplied(mData);

            if (mData.size() == 0) {
                mView.showSearchEmptyView();
            } else {
                mView.hideSearchEmptyView();
            }
        } else {
            mView.showSearchEmptyView();
        }


    }

    @Override
    public void onDataReceived(ArrayList<TreeMap<String, String>> data) {
        mView.hideLoadingView();
        if (data != null) {
            mData = Utils.convertMapToKeyValueList(data);
            mPermanentData.addAll(mData);
            mView.setupAdapter(mData);
        } else {
            onErrorRetrievingData();
        }
    }

    @Override
    public void onErrorRetrievingData() {
        mView.showDialogAndFinish();
    }

}
