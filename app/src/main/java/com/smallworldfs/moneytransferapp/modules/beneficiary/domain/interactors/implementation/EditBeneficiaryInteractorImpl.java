package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.interactors.implementation;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.interactors.EditBeneficiaryInteractor;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.CreateBeneficiaryRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.EditBeneficiaryFormRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.NewBeneficiaryFormRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.UpdateBeneficiaryRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.structure.EditBeneficiaryFormResponse;
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
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.GenericKeyValueDropContent;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationStepResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationsMessages;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.repository.GenericDropContentRepository;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.FormUtils;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

@Singleton
public class EditBeneficiaryInteractorImpl extends AbstractInteractor implements EditBeneficiaryInteractor {

    private CompositeSubscription mCompositeSubscription;
    private Handler mHandler;
    private EditBeneficiaryCallback mCallback;
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
    public EditBeneficiaryInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, EditBeneficiaryCallback callback, HashMap<Step, HashMap<String, String>> valuesSteps) {
        super(observeOn, subscribeOn);
        this.mCallback = callback;
        this.mCompositeSubscription = new CompositeSubscription();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mValuesByStep = valuesSteps;

        EditBeneficiaryInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        EditBeneficiaryInteractorImpl.DaggerHiltEntryPoint.class);
        loginRepository = hiltEntryPoint.provideLoginRepository();
    }

    @Override
    public void run() {
    }

    @Override
    public void removeCallbacks() {
        mCallback = null;
    }

    public void addCallback(EditBeneficiaryCallback callback) {
        mCallback = callback;
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
    public void requestDeliveryMethods(final Pair<String, String> country, String beneficiaryType) {

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

                        public void onNext(Response<CurrenciesResponse> response) {
                            if (response != null
                                    && response.body() != null
                                    && response.body().getMethods() != null) {
                                postDeliveryMethodsReceived(response.body().getMethods());
                            } else {
                                postDeliveryMethodsError();
                            }
                        }
                    }));
        }
    }

    /**
     * Get edit form init structure
     */
    public void getEditBeneficiaryForm(final String beneficiaryId, String beneficiaryType) {

        if (mCompositeSubscription != null &&
                mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }

        String userToken = loginRepository.getUserToken();
        String userId = loginRepository.getUserId();

        final EditBeneficiaryFormRequest request =
                new EditBeneficiaryFormRequest(beneficiaryId, userToken, userId, beneficiaryType);

        mCompositeSubscription.add(BeneficiaryRepository.getInstance()
                .requestEditBeneficiaryForm(request)
                .subscribeOn(this.mSubscriberOn)
                .observeOn(this.mObserveOn)
                .subscribe(new Subscriber<Response<EditBeneficiaryFormResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        postStructureError();
                    }

                    public void onNext(Response<EditBeneficiaryFormResponse> response) {
                        if (response != null && response.body() != null && response.body().getForm() != null) {
                            postEditBeneficiaryFormReceived(response.body().getForm());
                        } else {
                            postEditBeneficiaryFormError();
                        }
                    }
                }));
    }


    /**
     * Get form init structure
     */
    public void getBeneficiaryForm(final String deliveryMethod, final Pair<String, String> country, String beneficiaryType) {

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

        final NewBeneficiaryFormRequest request =
                new NewBeneficiaryFormRequest(deliveryMethod, userToken, userId, selectedCountry, beneficiaryType);

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
                            postBeneficiaryFormReceived(response.body().getForm());
                        } else {
                            postBeneficiaryFormError();
                        }
                    }
                }));
    }

    public void requestCreateNewBeneficiary(final Step step, final String deliveryMethod, final Pair<String, String> country, String beneficiaryType) {

        // Fill fixed values
        String userToken = loginRepository.getUserToken();
        String userId = loginRepository.getUserId();
        String selectedCountry = "";
        if (country != null) {
            selectedCountry = country.first;
        }

        CreateBeneficiaryRequest request = new CreateBeneficiaryRequest(deliveryMethod, userToken, selectedCountry, userId, beneficiaryType);

        for (Map.Entry<Step, HashMap<String, String>> entry : mValuesByStep.entrySet()) {

            HashMap<String, String> values = entry.getValue();

            // Fill validate step request
            for (Map.Entry<String, String> value : values.entrySet()) {
                request.put(value.getKey(), value.getValue() != null ? value.getValue() : "");
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
                        postValidatingError(step);
                    }

                    public void onNext(Response<BeneficiaryValidationResponse> response) {
                        NewGenericError error = NewGenericError.processError(response);
                        if (error == null) {
                            postValidateSusccessfully(step, response.body());
                        } else if (error.errorType == NewGenericError.ErrorType.VALIDATION_ERROR) {
                            processAndPostValidationErrors(step, response);
                        } else {
                            postValidatingError(step);
                        }
                    }
                }));
    }

    public void requestUpdateBeneficiary(final Step step, final String deliveryMethod,
                                         final Pair<String, String> country, String beneficiaryId,
                                         String beneficiaryType) {

        // Fill fixed values
        String userToken = loginRepository.getUserToken();
        String userId = loginRepository.getUserId();
        String selectedCountry = "";
        if (country != null) {
            selectedCountry = country.first;
        }

        UpdateBeneficiaryRequest request = new UpdateBeneficiaryRequest(deliveryMethod, userToken,
                selectedCountry, userId, beneficiaryId, beneficiaryType);

        for (Map.Entry<Step, HashMap<String, String>> entry : mValuesByStep.entrySet()) {

            HashMap<String, String> values = entry.getValue();

            // Fill validate step request
            for (Map.Entry<String, String> value : values.entrySet()) {
                request.put(value.getKey(), value.getValue() != null ? value.getValue() : "");
            }

            if (entry.getKey().getFormData() != null && entry.getKey().getFormData().getFields() != null) {
                FormUtils.setClearErrors(null, entry.getKey().getFormData().getFields());
            }
        }

        // Append user token, operation, payout countries and currency
        mCompositeSubscription.add(BeneficiaryRepository.getInstance()
                .requestUpdateBeneficiary(request)
                .subscribeOn(this.mSubscriberOn)
                .observeOn(this.mObserveOn)
                .subscribe(new Subscriber<Response<BeneficiaryValidationResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        postValidatingError(step);
                    }

                    public void onNext(Response<BeneficiaryValidationResponse> response) {
                        NewGenericError error = NewGenericError.processError(response);
                        if (error == null) {
                            postValidateSusccessfully(step, response.body());
                        } else if (error.errorType == NewGenericError.ErrorType.VALIDATION_ERROR) {
                            processAndPostValidationErrors(step, response);
                        } else {
                            postValidatingError(step);
                        }
                    }
                }));
    }


    public String getTranslatedDeliveryMethod(String selectedDeliveryMethod) {
        return CalculatorRepositoryLegacy.getInstance().getTranslatedDeliveryMethod(selectedDeliveryMethod);
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

    private void postStructureError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onStructureError();
                }
            });
        }
    }


    private void postValidatingError(final Step step) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onRequestingStepContentError(step);
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


    private void postBeneficiaryFormReceived(final FormData formData) {
        fillRetainedData(formData);
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.getBeneficiaryFormReceived(formData);
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

    private void postEditBeneficiaryFormReceived(final FormData formData) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> formalizeAutoReceivedData(formData));
        }
    }

    private void postEditBeneficiaryFormError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.getEditBeneficiaryFormError();
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

    private void formalizeAutoReceivedData(final FormData formData) {
        if (formData != null) {
            for (Field receivedField : formData.getFields()) {
                if (receivedField.getType().equalsIgnoreCase(Constants.FIELD_TYPE.COMBO)) {
                    if (receivedField.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.COMBO_OWN) || receivedField.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.COMBO_REQUEST))
                        normalizeFieldCombo(receivedField);
                    else if (receivedField.getSubtype().equalsIgnoreCase(Constants.FIELD_SUBTYPES.COMBO_API))
                        normalizeFieldApi(receivedField, formData);
                }
            }
            if (tasks.size() > 0) {
                Observable.merge(tasks).subscribe(response -> {
                    if (mCallback != null) {
                        mCallback.getEditBeneficiaryFormReceived(formData);
                    }
                });
            } else if (mCallback != null) {
                mCallback.getEditBeneficiaryFormReceived(formData);
            }
        }
    }


    private void normalizeFieldCombo(Field field) {
        String key = field.getValue();
        if (!TextUtils.isEmpty(key) && field.getData() != null) {
            for (TreeMap<String, String> map : field.getData()) {
                if (map.containsKey(key)) {
                    field.setValue(Objects.requireNonNull(map.firstEntry()).getValue());
                    field.setKeyValue(key);
                    break;
                }
            }
        }
    }

    private void normalizeFieldApi(Field field, FormData formData) {
        if (field.getData() == null) {
            addTask(requestData(field, formData));
        } else {
            normalizeFieldCombo(field);
        }
    }

    private final ArrayList<Observable<Response<GenericKeyValueDropContent>>> tasks = new ArrayList<>();

    private void addTask(Observable<Response<GenericKeyValueDropContent>> task) {
        tasks.add(task);
    }

    private Observable<Response<GenericKeyValueDropContent>> requestData(final Field field, final FormData formData) {
        String urlApi = formatUrl(field, formData);
        return GenericDropContentRepository.getInstance()
                .getDropContent(urlApi)
                .subscribeOn(this.mSubscriberOn)
                .observeOn(this.mObserveOn)
                .doOnNext(response -> processData(field, response));
    }

    private String formatUrl(Field field, FormData formData) {
        String urlFormatted = "";
        if (TextUtils.isEmpty(field.getTriggers())) {
            urlFormatted = Utils.formatUrlWithFields(field.getRefApi().getUrl(), field.getRefApi().getParams(), null);
        } else {
            for (Field fieldAux : formData.getFields()) {
                if (!TextUtils.isEmpty(fieldAux.getName()) && fieldAux.getName().equalsIgnoreCase(field.getTriggers())) {
                    urlFormatted = Utils.formatUrlWithFields(field.getRefApi().getUrl(), field.getRefApi().getParams(), fieldAux.getKeyValue());
                    break;
                }
            }
        }
        return urlFormatted;
    }

    private void processData(final Field field, Response<GenericKeyValueDropContent> response) {
        NewGenericError error = NewGenericError.processError(response);
        if (error == null && response.body() != null && response.body().getData() != null && response.body().getData().size() > 0) {
            field.setData(response.body().getData());
            normalizeFieldCombo(field);
        } else {
            if (error != null)
                Log.e("DATA", "processData:e:--------------------------:" + error.toString());
            else
                Log.e("DATA", "processData:e:--------------------------:" + "Null data");
        }
    }
}
