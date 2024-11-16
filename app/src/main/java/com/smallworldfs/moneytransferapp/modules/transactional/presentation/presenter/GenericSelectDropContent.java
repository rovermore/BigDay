package com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;

import java.util.ArrayList;

/**
 * Created by luismiguel on 7/8/17.
 */

public interface GenericSelectDropContent extends BasePresenter {
    interface View {
        void configureView();

        void onFilterApplied(ArrayList<KeyValueData> mData);

        void showSearchEmptyView();

        void setupAdapter(ArrayList<KeyValueData> mData);

        void hideSearchEmptyView();

        void showLoadingView();

        void hideLoadingView();

        void showDialogAndFinish();
    }
}
