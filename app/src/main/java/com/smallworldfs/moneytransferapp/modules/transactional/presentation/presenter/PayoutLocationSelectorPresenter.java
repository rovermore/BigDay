package com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter;

import androidx.annotation.NonNull;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;

import java.util.ArrayList;

/**
 * Created by luismiguel on 25/8/17.
 */

public interface PayoutLocationSelectorPresenter extends BasePresenter {
    void onPayouLocationsLoaded(@NonNull ArrayList<Field> payoutLocation);

    interface View {
        void configureView();

        void configureDataInAdapter(ArrayList<Field> mPayoutLocations, String currency);
    }
}
