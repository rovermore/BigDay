package com.smallworldfs.moneytransferapp.modules.checkout.domain.interactors;

import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;
import com.smallworldfs.moneytransferapp.modules.home.domain.model.ComplianceRuleModel;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.MoreField;

import java.util.ArrayList;

/**
 * Created by luismiguel on 26/9/17.
 */

public interface CheckoutInteractor extends Interactor {
    interface Callback {
        void onCheckoutInformationReceived(Checkout checkout);
        void onCheckoutInformationError(String error);
        void onMoreFieldsReceived(MoreField moreFields, int position);
        void onMoreFieldsError();
        void onCreateCheckoutSuscessfull(CreateTransactionResponse transactionResponse);
        void onCreateCheckoutErrors();
        void onCreateCheckoutServerErrors();
        void onCreateCheckoutTaxCodeError(String title, String description);
        void onSendEmailSusccessfull();
        void onSendEmailError();
        void checkComplianceRule(ArrayList<ComplianceRuleModel> complianceList);
    }
}
