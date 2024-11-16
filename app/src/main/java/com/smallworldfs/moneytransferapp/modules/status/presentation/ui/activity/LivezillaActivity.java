package com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SendEmailActivity.CONTACT_SUPPORT_INFO_EXTRA;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.api.Api;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ContactSupportInfo;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.LivezillaPresenter;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation.LivezillaPresenterImpl;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by michel on 27/6/18.
 */
@AndroidEntryPoint
public class LivezillaActivity extends GenericActivity implements LivezillaPresenter.View {

    Unbinder mUnbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.general_loading_view)
    View mLoadingView;
    @BindView(R.id.error_view)
    View mErrorView;
    @BindView(R.id.web_view)
    WebView mWebView;


    private LivezillaPresenterImpl mPresenter;
    private MaterialDialog mProgressDialog;
    private ContactSupportInfo mContactCustomerSupportInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livezilla);
        if (getIntent().getExtras() != null) {
            mContactCustomerSupportInfo = getIntent().getExtras().getParcelable(CONTACT_SUPPORT_INFO_EXTRA);
        }
        mUnbinder = ButterKnife.bind(this);
        mPresenter = new LivezillaPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, this);
        mPresenter.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mPresenter.destroy();
        mUnbinder.unbind();
    }

    @Override
    public void configureView() {
        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.contact_chat));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back_toolbar_white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mLoadingView.setVisibility(View.VISIBLE);
        showHideContentLayout(false);
        showHideGeneralLoadingView(true);
        configureWebView();
    }

    public void configureWebView() {

        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setDomStorageEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mWebView != null) {
                    mWebView.setVisibility(View.VISIBLE);
                }
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (mWebView != null) {
                    mWebView.setVisibility(View.GONE);
                }
                if (mErrorView != null) {
                    mErrorView.setVisibility(View.VISIBLE);
                }
            }
        });
        String url = Api.WEB_URL + EndPoint.LIVEZILLA_ENDPOINT.replace("%%s", mContactCustomerSupportInfo.getLivezilla());
        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        registerEvent("click_back");
        LivezillaActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
    }

    @Override
    public void showHideGeneralLoadingView(boolean show) {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showHideContentLayout(boolean show) {
    }

    @Override
    public void showTopErrorView() {
    }

    @OnClick(R.id.button_text_retry)
    public void onRetryButtonClick() {
        mLoadingView.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mPresenter.onRetryClick();
    }

    private void registerEvent(String eventAction) {
        trackEvent(
                new UserActionEvent(
                        ScreenCategory.CONTACT.getValue(),
                        eventAction,
                        "",
                        getHierarchy(""),
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                )
        );
    }
}
