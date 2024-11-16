package com.smallworldfs.moneytransferapp.modules.login.domain.interactors;

import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.Country;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;

/**
 * Created by luismiguel on 4/5/17.
 */

public interface LoginInteractor extends Interactor {
    interface Callback{
        void onLogout();
    }
}
