package com.smallworldfs.moneytransferapp.modules.checkout.presentation.presenter;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionItemValue;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;

import java.util.ArrayList;

/**
 * Created by luis on 14/9/17.
 */

public interface CheckoutPresenter extends BasePresenter {
    interface View {
        void configureView();

        void showHideLoadingView(boolean show);

        void fillDataHeaderInfo(String beneficiaryName, String formattedAmountAndCurrency, String beneficiaryCountry, String beneficiaryFirstLetter, String beneficiarySecondLetter, String deliveryMethod, Pair<String, String> stringStringPair);

        void fillDataTransactionInfo(String subtotal, String feeFormatted, String totalToPay, ArrayList<TransactionItemValue> deliveryInformation, ArrayList<TransactionItemValue> transactionInformation, ArrayList<TransactionItemValue> transactionTaxes, String promotionAmount, String footer);

        void showHideTotalErrorView(boolean show);

        void configureForm(ArrayList<Field> fields);

        void updateComboGroupValueData(ArrayList<KeyValueData> values, int positionField);

        void showProgressDialog(boolean show, String title, String content, boolean showTitle);

        void showTopErrorView();

        void showTopServerErrorView();

        void hideTopErrorView();

        void styleConfirmButtonInRetryMode();

        void enableDisableEditTextListeners(boolean enable);

        void notifyAddedRemoveFields(int i, int i1, boolean b);

        void showCreateTransactionFragmentErrors(ArrayList<TransactionErrors> errors);

        void notifyGlobalChanges();

        void showFlinksValidation();

        void showMessageEmailSent();

        void sendCheckoutAnalytics(Checkout checkout);

        void onCheckoutSuccess();

        void updateCheckoutData(Checkout checkoutData);

        String getPaymentMethod();

        void showDateRangeSelector(Field field, int position, String type, String value);

        void showAlertErrorView(String title, String description);

        void checkBankWire();

        void transactionSuccess();
    }
}
