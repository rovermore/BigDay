package com.smallworldfs.moneytransferapp.modules.status.domain.interactor;

import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TipInfo;

import java.util.ArrayList;

/**
 * Created by luismiguel on 27/10/17.
 */

public interface TransferTipsDetailsInteractor extends Interactor {
    interface Callback {
        void onTipsReceivedSusccessfull(ArrayList<TipInfo> data);
        void onTipsError();
    }
}
