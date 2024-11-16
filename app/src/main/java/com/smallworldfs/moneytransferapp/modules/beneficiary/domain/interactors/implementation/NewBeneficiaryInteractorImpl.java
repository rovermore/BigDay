package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.interactors.implementation;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.interactors.NewBeneficiaryInteractor;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.CreateBeneficiaryRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.NewBeneficiaryFormRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.structure.NewBeneficiaryFormResponse;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.validation.BeneficiaryValidationResponse;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.repository.BeneficiaryRepository;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CurrenciesResponse;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCurrencieRequest;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.repository.CalculatorRepositoryLegacy;
import com.smallworldfs.moneytransferapp.modules.common.domain.data.DataContainer;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationStepResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationsMessages;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.FormUtils;
import com.smallworldfs.moneytransferapp.utils.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Scheduler;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

@Singleton
public class NewBeneficiaryInteractorImpl extends AbstractInteractor implements NewBeneficiaryInteractor {

    private CompositeSubscription mCompositeSubscription;
    private Handler mHandler;
    private NewBeneficiaryInteractor.NewBeneficiaryCallback mCallback;
    private final HashMap<Step, HashMap<String, String>> mValuesByStep;


    // Retained data
    private ArrayList<Field> mRetainedData;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        LoginRepository provideLoginRepository();
    }

    public LoginRepository loginRepository;

    @Inject
    public NewBeneficiaryInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, NewBeneficiaryInteractor.NewBeneficiaryCallback callback, HashMap<Step, HashMap<String, String>> valuesSteps) {
        super(observeOn, subscribeOn);
        this.mCallback = callback;

        this.mCompositeSubscription = new CompositeSubscription();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mValuesByStep = valuesSteps;

        NewBeneficiaryInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        NewBeneficiaryInteractorImpl.DaggerHiltEntryPoint.class);
        loginRepository = hiltEntryPoint.provideLoginRepository();
    }

    @Override
    public void run() {

    }

    @Override
    public void removeCallbacks() {
        mCallback = null;
    }

    @Override
    public void destroy() {

        clearDataCollector();

        if (mHandler != null) {
            mHandler = null;
        }
        if (mCallback != null) {
            mCallback = null;
        }
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
            mCompositeSubscription = null;
        }
    }

    /**
     * Request Delivery Methods
     */
    public void requestDeliveryMethods(final Pair<String, String> country, final String beneficiaryType) {

        // Get currency
        User user = loginRepository.getUser();
        if (user != null) {
            String countryOrigin = user.getCountry().firstKey();
            ServerCurrencieRequest request = new ServerCurrencieRequest(countryOrigin, country.first, beneficiaryType, true);
            mCompositeSubscription.add(CalculatorRepositoryLegacy.getInstance()
                    .getCurrencies(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<CurrenciesResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        public void onNext(retrofit2.Response<CurrenciesResponse> response) {
                            if (response != null && response.body() != null && response.body().getMethods() != null
                                    && response.body().getMethods() != null) {

                                postDeliveryMethodsReceived(response.body().getMethods());
                            } else {
                                postDeliveryMethodsError();
                            }
                        }
                    }));
        } else {
            postDeliveryMethodsError();
        }
    }


    /**
     * Get form init structure
     */
    public void getBeneficiaryForm(final String deliveryMethod, final Pair<String, String> country, final String beneficiaryType) {

        if (mCompositeSubscription != null &&
                mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }

        // Fill fixed values
        String userToken = loginRepository.getUserToken();
        String userId = loginRepository.getUserId();
        String selectedCountry = "";
        if (country != null) {
            selectedCountry = country.first;
        }

        final NewBeneficiaryFormRequest request = new NewBeneficiaryFormRequest(deliveryMethod, userToken, userId, selectedCountry, beneficiaryType);

        mCompositeSubscription.add(BeneficiaryRepository.getInstance()
                .requestNewBeneficiaryForm(request)
                .subscribeOn(this.mSubscriberOn)
                .observeOn(this.mObserveOn)
                .subscribe(new Subscriber<Response<NewBeneficiaryFormResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        postStructureError();
                    }

                    public void onNext(Response<NewBeneficiaryFormResponse> response) {
                        if (response != null && response.body() != null && response.body().getForm() != null) {
                            postBeneficiaryFormReceived(response.body().getForm(), Objects.requireNonNull(country));
                        } else {
                            postBeneficiaryFormError();
                        }
                    }
                }));
    }

    public void requestCreateNewBeneficiary(final Step step, final String deliveryMethod,
                                            final Pair<String, String> country, String beneficiaryType) {

        // Fill fixed values
        String userToken = loginRepository.getUserToken();
        String userId = loginRepository.getUserId();
        String selectedCountry = "";
        if (country != null) {
            selectedCountry = country.first;
        }


        CreateBeneficiaryRequest request = new CreateBeneficiaryRequest(deliveryMethod, userToken,
                selectedCountry, userId, beneficiaryType);

        for (Map.Entry<Step, HashMap<String, String>> entry : mValuesByStep.entrySet()) {

            HashMap<String, String> values = entry.getValue();

            // Fill validate step request
            for (Map.Entry<String, String> value : values.entrySet()) {
                if ((value.getKey() != null) && (!value.getKey().isEmpty())) {
                    request.put(value.getKey(), value.getValue() != null ? value.getValue() : "");
                }
            }

            if (entry.getKey().getFormData() != null && entry.getKey().getFormData().getFields() != null) {
                FormUtils.setClearErrors(null, entry.getKey().getFormData().getFields());
            }
        }

        // Append user token, operation, payout countries and currency
        mCompositeSubscription.add(BeneficiaryRepository.getInstance()
                .requestCreateNewBeneficiary(request)
                .subscribeOn(this.mSubscriberOn)
                .observeOn(this.mObserveOn)
                .subscribe(new Subscriber<Response<BeneficiaryValidationResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        postDataValidationError(step);
                    }

                    public void onNext(Response<BeneficiaryValidationResponse> response) {
                        NewGenericError error = NewGenericError.processError(response);
                        if (error == null) {
                            postValidateSusccessfully(step, response.body());
                            if (response.body() != null) {
                                BeneficiaryRepository.getInstance().setLastCreatedBeneficiary(response.body().getBeneficiary());
                            }
                        } else if (error.errorType == NewGenericError.ErrorType.VALIDATION_ERROR) {
                            processAndPostValidationErrors(step, response);
                        } else {
                            postDataValidationError(step);
                        }
                    }
                }));
    }

    private void processAndPostValidationErrors(Step step, Response<BeneficiaryValidationResponse> response) {

        ResponseBody body = response.errorBody();
        JsonParser parser = new JsonParser();
        JsonElement mJson;

        try {
            mJson = parser.parse(Objects.requireNonNull(body).string());
            Gson gson = new Gson();
            ValidationsMessages errorResponse = gson.fromJson(mJson, ValidationsMessages.class);
            if (errorResponse != null) {
                ValidationStepResponse data = new ValidationStepResponse(errorResponse);
                FormUtils.setClearErrors(data, step.getFormData().getFields());
                postNotValidatingStep(step);
            }
        } catch (IOException ex) {
            Log.e("STACK", "----------------------", ex);
            postNotValidatingStep(step);
        }
    }


    private void clearDataCollector() {
        DataContainer.getInstance().clearDataByKey(Constants.DATA_CONTAINER_KEYS.LOCATIONS_CONTAINER_KEY);
    }

    public void retainFormData(Step step) {
        if (step.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.BENEFICIARY_FORM) ||
                step.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.FORM)) {
            if (step.getFormData() != null && step.getFormData().getFields() != null) {
                if (mRetainedData == null) {
                    mRetainedData = new ArrayList<>();
                }
                mRetainedData.addAll(step.getFormData().getFields());
            }
        }
    }

    private void fillRetainedData(FormData formData) {
        if (formData != null) {
            if (this.mRetainedData != null && this.mRetainedData.size() > 0) {
                for (Field fieldRetained : mRetainedData) {
                    for (Field receivedField : formData.getFields()) {
                        if (!TextUtils.isEmpty(fieldRetained.getName()) && !TextUtils.isEmpty(receivedField.getName()) && fieldRetained.getName().equalsIgnoreCase(receivedField.getName())) {
                            switch (fieldRetained.getType()) {
                                case Constants.FIELD_TYPE.TEXT:
                                case Constants.FIELD_TYPE.PASSWORD:
                                case Constants.FIELD_TYPE.FILE:
                                case Constants.FIELD_TYPE.EMAIL:
                                    receivedField.setValue(fieldRetained.getValue());
                                    receivedField.setKeyValue(fieldRetained.getKeyValue());
                                    break;
                                default:
                                    break;
                            }
                            break;
                        }
                    }
                }
            }
        }

        //this.mRetainedData.clear();
    }

    private void fillRetainedData(BeneficiaryValidationResponse validationData) {
        if (this.mRetainedData != null && this.mRetainedData.size() > 0) {
            if (validationData != null && validationData.getNextStep() != null) {
                if (validationData.getNextStep().getFormData() != null && validationData.getNextStep().getFormData().getFields() != null) {
                    if (validationData.getNextStep().getStepType().equalsIgnoreCase(Constants.STEP_TYPE.BENEFICIARY_FORM)) {
                        for (Field fieldRetained : mRetainedData) {
                            for (Field receivedField : validationData.getNextStep().getFormData().getFields()) {
                                if (!TextUtils.isEmpty(fieldRetained.getName()) && !TextUtils.isEmpty(receivedField.getName()) && fieldRetained.getName().equalsIgnoreCase(receivedField.getName())) {
                                    switch (fieldRetained.getType()) {
                                        case Constants.FIELD_TYPE.TEXT:
                                        case Constants.FIELD_TYPE.PASSWORD:
                                        case Constants.FIELD_TYPE.FILE:
                                        case Constants.FIELD_TYPE.EMAIL:
                                            receivedField.setValue(fieldRetained.getValue());
                                            receivedField.setKeyValue(fieldRetained.getKeyValue());
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private void postNotValidatingStep(final Step step) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onValidatingStepErrors(step);
                }
            });
        }
    }

    private void postDataValidationError(final Step step) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onRequestingStepContentError(step);
                }
            });
        }
    }

    private void postStructureError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onStructureError();
                }
            });
        }
    }

    private void postValidateSusccessfully(final Step step, final BeneficiaryValidationResponse validationData) {
        // Check retained data
        fillRetainedData(validationData);

        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onValidateStepOk(step, validationData);
                }
            });
        }
    }

    private void postBeneficiaryFormReceived(final @NonNull FormData formData, final @NonNull Pair<String, String> selectedCountry) {
        fillRetainedData(formData);
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.getBeneficiaryFormReceived(formData, selectedCountry);
                }
            });
        }
    }

    private void postBeneficiaryFormError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.getBeneficiaryFormError();
                }
            });
        }
    }

    private void postDeliveryMethodsReceived(final ArrayList<Method> deliveryMethods) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.requestDeliveryMethodsReceived(deliveryMethods);
                }
            });
        }
    }

    private void postDeliveryMethodsError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.requestDeliveryMethodsError();
                }
            });
        }
    }
}
