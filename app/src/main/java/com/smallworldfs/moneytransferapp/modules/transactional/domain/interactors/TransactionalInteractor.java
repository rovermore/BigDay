package com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors;

import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.MoreField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationStepResponse;

import java.util.ArrayList;

public interface TransactionalInteractor extends Interactor {

    interface Callback {
        // Structure
        void onStructureReceived(ArrayList<Step> steps);

        void onDeliveryMethodsReceived();

        void onStructureError();

        void onNeedToRequestAgainDataStep(int positionStep);

        // Validate
        void onValidateStepOk(Step step, ValidationStepResponse list, String beneficiaryId);

        void goToCheckout(String paymentMethod);

        void onRequestingStepContentError(Step step, String error);

        void onValidatingStepErrors(Step step, String error);

        void onMoreFieldsReceived(Step step, MoreField moreFields, int position);

        void onMoreFieldsError();

        void onQuickReminderInfoReceived(QuickReminderResponse quickReminderResponse);

    }

}
