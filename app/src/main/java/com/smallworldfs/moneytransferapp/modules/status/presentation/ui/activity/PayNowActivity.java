package com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.US_COUNTRY_VALUE;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.INT_ZERO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.implementation.PayNowInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.PaymentUrl;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.PayNowPresenter;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation.PayNowPresenterImpl;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.ConstantsKt;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@AndroidEntryPoint
public class PayNowActivity extends GenericActivity implements PayNowPresenter.View {

    public static final String TRANSACTION_MTN_EXTRA = "TRANSACTION_MTN_EXTRA";
    public static final String TRANSACTION_URL_EXTRA = "TRANSACTION_URL_EXTRA";
    private static final String TAG = "PayNowActivity";
    @Inject
    UserDataRepository userDataRepository;

    Unbinder mUnbinder;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.general_loading_view)
    View mLoadingView;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.error_view)
    View mGeneralErrorView;
    @BindView(R.id.activityPayNowFabButton)
    FloatingActionButton mFabButton;

    private PayNowPresenterImpl mPresenter;
    private String mTransactionMtn;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean showCustomUrl;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);

        mUnbinder = ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            mTransactionMtn = getIntent().getStringExtra(TRANSACTION_MTN_EXTRA);
        }
        String mUrlToShow = getIntent().hasExtra(TRANSACTION_URL_EXTRA) ? getIntent().getStringExtra(TRANSACTION_URL_EXTRA) : null;
        showCustomUrl = !(mUrlToShow == null || mUrlToShow.isEmpty());

        mPresenter = new PayNowPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, mTransactionMtn, mUrlToShow, this);
        mPresenter.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.destroy();
        mUnbinder.unbind();
    }

    @Override
    public void configureView() {
        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);

        showHideLoadingView(true);

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(showCustomUrl ? getString(R.string.secondary_button_text_action_print_boleto).substring(ConstantsKt.INT_ZERO, ConstantsKt.INT_ONE).toUpperCase(Locale.getDefault()) + getString(R.string.secondary_button_text_action_print_boleto).substring(ConstantsKt.INT_ONE).toLowerCase() : getString(R.string.pay_now_activity_title));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back_toolbar_white));

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        mFabButton.setEnabled(false);
        mFabButton.setVisibility(showCustomUrl ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (showCustomUrl) {
            finish();
        } else {
            new DialogExt().showDoubleActionGeneralDialog(this, getString(R.string.exit_pay_now_activity_dialog_title), getString(R.string.exit_pay_now_activity_dialog_content), getString(R.string.accept_text), this::finish, getString(R.string.cancel), () -> {
            });
        }
    }

    @Override
    public void showHideLoadingView(boolean show) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void setPaymentUrl(PaymentUrl paymentData) {
        if (mWebview != null) {
            WebSettings settings = mWebview.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setDomStorageEnabled(true);
            settings.setSaveFormData(false);

            // Override JavaScript window.print() function and set android function
            mWebview.addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void print() {
                    runOnUiThread(() -> createWebPrintJob(webView));
                }
            }, "customPrint");

            mWebview.clearCache(true);
            mWebview.clearHistory();

            clearCookies(this, mWebview);

            mWebview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                    Log.d(TAG, url);
                    if (url.contains(getString(R.string.app_scheme))) {
                        mHandler.post(() -> {
                            if (!isFinishing() && !isDestroyed()) {
                                showContentUrl(url);
                            }
                        });
                    } else {
                        view.loadUrl(url);
                    }
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    Log.d(TAG, "Page finished loading " + url);

                    if (showCustomUrl) {
                        mFabButton.setEnabled(true);
                    } else {
                        User user = userDataRepository.retrieveUser();
                        if (user != null && user.getCountry().firstKey().equals(US_COUNTRY_VALUE)) {
                            view.scrollTo(INT_ZERO, view.getContentHeight());
                        }
                    }

                    // Override JavaScript window.print() function and set android function
                    webView = view;
                    if (mWebview != null) {
                        mWebview.loadUrl("javascript:window.print = function() {customPrint.print()}");
                    }
                }
            });

            PayNowInteractorImpl.HttpMethod method = PayNowInteractorImpl.HttpMethod.fromKey(paymentData.getHttp());
            openUrl(paymentData.getUrl(), method);
        }
    }

    private void createWebPrintJob(WebView webView) {
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + " Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance
        Objects.requireNonNull(printManager).print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }

    @Override
    public void showHideGeneralErrorView(boolean show) {
        if (mGeneralErrorView != null) {
            mGeneralErrorView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showContentUrl(final String url) {
        if (!TextUtils.isEmpty(url) && url.contains(Constants.PAY_CONSTANTS.MTN) && url.contains(Constants.PAY_CONSTANTS.SUCCESS)) {
            // Track ecomerce
            mPresenter.trackEcomerce(mTransactionMtn);

            // Success payment
            if (showCustomUrl) {
                finish();
            } else {
                new DialogExt().showSingleActionInfoDialog(
                        this, getString(R.string.pay_now_activity_payment_confirmed),
                        getString(R.string.payment_accepted_message),
                        getString(R.string.cancel_transaction_transaction_status_button_close),
                        () -> {
                            User user = userDataRepository.retrieveUser();
                            if (user != null && user.getCountry().firstKey().equals(US_COUNTRY_VALUE)) {
                                mPresenter.showReceipt();
                            } else {
                                finish();
                            }
                        },
                        "");
            }
        } else if (!TextUtils.isEmpty(url) && url.contains(Constants.PAY_CONSTANTS.MTN) && !url.contains(Constants.PAY_CONSTANTS.SUCCESS)) {
            String message = "";
            String title = getString(R.string.delete_beneficiary_error_title);
            String actionText = getString(R.string.payment_title_unsuccesfull_payment_action_button);
            if (url.contains(Constants.PAY_CONSTANTS.REFUSED)) {
                message = getString(R.string.payment_refused_message);
                title = getString(R.string.payment_title_unsuccesfull_payment);
            } else if (url.contains(Constants.PAY_CONSTANTS.CVV_WARNING)) {
                message =   getString(R.string.payment_cvv_warning_message);
            } else if (url.contains(Constants.PAY_CONSTANTS.BLOCKED_WARNING)) {
                message = getString(R.string.payment_blocked_warning_message);
                title = getString(R.string.payment_title_unsuccesfull_payment);
            } else if (url.contains(Constants.PAY_CONSTANTS.BLOCKED)) {
                message = getString(R.string.payment_blocked_message);
                title = getString(R.string.payment_title_user_blocked);
                actionText = getString(R.string.cancel_transaction_transaction_status_button_close);
            }
            if (showCustomUrl) {
                finish();
            } else {
                new DialogExt().showSingleActionErrorDialog(this, title, message, actionText, this::finish);
            }
        }

    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context, WebView webview) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().removeSessionCookies(null);

            /*  If your app targets API level 21 or higher the system blocks mixed content and third party cookies by default.
                If your app targets API levels lower than 21 the system allows mixed content and third party cookies,
                and always renders the whole document at once.  */
            CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true);
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }


    public void openUrl(String url, PayNowInteractorImpl.HttpMethod method) {
        switch (method) {
            case POST:
                mWebview.postUrl(url, null);
                break;
            case GET:
                mWebview.loadUrl(url);
                break;
        }
    }

    @OnClick(R.id.activityPayNowFabButton)
    public void activityPayNowFabButtonClick() {
        createWebPrintJob(webView);
    }

    @OnClick(R.id.button_text_retry)
    public void onRetryButtonClick() {
        mPresenter.retryButtonClick();
    }
}
