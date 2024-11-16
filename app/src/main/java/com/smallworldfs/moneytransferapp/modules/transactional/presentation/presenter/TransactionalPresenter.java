package com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter;

import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.GenericFormField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderMessage;
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickupResultModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by luismiguel on 10/7/17
 */
public interface TransactionalPresenter extends BasePresenter {
    interface View {
        // Configure init view
        void configureView();

        // Append step based on init structure
        void appendStep(Step step, int position, boolean isLastPosition);

        // Draw content inside step
        void drawContentStep(Step step, ArrayList<? extends GenericFormField> listDeliveryMethods, String currentYouPayAmount);

        void drawContentStep(Step step, CashPickupResultModel cashPickupResultModel);

        // Complete step
        void completeStep(Step step);

        // Show step in validaing/loading mode
        void showValidatingLoadingStepView(Step step);

        // Hide validating/loading step mode
        void hideValidatingLoadingStepView(Step step);

        // Mark current step
        void markCurrentStepEditing(Step step);

        void drawStepErrorView(Step step);

        void hideStepErrorView(Step step);

        void hideGeneralLoadingView();

        void onStructureError();

        // Status animations

        void togleStep(Step step);

        void collapseAllSteps(Step step);

        void openStep(Step step);

        void restoreStepIndicator(Step step);

        void enableStep(Step step);

        void deleteStep(Step step);

        void resetContentStep(Step step);

        void closeStep(Step step);

        void disableStep(Step step);

        void hideGeneralStructureErrorView();

        void showGeneralLoadingView();

        void updateNewLastStep(Step step);

        void updateComboGroupValueData(ArrayList<KeyValueData> keyValuesSelected, Step step, int field);

        void showDateRangeSelector(Field field, int position, String stepId, String type, String value);

        void resetStepViewStatus();

        void notifyGlobalChanges(Step step);

        void checkAuthenticatedUser(Step step, User user);

        void notifyAddedRemoveFields(Step step, int position, int count, boolean add);

        void checkAndRequestPermissions();

        void showStepEmptyView(Step nextStep, String deliveryMethod);

        void updateLocationPayoutContentStep(Field field, Step step, String currency);

        void enableDisableEditTextListeners(Step step, boolean enable);

        void showProgressDialog(boolean show);

        void showHideCalculatorErroView(boolean show, String errorText);

        void setDeliveryMethodAutoSelectedInAdapter(Step step, Method method);

        void showQuickReminderPopup(String title, ArrayList<QuickReminderMessage> messages);


        // ----------------------------
        // Calculator Callbacks
        // ----------------------------

        // Configure top static calculator
        void configureStaticCalculator(String payoutCountryKey, String beneficiaryReceiveAmount, String beneficiaryCurrency);

        // Show calculator depends on side touched
        void showCalculator(int side);

        // Close calculator
        void closeCalculator();

        // Show/Hide calculator loading view
        void showCalculatorLoadingView(boolean show);


        // Show you pay calculated with remote calculate in enter transactional request
        void showYouPayCalculated(String youPayAmount, String youPayCurrency);

        // Show you pay calculated with remote calculate in bottom calculator
        void showPayoutBottomCalculator(String payoutPrincipal, String payoutPrincipalCurrency, String  principal, String principalCurrency);


        void trackBeginCheckout();

        void onDeliveryMethodSelected(String deliveryMethod);

        void onFieldClicked(Field field);

        void onStepCompleted(Step validatedStep);

        void onStepError(Step step, String error);

        void onChoosePickUpLocation(CashPickupResultModel cashPickupResultModel);

        void onClickMore();

        void onChangeClicked();

        void registerAddToCartEvent();

        void registerBrazeEvent(String eventName, HashMap<String, String> eventProperties);
    }
}
