package com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.RESULT_CODES.BENEFICIARY_CREATED;
import static com.smallworldfs.moneytransferapp.utils.Constants.RESULT_CODES.BENEFICIARY_UPDATED;
import static com.smallworldfs.moneytransferapp.utils.Constants.STEP_TYPE.BENEFICIARY_FORM;
import static com.smallworldfs.moneytransferapp.utils.Constants.STEP_TYPE.DELIVERY_METHOD;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.interactors.implementation.EditBeneficiaryInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Beneficiary;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.validation.BeneficiaryValidationResponse;
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter.EditBeneficiaryPresenter;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.StepStatus;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation.TransactionalPresenterImpl;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.FormUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import rx.Scheduler;

public class EditBeneficiaryPresenterImpl extends TransactionalPresenterImpl implements EditBeneficiaryPresenter, EditBeneficiaryInteractorImpl.EditBeneficiaryCallback {

    private static final String DELIVERY_METHOD_KEY = "deliveryMethod";
    private final EditBeneficiaryPresenter.View mView;
    private final EditBeneficiaryInteractorImpl mInteractor;
    private String mDeliveryMethod = "";
    private ArrayList<Method> mDeliveryMethods;
    private boolean mStructureCreated;
    private final Beneficiary mBeneficiary;
    private boolean mInitialDeliveryMethodSelection;
    private boolean mIsEditingBeneficiary;
    private Method mDeliveryMethodToChange;

    public EditBeneficiaryPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, GenericActivity activity, EditBeneficiaryPresenter.View view, Beneficiary beneficiary, String beneficiaryType) {
        super(observeOn, susbscribeOn, context, view, activity, null, null, beneficiaryType);
        this.mContext = context;
        this.mView = view;

        mCountryOrigin = new Pair<>(Objects.requireNonNull(beneficiary.getPayoutCountry().firstEntry()).getKey(), Objects.requireNonNull(beneficiary.getPayoutCountry().firstEntry()).getValue());

        this.mBeneficiary = beneficiary;
        this.mInteractor = new EditBeneficiaryInteractorImpl(observeOn, susbscribeOn, this, mValuesByStep);
        this.mStructureCreated = false;
        this.mInitialDeliveryMethodSelection = true;
        this.mIsEditingBeneficiary = true;
        this.mActivity = activity;
    }

    @Override
    public void create() {
        super.create();
        addCallback();
        mView.configureView();
        mView.configureCountryView(mCountryOrigin);
    }

    @Override
    public void resume() {
        super.resume();
        addCallback();

        if (!this.mStructureCreated) {
            createStructure();
            requestDeliveryMethods();
            this.mStructureCreated = true;
        }
    }

    @Override
    public void pause() {
        super.pause();
        removeCallbacks();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void destroy() {
        removeCallbacks();
        super.destroy();
    }

    private void addCallback() {
        mInteractor.addCallback(this);
    }

    private void removeCallbacks() {
        mInteractor.removeCallbacks();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    /**
     * Request structure data to server
     */
    protected void requestTransactionalStructure(int positionStepQueue) {
        // Empty method to avoid request structure
    }


    private void createStructure() {
        if (mStructureSteps == null) {

            mStructureSteps = new ArrayList<>();

            Step deliveryMethodStep = new Step(SmallWorldApplication.getStr(R.string.new_beneficiary_delivery_method_step_title), Constants.STEP_TAGS.STEP_1, DELIVERY_METHOD);
            deliveryMethodStep.setIsFinal(0);
            mStructureSteps.add(deliveryMethodStep);

            Step beneficiaryFormStep = new Step(SmallWorldApplication.getStr(R.string.new_beneficiary_form_step_title), Constants.STEP_TAGS.STEP_2, BENEFICIARY_FORM);
            beneficiaryFormStep.setIsFinal(1);
            mStructureSteps.add(beneficiaryFormStep);

            drawStructureForm();
        }
    }

    @Override
    public void paintContentSteps() {
        for (Step step : mStructureSteps) {
            if (step.getStepType().equals(Constants.STEP_TYPE.DELIVERY_METHOD)) {
                if (mDeliveryMethods != null && mDeliveryMethods.size() > 0) {
                    mView.drawContentStep(step, mDeliveryMethods, CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount());
                }
            }
        }
    }

    private void requestDeliveryMethods() {
        Step deliveryMethodStep = mStructureSteps.get(0);
        mView.showValidatingLoadingStepView(deliveryMethodStep);

        mInteractor.requestDeliveryMethods(mCountryOrigin, beneficiaryType);
    }

    private void getBeneficiaryForm() {
        mInteractor.getBeneficiaryForm(mDeliveryMethod, mCountryOrigin, beneficiaryType);
    }

    private void getEditBeneficiaryForm() {
        mInteractor.getEditBeneficiaryForm(mBeneficiary.getId(), beneficiaryType);
    }

    @Override
    public void onSelectDeliveryMethodClick(Step step, Method method) {
        onSelectDeliveryMethodClick(step, method, true);
    }

    private void onSelectDeliveryMethodClick(Step step, Method method, boolean shouldAskUser) {

        if (shouldAskUser && mIsEditingBeneficiary) {
            mDeliveryMethodToChange = method;
            mView.showDeliveryMethodChangeWarning();
            return;
        }

        if (mInitialDeliveryMethodSelection) {
            mInitialDeliveryMethodSelection = false;
        }


        // Check if can retain data
        Step nextStep = mStructureSteps.get(1);
        if (nextStep != null) {
            mInteractor.retainFormData(nextStep);
        }

        mView.collapseAllSteps(step);
        mView.resetStepViewStatus();

        for (Step stepAux : mStructureSteps) {
            if (!stepAux.getStepType().equals(Constants.STEP_TYPE.DELIVERY_METHOD)) {
                mView.resetContentStep(stepAux);
                mView.disableStep(stepAux);
                mView.restoreStepIndicator(stepAux);
            }
        }

        mView.showValidatingLoadingStepView(step);
        mView.hideStepErrorView(step);

        mDeliveryMethod = method.getMethod().firstKey();

        // Mark value in step for show selected in form
        step.setStepSelectedItem(mInteractor.getTranslatedDeliveryMethod(Objects.requireNonNull(method.getMethod().firstEntry()).getKey()));

        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>(DELIVERY_METHOD_KEY, Objects.requireNonNull(method.getMethod().firstEntry()).getKey()));
        fillDataForm(step, list);

        markCompletedStep(step);

        if (mIsEditingBeneficiary) {
            getEditBeneficiaryForm();
        } else {
            getBeneficiaryForm();
        }
    }

    @Override
    public void requestDeliveryMethodsReceived(ArrayList<Method> deliveryMethods) {
        mDeliveryMethods = deliveryMethods;
        Step step = mStructureSteps.get(0);
        mView.restoreStepIndicator(step);
        drawStructureForm();

        if (mDeliveryMethods != null) {
            for (Method method : deliveryMethods) {
                if ((method.getMethod().firstEntry() != null) && (Objects.requireNonNull(method.getMethod().firstEntry()).getKey() != null)) {
                    if (Objects.requireNonNull(method.getMethod().firstEntry()).getKey().equalsIgnoreCase(mBeneficiary.getDeliveryMethod().firstKey())) {
                        mDeliveryMethod = method.getMethod().firstKey();
                        mView.setSelectedDeliveryMethod(method);
                        onSelectDeliveryMethodClick(step, method, false);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void requestDeliveryMethodsError() {

        mView.hideGeneralLoadingView();
        Step step = mStructureSteps.get(0);
        if (step != null) {
            step.setStatus(StepStatus.ERROR);

            mView.restoreStepIndicator(step);
            mView.openStep(step);
            mView.drawStepErrorView(step);
        }
    }

    public void userChoisedCreateNewBeneficiary() {
        Step step = mStructureSteps.get(0);
        mIsEditingBeneficiary = false;
        onSelectDeliveryMethodClick(step, mDeliveryMethodToChange, false);
    }

    public void userChoisedNotToCreateNewBeneficiary() {
        for (Method method : mDeliveryMethods) {
            if (Objects.requireNonNull(method.getMethod().firstEntry()).getKey().equalsIgnoreCase(mDeliveryMethod)) {
                mView.setSelectedDeliveryMethod(method);
                break;
            }
        }
    }

    @Override
    public void getBeneficiaryFormReceived(FormData formData) {
        Step firstStep = mStructureSteps.get(0);
        Step nextStep = mStructureSteps.get(1);

        mView.hideValidatingLoadingStepView(firstStep);
        nextStep.setFormData(formData);

        enableStep(nextStep);

        mView.markCurrentStepEditing(nextStep);

        // Check field content
        if (nextStep.getFormData() != null && nextStep.getFormData().getFields() != null && nextStep.getFormData().getFields().size() != 0) {
            mView.drawContentStep(nextStep, nextStep.getFormData().getFields(), CalculatorInteractorImpl.getInstance().getSendingCurrency());
        }
        openStep(nextStep);
        mView.showSaveButton();
    }

    @Override
    public void getBeneficiaryFormError() {

        mView.hideGeneralLoadingView();
        Step step = mStructureSteps.get(1);
        if (step != null) {
            step.setStatus(StepStatus.ERROR);

            mView.restoreStepIndicator(step);
            mView.openStep(step);
            mView.drawStepErrorView(step);
        }
    }

    @Override
    public void getEditBeneficiaryFormReceived(FormData formData) {
        Step firstStep = mStructureSteps.get(0);
        Step nextStep = mStructureSteps.get(1);

        mView.hideValidatingLoadingStepView(firstStep);
        nextStep.setFormData(formData);

        enableStep(nextStep);

        mView.markCurrentStepEditing(nextStep);

        // Check field content
        if (nextStep.getFormData() != null && nextStep.getFormData().getFields() != null && nextStep.getFormData().getFields().size() != 0) {
            mView.drawContentStep(nextStep, nextStep.getFormData().getFields(), CalculatorInteractorImpl.getInstance().getSendingCurrency());
        }
        openStep(nextStep);
        mView.showSaveButton();
    }

    @Override
    public void getEditBeneficiaryFormError() {
        mView.hideGeneralLoadingView();
        Step step = mStructureSteps.get(1);
        if (step != null) {
            step.setStatus(StepStatus.ERROR);

            mView.restoreStepIndicator(step);
            mView.openStep(step);
            mView.drawStepErrorView(step);
        }
    }

    public void onSubmitFormClick(Step step) {

        mView.showValidatingLoadingStepView(step);
        mView.hideStepErrorView(step);

        HashMap<String, String> properties = new HashMap<>();

        String beneficiaryName = step.getFormData().getFields().get(1).getValue() + " " + step.getFormData().getFields().get(2).getValue();

        properties.put(BrazeEventProperty.DESTINATION_COUNTRY.getValue(), mCountryOrigin.first);
        properties.put(BrazeEventProperty.BENEFICIARY_FULL_NAME.getValue(), beneficiaryName);

        mView.registerBrazeEvent(BrazeEventName.BENEFICIARY_CREATED.getValue(), properties);


        ArrayList<Pair<String, String>> list = FormUtils.fillDataFormToPerformRequest(step.getFormData().getFields());
        for (Field field : step.getFormData().getFields()) {
            // Update value selected in step title
            if (!TextUtils.isEmpty(field.getName()) && field.getName().equalsIgnoreCase(Constants.FIELD_CONSTANS_KEYS.NAME)) {
                step.setStepSelectedItem(TextUtils.isEmpty(field.getValue()) ? "" : field.getValue());
                break;
            }
        }

        fillDataForm(step, list);
        mView.showGeneralLoadingView();
        if (mIsEditingBeneficiary)
            requestUpdateBeneficiary(step);
        else
            requestCreateNewBeneficiary(step);
        //mView.notifyGlobalChanges(formStep);
    }

    private void requestCreateNewBeneficiary(Step step) {
        mInteractor.requestCreateNewBeneficiary(step, mDeliveryMethod, mCountryOrigin, beneficiaryType);
    }

    private void requestUpdateBeneficiary(Step step) {
        mInteractor.requestUpdateBeneficiary(step, mDeliveryMethod, mCountryOrigin, mBeneficiary.getId(), beneficiaryType);
    }

    @Override
    public void onValidateStepOk(Step validatedStep, BeneficiaryValidationResponse nextStepData) {
        Beneficiary beneficiary = null;
        if (nextStepData != null) {
            beneficiary = nextStepData.getBeneficiary();
        }
        backToPreviousScreenWithResultOK(beneficiary);
    }

    @Override
    public void onValidatingStepErrors(Step step) {
        mView.hideGeneralLoadingView();
        if (step != null) {
            step.setStatus(StepStatus.ERROR);
            mView.restoreStepIndicator(step);
            mView.openStep(step);
            mView.drawStepErrorView(step);
            mView.notifyGlobalChanges(step);
        }
    }

    @Override
    public void onRequestingStepContentError(Step step) {
        mView.hideGeneralLoadingView();
        if (step != null) {
            step.setStatus(StepStatus.ERROR);
            mView.restoreStepIndicator(step);
            mView.openStep(step);
            mView.drawStepErrorView(step);
        }
    }

    private void backToPreviousScreenWithResultOK(Beneficiary beneficiary) {
        if (mIsEditingBeneficiary) {
            mView.backToPreviousScreenWithResultOK(beneficiary, BENEFICIARY_UPDATED);
        } else {
            mView.backToPreviousScreenWithResultOK(beneficiary, BENEFICIARY_CREATED);
        }
        removeCallbacks();
    }

    public void onComboApiDataSelected(Field field, int position, Step step) {
        if (field != null) {
            FormUtils.processApiFieldType(mContext, mActivity, field, position, field.getType(), step.getFormData().getFields(), step.getStepId(), "");
        }
    }
}
