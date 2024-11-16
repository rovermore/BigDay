package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.interactors;

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.validation.BeneficiaryValidationResponse;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.MoreField;

import java.util.ArrayList;

/**
 * Created by pedro del castillo on 7/9/17
 */
public interface EditBeneficiaryInteractor extends Interactor {

    interface EditBeneficiaryCallback {
        // Structure
        void onStructureError();

        // Validate
        void onValidateStepOk(Step step, BeneficiaryValidationResponse list);

        void onRequestingStepContentError(Step step);

        void onValidatingStepErrors(Step step);

        void onMoreFieldsReceived(Step step, MoreField moreFields, int position);

        void onMoreFieldsError();

        void requestDeliveryMethodsReceived(ArrayList<Method> deliveryMethods);

        void requestDeliveryMethodsError();

        void getBeneficiaryFormReceived(FormData formData);

        void getBeneficiaryFormError();

        void getEditBeneficiaryFormReceived(FormData formData);

        void getEditBeneficiaryFormError();
    }
}
