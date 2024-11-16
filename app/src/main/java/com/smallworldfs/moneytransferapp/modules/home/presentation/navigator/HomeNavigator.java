package com.smallworldfs.moneytransferapp.modules.home.presentation.navigator;

import static com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryActivity.EXTRA_KEY;
import static com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryActivity.EXTRA_VALUE;
import static com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity.BENEFICIARY_TYPE;
import static com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity.FROM_TRANSACTIONAL;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.PayNowActivity.TRANSACTION_MTN_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.PayNowActivity.TRANSACTION_URL_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.TransactionalActivity.BENEFICIARY_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.TransactionalActivity.YOU_PAY_EXTRA;
import static com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity.TRANSACTION_EXTRA;
import static com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity.TRANSACTION_MTN;
import static com.smallworldfs.moneytransferapp.utils.Constants.REQUEST_CODES.MY_ACTIVITY_REQUEST_CODE;
import static com.smallworldfs.moneytransferapp.utils.Constants.REQUEST_CODES.PAY_NOW_ACTIVITY_REQUEST_CODE;
import static com.smallworldfs.moneytransferapp.utils.Constants.REQUEST_CODES.TRANSACTION_DETAIL_REQUEST_CODE;
import static com.smallworldfs.moneytransferapp.utils.Constants.REQUEST_CODES.TRANSFER_DETAILS_REQUEST_CODE;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryStepCountryActivity;
import com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity;
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.PayNowActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.TransactionalActivity;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.BeneficiaryListActivity;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;
import com.smallworldfs.moneytransferapp.presentation.account.contact.SelectContactSupportActivity;
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.MyDocumentsActivity;
import com.smallworldfs.moneytransferapp.presentation.account.offices.list.OfficesActivity;
import com.smallworldfs.moneytransferapp.presentation.account.profile.show.ProfileActivity;
import com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection.CountrySelectionActivity;
import com.smallworldfs.moneytransferapp.presentation.login.LoginActivity;
import com.smallworldfs.moneytransferapp.presentation.marketing.MarketingPreferencesActivity;
import com.smallworldfs.moneytransferapp.presentation.mtn.MTNActivity;
import com.smallworldfs.moneytransferapp.presentation.myactivity.TransactionHistoryActivity;
import com.smallworldfs.moneytransferapp.presentation.promotions.PromotionsActivity;
import com.smallworldfs.moneytransferapp.presentation.settings.SettingsActivity;
import com.smallworldfs.moneytransferapp.presentation.softregister.SignupActivity;
import com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity;
import com.smallworldfs.moneytransferapp.presentation.transferdetails.TransferDetailActivity;
import com.smallworldfs.moneytransferapp.presentation.welcome.WelcomeActivity;
import com.smallworldfs.moneytransferapp.utils.Constants;

public class HomeNavigator {

    public static void navigateToContactSupportActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, SelectContactSupportActivity.class);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivity(intent, compat.toBundle());
        }
    }

    public static void navigateToTransferDetails(Activity activity, Transaction transaction) {
        if (activity != null) {
            Intent intent = new Intent(activity, TransferDetailActivity.class);
            intent.putExtra(TRANSACTION_EXTRA, transaction);
            activity.startActivityForResult(intent, TRANSFER_DETAILS_REQUEST_CODE);
        }
    }

    public static void showSelectPromoCodeActivity(Activity activity, int requestCode, String payoutCountry) {
        if (activity != null) {
            Intent intent = new Intent(activity, PromotionsActivity.class);
            intent.putExtra(PromotionsActivity.PAYOUT_COUNTRY, payoutCountry);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void showTransactionalActivity(@NonNull final Activity activity, @Nullable final BeneficiaryUIModel beneficiarySelected, @Nullable final String youPay, @Nullable final String beneficiaryType) {
        Intent intent = new Intent(activity, TransactionalActivity.class);
        if (beneficiarySelected != null) {
            intent.putExtra(BENEFICIARY_EXTRA, (Parcelable) beneficiarySelected);
            intent.putExtra(YOU_PAY_EXTRA, youPay);
            intent.putExtra(BENEFICIARY_TYPE, beneficiaryType);
        }
        activity.startActivityForResult(intent, Constants.REQUEST_CODES.TRANSACTIONAL_REQUEST_CODE);
    }

    public static void navigateToLoginActivity(Activity activity, boolean clearStack) {
        if (activity != null) {
            Intent intent = new Intent(activity, LoginActivity.class);
            if (clearStack) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public static void navigateToSignUpActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, SignupActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public static void navigateToWelcomeActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(WelcomeActivity.PASSCODE_DIALOG_KEY, WelcomeActivity.LOGIN);
            activity.startActivity(intent);
            activity.finish();
        }
    }


    public static void navigateToProfileActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, ProfileActivity.class);
            activity.startActivity(intent);
        }
    }

    public static void navigateToMyBeneficiariesActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, BeneficiaryListActivity.class);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivityForResult(intent, Constants.REQUEST_CODES.BENEFICIARY_LIST, compat.toBundle());
        }
    }

    public static void navigateToMyActivityActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, TransactionHistoryActivity.class);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivityForResult(intent, MY_ACTIVITY_REQUEST_CODE, compat.toBundle());
        }
    }

    public static void navigateToMyDocumentsActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, MyDocumentsActivity.class);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivity(intent, compat.toBundle());
        }
    }

    public static void navigateToSettingsActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, SettingsActivity.class);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivity(intent, compat.toBundle());
        }
    }

    public static void navigateToMTNActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, MTNActivity.class);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivity(intent, compat.toBundle());
        }
    }

    public static void navigateToCreateBeneficiaryActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, NewBeneficiaryStepCountryActivity.class);
            activity.startActivityForResult(intent, Constants.REQUEST_CODES.NEW_BENEFICIARY);
        }
    }

    public static void navigateToC2BActivity(@NonNull final Activity activity,
                                             @NonNull final Pair<String, String> country,
                                             final boolean fromTransactional) {
        Intent intent = new Intent(activity, C2BActivity.class);
        intent.putExtra(EXTRA_KEY, country.first);
        intent.putExtra(EXTRA_VALUE, country.second);
        intent.putExtra(FROM_TRANSACTIONAL, fromTransactional);
        int requestCode = fromTransactional ? Constants.REQUEST_CODES.TRANSACTIONAL_REQUEST_CODE :
                Constants.REQUEST_CODES.NEW_BENEFICIARY;
        activity.startActivityForResult(intent, requestCode);
    }

    public static void navigateToPayNowActivity(Activity activity, String transactionMtn) {
        if (activity != null) {
            Intent intent = new Intent(activity, PayNowActivity.class);
            intent.putExtra(TRANSACTION_MTN_EXTRA, transactionMtn);
            activity.startActivityForResult(intent, PAY_NOW_ACTIVITY_REQUEST_CODE);
        }
    }

    // Duplicated method to allow optional parameter, delete it when migrate to kotlin
    public static void navigateToPayNowActivity(Activity activity, String transactionMtn, String url) {
        if (activity != null) {
            Intent intent = new Intent(activity, PayNowActivity.class);
            intent.putExtra(TRANSACTION_MTN_EXTRA, transactionMtn);
            intent.putExtra(TRANSACTION_URL_EXTRA, url);
            activity.startActivity(intent);
        }
    }

    public static void navigateToOfficesActivity(Activity activity, String sectionTitle) {
        if (activity != null) {
            Intent intent = new Intent(activity, OfficesActivity.class);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivity(intent, compat.toBundle());
        }
    }

    public static void navigateToTransactionStatusDetail(Activity activity, Long mtn) {
        if (activity != null) {
            Intent intent = new Intent(activity, TransactionStatusDetailActivity.class);
            intent.putExtra(TRANSACTION_MTN, mtn);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivityForResult(intent, TRANSACTION_DETAIL_REQUEST_CODE, compat.toBundle());
        }
    }

    public static void navigateToSettingsMarketingPreferences(Activity activity, String title, String from) {
        if (activity != null) {
            Intent intent = new Intent(activity, MarketingPreferencesActivity.class);
            intent.putExtra(MarketingPreferencesActivity.TITLE, title);
            intent.putExtra(MarketingPreferencesActivity.FROM, from);
            activity.startActivity(intent);
        }
    }

    public static void navigateToCountrySelector(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, CountrySelectionActivity.class);
            activity.startActivity(intent);
        }
    }
}
