package com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.DEFAULT_AMOUNT;
import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.DEFAULT_OPERATION;
import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.TOTALSALE;
import static com.smallworldfs.moneytransferapp.utils.Constants.DELIVERY_METHODS.CASH_PICKUP;
import static com.smallworldfs.moneytransferapp.utils.Constants.DELIVERY_METHODS.ON_CALL;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.AMOUNT;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.BENEFICIARY_ID;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.BENEFICIARY_TYPE;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.CURRENCY;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.CURRENCY_ORIGIN;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.CURRENCY_TYPE;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.OPERATION;
import static com.smallworldfs.moneytransferapp.utils.Constants.USER_PARAMS.COUNTRY;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.INT_ZERO;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_EMPTY;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.EcommerceCheckoutInfo;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateValues;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.FormAbstracPresenter;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.CalculatorPromotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.repository.PromotionsRepository;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors.implementation.TransactionalInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.StepStatus;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.MoreField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Payout;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationStepResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.TransactionalPresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.navigator.TransactionalNavigator;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.DeliveryMethodUIModel;
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickupResultModel;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.FormUtils;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;

import rx.Scheduler;

public class TransactionalPresenterImpl extends FormAbstracPresenter implements TransactionalPresenter, TransactionalInteractorImpl.Callback, CalculatorInteractorImpl.Callback {

    private static final String TAG = TransactionalPresenterImpl.class.getSimpleName();
    private static final String ADD_ACTION = "ADD";
    private static final String DELETE_ACTION = "DELETE_ACTION";
    private static final String DELIVERY_METHOD_KEY = "deliveryMethod";
    private static final int FINISH_ACTIVITY_DELAY = 300;
    protected ArrayList<Step> mStructureSteps;
    private HashMap<Step, String> mMapNewContentSteps;
    protected HashMap<Step, HashMap<String, String>> mValuesByStep;
    private final String mOperation;
    private final TransactionalPresenter.View mView;
    private boolean mShowingCalculator = false;
    private final TransactionalInteractorImpl mInteractor;
    private BeneficiaryUIModel mBeneficiary;
    private String mYouPayAmount;
    protected String beneficiaryType;
    private String deliveryMethod;

    private HashMap<String, String> triggers = new HashMap<>();
    public String payoutName = "";
    protected Pair<String, String> mCountryOrigin;

    public TransactionalPresenterImpl(Scheduler observeOn, Scheduler subscribeOn, Context context, TransactionalPresenter.View view, GenericActivity activity, BeneficiaryUIModel beneficiary, String youPayAmount, String beneficiaryType) {
        super(observeOn, context);
        this.mView = view;
        this.mMapNewContentSteps = new HashMap<>();
        this.mValuesByStep = new HashMap<>();
        this.mBeneficiary = beneficiary;
        this.mInteractor = new TransactionalInteractorImpl(observeOn, subscribeOn, this, mValuesByStep, mBeneficiary != null ? mBeneficiary.getId() : "");
        mYouPayAmount = youPayAmount;
        this.mOperation = "SEND_TO";
        this.mActivity = activity;
        this.beneficiaryType = beneficiaryType;
    }

    @Override
    public void create() {
        super.create();

        mView.configureView();

        // Configure top calculator
        configureStaticCalculator();

        // Request Structure Form
        requestTransactionalStructure(-1);

        // Add Calculator callback
        CalculatorInteractorImpl.getInstance().addCallback(this);

        // Calculate
        if (CalculatorInteractorImpl.getInstance().getCurrencyType().equals(Constants.CALCULATOR.TOTALSALE)) {
            mYouPayAmount = CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount();
            CalculatorInteractorImpl.getInstance().calculateTransactionalAmount(mYouPayAmount);
        } else {
            CalculatorInteractorImpl.getInstance().calculateTransactionalAmount(CalculatorInteractorImpl.getInstance().getCurrentBeneficiaryAmount());
        }

        requestQuickReminderInfo();

        // Set user property

        CalculatorInteractorImpl.getInstance().saveLastCalculatorData();

        triggers = new HashMap<>();

    }


    @Override
    public void resume() {
        super.resume();
        if (CalculatorInteractorImpl.getInstance().getCalculatorStatus().equals(CalculatorInteractorImpl.CalculatorStatus.RESET_VALUE)) {
            CalculatorInteractorImpl.getInstance().setCalculatorStatus(CalculatorInteractorImpl.CalculatorStatus.OK);
        }

    }

    @Override
    public void destroy() {

        // Remove Calculator callback
        CalculatorInteractorImpl.getInstance().removeCallback(this);

        if (mInteractor != null) {
            mInteractor.destroy();
        }

        if (mStructureSteps != null) {
            mStructureSteps.clear();
            mStructureSteps = null;
        }

        if (mMapNewContentSteps != null) {
            mMapNewContentSteps.clear();
            mMapNewContentSteps = null;
        }

        super.destroy();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    public void setShowingCalculator(boolean showing) {
        this.mShowingCalculator = showing;
    }

    private void configureStaticCalculator() {
        // Calculator header info
        if (CalculatorInteractorImpl.getInstance().getCalculatorStatus() == CalculatorInteractorImpl.CalculatorStatus.OK) {

            mView.configureStaticCalculator(CalculatorInteractorImpl.getInstance().getPayoutCountryKey(),
                    CalculatorInteractorImpl.getInstance().getCurrentBeneficiaryAmount(),
                    CalculatorInteractorImpl.getInstance().getPayoutCurrency());

            mView.showYouPayCalculated(CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount(),
                    CalculatorInteractorImpl.getInstance().getSendingCurrency());
            mView.showPayoutBottomCalculator(
                    CalculatorInteractorImpl.getInstance().getCurrentBeneficiaryAmount(),
                    CalculatorInteractorImpl.getInstance().getPayoutCurrency(),
                    CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount(),
                    CalculatorInteractorImpl.getInstance().getSendingCurrency()
            );

        } else {
            // Recovery last calculator status
            CalculatorInteractorImpl.getInstance().recoveryLastStatusCalculator(true);
        }
    }

    public Method getDeliveryMethod() {
        return mInteractor.getSelectedDeliveryMethod();
    }

    public void stepClicked(Step step) {
        mView.collapseAllSteps(step);
        mView.togleStep(step);

    }


    /**
     * Request structure data to server
     */
    protected void requestTransactionalStructure(int positionStepQueue) {
        mInteractor.getFormStructures(positionStepQueue, mOperation, beneficiaryType);
    }

    private void requestQuickReminderInfo() {
        String country = (mCountryOrigin != null) ? mCountryOrigin.first : CalculatorInteractorImpl.getInstance().getPayoutCountryKey();
        mInteractor.getQuickReminderInfo(country);
    }

    /**
     * Draw structure data in view
     */
    protected void drawStructureForm() {
        if (mBeneficiary != null && mBeneficiary.getDeliveryMethod() == null) {
            onStructureError();
        } else {
            // Check new content
            if (mMapNewContentSteps.size() != 0) {
                for (HashMap.Entry<Step, String> entry : mMapNewContentSteps.entrySet()) {
                    if (entry.getValue().equals(ADD_ACTION)) {
                        entry.getKey().setNewStep(true);
                        mStructureSteps.add(entry.getKey());
                    }
                }
            }

            if (mStructureSteps != null && mStructureSteps.size() > 0) {
                int position = 0;
                for (Step step : mStructureSteps) {
                    mView.appendStep(step, position, position == mStructureSteps.size() - 1);
                    position++;
                }
                // Check if existing beneficiary
                if (mBeneficiary == null) {
                    mView.hideGeneralLoadingView();
                }
                paintContentSteps();

                // Check if existing beneficiary to autorequest step 1 & 2
                if (mBeneficiary != null) {
                    final DeliveryMethodUIModel deliveryMethod = mBeneficiary.getDeliveryMethod();
                    if (!deliveryMethod.getType().isEmpty() && !deliveryMethod.getName().isEmpty()) {
                        TreeMap<String, String> delivery = new TreeMap<>();
                        delivery.put(deliveryMethod.getType(), deliveryMethod.getName());
                        onSelectDeliveryMethodClick(mStructureSteps.get(0), new Method(delivery));
                        mView.setDeliveryMethodAutoSelectedInAdapter(mStructureSteps.get(0), new Method(delivery));
                    }
                }
            }

            // Check old content
            if (mMapNewContentSteps.size() != 0) {
                try {
                    for (HashMap.Entry<Step, String> entry : mMapNewContentSteps.entrySet()) {
                        if (entry.getValue().equals(DELETE_ACTION)) {
                            int index = -1;
                            for (Step step : mStructureSteps) {
                                if (step.getStepId().equals(entry.getKey().getStepId())) {
                                    index = mStructureSteps.indexOf(step);
                                }
                            }
                            if (index != -1 && index <= mStructureSteps.size() - 1) {
                                mView.deleteStep(mStructureSteps.get(index));
                                mView.updateNewLastStep(mStructureSteps.get(index - 1));
                                mStructureSteps.remove(index);
                            }
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ee) {
                    Log.e(TAG, "drawStructureForm:e:-----------------------------------", ee);
                }
            }
        }
        mMapNewContentSteps.clear();
    }

    public void paintContentSteps() {
        for (Step step : mStructureSteps) {
            if (step.getStepType().equals(Constants.STEP_TYPE.DELIVERY_METHOD)) {
                if ((mInteractor != null) && (mInteractor.getDeliveryMethods() != null) && (mInteractor.getDeliveryMethods().size() > 0)) {
                    mView.drawContentStep(step, mInteractor.getDeliveryMethods(), CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount());
                }
            }
            break;
        }
    }


    protected void enableStep(Step step) {
        mView.enableStep(step);
    }


    protected void openStep(Step prevStep) {
        mView.openStep(prevStep);
    }


    public void onSelectDeliveryMethodClick(final Step step, final Method method) {

        mView.showValidatingLoadingStepView(step);
        mView.hideStepErrorView(step);

        // Mark value in step for show selected in form
        step.setStepSelectedItem(mInteractor.getTranslatedDeliveryMethod(method.getMethod().firstKey()));
        deliveryMethod = method.getMethod().firstKey();
        mView.onDeliveryMethodSelected(deliveryMethod);
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>(DELIVERY_METHOD_KEY, method.getMethod().firstKey()));
        fillDataForm(step, list);

        if ((mInteractor != null) && (mInteractor.getSelectedDeliveryMethod() != null) && (mInteractor.getSelectedDeliveryMethod().getMethod() != null) &&
                (mInteractor.getSelectedDeliveryMethod().getMethod().firstEntry() != null) &&
                (Objects.requireNonNull(mInteractor.getSelectedDeliveryMethod().getMethod().firstEntry()).getKey() != null) &&
                (method.getMethod() != null) &&
                (method.getMethod().firstEntry() != null) &&
                (Objects.requireNonNull(mInteractor.getSelectedDeliveryMethod().getMethod().firstEntry()).getKey().equals(Objects.requireNonNull(method.getMethod().firstEntry()).getKey()))) {

            validateStepAndRequestNextData(step);

        } else {
            // Clear prev request
            CalculatorInteractorImpl.getInstance().clearPrevRequest();

            // Clear beneficiary
            mInteractor.clearBeneficiaryId();
            mBeneficiary = null;

            // Check if can retain data
            Step nextStep = mStructureSteps.get(1);
            if (nextStep != null) {
                mInteractor.retainFormData(nextStep);
            }

            requestTransactionalStructure(mStructureSteps.indexOf(step));

            mView.showCalculatorLoadingView(true);
            mView.collapseAllSteps(step);
            mView.resetStepViewStatus();
            mView.showHideCalculatorErroView(false, "");

            CalculatorInteractorImpl.getInstance().updateCalculatorWithDeliveryMethod(method);

            for (Step stepAux : mStructureSteps) {
                if (!stepAux.getStepType().equals(Constants.STEP_TYPE.DELIVERY_METHOD)) {
                    mView.resetContentStep(stepAux);
                    mView.disableStep(stepAux);
                    mView.restoreStepIndicator(stepAux);
                }
            }
        }


    }

    public void registerAddToCartEcommerceEvent() {
        mView.registerAddToCartEvent();
    }

    public void onSubmitFormClick(Step step) {

        if (mActivity != null && mActivity.getCurrentFocus() != null) {
            mActivity.getCurrentFocus().clearFocus();
        }

        Utils.logActionCrashlytics("Submit for step " + step.getStepId() + " done");

        if (step.getFormData() != null && (CalculatorInteractorImpl.getInstance().getCalculatorStatus() != CalculatorInteractorImpl.CalculatorStatus.ERROR)) {

            mView.showValidatingLoadingStepView(step);
            mView.hideStepErrorView(step);

            ArrayList<Pair<String, String>> list = FormUtils.fillDataFormToPerformRequest(step.getFormData().getFields());
            for (Field field : step.getFormData().getFields()) {
                // Update value selected in step title
                if (!TextUtils.isEmpty(field.getName()) && field.getName().equalsIgnoreCase(Constants.FIELD_CONSTANS_KEYS.NAME)) {
                    step.setStepSelectedItem(TextUtils.isEmpty(field.getValue()) ? "" : field.getValue());
                    break;
                }
            }

            fillDataForm(step, list);
            validateStepAndRequestNextData(step);

        } else {
            // Open step to change delivery Method
            for (Step stepAux : mStructureSteps) {
                if (stepAux.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.DELIVERY_METHOD)) {
                    mView.openStep(stepAux);
                } else {
                    mView.closeStep(stepAux);
                }
            }
        }
    }

    public void onLocationSelected(Step step, String locationCode, String representativeCode) {
        if (step != null && !TextUtils.isEmpty(locationCode)) {

            step.setSingleStepValue(new KeyValueData(Constants.FIELD_CONSTANS_KEYS.LOCATION_CODE, locationCode));
            fillDataForm(step);
            step.setSingleStepValue(new KeyValueData(Constants.FIELD_CONSTANS_KEYS.REPRESENTATIVE_CODE, representativeCode));
            fillDataForm(step);

            mView.showValidatingLoadingStepView(step);
            mView.hideStepErrorView(step);

            validateStepAndRequestNextData(step);
        }

    }


    private void validateStepAndRequestNextData(Step step) {
        Step nextStep = null;
        if (step.getIsFinal() != 1) {
            nextStep = mStructureSteps.get(mStructureSteps.indexOf(step) + 1);
        }
        mInteractor.validateStepAndRequestNextData(step, mOperation, nextStep, beneficiaryType);
    }


    /**
     * Side = 0 -> Beneficiary click, Side = 1 -> You pay click
     */
    public void clickCalculator(int side) {
        if (CalculatorInteractorImpl.getInstance().getCalculatorStatus() == CalculatorInteractorImpl.CalculatorStatus.ERROR) {
            // Check error type
            if (CalculatorInteractorImpl.getInstance().getCurrentDeliveryMethod() != null &&
                    CalculatorInteractorImpl.getInstance().getCurrentDeliveryMethod().getCurrencies() != null &&
                    CalculatorInteractorImpl.getInstance().getCurrentDeliveryMethod().getCurrencies().size() > 1) {
                showCalculator(side);
            } else {
                if (mStructureSteps != null) {
                    for (Step step : mStructureSteps) {
                        if (step.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.DELIVERY_METHOD)) {
                            mView.openStep(step);
                        } else {
                            mView.closeStep(step);
                        }
                    }
                }
            }
        } else if (CalculatorInteractorImpl.getInstance().getCalculatorStatus() != CalculatorInteractorImpl.CalculatorStatus.LOADING_DATA) {
            showCalculator(side);
        }
    }

    private void showCalculator(int side) {
        if (!mShowingCalculator) {
            mShowingCalculator = true;
            mView.showCalculator(side);
        }
    }


    public void retryGeneralStructureClick() {
        mView.hideGeneralStructureErrorView();
        mView.showGeneralLoadingView();
        requestTransactionalStructure(-1);
    }

    protected void markCompletedStep(Step step) {
        step.setStatus(StepStatus.COMPLETED);
        if (step.getFormData() != null && step.getFormData().getFields() != null) {
            FormUtils.setClearErrors(null, step.getFormData().getFields());
        }
        mView.notifyGlobalChanges(step);
        mView.completeStep(step);

        if (step.getIsFinal() == 0) {
            mView.closeStep(step);
        }

        Utils.logActionCrashlytics("Step " + step.getStepId() + " done");
    }

    private void checkNewOldContent(ArrayList<Step> steps) {
        for (Step newStep : steps) {
            boolean alreadyExist = false;
            for (Step step : mStructureSteps) {
                if (newStep.getStepId().equals(step.getStepId())) {
                    alreadyExist = true;
                    break;
                }
            }
            if (!alreadyExist) {
                // New step
                mMapNewContentSteps.put(newStep, ADD_ACTION);
            }
        }

        // Reserve action
        for (Step step : mStructureSteps) {
            boolean exist = false;
            for (Step newStep : steps) {
                if (step.getStepId().equals(newStep.getStepId())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                // New step
                mMapNewContentSteps.put(step, DELETE_ACTION);
            }
        }
    }


    private void updateStepInList(Step step) {
        for (Step stepAux : mStructureSteps) {
            if (step.getStepId().equals(stepAux.getStepId())) {
                stepAux.updateStep(step);
            }
        }
    }

    /**
     * Fill data for validate and submit form
     */
    protected void fillDataForm(Step step, ArrayList<Pair<String, String>> data) {
        if (mValuesByStep.containsKey(step)) {
            // Clear prev status
            if (mValuesByStep.get(step) != null) {
                Objects.requireNonNull(mValuesByStep.get(step)).clear();
            }

            HashMap<String, String> stepData = mValuesByStep.get(step);
            for (Pair<String, String> value : data) {
                Objects.requireNonNull(stepData).put(value.first, value.second);
            }
        } else {
            HashMap<String, String> stepData = new HashMap<>();
            for (Pair<String, String> value : data) {
                stepData.put(value.first, value.second);
            }
            mValuesByStep.put(step, stepData);
        }
    }

    /**
     * Fill data for validate and submit form
     */
    private void fillDataForm(Step step) {
        if (mValuesByStep.containsKey(step)) {
            HashMap<String, String> stepData = mValuesByStep.get(step);
            if (step.getSingleStepValue() != null) {
                Objects.requireNonNull(stepData).put(step.getSingleStepValue().getKey(), step.getSingleStepValue().getValue());
            }
        } else {
            HashMap<String, String> stepData = new HashMap<>();
            if (step.getSingleStepValue() != null) {
                stepData.put(step.getSingleStepValue().getKey(), step.getSingleStepValue().getValue());
            }
            mValuesByStep.put(step, stepData);
        }
    }


    public void onDropContentSelected(KeyValueData keyValueSelected, String stepId, int position) {
        if (keyValueSelected != null && !TextUtils.isEmpty(stepId) && position != -1) {
            if (mStructureSteps != null)
                for (Step step : mStructureSteps) {
                    if (step.getStepId().equalsIgnoreCase(stepId)) {
                        afterTriggeredFieldChange(stepId, position);

                        ArrayList<KeyValueData> values = new ArrayList<>();
                        values.add(keyValueSelected);
                        mView.updateComboGroupValueData(values, step, position);
                        checkIfShouldRequestMoreFields(stepId, position);
                        break;
                    }
                }
        }
    }

    private void checkIfShouldRequestMoreFields(String stepId, int position) {
        if (!TextUtils.isEmpty(stepId) && position > 0) {
            for (Step step : mStructureSteps) {
                if (step.getStepId().equalsIgnoreCase(stepId)) {
                    Field field = null;
                    if (step.getFormData().getFields().size() > position) {
                        field = step.getFormData().getFields().get(position);
                    }
                    if (field != null && field.getType().equalsIgnoreCase(Constants.FIELD_TYPE.COMBO) && field.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.COMBO_REQUEST)) {
                        mView.showProgressDialog(true);
                        mInteractor.getMoreFields(step, Utils.formatUrlWithFields(field.getRefApi().getUrl(), field.getRefApi().getParams(), field.getKeyValue()), position);
                    }
                    break;
                }
            }
        }
    }

    public void onRequestInfoFromContacts() {
        mView.checkAndRequestPermissions();
    }

    public void permissionsGranted() {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        mActivity.startActivityForResult(Intent.createChooser(i, "Select contact"), Constants.REQUEST_CODES.INFO_CONTACTS_REQUEST_CODE);
    }

    public void permissionsDenied() {

    }

    public void onDateSelected(int position, int day, int month, int year, String stepId) {
        if (!TextUtils.isEmpty(stepId) && position != -1) {
            // Search specific step
            for (Step step : mStructureSteps) {
                if (step.getStepId().equalsIgnoreCase(stepId)) {
                    ArrayList<KeyValueData> values = new ArrayList<>();

                    values.add(new KeyValueData("", String.valueOf(day)));
                    values.add(new KeyValueData("", String.valueOf(month)));
                    values.add(new KeyValueData("", String.valueOf(year)));

                    mView.updateComboGroupValueData(values, step, position);
                    break;
                }
            }
        }
    }


    public void onContactSelected(Uri uriContact) {

        Cursor cursor;
        String id = "";
        String phone = "";
        String name = "";
        String lastName = "";
        String email = "";
        String address = "";
        String city = "";
        String postalCode = "";

        try {
            cursor = mContext.getContentResolver().query(uriContact, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                cursor.close();
            }

            Cursor emailCur = mContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{id}, null);

            if (emailCur != null) {
                while (emailCur.moveToNext()) {
                    email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emailCur.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "onContactSelected:e:---------------------------------------------", e);
        }

        Uri uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
        String sortOrder = ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cr = mContext.getContentResolver().query(uri, null, ContactsContract.Data.CONTACT_ID + "=" + id, null, sortOrder);
        if (cr != null) {
            while (cr.moveToNext()) {
                address = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                postalCode = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                city = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            }
            cr.close();
        }


        // Search step and fill values
        if (mStructureSteps != null)
            for (Step step : mStructureSteps) {
                if (step.getStepType().equals(Constants.STEP_TYPE.BENEFICIARY_FORM)) {
                    step.setStepSelectedItem(name);
                    if (step.getFormData() != null && step.getFormData().getFields() != null) {
                        for (Field field : step.getFormData().getFields()) {
                            if (field.getChilds() != null && field.getChilds().size() > 1) {
                                for (Field fieldAux : field.getChilds()) {
                                    if (!TextUtils.isEmpty(fieldAux.getName())) {
                                        switch (fieldAux.getName()) {
                                            case Constants.FIELD_CONSTANS_KEYS.NAME:
                                                fieldAux.setValue(name);
                                                break;
                                            case Constants.FIELD_CONSTANS_KEYS.LAST_NAME:
                                                fieldAux.setValue(lastName);
                                                break;
                                            case Constants.FIELD_CONSTANS_KEYS.PHONE_NUMBER:
                                                fieldAux.setValue(phone);
                                                break;
                                            case Constants.FIELD_CONSTANS_KEYS.EMAIL:
                                                fieldAux.setValue(email);
                                                break;
                                            case Constants.FIELD_CONSTANS_KEYS.ADDRESS:
                                                fieldAux.setValue(address);
                                                break;
                                            case Constants.FIELD_CONSTANS_KEYS.CITY:
                                                fieldAux.setValue(city);
                                                break;
                                            case Constants.FIELD_CONSTANS_KEYS.POSTAL_CODE:
                                                fieldAux.setValue(postalCode);
                                                break;
                                        }
                                    }
                                }
                            } else {
                                if (!TextUtils.isEmpty(field.getName())) {
                                    switch (field.getName()) {
                                        case Constants.FIELD_CONSTANS_KEYS.NAME:
                                            field.setValue(name);
                                            break;
                                        case Constants.FIELD_CONSTANS_KEYS.LAST_NAME:
                                            field.setValue(lastName);
                                            break;
                                        case Constants.FIELD_CONSTANS_KEYS.PHONE_NUMBER:
                                            field.setValue(phone);
                                            break;
                                        case Constants.FIELD_CONSTANS_KEYS.EMAIL:
                                            field.setValue(email);
                                            break;
                                        case Constants.FIELD_CONSTANS_KEYS.ADDRESS:
                                            field.setValue(address);
                                            break;
                                        case Constants.FIELD_CONSTANS_KEYS.CITY:
                                            field.setValue(city);
                                            break;
                                        case Constants.FIELD_CONSTANS_KEYS.POSTAL_CODE:
                                            field.setValue(postalCode);
                                            break;
                                    }

                                }
                            }
                        }
                    }
                    mView.notifyGlobalChanges(step);
                }
            }
    }

    public void onLocationListMoreClick(@NonNull final ArrayList<Field> payoutsLocations,
                                        @NonNull final String stepId,
                                        @NonNull final String locationCode,
                                        @NonNull final String title,
                                        @NonNull final String locationTextFormatted,
                                        @NonNull final String nameSelected,
                                        @NonNull final String payoutType,
                                        final double taxSelected) {
        mInteractor.persistPayoutLocations(payoutsLocations);
        TransactionalNavigator.navigateToSelectPayoutLocationActivity(mActivity, payoutsLocations, stepId, locationCode, title, locationTextFormatted,
                nameSelected, payoutType, taxSelected);

        if (title.equalsIgnoreCase(SmallWorldApplication.getStr(R.string.select_cash_pickup_payers_title)))
            mView.onChoosePickUpLocation(null);
        if (title.equalsIgnoreCase(SmallWorldApplication.getStr(R.string.select_available_payers_title)))
            mView.onClickMore();

    }

    public void onPayoutLocationSelected(Field field, String stepId) {
        if (field != null && !TextUtils.isEmpty(stepId)) {
            if (mStructureSteps != null)
                for (Step step : mStructureSteps) {
                    if (step.getStepId().equalsIgnoreCase(stepId)) {
                        mView.showCalculatorLoadingView(true);
                        this.payoutName = field.getPayout().getLocationName();
                        mView.updateLocationPayoutContentStep(field, step, CalculatorInteractorImpl.getInstance().getSendingCurrency());
                        break;
                    }
                }
        }
    }

    public String getCitySelectedToShowInPayoutLocation() {
        for (Step step : mStructureSteps) {
            if (step.getStepType().equals(Constants.STEP_TYPE.BENEFICIARY_FORM) &&
                    step.getFormData() != null && step.getFormData().getFields() != null) {
                for (Field field : step.getFormData().getFields()) {
                    if (!TextUtils.isEmpty(field.getName()) && field.getName().equalsIgnoreCase(Constants.FIELD_CONSTANS_KEYS.CITY) &&
                            !TextUtils.isEmpty(field.getValue())) {
                        return field.getValue();
                    }
                }
            }
        }
        return "";
    }

    public String getBankSelectedToShowInPayoutLocation() {
        for (Step step : mStructureSteps) {
            if (step.getStepType().equals(Constants.STEP_TYPE.BENEFICIARY_FORM) &&
                    step.getFormData() != null && step.getFormData().getFields() != null) {
                for (Field field : step.getFormData().getFields()) {
                    if (!TextUtils.isEmpty(field.getName()) && field.getName().equalsIgnoreCase(Constants.FIELD_CONSTANS_KEYS.BANK_ID) &&
                            !TextUtils.isEmpty(field.getValue())) {
                        return field.getValue();
                    }
                }
            }
        }
        return "";
    }

    public void onChangeLocationClickInStep3(Step step) {
        mView.closeStep(step);
        mView.onChangeClicked();
        for (Step stepAux : mStructureSteps) {
            if (stepAux.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.BENEFICIARY_FORM)) {
                mView.openStep(stepAux);
                break;
            }
        }
    }

    public void onLocationMapButtonClick(Payout payout) {
        TransactionalNavigator.navigateToLocationMapActivity(mActivity, payout, SmallWorldApplication.getStr(R.string.locations_maps_title_activity));
    }

    public boolean goToLoseInformation() {
        if (mStructureSteps != null) {
            for (Step step : mStructureSteps) {
                if (step.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.DELIVERY_METHOD)) {
                    if (step.getStatus() == StepStatus.COMPLETED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void exitTransaction() {
        if (CalculatorInteractorImpl.getInstance().getCalculatorStatus() == CalculatorInteractorImpl.CalculatorStatus.ERROR)
            CalculatorInteractorImpl.getInstance().recoveryLastStatusCalculator(true);
        CalculatorInteractorImpl.getInstance().setYouPayAmount(DEFAULT_AMOUNT);
        CalculatorInteractorImpl.getInstance().setCalculatorStatus(CalculatorInteractorImpl.CalculatorStatus.RESET_VALUE);
        CalculatorInteractorImpl.getInstance().setCurrencyType(TOTALSALE);

        mInteractor.clearCurrentTransaction();
    }

    public void onTransactionFinished(CreateTransactionResponse transactionResponse, boolean showCheckoutDialogExtra, Checkout checkout) {
        TransactionalNavigator.navigateToHomeActivity(mActivity, transactionResponse, showCheckoutDialogExtra, checkout, payoutName);
    }

    // -----------------------------
    // FORM EVENTS CLICKS
    // -----------------------------


    public void onCountryPhoneClickFormEvent(Field field, int position, Step step) {
        if (field != null && field.getChilds() != null) {
            ArrayList<TreeMap<String, String>> dataField = field.getChilds().get(0).getData();
            String typeCell = field.getSubtype();
            String type = field.getType();
            //String value = field.getValue();
            FormUtils.showGenericSelectorActivity(mActivity, dataField, typeCell, position, type, step.getStepId(), field, deliveryMethod);
        }
    }

    public void onComboOwnDataSelected(Field field, int position, Step step) {
        if (field != null) {
            ArrayList<TreeMap<String, String>> dataField = field.getData();
            FormUtils.showGenericSelectorActivity(mActivity, dataField, field.getSubtype(), position, field.getType(), step.getStepId(), field, deliveryMethod);
            beforeTriggeredFieldChange(field, step);
            mView.onFieldClicked(field);
        }
    }

    private void beforeTriggeredFieldChange(Field field, Step step) {
        String trigger = field.getName();
        for (Field f : step.getFormData().getFields()) {
            if (f.getTriggers() != null && f.getTriggers().equalsIgnoreCase(trigger)) {
                triggers.put(trigger, field.getKeyValue());
                break;
            }
        }
    }

    private void afterTriggeredFieldChange(String stepId, int position) {
        for (Step step : mStructureSteps) {
            if (step.getStepId().equalsIgnoreCase(stepId) && position >= 0 && position < step.getFormData().getFields().size()) {
                Field trigger = step.getFormData().getFields().get(position);
                String val = triggers.get(trigger.getName());
                if (val != null && !trigger.getValue().equalsIgnoreCase(val)) {
                    for (Field field : step.getFormData().getFields()) {
                        if (field.getTriggers() != null && field.getTriggers().equalsIgnoreCase(trigger.getName())) {
                            field.setValue(null);
                            field.setKeyValue(null);
                            mView.notifyGlobalChanges(step);
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    public void onComboApiDataSelected(Field field, int position, Step step) {
        if (field != null) {
            FormUtils.processApiFieldType(mContext, mActivity, field, position, field.getType(), step.getFormData().getFields(), step.getStepId(), deliveryMethod);
        }
        mView.onFieldClicked(field);
    }

    public void onAttachSendFileButtonSelected(Field field, int position) {
    }

    public boolean isBeneficiaryPresent() {
        return mBeneficiary != null;
    }


    @Override
    public void showRangeDateSelector(String stepId, Field field, int position, String type, String value) {
        mView.showDateRangeSelector(field, position, stepId, value, type);
    }


    /**
     * Transactional Callbacks
     */

    @Override
    public void onStructureReceived(ArrayList<Step> steps) {
        if (mStructureSteps == null) {
            mStructureSteps = new ArrayList<>();
            mStructureSteps.addAll(steps);

        } else if (mStructureSteps.size() > 0) {
            if (steps.size() != mStructureSteps.size()) {
                checkNewOldContent(steps);
            }
            // Update steps
            for (Step stepReceived : steps) {
                updateStepInList(stepReceived);
            }
        }
        // Obtener delivery methods
        if (mInteractor != null && CalculatorInteractorImpl.getInstance().getCalculatorStatus() != CalculatorInteractorImpl.CalculatorStatus.ERROR) {
            mInteractor.requestDeliveryMethods(CalculatorInteractorImpl.getInstance().getPayoutCountryKey(), beneficiaryType);
        }
    }


    @Override
    public void onDeliveryMethodsReceived() {
        drawStructureForm();
    }

    @Override
    public void onStructureError() {
        mView.hideGeneralLoadingView();
        mView.onStructureError();
    }

    @Override
    public void onNeedToRequestAgainDataStep(int positionStep) {
        if (positionStep != -1 && mStructureSteps.size() > positionStep && mStructureSteps.get(positionStep) != null) {
            validateStepAndRequestNextData(mStructureSteps.get(positionStep));
        }
    }

    @Override
    public void onValidateStepOk(Step validatedStep, ValidationStepResponse nextStepData, String beneficiaryId) {
        markCompletedStep(validatedStep);

        if (validatedStep.getIsFinal() == 0) {
            Step nextStep = mStructureSteps.get(mStructureSteps.indexOf(validatedStep) + 1);

            nextStep.setFormData(nextStepData.getNextStep().getFormData());
            nextStep.setStepSubType(nextStepData.getNextStep().getStepSubType());

            enableStep(nextStep);

            mView.markCurrentStepEditing(nextStep);

            // Check field content
            if (nextStep.getStepType().equals(Constants.STEP_TYPE.LOCATION_LIST) &&
                    nextStepData.getNextStep().getStepSubType() != null &&
                    (nextStepData.getNextStep().getStepSubType().equals(CASH_PICKUP) || nextStepData.getNextStep().getStepSubType().equals(ON_CALL))) {
                CashPickupResultModel locationSelected = null;
                if (nextStep.getFormData().getFields() != null && nextStep.getFormData().getFields().size() > INT_ZERO) {
                    Payout location = nextStep.getFormData().getFields().get(INT_ZERO).getPayout();
                    locationSelected = new CashPickupResultModel(location.getRepresentativeName(), location.getLocationName(), location.getLocationAddress(), location.getFee(), String.valueOf(location.getRate()), location.getDeliveryTime(), location.getLocationCode(), String.valueOf(location.getRepresentativeCode()));
                }
                mView.drawContentStep(nextStep, locationSelected);
            } else {
                if ((nextStep.getFormData() == null || nextStep.getFormData().getFields() == null || nextStep.getFormData().getFields().size() == 0) && mInteractor.getSelectedDeliveryMethod() != null && mInteractor.getSelectedDeliveryMethod().getMethod() != null) {
                    mView.showStepEmptyView(nextStep, Objects.requireNonNull(mInteractor.getSelectedDeliveryMethod().getMethod().firstEntry()).getKey());
                } else {
                    mView.drawContentStep(nextStep, nextStep.getFormData().getFields(), CalculatorInteractorImpl.getInstance().getSendingCurrency());
                    if (nextStep.getFormData().getFields().get(0).getPayout() != null) {
                        this.payoutName = nextStep.getFormData().getFields().get(0).getPayout().getRepresentativeName();
                    }
                }
            }
            openStep(nextStep);

            // Check if should validate next step with existing beneficiary
            if (mBeneficiary != null && nextStep.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.BENEFICIARY_FORM)) {
                onSubmitFormClick(nextStep);
            } else {
                mBeneficiary = new BeneficiaryUIModel();
                mBeneficiary.setId(beneficiaryId);
                mView.hideGeneralLoadingView();
            }
        } else {
            mView.trackBeginCheckout();
            //goToCheckout(validatedStep.getStepSelectedItem());
            mView.checkAuthenticatedUser(validatedStep, getUser());

        }
        mView.onStepCompleted(validatedStep);
    }

    public void goToCheckout(String payer) {
        TransactionalNavigator.navigateToCheckoutActivity(mActivity, fillDataToCheckout(), mInteractor.getSelectedDeliveryMethod().getMethod().firstKey(), STRING_EMPTY, payer);
    }

    /**
     * Fill all necesary data to perform checkout request in next activity
     */
    private ArrayList<KeyValueData> fillDataToCheckout() {
        ArrayList<KeyValueData> data = FormUtils.normalizeDataToFillInCheckout(mValuesByStep);

        data.add(new KeyValueData(BENEFICIARY_ID, mInteractor.getBeneficiaryId()));
        data.add(new KeyValueData(BENEFICIARY_TYPE, beneficiaryType != null ? beneficiaryType : ""));
        data.add(new KeyValueData(CURRENCY_TYPE, CalculatorInteractorImpl.getInstance().getCurrencyType()));
        data.add(new KeyValueData(CURRENCY, CalculatorInteractorImpl.getInstance().getPayoutCurrency()));
        data.add(new KeyValueData(AMOUNT, CalculatorInteractorImpl.getInstance().getCurrencyType().equals(Constants.CALCULATOR.CURRENCY_TYPE_PAYOUT_PRINCIPAL) ? AmountFormatter.normalizeDoubleString(mInteractor.getCurrentPayoutAmount()) : AmountFormatter.normalizeAmountToSend(mInteractor.getCurrentYouPayAmount())));
        data.add(new KeyValueData(CURRENCY_ORIGIN, mInteractor.getCurrencyOrigin()));
        data.add(new KeyValueData(OPERATION, DEFAULT_OPERATION));
        data.add(new KeyValueData(COUNTRY, CalculatorInteractorImpl.getInstance().getPayoutCountryKey()));

        return data;
    }

    public EcommerceCheckoutInfo getEventInfo() {
        Promotion promotionSelected = PromotionsRepository.getInstance().getPromotionSelected();
        CalculatorPromotion calculatorPromotion = PromotionsRepository.getInstance().getmCalculatorPromotion();
        String promotionName = promotionSelected != null
                ? promotionSelected.getPromotionName() : calculatorPromotion != null
                ? calculatorPromotion.getPromotionName() : "";
        return new EcommerceCheckoutInfo(
                mInteractor.getUser().getCountry().firstKey() != null ? mInteractor.getUser().getCountry().firstKey() : "",
                CalculatorInteractorImpl.getInstance().getPayoutCountryKey() != null ? CalculatorInteractorImpl.getInstance().getPayoutCountryKey() : "",
                mInteractor.getSelectedDeliveryMethod().getMethod().firstKey() != null ? mInteractor.getSelectedDeliveryMethod().getMethod().firstKey() : "",
                mInteractor.getCurrentYouPayAmount() != null ? mInteractor.getCurrentYouPayAmount() : "",
                mInteractor.getCurrencyOrigin() != null ? mInteractor.getCurrencyOrigin() : "",
                CalculatorInteractorImpl.getInstance().getPayoutCurrency() != null ? CalculatorInteractorImpl.getInstance().getPayoutCurrency() : "",
                promotionName != null ? promotionName : ""
        );
    }

    @Override
    public void onRequestingStepContentError(Step step, String error) {
        if (step != null) {
            step.setStatus(StepStatus.ERROR);

            mView.restoreStepIndicator(step);
            mView.openStep(step);
            mView.drawStepErrorView(step);
            mView.hideGeneralLoadingView();
            mView.onStepError(step, error);
        }
    }

    @Override
    public void onValidatingStepErrors(Step step, String error) {
        if (step != null) {
            step.setStatus(StepStatus.ERROR);

            mView.restoreStepIndicator(step);
            mView.openStep(step);
            mView.drawStepErrorView(step);
            mView.notifyGlobalChanges(step);
            mView.hideGeneralLoadingView();
            mView.onStepError(step, error);
        }
    }

    @Override
    public void onMoreFieldsReceived(Step step, MoreField moreFields, int position) {

        // Build map with current data
        HashMap<String, Field> currentStepFormData = new HashMap<>();
        for (Field field : step.getFormData().getFields()) {
            if (!TextUtils.isEmpty(field.getName())) {
                currentStepFormData.put(field.getName(), field);
            }
        }

        if (moreFields != null && moreFields.getFormData() != null && moreFields.getFormData().getFields() != null) {
            ArrayList<Field> listFields = moreFields.getFormData().getFields();
            if (listFields != null && listFields.size() > 0) {

                ArrayList<Pair<Integer, Field>> deleteAddList = new ArrayList<>();
                ArrayList<Field> addList = new ArrayList<>();

                for (Field fieldReceived : listFields) {
                    // Check if should add or remove
                    if (!TextUtils.isEmpty(fieldReceived.getType()) && fieldReceived.getType().equalsIgnoreCase(Constants.FIELD_TYPE.DELETE)) {
                        int positionStepForm = 0;
                        for (Field fieldStep : step.getFormData().getFields()) {
                            if (!TextUtils.isEmpty(fieldStep.getName()) && !TextUtils.isEmpty(fieldReceived.getName()) &&
                                    fieldStep.getName().equalsIgnoreCase(fieldReceived.getName())) {
                                deleteAddList.add(new Pair<>(positionStepForm, fieldStep));
                            }
                            positionStepForm++;
                        }
                    } else if (!TextUtils.isEmpty(fieldReceived.getName()) && !currentStepFormData.containsKey(fieldReceived.getName())) {
                        addList.add(fieldReceived);
                    } else if (!TextUtils.isEmpty(fieldReceived.getName()) && currentStepFormData.containsKey(fieldReceived.getName())) {
                        int positionStepForm = 0;
                        for (Field fieldStep : step.getFormData().getFields()) {
                            if (!TextUtils.isEmpty(fieldStep.getName()) && !TextUtils.isEmpty(fieldReceived.getName()) &&
                                    fieldStep.getName().equalsIgnoreCase(fieldReceived.getName())) {
                                deleteAddList.add(new Pair<>(positionStepForm, fieldStep));
                            }
                            positionStepForm++;
                        }
                        addList.add(fieldReceived);
                    }
                }

                if (deleteAddList.size() > 0) {
                    mView.enableDisableEditTextListeners(step, false);
                    for (Pair<Integer, Field> pairDelete : deleteAddList) {
                        step.getFormData().getFields().remove(pairDelete.second);
                        mView.notifyAddedRemoveFields(step, pairDelete.first, 1, false);
                    }
                    mView.enableDisableEditTextListeners(step, true);
                    deleteAddList.clear();
                }

                if (addList.size() > 0) {
                    step.getFormData().getFields().addAll(position + 1, addList);
                    mView.enableDisableEditTextListeners(step, false);
                    mView.notifyAddedRemoveFields(step, position + 1, addList.size(), true);
                    mView.enableDisableEditTextListeners(step, true);
                    addList.clear();
                }
                mView.showProgressDialog(false);
            }
        }
    }

    @Override
    public void onMoreFieldsError() {
        mView.showProgressDialog(false);
    }

    @Override
    public void onQuickReminderInfoReceived(QuickReminderResponse quickReminderResponse) {
        if (quickReminderResponse != null && quickReminderResponse.getMessages() != null && quickReminderResponse.getMessages().size() > 0) {
            mView.showQuickReminderPopup(quickReminderResponse.getTitle(), quickReminderResponse.getMessages());
        }
    }

    //----------------------------------------
    //-------- CALCULATOR CALLBACKS ----------
    //----------------------------------------

    @Override
    public void onBeneficiaryInfoAvailable() {
        Log.d(TAG, "Currency Beneficiary: " + CalculatorInteractorImpl.getInstance().getPayoutCurrency());
        mView.showYouPayCalculated(CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount(),
                CalculatorInteractorImpl.getInstance().getSendingCurrency());
    }


    @Override
    public void totalSaleCalculated(double totalSale, RateValues currentRate, String operationType,
                                    androidx.core.util.Pair<String, String> sendingCurrency,
                                    Promotion promotion, CalculatorPromotion mandatoryPromotion) {
        configureStaticCalculator();
        mView.showCalculatorLoadingView(false);

    }

    @Override
    public void onRetrievingBeneficiaryMethodsError() {
        // Not applicate
    }

    @Override
    public void onRetrievingRateError() {
        mView.showCalculatorLoadingView(false);
        // Check number of currencies to show diferents error texts
        ArrayList<TreeMap<String, String>> currencies = CalculatorInteractorImpl.getInstance().getCurrentDeliveryMethod().getCurrencies();
        String errorText = currencies != null && currencies.size() > 1 ?
                SmallWorldApplication.getStr(R.string.you_pay_calculator_text_error) :
                SmallWorldApplication.getStr(R.string.calculator_error_change_delivery_method_text);
        Log.e(TAG, "onRetrievingRateError---------------------------------------------------");
        mView.showHideCalculatorErroView(true, errorText);
    }


    /**
     * Cash Pick Up Select Location: navigate to selector
     */
    public void onActionChooseLocationForPickUp(Boolean isAnyWherePickup, CashPickupResultModel cashPickupResultModel) {
        mView.onChoosePickUpLocation(cashPickupResultModel);
        String amount = CalculatorInteractorImpl.getInstance().getCurrencyType().equals(Constants.CALCULATOR.CURRENCY_TYPE_PAYOUT_PRINCIPAL) ? AmountFormatter.normalizeAmountToSend(CalculatorInteractorImpl.getInstance().getCurrentBeneficiaryAmount()) : AmountFormatter.normalizeAmountToSend(CalculatorInteractorImpl.getInstance().getCurrentBeneficiaryAmount());
        String currencyType = CalculatorInteractorImpl.getInstance().getCurrencyType();
        String sendingCurrency = CalculatorInteractorImpl.getInstance().getSendingCurrency();
        String beneficiaryId = mBeneficiary != null ? mBeneficiary.getId() : STRING_EMPTY;
        TransactionalNavigator.navigateToChooseLocationForPickUp(mActivity, amount, currencyType, sendingCurrency, beneficiaryId, isAnyWherePickup);
    }


    /**
     * Cash Pick Up Select Location: result obtained
     */
    public void onPickUpLocationSelected(CashPickupResultModel locationSelected) {
        for (Step step : mStructureSteps) {
            if (step.getStepType().equals(Constants.STEP_TYPE.LOCATION_LIST)) {
                mView.drawContentStep(step, locationSelected);
            }
        }
    }

    public User getUser() {
        return mInteractor.getUser();
    }
}
