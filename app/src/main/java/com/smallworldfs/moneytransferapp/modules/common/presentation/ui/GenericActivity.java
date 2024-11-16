package com.smallworldfs.moneytransferapp.modules.common.presentation.ui;

import static com.smallworldfs.moneytransferapp.modules.notifications.domain.handler.PushPrimeClickHandler.REQUEST_NOTIFICATION_PERMISSION;
import static com.smallworldfs.moneytransferapp.presentation.common.session.SessionHandler.SESSION_EXPIRED_ACTION;
import static com.smallworldfs.moneytransferapp.utils.ActivityExtensionsKt.registerBroadcastReceiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.braze.Braze;
import com.braze.models.outgoing.BrazeProperties;
import com.smallworldfs.moneytransferapp.BuildConfig;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.data.net.RiskifiedInstance;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsSender;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserProperty;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExtKt;
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker;
import com.smallworldfs.moneytransferapp.domain.usertoken.repository.local.UserTokenLocal;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.home.presentation.navigator.HomeNavigator;
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository;
import com.smallworldfs.moneytransferapp.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class GenericActivity extends AppCompatActivity {

    public boolean mErrorViewShowing = false;
    private android.app.Dialog mSessionExpiredDialog;
    public GenericPresenterImpl mPresenter;

    @Inject
    LoginRepository loginRepository;

    @Inject
    RiskifiedInstance riskifiedInstance;

    @Inject
    UserTokenLocal userTokenLocal;

    @Inject
    AnalyticsSender analyticsSender;

    @Inject
    CapabilityChecker capabilityChecker;

    @Inject
    Braze braze;

    private BroadcastReceiver sessionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DialogExtKt.showSessionExpiredDialog(GenericActivity.this, dialog -> {
                String eventCategory = dialog.getCategory();
                trackEvent(new UserActionEvent(
                        eventCategory,
                        "click_accept",
                        "",
                        ScreenName.MODAL_SESSION_EXPIRED.getValue() +"_"+eventCategory,
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                ));
                UserTokenLocal userTokenLocal = new UserTokenLocal();
                userTokenLocal.clearUserToken();
                HomeNavigator.navigateToWelcomeActivity(GenericActivity.this);
                return null;
            });
        }
    };

    private final BroadcastReceiver inAppMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ArrayList<String> permissions = new ArrayList<>();
                permissions.add(Manifest.permission.POST_NOTIFICATIONS);
                capabilityChecker.requestPermissions(
                        getCurrentActivity(),
                        permissions.toArray(new String[0]),
                        (granted, permissionGrantedResponses) -> null
                );
            }
        }
    };

    private GenericActivity getCurrentActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.logCrashlyticsScreen(this.getClass().getName());

        if (!userTokenLocal.getUserToken().isEmpty()) {
            riskifiedInstance.startBeacon(userTokenLocal.getUserToken());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcastReceiver(this, inAppMessageReceiver, new IntentFilter(REQUEST_NOTIFICATION_PERMISSION), RECEIVER_NOT_EXPORTED);
        registerBroadcastReceiver(this, sessionReceiver, new IntentFilter(SESSION_EXPIRED_ACTION), RECEIVER_NOT_EXPORTED);

        Utils.logActionCrashlytics("On resume in " + this.getClass().getSimpleName());
        if (Utils.isDeviceRooted() && !BuildConfig.DEBUG) {
            (new DialogExt()).showDeviceRootedDialog(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(sessionReceiver);
        unregisterReceiver(inAppMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        Utils.logActionCrashlytics("On destroy in " + this.getClass().getSimpleName());
        super.onDestroy();
        if (mSessionExpiredDialog != null) {
            mSessionExpiredDialog.dismiss();
        }
        mSessionExpiredDialog = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        riskifiedInstance.removeLocUpdates();
    }

    protected void showErrorView(String text, String subtitle, final View mErrorView) {

        this.mErrorViewShowing = true;

        if (mErrorView != null) {

            mErrorView.setVisibility(View.VISIBLE);

            ((TextView) mErrorView.findViewById(R.id.error_title)).setText(text != null ? text : getString(R.string.generic_error_view_action_subtitle));
            ((TextView) mErrorView.findViewById(R.id.error_subtitle)).setText(subtitle != null ? subtitle : getString(R.string.generic_subtitle_error));

            mErrorView.setTranslationY(-mErrorView.getHeight());
            mErrorView.animate().translationYBy(mErrorView.getHeight()).setDuration(300).start();

            // Set click listener to close view
            mErrorView.findViewById(R.id.close_error).setOnClickListener(v -> mErrorView.animate().translationYBy(-mErrorView.getHeight()).setDuration(300));

            //Clickable text error
            if (loginRepository.getUser() != null) {
                TextView textViewSubtitle = mErrorView.findViewById(R.id.error_subtitle);
                if (subtitle != null && subtitle.toLowerCase().contains(getString(R.string.generic_error_view_action_subtitle).toLowerCase())) {
                    setClickableString(getString(R.string.generic_error_view_action_subtitle), subtitle, textViewSubtitle);
                }
            } else {
                //If contains contact support string - replace by generic
                if (subtitle != null && subtitle.contains(getString(R.string.generic_error_view_action_subtitle))) {
                    ((TextView) mErrorView.findViewById(R.id.error_subtitle)).setText(getString(R.string.generic_subtitle_error));
                }
            }
        }
    }

    //Redirect to Contact Customer Support
    private void setClickableString(String clickableValue, String wholeValue, TextView textView) {
        SpannableString spannableString = new SpannableString(wholeValue);
        int startIndex = !wholeValue.contains(clickableValue) ? 0 : wholeValue.indexOf(clickableValue);
        int endIndex = startIndex + clickableValue.length();
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                HomeNavigator.navigateToContactSupportActivity(GenericActivity.this);
            }
        }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setLinkTextColor(Color.WHITE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance()); // <-- important, onClick in ClickableSpan won't work without this
    }

    protected void showGenericErrorView(final View errorView) {
        showErrorView(getString(R.string.generic_error_view_text), getString(R.string.generic_error_view_subtitle), errorView);
    }

    protected void hideErrorView(final View mErrorView) {
        this.mErrorViewShowing = false;
        if (mErrorView.getVisibility() == View.VISIBLE) {
            mErrorView.animate().translationYBy(-mErrorView.getHeight()).setDuration(300);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void trackScreen(String screenName) {
        analyticsSender.trackScreen(screenName);
    }

    public void trackScreenBraze(String screenName, Map<String, String> properties) {
        analyticsSender.trackScreenBraze(screenName, properties);
    }

    public void trackEvent(AnalyticsEvent analyticsEvent){
        analyticsSender.trackEvent(analyticsEvent);
    }

    public void updateUserProperties(List<UserProperty> userProperties) {
        for (UserProperty userProperty: userProperties) {
            analyticsSender.updateUserProperty(userProperty);
        }
    }

    public String getHierarchy(String screenName) {
        ScreenEvent screenEvent;
        if (screenName != null && !screenName.isEmpty())
            screenEvent = analyticsSender.getScreenEventProperties(screenName);
        else
            screenEvent = analyticsSender.getScreenEventProperties(this.getClass().getSimpleName());
        return screenEvent.getHierarchy();
    }

    public String getEventCategory(String screenName) {
        ScreenEvent screenEvent;
        if (screenName != null && !screenName.isEmpty()) {
            screenEvent = analyticsSender.getScreenEventProperties(screenName);
        } else {
            screenEvent = analyticsSender.getScreenEventProperties(this.getClass().getSimpleName());
        }
        return screenEvent.getScreenCategory();
    }

    public void trackEvent(BrazeEvent event) {
        HashMap<String, String> map = new HashMap<>(event.getProperties());
        map.put("Platform", "Android");
        event.setProperties(map);
        braze.logCustomEvent(
                event.getName(),
                new BrazeProperties(
                        new JSONObject(
                                event.getProperties()
                        )
                )
        );
    }
}
