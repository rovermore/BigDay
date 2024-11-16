package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.navigator;

import static com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.activity.CheckoutActivity.DELIVERY_METHOD;
import static com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.activity.CheckoutActivity.PAYER;
import static com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.activity.CheckoutActivity.TRANSACTIONAL_DATA_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.CHECKOUT_DATA;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.SHOW_CHECKOUT_DIALOG_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.TRANSACTION_DATA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.LocationMapActivity.ACTIVITY_TITLE;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.LocationMapActivity.PAYOUT_LOCATIONS_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.PayoutLocationSelectorActivity.ITEM_SELECTED_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.PayoutLocationSelectorActivity.LOCATION_TEXT;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.PayoutLocationSelectorActivity.NAME_SELECTED_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.PayoutLocationSelectorActivity.PAYOUT_TYPE;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.PayoutLocationSelectorActivity.STEP_ID_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.PayoutLocationSelectorActivity.TAX_SELECTED_EXTRA;
import static com.smallworldfs.moneytransferapp.utils.Constants.REQUEST_CODES.CASH_PICK_UP;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.INPUT_STATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;

import com.smallworldfs.moneytransferapp.BuildConfig;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.activity.CheckoutActivity;
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Payout;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.LocationMapActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.PayoutLocationSelectorActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.TransactionalActivity;
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.CashPickUpMapActivity;
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.CashPickUpMapState;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.OwnFileProvider;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by luismiguel on 25/8/17.
 */

public class TransactionalNavigator {

    public static void navigateToSelectPayoutLocationActivity(@NonNull final Activity activity,
                                                              @NonNull final ArrayList<Field> payoutLocations,
                                                              @NonNull final String stepId,
                                                              @NonNull final String locationCode,
                                                              @NonNull final String title,
                                                              @NonNull final String locationTextFormatted,
                                                              @NonNull final String nameSelected,
                                                              @NonNull final String payoutType,
                                                              final double taxSelected) {

        final Intent intent = new Intent(activity, PayoutLocationSelectorActivity.class);
        intent.putExtra(STEP_ID_EXTRA, stepId);
        intent.putExtra(ITEM_SELECTED_EXTRA, locationCode);
        intent.putExtra(NAME_SELECTED_EXTRA, nameSelected);
        intent.putExtra(TAX_SELECTED_EXTRA, taxSelected);
        intent.putExtra(ACTIVITY_TITLE, title);
        intent.putExtra(LOCATION_TEXT, locationTextFormatted);
        intent.putExtra(PAYOUT_TYPE, payoutType);
        PayoutLocationSelectorActivity.mData = new ArrayList<>();
        PayoutLocationSelectorActivity.mData.addAll(payoutLocations);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
        activity.startActivityForResult(intent, Constants.REQUEST_CODES.SELECT_LOCATION_REQUEST_CODE, compat.toBundle());

    }

    public static void navigateToLocationMapActivity(Activity activity, Payout payout, String title) {
        if (activity != null) {
            Intent intent = new Intent(activity, LocationMapActivity.class);
            intent.putExtra(PAYOUT_LOCATIONS_EXTRA, payout);
            intent.putExtra(ACTIVITY_TITLE, title);

            activity.startActivity(intent);
        }
    }

    public static void navigateToChooseLocationForPickUp(Activity activity, String amount, String currencyType, String currencyOrigin, String beneficiaryId, Boolean isAnyWherePickUp) {
        if (activity != null) {
            Intent i = new Intent(activity, CashPickUpMapActivity.class);
            i.putExtra(INPUT_STATE, new CashPickUpMapState(amount, currencyType, currencyOrigin, beneficiaryId, isAnyWherePickUp));
            activity.startActivityForResult(i, CASH_PICK_UP);
        }
    }

    public static void navigateToCheckoutActivity(Activity activity, ArrayList<KeyValueData> transactionalDataList, String deliveryMethod, String paymentMethod, String payer) {
        if (activity != null) {
            Intent intent = new Intent(activity, CheckoutActivity.class);
            intent.putExtra(TRANSACTIONAL_DATA_EXTRA, transactionalDataList);
            intent.putExtra(DELIVERY_METHOD, deliveryMethod);
            intent.putExtra(PAYER, payer);

            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivityForResult(intent, Constants.REQUEST_CODES.CHECKOUT_REQUEST_CODE, compat.toBundle());
        }
    }

    public static void navigateToPdfReceiptDetail(Activity activity, File file, Context context) {
        if (activity != null) {

            try {
                Uri filePath;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    filePath = OwnFileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                } else {
                    filePath = Uri.fromFile(file);
                }

                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(filePath, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(pdfIntent, SmallWorldApplication.getStr(R.string.open_document));
                activity.startActivity(intent);
            } catch (Exception e) {
                new DialogExt().showSingleActionErrorDialog(activity, activity.getString(R.string.generic_title_error), activity.getString(R.string.not_app_pdf_viewer), null);
            }
        }
    }


    public static void navigateToHomeActivity(Activity activity, CreateTransactionResponse transactionResponse, boolean showCheckoutDialogExtra, Checkout checkout, String payoutName) {
        if (activity != null) {
            Intent returnIntent = new Intent(activity, HomeActivity.class);
            returnIntent.putExtra(SHOW_CHECKOUT_DIALOG_EXTRA, showCheckoutDialogExtra);
            returnIntent.putExtra(TRANSACTION_DATA, transactionResponse);
            returnIntent.putExtra(CHECKOUT_DATA, checkout);
            activity.setResult(Activity.RESULT_OK, returnIntent);

            activity.finish();
            ((TransactionalActivity) activity).overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
        }
    }

}
