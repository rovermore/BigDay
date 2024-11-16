package com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.STEP_TYPE.BENEFICIARY_FORM;
import static com.smallworldfs.moneytransferapp.utils.Constants.STEP_TYPE.DELIVERY_METHOD;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.interactors.implementation.NewBeneficiaryInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.validation.BeneficiaryValidationResponse;
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter.NewBeneficiaryPresenter;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.StepStatus;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation.TransactionalPresenterImpl;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.FormUtils;

import java.util.ArrayList;
import java.util.Objects;

import rx.Scheduler;

public class NewBeneficiaryPresenterImpl extends TransactionalPresenterImpl implements NewBeneficiaryPresenter, NewBeneficiaryInteractorImpl.NewBeneficiaryCallback {

    public static final String DELIVERY_METHOD_KEY = "deliveryMethod";
    private final NewBeneficiaryPresenter.View mView;
    private final NewBeneficiaryInteractorImpl mInteractor;
    private String mDeliveryMethod = "";
    private ArrayList<Method> mDeliveryMethods;
    private boolean mStructureCreated;

    public NewBeneficiaryPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, GenericActivity activity, NewBeneficiaryPresenter.View view, Pair<String, String> countryOrigin, String beneficiaryType) {
        super(observeOn, susbscribeOn, context, view, activity, null, null, beneficiaryType);
        this.mContext = context;
        this.mView = view;
        this.mCountryOrigin = countryOrigin;
        this.mInteractor = new NewBeneficiaryInteractorImpl(observeOn, susbscribeOn, this, mValuesByStep);
        this.mStructureCreated = false;
    }

    @Override
    public void create() {
        super.create();
        addCallbacks();
        mView.configureView();
        mView.configureCountryView(mCountryOrigin);
    }

    @Override
    public void resume() {
        super.resume();
        if (!this.mStructureCreated) {
            createStructure();
            requestDeliveryMethods();
            this.mStructureCreated = true;
        }

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
        mInteractor.destroy();
        super.destroy();
    }

    private void addCallbacks() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    @Override
    protected void requestTransactionalStructure(int positionStepQueue) {
        // not aplicate
    }

    public void createStructure() {
        if (mStructureSteps == null) {

            mStructureSteps = new ArrayList<>();

            Step deliveryMethodStep = new Step(SmallWorldApplication.getStr(R.string.new_beneficiary_delivery_method_step_title), Constants.STEP_TAGS.STEP_1, DELIVERY_METHOD);
            deliveryMethodStep.setIsFinal(0);
            mStructureSteps.add(deliveryMethodStep);

            Step beneficiaryFormStep = new Step(SmallWorldApplication.getStr(R.string.new_beneficiary_form_step_title), Constants.STEP_TAGS.STEP_2, BENEFICIARY_FORM);
            beneficiaryFormStep.setIsFinal(1);
            mStructureSteps.add(beneficiaryFormStep);
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

    public void requestDeliveryMethods() {
        Step deliveryMethodStep = mStructureSteps.get(0);
        mView.showValidatingLoadingStepView(deliveryMethodStep);

        mInteractor.requestDeliveryMethods(mCountryOrigin, beneficiaryType);
    }

    private void getBeneficiaryForm() {
        mInteractor.getBeneficiaryForm(mDeliveryMethod, mCountryOrigin, beneficiaryType);
    }

    @Override
    public void onSelectDeliveryMethodClick(Step step, Method method) {

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
        step.setStepSelectedItem(Objects.requireNonNull(method.getMethod().firstEntry()).getValue());

        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>(DELIVERY_METHOD_KEY, Objects.requireNonNull(method.getMethod().firstEntry()).getKey()));
        fillDataForm(step, list);

        markCompletedStep(step);

        getBeneficiaryForm();

    }


    @Override
    public void requestDeliveryMethodsReceived(ArrayList<Method> deliveryMethods) {
        mDeliveryMethods = deliveryMethods;
        Step step = mStructureSteps.get(0);
        mView.restoreStepIndicator(step);
        drawStructureForm();
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

    @Override
    public void getBeneficiaryFormReceived(@NonNull final FormData formData,
                                           @NonNull final Pair<String, String> selectedCountry) {
        Step firstStep = mStructureSteps.get(0);
        Step nextStep = mStructureSteps.get(1);

        mView.hideValidatingLoadingStepView(firstStep);
        nextStep.setFormData(formData);

        enableStep(nextStep);

        mView.markCurrentStepEditing(nextStep);

        // Check field content
        if (nextStep.getFormData() != null && nextStep.getFormData().getFields() != null && nextStep.getFormData().getFields().size() != 0) {
            final ArrayList<Field> fields = nextStep.getFormData().getFields();
            final Field countriesField = getTelephoneCountriesField(fields);
            if (countriesField != null && countriesField.getValue() == null) {
                countriesField.setValue(selectedCountry.first);
            }
            mView.drawContentStep(nextStep, fields, CalculatorInteractorImpl.getInstance().getSendingCurrency());
        }
        openStep(nextStep);
    }

    @Nullable
    private Field getTelephoneCountriesField(@NonNull final ArrayList<Field> fields) {
        for (Field field : fields) {
            final String subtype = field.getSubtype();
            if (subtype != null && subtype.equals(Constants.FIELD_SUBTYPES.PHONE)) {
                final ArrayList<Field> children = field.getChilds();
                if (children != null && !children.isEmpty()) {
                    return children.get(0);
                }
            }
        }
        return null;
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

    public void onSubmitFormClick(Step step) {

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
        mView.showGeneralLoadingView();
        requestCreateNewBeneficiary(step);
    }

    private void requestCreateNewBeneficiary(Step step) {
        mInteractor.requestCreateNewBeneficiary(step, mDeliveryMethod, mCountryOrigin, beneficiaryType);
    }

    @Override
    public void onValidateStepOk(Step validatedStep, BeneficiaryValidationResponse nextStepData) {
        mView.finishWithSuccess(validatedStep.getFormData().getFields().get(1).getValue() + " " + validatedStep.getFormData().getFields().get(2).getValue());
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
            mView.trackBrazeError(NewGenericError.ErrorType.SERVER_ERROR, step.getFormData().getFields().get(1).getValue() + " " + step.getFormData().getFields().get(2).getValue());
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
            mView.trackBrazeError(NewGenericError.ErrorType.VALIDATION_ERROR, step.getFormData().getFields().get(1).getValue() + " " + step.getFormData().getFields().get(2).getValue());
        }
    }
}
