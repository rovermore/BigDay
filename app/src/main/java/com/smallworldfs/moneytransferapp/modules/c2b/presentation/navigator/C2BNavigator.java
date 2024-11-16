package com.smallworldfs.moneytransferapp.modules.c2b.presentation.navigator;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryActivity;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.TransactionalActivity;
import com.smallworldfs.moneytransferapp.utils.Constants;

import static com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryActivity.EXTRA_KEY;
import static com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryActivity.EXTRA_VALUE;
import static com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity.BENEFICIARY_TYPE;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.SHOW_CHECKOUT_DIALOG_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.TRANSACTION_DATA;

public class C2BNavigator {

    public static void navigateToNewBeneficiary(Activity activity, Pair<String, String> country, String type) {
        Intent intent = new Intent(activity, NewBeneficiaryActivity.class);
        intent.putExtra(EXTRA_KEY, country.first);
        intent.putExtra(EXTRA_VALUE, country.second);
        intent.putExtra(BENEFICIARY_TYPE, type);
        activity.startActivityForResult(intent, Constants.REQUEST_CODES.NEW_BENEFICIARY);
    }

    public static void showTransactionalActivity(@NonNull final Activity activity, @NonNull Pair<String, String> country, String type) {
        Intent intent = new Intent(activity, TransactionalActivity.class);
        intent.putExtra(EXTRA_KEY, country.first);
        intent.putExtra(EXTRA_VALUE, country.second);
        intent.putExtra(BENEFICIARY_TYPE, type);
        activity.startActivityForResult(intent, Constants.REQUEST_CODES.TRANSACTIONAL_REQUEST_CODE);
    }

    public static void navigateToHomeActivity(Activity activity, CreateTransactionResponse transactionResponse, boolean showCheckoutDialogExtra) {
        if (activity != null) {
            Intent returnIntent = new Intent(activity, HomeActivity.class);
            returnIntent.putExtra(SHOW_CHECKOUT_DIALOG_EXTRA, showCheckoutDialogExtra);
            returnIntent.putExtra(TRANSACTION_DATA, transactionResponse);
            activity.setResult(Activity.RESULT_OK, returnIntent);

            activity.finish();
            activity.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
        }
    }
}
