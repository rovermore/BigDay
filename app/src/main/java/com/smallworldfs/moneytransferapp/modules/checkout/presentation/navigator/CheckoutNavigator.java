package com.smallworldfs.moneytransferapp.modules.checkout.presentation.navigator;

import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.CHECKOUT_DATA;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.SHOW_CHECKOUT_DIALOG_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.TRANSACTION_DATA;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.flinks.presentation.ui.activity.FlinksActivity;
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity;
import com.smallworldfs.moneytransferapp.presentation.account.documents.verification.VerificationActivity;
import com.smallworldfs.moneytransferapp.utils.Constants;

/**
 * Created by luismiguel on 28/9/17.
 */

public class CheckoutNavigator {

    public static void clearStackAndNavigateToHome(Activity activity, CreateTransactionResponse transactionResponse, Checkout checkout) {
        Intent returnIntent = new Intent(activity, HomeActivity.class);
        returnIntent.putExtra(SHOW_CHECKOUT_DIALOG_EXTRA, true);
        returnIntent.putExtra(TRANSACTION_DATA, transactionResponse);
        returnIntent.putExtra(CHECKOUT_DATA, checkout);
        activity.setResult(Activity.RESULT_OK, returnIntent);

        activity.finish();
        activity.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
    }

    public static void navigateToFlinksValidation(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, FlinksActivity.class);
            activity.startActivity(intent);
        }
    }

    public static void backToTransactionalDueError(Activity activity, String error) {
        if (activity != null) {
            Intent returnIntent = new Intent();
            returnIntent.setData(Uri.parse(error));
            activity.setResult(Activity.RESULT_CANCELED, returnIntent);

            activity.finish();
            activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left);
        }
    }

    public static void navigateToVerification(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, VerificationActivity.class);
            activity.startActivityForResult(intent, Constants.REQUEST_CODES.TRANSACTION_UPLOAD_DOCUMENTS_REQUEST_CODE);
        }
    }
}
