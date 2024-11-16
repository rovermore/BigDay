package com.smallworldfs.moneytransferapp.modules.checkout.domain.interactors.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.AMOUNT;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.BENEFICIARY_ID;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.CLIENT_RELATION;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.COUNTRY_ORIGIN;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.CURRENCY_ORIGIN;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.CURRENCY_TYPE;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.LOCATION_CODE;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.MTN_PURPOSE;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.ONE_SWIPE_SEND;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.PROMOTION_CODE;
import static com.smallworldfs.moneytransferapp.utils.Constants.FIELD_CONSTANS_KEYS.REPRESENTATIVE_CODE;
import static com.smallworldfs.moneytransferapp.utils.Constants.USER_PARAMS.KOUNT_SESS_ID;
import static com.smallworldfs.moneytransferapp.utils.Constants.USER_PARAMS.USER_ID;
import static com.smallworldfs.moneytransferapp.utils.Constants.USER_PARAMS.USER_TOKEN;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_EMPTY;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.repository.CalculatorRepositoryLegacy;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.interactors.CheckoutInteractor;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.server.CheckoutRequest;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.repository.CheckoutRepository;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.repository.PromotionsRepository;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ContactSupportInfo;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ContactSupportResponse;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ContactSupportRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.SendEmailRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.repository.ContactSupportRepository;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.MoreField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationStepResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationsMessages;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.repository.GenericDropContentRepository;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.FormUtils;
import com.smallworldfs.moneytransferapp.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Scheduler;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

public class CheckoutInteractorImpl extends AbstractInteractor implements CheckoutInteractor {

    private CompositeSubscription mCompositeSubscription;
    private Handler mHandler;
    private CheckoutInteractor.Callback mCallback;

    // Form data
    private ArrayList<Field> mFormData;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();

    }

    public UserDataRepository userDataRepository;

    public CheckoutInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, CheckoutInteractor.Callback callback) {
        super(observeOn, subscribeOn);
        this.mCompositeSubscription = new CompositeSubscription();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mCallback = callback;

        DaggerHiltEntryPoint hiltEntryPoint = EntryPointAccessors.fromApplication(SmallWorldApplication.getApp(), DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();

    }

    @Override
    public void run() {

    }

    @Override
    public void removeCallbacks() {

    }

    @Override
    public void destroy() {
        mCallback = null;
        if (mHandler != null) {
            mHandler = null;
        }
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
            mCompositeSubscription = null;
        }
    }

    public void syncronizeFormData(ArrayList<Field> formData) {
        this.mFormData = formData;
    }

    public void requestCheckoutInfo(ArrayList<KeyValueData> transactionalData) {

        // Fill data to request checkout summary
        CheckoutRequest request = new CheckoutRequest();
        User user = userDataRepository.retrieveUser();
        if (user != null) {
            request.put(USER_TOKEN, user.getUserToken());
            request.put(USER_ID, user.getId());
            request.put(COUNTRY_ORIGIN, user.getCountry().firstEntry().getKey());
            for (KeyValueData data : transactionalData) {
                request.put(data.getKey(), data.getValue());
            }

            checkAndFillSelectedPromotion(request);

        }

        mCompositeSubscription.add(CheckoutRepository.getInstance()
                .getCheckoutInformation(request)
                .subscribeOn(this.mSubscriberOn)
                .observeOn(this.mObserveOn)
                .subscribe(new Subscriber<Response<Checkout>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        onNext(null);
                    }

                    public void onNext(Response<Checkout> response) {
                        if (mCompositeSubscription != null) {
                            mCompositeSubscription.clear();
                        }
                        NewGenericError error = NewGenericError.processError(response);
                        if (error == null) {
                            postCheckoutInfoReceived(response.body());
                        } else {
                            postCheckoutError(response);
                        }
                    }
                }));
    }

    private void checkAndFillSelectedPromotion(CheckoutRequest request) {
        // Check promotions
        String promotionNumber = "";
        if (PromotionsRepository.getInstance().getCalculatorPromotion() != null) {
            promotionNumber = PromotionsRepository.getInstance().getCalculatorPromotion().getPromotionNumber();
        } else if (PromotionsRepository.getInstance().getPromotionSelected() != null) {
            promotionNumber = PromotionsRepository.getInstance().getPromotionSelected().getPromotionCode();
        }

        if (!TextUtils.isEmpty(promotionNumber)) {
            request.put(PROMOTION_CODE, promotionNumber);
        }
    }

    public void getMoreFields(String url, final int position) {
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
                                postMoreFieldsReceived(response.body(), position);
                            } else {
                                postMoreFieldsError();
                            }
                        }
                    }));
        } else postMoreFieldsError();
    }


    public void createTransaction(ArrayList<Pair<String, String>> listData, ArrayList<KeyValueData> transactionalData, Checkout checkoutData) {
        try {
            // Fill data to request checkout summary
            if (mCompositeSubscription != null) {
                mCompositeSubscription.clear();
            }

            CheckoutRequest request = new CheckoutRequest();
            User user = userDataRepository.retrieveUser();

            if (user != null) {
                request.put(USER_TOKEN, user.getUserToken());
                request.put(USER_ID, user.getId());
                request.put(COUNTRY_ORIGIN, user.getCountry().firstEntry().getKey());
                request.put(KOUNT_SESS_ID, user.getKountsessid());
                //request.put(OCUPATION, user.getOcupation());
            }

            if (checkoutData != null && checkoutData.getTransaction() != null) {
                if (checkoutData.getTransactionSummary() != null) {
                    request.put(AMOUNT, AmountFormatter.normalizeAmountToSend(AmountFormatter.formatDoubleAmountNumber(checkoutData.getTransactionSummary().getTotalPayout())));
                    request.put(CURRENCY_TYPE, !TextUtils.isEmpty(checkoutData.getTransactionSummary().getCurrencyType()) ? checkoutData.getTransactionSummary().getCurrencyType() : "");
                }

                if (checkoutData.getTransactionDetails() != null) {
                    request.put(CURRENCY_ORIGIN, !TextUtils.isEmpty(checkoutData.getTransactionDetails().getCurrencyOrigin()) ? checkoutData.getTransactionDetails().getCurrencyOrigin() : "");
                }

                request.put(BENEFICIARY_ID, !TextUtils.isEmpty(checkoutData.getTransaction().getBeneficiaryId()) ? checkoutData.getTransaction().getBeneficiaryId() : "");

                if (checkoutData.getTransaction().getTransactionInfo() != null) {
                    request.put(REPRESENTATIVE_CODE, !TextUtils.isEmpty(checkoutData.getTransaction().getTransactionInfo().getLocationInfo().getRepresentativeCode()) ? checkoutData.getTransaction().getTransactionInfo().getLocationInfo().getRepresentativeCode() : "");
                    request.put(LOCATION_CODE, !TextUtils.isEmpty(checkoutData.getTransaction().getTransactionInfo().getLocationInfo().getLocationCode()) ? checkoutData.getTransaction().getTransactionInfo().getLocationInfo().getLocationCode() : "");
                }

                if (checkoutData.getTransaction().getTransactionInfo().getPurposeInfo() != null) {
                    request.put(MTN_PURPOSE, !TextUtils.isEmpty(checkoutData.getTransaction().getTransactionInfo().getPurposeInfo().getMtnPurpose()) ? checkoutData.getTransaction().getTransactionInfo().getPurposeInfo().getMtnPurpose() : "");
                    request.put(CLIENT_RELATION, !TextUtils.isEmpty(checkoutData.getTransaction().getTransactionInfo().getPurposeInfo().getClientRelation()) ? checkoutData.getTransaction().getTransactionInfo().getPurposeInfo().getClientRelation() : "");
                }
            }

            // Set one swipe email disabled
            request.put(ONE_SWIPE_SEND, "0");

            if (listData != null) {
                for (Pair<String, String> data : listData) {
                    request.put(data.first, data.second);
                }
            }

            if (transactionalData != null) {
                for (KeyValueData data : transactionalData) {
                    if (!TextUtils.isEmpty(data.getKey()) && !TextUtils.isEmpty(data.getValue())) {
                        request.put(data.getKey(), data.getValue());
                    }
                }
                if (request.containsKey(AMOUNT)) {
                    String currecyType = CalculatorInteractorImpl.getInstance().getCurrencyType();
                    request.put(AMOUNT, AmountFormatter.normalizeAmountToSend(CalculatorInteractorImpl.getInstance().getAmount(currecyType)));
                }
            }

            checkAndFillSelectedPromotion(request);

            // Avoid null key or value in map request
            CheckoutRequest filteredRequest = new CheckoutRequest();
            for (HashMap.Entry<String, Object> entry : request.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    filteredRequest.put(entry.getKey(), entry.getValue());
                }
            }


            mCompositeSubscription.add(CheckoutRepository.getInstance()
                    .createTransaction(filteredRequest)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<CreateTransactionResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        public void onNext(Response<CreateTransactionResponse> response) {
                            clearComposite();
                            NewGenericError error = NewGenericError.processError(response);
                            if (error != null) {
                                postCreatingTransactionError(error, response);
                            } else {
                                CalculatorInteractorImpl.getInstance().setCurrencyType(Constants.CALCULATOR.TOTALSALE); //reset currency type
                                PromotionsRepository.getInstance().setPromotionSelectedByUser(null);
                                postCreatingTransactionSuccess(response.body());
                            }
                        }
                    }));
        } catch (Exception e) {
            Log.e("STACK", "----------------------", e);
            postCreatingTransactionError(null);
        }
    }

    private void postCreatingTransactionError(NewGenericError error, Response<CreateTransactionResponse> response) {
        if (error.errorType == NewGenericError.ErrorType.TAX_CODE_ERROR) {
            if (mHandler != null && mCallback != null) {
                mHandler.post(() -> {
                    if (mCallback != null) {
                        mCallback.onCreateCheckoutTaxCodeError(error.title, STRING_EMPTY);
                    }
                });
            }
        } else {
            postCreatingTransactionError(response);
        }
    }

    public void sendEmail(final String subject, final String message, ContactSupportInfo info) {
        if (info == null) {
            User user = userDataRepository.retrieveUser();
            if (user != null) {

                final ContactSupportRequest request = new ContactSupportRequest(user.getCountry().firstKey());

                mCompositeSubscription.add(ContactSupportRepository.getInstance()
                        .getContactSupportInfo(request)
                        .subscribeOn(this.mSubscriberOn)
                        .observeOn(this.mObserveOn)
                        .subscribe(new Subscriber<Response<ContactSupportResponse>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                onNext(null);
                            }

                            public void onNext(Response<ContactSupportResponse> response) {
                                clearComposite();
                                NewGenericError error = NewGenericError.processError(response);
                                if (error != null) {
                                    postSendingEmailError();
                                } else {
                                    sendEmail(subject, message, response.body().getContactSupportInfo());
                                    ContactSupportRepository.getInstance().storeContactSupportResponse(response.body());
                                }
                            }
                        }));
            }
        } else {
            User user = userDataRepository.retrieveUser();
            if (user != null) {
                SendEmailRequest request = new SendEmailRequest(subject, message, info.getEmail(), user.getUserToken(), user.getId());
                mCompositeSubscription.add(ContactSupportRepository.getInstance()
                        .sendEmail(request)
                        .subscribeOn(this.mSubscriberOn)
                        .observeOn(this.mObserveOn)
                        .subscribe(new Subscriber<Response<Void>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                onNext(null);
                            }

                            public void onNext(Response<Void> response) {
                                clearComposite();
                                NewGenericError error = NewGenericError.processError(response);
                                if (error != null) {
                                    postSendingEmailError();
                                } else {
                                    postSendEmailSusccessfull();
                                }
                            }
                        }));
            }
        }
    }

    public String getTranslatedDeliveryMethod(String deliveryMethod) {
        return CalculatorRepositoryLegacy.getInstance().getTranslatedDeliveryMethod(deliveryMethod);
    }

    public User getUser() {
        return userDataRepository.retrieveUser();
    }

    public void clearUsedPromotions() {
        PromotionsRepository.getInstance().clearCalculatorPromotion();
        PromotionsRepository.getInstance().setPromotionSelectedByUser(null);
        PromotionsRepository.getInstance().clearListPromotionsAvailables();
    }

    private void postCreatingTransactionSuccess(final CreateTransactionResponse transactionResponse) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onCreateCheckoutSuscessfull(transactionResponse);
                }
            });
        }
    }

    private void clearComposite() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    private void postCreatingTransactionError(final Response<CreateTransactionResponse> response) {

        try {
            if (response != null && response.errorBody() != null) {
                ResponseBody body = response.errorBody();
                JsonParser parser = new JsonParser();
                JsonElement mJson;

                mJson = parser.parse(body.string());
                Gson gson = new Gson();
                ValidationsMessages errorResponse = gson.fromJson(mJson, ValidationsMessages.class);
                if (errorResponse != null) {
                    ValidationStepResponse data = new ValidationStepResponse(errorResponse);
                    FormUtils.setClearErrors(data, this.mFormData);
                }
            }
        } catch (Exception ex) {
            Log.e("STACK", "----------------------", ex);
        }

        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    if (response != null) {
                        mCallback.onCreateCheckoutErrors();
                    } else {
                        mCallback.onCreateCheckoutServerErrors();
                    }
                }
            });
        }
    }


    private void postCheckoutInfoReceived(final Checkout checkoutInfo) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onCheckoutInformationReceived(checkoutInfo);
                }
            });
        }
    }

    private void postCheckoutError(final Response<Checkout> response) {
        StringBuilder error = new StringBuilder();
        try {
            if (response != null && response.errorBody() != null) {
                ResponseBody body = response.errorBody();
                JsonParser parser = new JsonParser();
                JsonElement mJson = parser.parse(body.string());
                Gson gson = new Gson();
                ValidationsMessages errorResponse = gson.fromJson(mJson, ValidationsMessages.class);
                if (errorResponse != null) {
                    HashMap<String, ArrayList<String>> msg = errorResponse.getValidationStepErrros();
                    for (ArrayList<String> errorMsg: msg.values()) {
                        error.append(errorMsg.get(0)).append("\n ");
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("STACK", "----------------------", ex);
        }

        if (mHandler != null && mCallback != null) {
            String finalError = error.toString();
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onCheckoutInformationError(finalError);
                }
            });
        }
    }

    private void postMoreFieldsReceived(final MoreField moreFields, final int position) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onMoreFieldsReceived(moreFields, position);
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


    private void postSendEmailSusccessfull() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onSendEmailSusccessfull();
                }
            });
        }
    }

    private void postSendingEmailError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                // not aplicate
            });
        }
    }
}
