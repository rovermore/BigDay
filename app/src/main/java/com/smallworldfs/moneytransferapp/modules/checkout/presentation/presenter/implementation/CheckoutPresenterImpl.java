package com.smallworldfs.moneytransferapp.modules.checkout.presentation.presenter.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.FACEBOOK_ANALYTICS_KEYS.CONTENT_ID;
import static com.smallworldfs.moneytransferapp.utils.Constants.FACEBOOK_ANALYTICS_KEYS.CONTENT_TYPE;
import static com.smallworldfs.moneytransferapp.utils.Constants.FACEBOOK_ANALYTICS_KEYS.CURRENCY;
import static com.smallworldfs.moneytransferapp.utils.Constants.FACEBOOK_ANALYTICS_KEYS.VALUE_TO_SUM;
import static com.smallworldfs.moneytransferapp.utils.Constants.FACEBOOK_ANALYTICS_VALUES.PURCHASE;
import static com.smallworldfs.moneytransferapp.utils.Constants.PAYNMENT_METHODS.BANKWIRE;
import static com.smallworldfs.moneytransferapp.utils.Constants.PAYNMENT_METHODS.BANKWIRE_KEY_FIELDS.DEPOSIT_BANK_BRANCH_ID;
import static com.smallworldfs.moneytransferapp.utils.Constants.PAYNMENT_METHODS.BANKWIRE_KEY_FIELDS.DEPOSIT_BANK_ID;
import static com.smallworldfs.moneytransferapp.utils.Constants.PAYNMENT_METHODS.PAYNMENT_METHOD_KEY;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_EMPTY;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.util.Pair;

import com.contentsquare.android.Contentsquare;
import com.contentsquare.android.api.model.Transaction;
import com.facebook.appevents.AppEventsLogger;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.interactors.CheckoutInteractor;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.interactors.implementation.CheckoutInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionItemValue;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.navigator.CheckoutNavigator;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.presenter.CheckoutPresenter;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.FormAbstracPresenter;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.home.domain.model.ComplianceRuleModel;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.MoreField;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.FormUtils;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import rx.Scheduler;

/**
 * Created by luis on 14/9/17
 */
@Singleton
public class CheckoutPresenterImpl extends FormAbstracPresenter implements CheckoutPresenter, CheckoutInteractor.Callback {

    public UserDataRepository userDataRepository;
    private Context mContext;
    private CheckoutPresenter.View mView;
    private CheckoutInteractorImpl mInteractor;
    // Temp vars
    private ArrayList<Field> mFormData;
    private ArrayList<Field> mMoreFormDataAdded;
    private ArrayList<KeyValueData> mTransactionalData;
    private Checkout mCheckoutData;
    private CreateTransactionResponse mCurrentCreateTransactionResponse;

    @Inject
    public CheckoutPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, View view, GenericActivity activity, ArrayList<KeyValueData> mTransactionalData, Checkout checkout) {
        super(observeOn, context);
        this.mContext = context;
        this.mView = view;
        this.mActivity = activity;
        this.mTransactionalData = mTransactionalData;
        this.mMoreFormDataAdded = new ArrayList<>();
        this.mCheckoutData = checkout;
        this.mInteractor = new CheckoutInteractorImpl(observeOn, susbscribeOn, this);

        CheckoutPresenterImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        CheckoutPresenterImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
    }

    @Override
    public void create() {
        super.create();
        mView.configureView();

        if (mCheckoutData == null) {
            if (mTransactionalData != null) {
                mInteractor.requestCheckoutInfo(mTransactionalData);
            }
        } else {
            // Reestructure data
            if (mCheckoutData.getFields() != null) {
                for (Field field : mCheckoutData.getFields()) {
                    field.setData(Utils.convertKeyListValueToTreeMap(field.getDataReplicated()));
                }
            }
            onCheckoutInformationReceived(mCheckoutData);
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
        mCurrentCreateTransactionResponse = null;
        mInteractor.destroy();
        super.destroy();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    private void checkFlinksValidation() {
        User currentUser = userDataRepository.retrieveUser();
        if (currentUser != null && currentUser.getFlinksState() != null && currentUser.getFlinksState().equals(Constants.FLINKS_STATE.FLINKS_CONNECT)) {
            mView.showFlinksValidation();
        }
    }

    //------------------
    // PROCESS METHODS
    //------------------
    private String formatBeneficiaryName(Checkout checkout) {
        if (checkout.getTransactionSummary() != null && checkout.getTransactionSummary().getName() != null) {
            return checkout.getTransactionSummary().getName() + " " + checkout.getTransactionSummary().getFirstLastName();
        }
        return "";

    }

    private String formatAmountWithCurrency(Checkout checkout) {
        if (checkout.getTransactionSummary() != null) {
            return String.valueOf(AmountFormatter.formatDoubleAmountNumber(checkout.getTransactionSummary().getTotalPayout())) + " " + checkout.getTransactionSummary().getCurrency();
        }
        return "";
    }

    private Pair<String, String> formatCheckoutDay(Checkout checkout) {
        SimpleDateFormat formatterProjection = new SimpleDateFormat("MMMM dd");
        String[] dateSplitted = formatterProjection.format(new Date()).split(" ");

        String month = "";
        if (dateSplitted[0] != null && !TextUtils.isEmpty(dateSplitted[0])) {
            if (dateSplitted[0].length() > 3) {
                month = dateSplitted[0].substring(0, 3).toUpperCase(Locale.getDefault());
            } else {
                month = dateSplitted[0].toUpperCase(Locale.getDefault());
            }
        }

        String day = "";
        if (dateSplitted[1] != null && !TextUtils.isEmpty(dateSplitted[1])) {
            day = dateSplitted[1];
        }

        return new Pair<>(month, day);
    }

    @Override
    public void onCheckoutInformationReceived(Checkout checkout) {

        if (checkout != null) {
            // Extract data
            String beneficiaryName = formatBeneficiaryName(checkout);
            String formattedAmountAndCurrency = formatAmountWithCurrency(checkout);
            String beneficiaryCountry = checkout.getTransactionSummary().getCountry();
            String beneficiaryFirstLetter = checkout.getTransactionSummary().getName().substring(0, 1).toUpperCase(Locale.getDefault());
            String beneficiarySecondLetter = !TextUtils.isEmpty(checkout.getTransactionSummary().getFirstLastName()) ? checkout.getTransactionSummary().getFirstLastName().substring(0, 1).toUpperCase(Locale.getDefault()) : "";
            String translatedDeliveryMethod = mInteractor.getTranslatedDeliveryMethod(checkout.getTransactionSummary().getDeliveryMethod());

            mView.fillDataHeaderInfo(beneficiaryName, formattedAmountAndCurrency, beneficiaryCountry, beneficiaryFirstLetter, beneficiarySecondLetter, translatedDeliveryMethod, formatCheckoutDay(checkout));

            String subtotal;
            String feeFormatted;
            String totalToPay;
            String promotionAmount;
            ArrayList<TransactionItemValue> transactionTaxes = new ArrayList<>();
            mCheckoutData = checkout;
            mView.updateCheckoutData(mCheckoutData);
            if (checkout.getTransactionDetails().getCurrencyOrigin().equals("USD")) {
                subtotal = "$" + AmountFormatter.formatDoubleAmountNumber(checkout.getTransactionDetails().getSubtotal());
                feeFormatted = "$" + AmountFormatter.parseStringCurrentCountryNumbers(checkout.getTransactionDetails().getFee());
                totalToPay = "$" + AmountFormatter.formatDoubleAmountNumber(checkout.getTransactionDetails().getTotalToPay());
                promotionAmount = checkout.getTransactionSummary().getPromotionAmount() == null ? null : "- " + "$" + AmountFormatter.formatDoubleAmountNumber(checkout.getTransactionSummary().getPromotionAmount());

                if (checkout.getTransactionDetails().getTransactionTaxes() != null) {
                    for (TransactionItemValue itemValue : checkout.getTransactionDetails().getTransactionTaxes()) {
                        transactionTaxes.add(new TransactionItemValue(itemValue.getTitle(), "$" + AmountFormatter.formatDoubleAmountNumber(Double.parseDouble(itemValue.getValue()))));
                    }
                }
            } else {
                subtotal = AmountFormatter.formatDoubleAmountNumber(checkout.getTransactionDetails().getSubtotal()) + " " + checkout.getTransactionDetails().getCurrencyOrigin();
                feeFormatted = AmountFormatter.parseStringCurrentCountryNumbers(checkout.getTransactionDetails().getFee()) + " " + checkout.getTransactionDetails().getCurrencyOrigin();
                totalToPay = AmountFormatter.formatDoubleAmountNumber(checkout.getTransactionDetails().getTotalToPay()) + " " + checkout.getTransactionDetails().getCurrencyOrigin();
                promotionAmount = checkout.getTransactionSummary().getPromotionAmount() == null ? null : "- " + AmountFormatter.formatDoubleAmountNumber(checkout.getTransactionSummary().getPromotionAmount()) + " " + checkout.getTransactionDetails().getCurrencyOrigin();

                if (checkout.getTransactionDetails().getTransactionTaxes() != null) {
                    for (TransactionItemValue itemValue : checkout.getTransactionDetails().getTransactionTaxes()) {
                        transactionTaxes.add(new TransactionItemValue(itemValue.getTitle(), AmountFormatter.formatDoubleAmountNumber(Double.parseDouble(itemValue.getValue())) + " " + checkout.getTransactionDetails().getCurrencyOrigin()));
                    }
                }
            }

            String footer = checkout.getTransactionDetails().getFooter();

            mView.fillDataTransactionInfo(subtotal, feeFormatted, totalToPay, checkout.getTransactionDetails().getDeliveryInformation(), checkout.getTransactionDetails().getTransactionInformation(), transactionTaxes, promotionAmount, footer);
            mView.sendCheckoutAnalytics(checkout);
            if (checkout.getFields() != null && checkout.getFields().size() > 0) {
                mFormData = checkout.getFields();
                mInteractor.syncronizeFormData(mFormData);
                mView.configureForm(mFormData);
            }

        } else {
            onCheckoutInformationError(null);
        }
        checkFlinksValidation();
        mView.showHideLoadingView(false);
    }


    //------------------
    // CALLBACKS
    //------------------

    @Override
    public void onCheckoutInformationError(String error) {
        if (error != null && !error.isEmpty()) {
            CheckoutNavigator.backToTransactionalDueError(mActivity, error);
        } else {
            mView.showHideLoadingView(false);
            mView.showHideTotalErrorView(true);
        }
    }

    @Override
    public void onCreateCheckoutSuscessfull(CreateTransactionResponse transactionResponse) {
        mView.showProgressDialog(false, "", "", false);
        mInteractor.clearUsedPromotions();
        // Check response.json
        if (transactionResponse.getTransaction() == null && transactionResponse.getErrors() != null) {
            this.mCurrentCreateTransactionResponse = transactionResponse;
            mView.showCreateTransactionFragmentErrors(transactionResponse.getErrors());
        } else {
            // When the transaction was successfully completed track Content Square Analytics
            try {
                float price = (!transactionResponse.getTransaction().getTotalSale().isEmpty()) ? Float.parseFloat(transactionResponse.getTransaction().getTotalSale()) : 0F;
                String id = (!transactionResponse.getTransaction().getMtn().isEmpty()) ? transactionResponse.getTransaction().getMtn() : "";
                String currency = (!transactionResponse.getTransaction().getSendingCurrency().isEmpty()) ? transactionResponse.getTransaction().getSendingCurrency() : "";

                Contentsquare.send(Transaction.builder(price, currency).id(id).build());
            } catch (Exception e) {
                Log.e("Error", "Exception creating Content Square Tracking: " + e);
            }

            // When the hard register has finished send fb statistics
            try {
                AppEventsLogger logger = AppEventsLogger.newLogger(mActivity);

                Bundle params = new Bundle();
                params.putString(CONTENT_ID, (!transactionResponse.getTransaction().getMtn().isEmpty()) ? transactionResponse.getTransaction().getMtn() : "");
                params.putString(CONTENT_TYPE, (!transactionResponse.getTransaction().getPaymentType().isEmpty()) ? transactionResponse.getTransaction().getPaymentType() : "");
                params.putString(CURRENCY, (!transactionResponse.getTransaction().getSendingCurrency().isEmpty()) ? transactionResponse.getTransaction().getSendingCurrency() : "");
                params.putDouble(VALUE_TO_SUM, (!transactionResponse.getTransaction().getTotalSale().isEmpty()) ? Float.parseFloat(transactionResponse.getTransaction().getTotalSale()) : 0F);
                logger.logEvent(PURCHASE, params);
            } catch (Exception e) {
                Log.e("FACEBOOK_ANALYTICS", "Purchase analytic not sent");
            }
            mView.transactionSuccess();
            // Navigate to home screen and show dialog
            CheckoutNavigator.clearStackAndNavigateToHome(mActivity, transactionResponse, mCheckoutData);
        }
    }

    @Override
    public void onCreateCheckoutErrors() {
        mView.showProgressDialog(false, "", "", false);
        mView.showTopErrorView();
        mView.styleConfirmButtonInRetryMode();
        mView.notifyGlobalChanges();
    }

    @Override
    public void onCreateCheckoutServerErrors() {
        mView.showProgressDialog(false, "", "", false);
        mView.showTopServerErrorView();
        mView.styleConfirmButtonInRetryMode();
        mView.notifyGlobalChanges();
    }

    @Override
    public void onCreateCheckoutTaxCodeError(String title, String description) {
        mView.showProgressDialog(false, STRING_EMPTY, STRING_EMPTY, false);
        mView.showAlertErrorView(title, description);
        mView.styleConfirmButtonInRetryMode();
        mView.notifyGlobalChanges();
    }

    @Override
    public void onSendEmailSusccessfull() {
        mView.showProgressDialog(false, "", "", false);
        mView.showMessageEmailSent();
    }

    @Override
    public void onSendEmailError() {
        mView.showProgressDialog(false, "", "", false);
    }

    @Override
    public void checkComplianceRule(ArrayList<ComplianceRuleModel> complianceRuleModelArrayList) {
        boolean hasToUploadDocument = false;
        User user = mInteractor.userDataRepository.retrieveUser();
        if (user != null && complianceRuleModelArrayList != null && !complianceRuleModelArrayList.isEmpty()) {
            switch (user.getCountry().firstKey()) {
                case "USA":
                    break;
                case "BRA":
                    for (ComplianceRuleModel iterator : complianceRuleModelArrayList) {
                        if (Objects.equals(iterator.getComplianceType(), Constants.COMPLIANCE_RULES.ID_MISSING_OR_EXPIRED) ||
                                Objects.equals(iterator.getComplianceType(), "FACE_VERIFICATION")) {
                            hasToUploadDocument = true;
                            break;
                        }
                    }
                case "CHE":
                    for (ComplianceRuleModel iterator : complianceRuleModelArrayList) {
                        if (Objects.equals(iterator.getComplianceType(), Constants.COMPLIANCE_RULES.ID_MISSING_OR_EXPIRED) ||
                                Objects.equals(iterator.getComplianceType(), "FACE_VERIFICATION")) {
                            hasToUploadDocument = true;
                            break;
                        }
                    }
                case "ITA":
                    for (ComplianceRuleModel iterator : complianceRuleModelArrayList) {
                        if (Objects.equals(iterator.getComplianceType(), Constants.COMPLIANCE_RULES.ID_MISSING_OR_EXPIRED) ||
                                Objects.equals(iterator.getComplianceType(), "TAX_CODE_DOCUMENT")) {
                            hasToUploadDocument = true;
                            break;
                        }
                    }
                default:
                    for (ComplianceRuleModel iterator : complianceRuleModelArrayList) {
                        if (Objects.equals(iterator.getComplianceType(), Constants.COMPLIANCE_RULES.ID_MISSING_OR_EXPIRED)) {
                            hasToUploadDocument = true;
                            break;
                        }
                    }

            }
            if (hasToUploadDocument && !Objects.equals(mView.getPaymentMethod(), BANKWIRE)) {
                CheckoutNavigator.navigateToVerification(mActivity);
            } else {
                mView.checkBankWire();
            }
        }
    }

    @Override
    public void onMoreFieldsReceived(MoreField moreFields, int position) {
        // Build map with current data
        HashMap<String, Field> currentStepFormData = new HashMap<>();
        for (Field field : this.mFormData) {
            if (!TextUtils.isEmpty(field.getName())) {
                currentStepFormData.put(field.getName(), field);
            }
        }

        if (moreFields != null && moreFields.getFormData() != null && moreFields.getFormData().getFields() != null && moreFields.getFormData().getFields().size() > 0) {
            ArrayList<Field> listFields = moreFields.getFormData().getFields();
            this.mMoreFormDataAdded = listFields;
            if (listFields != null && listFields.size() > 0) {

                ArrayList<android.util.Pair<Integer, Field>> deleteAddList = new ArrayList<>();
                ArrayList<Field> addList = new ArrayList<>();

                for (Field fieldReceived : listFields) {
                    // Check if should add or remove
                    if (!TextUtils.isEmpty(fieldReceived.getType()) && fieldReceived.getType().equalsIgnoreCase(Constants.FIELD_TYPE.DELETE)) {
                        int positionStepForm = 0;
                        for (Field fieldStep : this.mFormData) {
                            if (!TextUtils.isEmpty(fieldStep.getName()) && !TextUtils.isEmpty(fieldReceived.getName()) &&
                                    fieldStep.getName().equalsIgnoreCase(fieldReceived.getName())) {
                                deleteAddList.add(new android.util.Pair<>(positionStepForm, fieldStep));
                            }
                            positionStepForm++;
                        }
                    } else {
                        if (!TextUtils.isEmpty(fieldReceived.getName()) && !currentStepFormData.containsKey(fieldReceived.getName())) {
                            addList.add(fieldReceived);
                        }
                    }
                }

                if (deleteAddList.size() > 0) {
                    mView.enableDisableEditTextListeners(false);
                    for (android.util.Pair<Integer, Field> pairDelete : deleteAddList) {
                        this.mFormData.remove(pairDelete.second);
                        mView.notifyAddedRemoveFields(position + 1, 1, false);
                    }
                    mView.enableDisableEditTextListeners(true);
                    deleteAddList.clear();
                }

                if (addList.size() > 0) {
                    this.mFormData.addAll(position + 1, addList);
                    mView.enableDisableEditTextListeners(false);
                    mView.notifyAddedRemoveFields(position + 1, addList.size(), true);
                    mView.enableDisableEditTextListeners(true);
                    addList.clear();
                }

                mView.showProgressDialog(false, "", "", false);
            }
        } else {
            mView.showProgressDialog(false, "", "", false);

            // Delete prev more fields added

            if (this.mMoreFormDataAdded.size() > 0) {
                for (Field fieldToRemove : mMoreFormDataAdded) {
                    this.mFormData.remove(fieldToRemove);
                    mView.notifyAddedRemoveFields(position + 1, 1, false);
                }
                this.mMoreFormDataAdded.clear();
            }
        }
    }

    @Override
    public void onMoreFieldsError() {
        mView.showProgressDialog(false, "", "", false);
    }

    public void onRetryButtonClick() {
        mView.showHideTotalErrorView(false);
        mView.showHideLoadingView(true);

        mInteractor.requestCheckoutInfo(mTransactionalData);
    }


    //------------------
    // CLICK METHODS
    //------------------

    public void onConfirmButtonClick() {
        //Firstly check flinks validation
        User currentUser = userDataRepository.retrieveUser();
        if (currentUser != null && currentUser.getFlinksState() != null && currentUser.getFlinksState().equals(Constants.FLINKS_STATE.FLINKS_CONNECT)) {
            mView.showFlinksValidation();
        } else {
            //Try to create transaction
            mView.showProgressDialog(true, SmallWorldApplication.getStr(R.string.progress_dialog_transactional_content), SmallWorldApplication.getStr(R.string.loading_text), true);
            mView.hideTopErrorView();

            // no me hace falta "bank"
            ArrayList<android.util.Pair<String, String>> list = FormUtils.fillDataFormToPerformRequest(mFormData);
            ArrayList<android.util.Pair<String, String>> listByPaynmentMethodAux = fillSpecificDataFormCheckoutByPaymentMethod(getPaynmentMethodSelected(), list);

            // Match data
            if (list != null && listByPaynmentMethodAux != null) {
                list.addAll(listByPaynmentMethodAux);
            }

            mInteractor.createTransaction(list, mTransactionalData, mCheckoutData);
        }
    }

    public void onCountryPhoneClickFormEvent(Field field, int position) {
        if (field != null && field.getChilds() != null) {
            ArrayList<TreeMap<String, String>> dataField = field.getChilds().get(0).getData();

            String typeCell = field.getSubtype();
            String type = field.getType();
            String value = field.getValue();

            FormUtils.showGenericSelectorActivity(mActivity, dataField, typeCell, position, type, "", field, mCheckoutData.getTransactionSummary().getDeliveryMethod());
        }
    }

    //------------------
    // FORM CLICKS
    //------------------

    public void onComboOwnDataSelected(Field field, int position) {
        if (field != null) {
            ArrayList<TreeMap<String, String>> dataField = field.getData();
            FormUtils.showGenericSelectorActivity(mActivity, dataField, field.getSubtype(), position, field.getType(), "", field, mCheckoutData.getTransactionSummary().getDeliveryMethod());
        }
    }

    public void showRangeDateSelector(Field field, int position, String type, String value) {
        if (field != null) {
            mView.showDateRangeSelector(field, position, type, value);
        }
    }

    public void onDateSelected(int position, int day, int month, int year) {
        if (position != -1) {
            ArrayList<KeyValueData> values = new ArrayList<>();

            values.add(new KeyValueData("", String.valueOf(day)));
            values.add(new KeyValueData("", String.valueOf(month)));
            values.add(new KeyValueData("", String.valueOf(year)));

            mView.updateComboGroupValueData(values, position);
        }
    }

    public void onComboApiDataSelected(Field field, int position) {
        if (field != null) {
            FormUtils.processApiFieldType(mContext, mActivity, field, position, field.getType(), mFormData, "", mCheckoutData.getTransactionSummary().getDeliveryMethod());
        }
    }

    public void onDropContentSelected(KeyValueData keyValueSelected, int positionField) {
        if (keyValueSelected != null && positionField != -1 && mView != null) {

            // Search specific step
            ArrayList<KeyValueData> values = new ArrayList<>();
            values.add(keyValueSelected);
            mView.updateComboGroupValueData(values, positionField);
            checkIfShouldRequestMoreFields(positionField);
        }
    }

    public void checkIfShouldRequestMoreFields(int position) {
        if (position > 0 && mFormData != null && position < mFormData.size()) {
            Field field = mFormData.get(position);
            if (field != null) {
                if (field.getType().equalsIgnoreCase(Constants.FIELD_TYPE.COMBO) &&
                        field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.COMBO_REQUEST)) {
                    mView.showProgressDialog(true, SmallWorldApplication.getStr(R.string.progress_dialog_transactional_title), SmallWorldApplication.getStr(R.string.progress_dialog_transactional_content), true);
                    mInteractor.getMoreFields(Utils.formatUrlWithFields(field.getRefApi().getUrl(), field.getRefApi().getParams(), field.getKeyValue()), position);
                }
            }
        }
    }

    public void sendHelpEmail() {
        if (mCurrentCreateTransactionResponse != null && mCurrentCreateTransactionResponse.getErrors() != null) {
            mView.showProgressDialog(true, SmallWorldApplication.getStr(R.string.sending_email_text),
                    SmallWorldApplication.getStr(R.string.loading_text), true);

            User user = mInteractor.getUser();
            if (user != null) {
                String subject = String.format(SmallWorldApplication.getStr(R.string.customer_support_contact_user_error_checkout), user.getName());
                String preMessage = String.format(SmallWorldApplication.getStr(R.string.customer_support_contact_user_error_checkout_msg), user.getName(), user.getId());

                StringBuilder builder = new StringBuilder();
                builder.append(preMessage);
                builder.append("\n");
                for (TransactionErrors error : mCurrentCreateTransactionResponse.getErrors()) {
                    builder.append("->");
                    builder.append(error.getDescription());
                    builder.append("\n");
                }

                mInteractor.sendEmail(subject, builder.toString(), null);
            }
        }
    }

    private String getPaynmentMethodSelected() {
        for (Field field : mFormData) {
            if (!TextUtils.isEmpty(field.getName()) && field.getName().equalsIgnoreCase(PAYNMENT_METHOD_KEY)) {
                return field.getKeyValue();
            }
        }
        return "";
    }

    //-----------------
    // UTILS METHODS
    //-----------------

    public ArrayList<android.util.Pair<String, String>> fillSpecificDataFormCheckoutByPaymentMethod(String paynmentMethod, ArrayList<android.util.Pair<String, String>> alreadyAddedList) {
        if (TextUtils.isEmpty(paynmentMethod)) {
            return null;
        }

        ArrayList<android.util.Pair<String, String>> list = new ArrayList<>();

        for (Field field : mFormData) {
            if (field.getType().equalsIgnoreCase(Constants.FIELD_TYPE.COMBO) && field.getData() != null) {
                if (paynmentMethod.equalsIgnoreCase(Constants.PAYNMENT_METHODS.BANKWIRE) && !TextUtils.isEmpty(field.getName()) && field.getName().equalsIgnoreCase(DEPOSIT_BANK_ID)) {
                    String valueSelected = field.getValue();
                    if (TextUtils.isEmpty(valueSelected)) {
                        break;
                    }
                    for (TreeMap<String, String> map : field.getData()) {
                        if (valueSelected.equalsIgnoreCase(map.firstEntry().getValue())) {
                            String depositBankId = TextUtils.isEmpty(map.get(DEPOSIT_BANK_ID)) ? "" : map.get(DEPOSIT_BANK_ID);
                            String depositBankBranchId = TextUtils.isEmpty(map.get(DEPOSIT_BANK_BRANCH_ID)) ? "" : map.get(DEPOSIT_BANK_BRANCH_ID);

                            list.add(new android.util.Pair<>(DEPOSIT_BANK_ID, depositBankId));
                            list.add(new android.util.Pair<>(DEPOSIT_BANK_BRANCH_ID, depositBankBranchId));
                            break;
                        }
                    }
                } else {
                    boolean contained = false;
                    if (!TextUtils.isEmpty(field.getName())) {
                        for (android.util.Pair<String, String> addedField : alreadyAddedList) {
                            if (addedField.first.equalsIgnoreCase(field.getName())) {
                                contained = true;
                                break;
                            }
                        }
                    }

                    if (!contained) {
                        list.add(new android.util.Pair<>(field.getName(), TextUtils.isEmpty(field.getValue()) ? "" : field.getValue()));
                    }
                }
            }
        }

        return list;
    }

    public User getUser() {
        return userDataRepository.retrieveUser();
    }

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }
}
