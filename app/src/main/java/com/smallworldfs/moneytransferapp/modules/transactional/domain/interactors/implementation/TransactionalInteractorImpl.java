package com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.PROMOTION_CODE;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CurrenciesResponse;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCurrencieRequest;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.repository.CalculatorRepositoryLegacy;
import com.smallworldfs.moneytransferapp.modules.common.domain.data.DataContainer;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.repository.PromotionsRepository;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors.TransactionalInteractor;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.ClearTransactionRequest;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.GenericKeyValueDropContent;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.QuickReminderRequest;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.ServerTransactionalRequest;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.ValidateTransactionRequest;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.TransactionalStepResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.MoreField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationStepResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationsMessages;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.repository.GenericDropContentRepository;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.repository.TransactionalRepository;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.ConstantsKt;
import com.smallworldfs.moneytransferapp.utils.FormUtils;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

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
public class TransactionalInteractorImpl extends AbstractInteractor implements TransactionalInteractor {

    private final CompositeSubscription mCompositeSubscription;
    private Handler mHandler;
    private TransactionalInteractor.Callback mCallback;
    private final HashMap<Step, HashMap<String, String>> mValuesByStep;
    private String mBeneficiaryId;
    private final ArrayList<Field> mRetainedData;
    private ArrayList<Method> mDeliveryMethods;
    private final User mUser;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }

    public UserDataRepository userDataRepository;

    public TransactionalInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, TransactionalInteractor.Callback callback, HashMap<Step, HashMap<String, String>> valuesSteps, String beneficiaryId) {
        super(observeOn, subscribeOn);
        this.mCallback = callback;

        this.mCompositeSubscription = new CompositeSubscription();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mValuesByStep = valuesSteps;
        this.mBeneficiaryId = beneficiaryId;
        this.mRetainedData = new ArrayList<>();

        DaggerHiltEntryPoint hiltEntryPoint = EntryPointAccessors.fromApplication(SmallWorldApplication.getApp(), DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
        this.mUser = userDataRepository.retrieveUser();

    }

    @Override
    public void run() {

    }

    @Override
    public void removeCallbacks() {

    }

    @Override
    public void destroy() {

        clearDataCollector();
        clearPayoutLocations();

        if (mHandler != null) {
            mHandler = null;
        }
        if (mCallback != null) {
            mCallback = null;
        }
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    /**
     * Get Delivery Methods to be showed in Status 1
     */
    public ArrayList<Method> getDeliveryMethods() {
        return mDeliveryMethods;
    }

    public void requestDeliveryMethods(String requestPayoutCountry, String beneficiaryType) {
        User user = userDataRepository.retrieveUser();

        if (user != null) {
            String requestOriginCountry = user.getCountry().firstKey();

            ServerCurrencieRequest request = new ServerCurrencieRequest(requestOriginCountry, requestPayoutCountry, beneficiaryType, true);
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
                            postStructureError();
                        }

                        public void onNext(retrofit2.Response<CurrenciesResponse> response) {
                            NewGenericError error = NewGenericError.processError(response);
                            if (error == null) {
                                if (response != null && response.body() != null && response.body().getMethods() != null) {
                                    if (mCallback != null) {
                                        mDeliveryMethods = response.body().getMethods();
                                        mCallback.onDeliveryMethodsReceived();
                                    } else {
                                        postStructureError();
                                    }
                                } else {
                                    postStructureError();
                                }
                            } else {
                                postStructureError();
                            }
                        }
                    }));
        } else {
            if (mCallback != null) {
                mCallback.onStructureError();
            }
        }
    }

    /**
     * Get current delivery method selected
     *
     * @return method
     */
    public Method getSelectedDeliveryMethod() {
        return CalculatorInteractorImpl.getInstance().getCurrentDeliveryMethod();
    }

    /**
     * Get current payout amount
     */
    public String getCurrentPayoutAmount() {
        return CalculatorInteractorImpl.getInstance().getCurrentBeneficiaryAmount();
    }

    /**
     * Get current totalsale amount
     */
    public String getCurrentYouPayAmount() {
        return CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount();
    }

    /**
     * Get sending currency
     */
    public String getCurrencyOrigin() {
        return CalculatorInteractorImpl.getInstance().getSendingCurrency();
    }


    /**
     * Get form init structure
     */
    public void getFormStructures(final int positionStepQueue, String operation, String beneficiaryType) {

        if ((CalculatorInteractorImpl.getInstance() != null) &&
                (CalculatorInteractorImpl.getInstance().getCurrentDeliveryMethod() != null) &&
                (CalculatorInteractorImpl.getInstance().getCurrentDeliveryMethod().getMethod() != null)) {

            final ServerTransactionalRequest request =
                    new ServerTransactionalRequest(CalculatorInteractorImpl.getInstance().getCurrentDeliveryMethod().getMethod().firstKey(),
                            operation, CalculatorInteractorImpl.getInstance().getPayoutCountryKey(), beneficiaryType);

            mCompositeSubscription.add(TransactionalRepository.getInstance()
                    .requestTransactionalStructureSteps(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<TransactionalStepResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            postStructureError();
                        }

                        public void onNext(Response<TransactionalStepResponse> transactionalSteps) {
                            if (transactionalSteps != null && transactionalSteps.body() != null &&
                                    transactionalSteps.body().getStructure() != null && transactionalSteps.body().getStructure().size() > 0) {
                                postStructureSteps(transactionalSteps.body().getStructure());
                                if (positionStepQueue != -1) {
                                    postNeedToRequestDataStep(positionStepQueue);
                                }
                            } else {
                                postStructureError();
                            }
                        }
                    }));
        }

    }

    public void getQuickReminderInfo(String country) {
        User user = userDataRepository.retrieveUser();
        if (user != null) {
            QuickReminderRequest request = new QuickReminderRequest(user.getId(), user.getUserToken(), country);

            mCompositeSubscription.add(TransactionalRepository.getInstance()
                    .requestQuickReminderInfo(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<QuickReminderResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        public void onNext(Response<QuickReminderResponse> quickReminderResponse) {
                            NewGenericError error = NewGenericError.processError(quickReminderResponse);
                            if (error == null) {
                                postQuickReminderInfoReceived(quickReminderResponse.body());
                            }
                        }
                    }));
        }
    }


    public void validateStepAndRequestNextData(final Step step, String operation, Step nextStep,
                                               String beneficiaryType) {

        // Fill fixed values
        User user = userDataRepository.retrieveUser();
        if (user != null) {
            String userToken = user.getUserToken();
            String payoutCountry = CalculatorInteractorImpl.getInstance().getPayoutCountryKey();
            String payoutCurrency = CalculatorInteractorImpl.getInstance().getPayoutCurrency();
            String nextStepId = nextStep == null ?
                    Constants.FIELD_CONSTANS_KEYS.FINAL_STEP : nextStep.getStepId();
            String userId = user.getId();
            String currentStepId = step.getStepId();
            String amount = CalculatorInteractorImpl.getInstance().getCurrencyType().
                    equals(Constants.CALCULATOR.CURRENCY_TYPE_PAYOUT_PRINCIPAL) ?
                    AmountFormatter.normalizeAmountToSend(getCurrentPayoutAmount()) :
                    AmountFormatter.normalizeAmountToSend(getCurrentYouPayAmount());
            String currencyType = CalculatorInteractorImpl.getInstance().getCurrencyType();
            String currencyOrigin = CalculatorInteractorImpl.getInstance().getSendingCurrency();

            ValidateTransactionRequest tempRequest = new ValidateTransactionRequest(userToken,
                    payoutCurrency, operation, payoutCountry, nextStepId, userId, currentStepId,
                    amount, currencyType, currencyOrigin, mBeneficiaryId, beneficiaryType);

            for (Map.Entry<Step, HashMap<String, String>> entry : mValuesByStep.entrySet()) {

                HashMap<String, String> values = entry.getValue();

                // Fill validate step request
                for (Map.Entry<String, String> value : values.entrySet()) {
                    tempRequest.put(value.getKey(), value.getValue() != null ? value.getValue() : "");
                }

                if (entry.getKey().getFormData() != null && entry.getKey().getFormData().getFields() != null) {
                    FormUtils.setClearErrors(null, entry.getKey().getFormData().getFields());
                }
            }

            // Check promotions
            checkAndFillSelectedPromotion(tempRequest);

            // Avoid null key or value in map request
            ValidateTransactionRequest filteredRequest = new ValidateTransactionRequest();
            for (HashMap.Entry<String, Object> entry : tempRequest.entrySet()) {
                if (!TextUtils.isEmpty(entry.getKey()) && entry.getValue() != null) {
                    filteredRequest.put(entry.getKey(), entry.getValue());
                }
            }


            // Append user token, operation, payout countries and currency
            mCompositeSubscription.add(TransactionalRepository.getInstance()
                    .validateStep(filteredRequest)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<ValidationStepResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            postValidatingError(step, e.getMessage());
                        }

                        public void onNext(Response<ValidationStepResponse> response) {
                            NewGenericError error = NewGenericError.processError(response);
                            if (error == null) {
                                processAndPostValidatedStep(step, response.body());
                            } else if (error.errorType == NewGenericError.ErrorType.VALIDATION_ERROR) {
                                processAndPostValidationErrors(step, response);
                            } else {
                                postValidatingError(step, error.msg);
                            }
                        }
                    }));
        }
    }

    private void checkAndFillSelectedPromotion(ValidateTransactionRequest request) {
        // Check promotions
        String promotionCode = PromotionsRepository.getInstance().getPromotionCodePresent();
        if (!TextUtils.isEmpty(promotionCode)) {
            request.put(PROMOTION_CODE, promotionCode);
        }
    }

    public void getMoreFields(final Step step, String url, final int position) {
        if (!TextUtils.isEmpty(url)) {
            // Append user token, operation, payout countries and currency
            mCompositeSubscription.add(GenericDropContentRepository.getInstance()
                    .getMoreFieldsBasedOnRequestData(url)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<MoreField>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        public void onNext(Response<MoreField> response) {
                            NewGenericError error = NewGenericError.processError(response);
                            if (error == null) {
                                postMoreFieldsReceived(step, response.body(), position);
                            } else {
                                postMoreFieldsError();
                            }
                        }
                    }));
        }
    }


    public void clearCurrentTransaction() {
        User user = userDataRepository.retrieveUser();
        if (user != null) {
            ClearTransactionRequest request = new ClearTransactionRequest(user.getId(), user.getUserToken());

            TransactionalRepository.getInstance()
                    .clearTransaction(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<Void>>() {
                        public void onCompleted() {
                        }

                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        public void onNext(Response<Void> response) {
                        }
                    });
        }
    }


    private void processAndPostValidationErrors(Step step, Response<ValidationStepResponse> response) {

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
                String error = ConstantsKt.STRING_EMPTY;
                if (data.getValidationErrors() != null && data.getValidationErrors().getValidationStepErrros() != null
                    && data.getValidationErrors().getValidationStepErrros().entrySet().iterator().next() != null) {
                    Map.Entry<String, java.util.ArrayList<java.lang.String>> entry = data.getValidationErrors().getValidationStepErrros().entrySet().iterator().next();
                    ArrayList<String> list = entry.getValue() != null ? entry.getValue() : new ArrayList<>();
                    error = !list.get(0).isEmpty() ? list.get(0) : ConstantsKt.STRING_EMPTY;
                }
                postNotValidatingStep(step, error);
            }
        } catch (IOException ex) {
            Log.e("STACK", "----------------------", ex);
            postNotValidatingStep(step, ex.getMessage());
        }
    }

    // Get recent beneficiaryId created or existing id
    public String getBeneficiaryId() {
        return this.mBeneficiaryId;
    }

    public void clearBeneficiaryId() {
        this.mBeneficiaryId = "";
    }

    private void clearDataCollector() {
        DataContainer.getInstance().clearDataByKey(Constants.DATA_CONTAINER_KEYS.LOCATIONS_CONTAINER_KEY);
    }

    public void retainFormData(Step step) {
        if (step.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.BENEFICIARY_FORM) ||
                step.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.FORM)) {
            if (step.getFormData() != null && step.getFormData().getFields() != null) {
                mRetainedData.addAll(step.getFormData().getFields());
            }
        }
    }

    public void persistPayoutLocations(ArrayList<Field> payoutsLocations) {
        TransactionalRepository.getInstance().persistPayoutLocations(payoutsLocations);
    }

    private void clearPayoutLocations() {
        TransactionalRepository.getInstance().clearPayoutLocations();
    }

    private void fillRetainedData(ValidationStepResponse validationData) {
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
                                        case Constants.FIELD_TYPE.COMBO:
                                            receivedField.setValue(fieldRetained.getValue());
                                            receivedField.setKeyValue(fieldRetained.getKeyValue());
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                } else if (receivedField.getName() == null && fieldRetained.getName() == null && receivedField.getType().equalsIgnoreCase(Constants.FIELD_TYPE.GROUP) && fieldRetained.getType().equalsIgnoreCase(Constants.FIELD_TYPE.GROUP)) {
                                    // Without id
                                    if (receivedField.getChilds().get(0).getName().equalsIgnoreCase(fieldRetained.getChilds().get(0).getName())) {
                                        // Same group
                                        for (Field childRetainedField : fieldRetained.getChilds()) {
                                            for (Field childReceivedField : receivedField.getChilds()) {
                                                if (!TextUtils.isEmpty(childReceivedField.getName()) && !TextUtils.isEmpty(childRetainedField.getName()) && childReceivedField.getName().equalsIgnoreCase(childRetainedField.getName())) {
                                                    childReceivedField.setValue(childRetainedField.getValue());
                                                    childReceivedField.setKeyValue(childRetainedField.getKeyValue());
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.mRetainedData != null) {
            this.mRetainedData.clear();
        }
    }

    public String getTranslatedDeliveryMethod(String selectedDeliveryMethod) {
        return CalculatorRepositoryLegacy.getInstance().getTranslatedDeliveryMethod(selectedDeliveryMethod);
    }


    private void postNotValidatingStep(final Step step, final String error) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onValidatingStepErrors(step, error);
                }
            });
        }
    }


    private void postStructureSteps(final ArrayList<Step> steps) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onStructureReceived(steps);
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


    private void postValidatingError(final Step step, final String error) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onRequestingStepContentError(step, error);
                }
            });
        }
    }

    private void processAndPostValidatedStep(final Step validatedStep, final ValidationStepResponse validationData) {
        // Check validated step and retain beneficiaryId
        if (validatedStep.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.BENEFICIARY_FORM)) {
            if (validationData.getTransaction() != null && !TextUtils.isEmpty(validationData.getTransaction().getBeneficiaryId()) && TextUtils.isEmpty(mBeneficiaryId)) {
                mBeneficiaryId = validationData.getTransaction().getBeneficiaryId();
            }
        }

        // Formalize autoasign data
        formalizeAutoReceivedData(validatedStep, validationData);

        // Check retained data
        fillRetainedData(validationData);

        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onValidateStepOk(validatedStep, validationData, mBeneficiaryId);
                }
            });
        }
    }

    private void postQuickReminderInfoReceived(final QuickReminderResponse quickReminderResponse) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onQuickReminderInfoReceived(quickReminderResponse);
                }
            });
        }
    }


    private void formalizeAutoReceivedData(final Step validatedStep, final ValidationStepResponse validationData) {
        if (validationData != null
                && validationData.getNextStep() != null
                && !TextUtils.isEmpty(validationData.getNextStep().getStepType())
                && validationData.getNextStep().getStepType().equalsIgnoreCase(Constants.STEP_TYPE.BENEFICIARY_FORM)
                && !TextUtils.isEmpty(mBeneficiaryId)
                && validationData.getNextStep().getFormData() != null && validationData.getNextStep().getFormData().getFields() != null) {
            formalizeAutoReceivedDataHelper(validationData.getNextStep().getFormData(), validatedStep, validationData);
        }
    }


    private void formalizeAutoReceivedDataHelper(final FormData formData, final Step validatedStep, final ValidationStepResponse validationData) {
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
                        mCallback.onValidateStepOk(validatedStep, validationData, mBeneficiaryId);
                    }
                });
            } else {
                mCallback.onValidateStepOk(validatedStep, validationData, mBeneficiaryId);
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
                Log.e("DATA", "processData:e:--------------------------:", new Exception(error.toString()));
            else
                Log.e("DATA", "processData:e:--------------------------:", new Exception("Null data"));
        }
    }


    private void postNeedToRequestDataStep(final int positionStepQueue) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onNeedToRequestAgainDataStep(positionStepQueue);
                }
            });
        }
    }

    private void postMoreFieldsReceived(final Step step, final MoreField moreFields, final int position) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onMoreFieldsReceived(step, moreFields, position);
                }
            });
        }
    }

    private void postMoreFieldsError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onMoreFieldsError();
                }
            });
        }
    }

    public User getUser() {
        return userDataRepository.retrieveUser();
    }

}
