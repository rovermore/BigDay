package com.smallworldfs.moneytransferapp.modules.flinks.presentation.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.flinks.presentation.presenter.FlinksPresenter;
import com.smallworldfs.moneytransferapp.modules.flinks.presentation.presenter.implementation.FlinksPresenterImpl;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@AndroidEntryPoint
public class FlinksActivity extends GenericActivity implements FlinksPresenter.View {

    private static final String TAG = "FlinksActivity";

    Unbinder mUnbinder;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.general_loading_view)
    View mLoadingView;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.error_view)
    View mGeneralErrorView;

    private Dialog mProgressDialog;
    private FlinksPresenterImpl mPresenter;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flinks);

        mUnbinder = ButterKnife.bind(this);
        mHandler = new android.os.Handler();

        mPresenter = new FlinksPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, this);
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

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.flinks_activity_title));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_close_white));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    @Override
    public void showHideLoadingView(boolean show) {
        if (mLoadingView!=null) {
            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showHideGeneralErrorView(boolean show) {
        mGeneralErrorView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showHideValidationView(boolean show) {
        if (show && this.mProgressDialog == null) {
            this.mProgressDialog =
                    new DialogExt().showLoadingDialog(this,
                            getString(R.string.flinks_validation_title),
                            getString(R.string.flinks_validation_description),
                            true);

        } else if (!show && this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void startWebViewWithdUrl(String url) {
        mWebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                if (url.contains(Constants.CONFIGURATION.REDIRECT_ERROR_WEBVIEW)){
                    showHideGeneralErrorView(true);
                }else if (url.contains(getString(R.string.app_scheme)) && !url.contains(Constants.CONFIGURATION.REDIRECT_ERROR_WEBVIEW))
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.verifyFlinkState(url);
                        }
                    });
                else{
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showHideLoadingView(true);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!url.contains(Constants.CONFIGURATION.REDIRECT_WEBVIEW)) {
                    showHideLoadingView(false);
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                showHideGeneralErrorView(true);
            }
        });

        mWebview.getSettings().setJavaScriptEnabled(true);
        if (!url.contains(getString(R.string.app_scheme))){
            mWebview.loadUrl(url);
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

    @OnClick(R.id.button_text_retry)
    public void onRetryButtonClick(){
        mPresenter.retryButtonClick();
    }
}
