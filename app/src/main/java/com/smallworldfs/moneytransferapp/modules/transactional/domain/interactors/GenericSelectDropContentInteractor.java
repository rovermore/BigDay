package com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors;


import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;

import java.util.ArrayList;
import java.util.TreeMap;

public interface GenericSelectDropContentInteractor extends Interactor {
    interface Callback {
        void onDataReceived(ArrayList<TreeMap<String, String>> data);

        void onErrorRetrievingData();
    }
}
