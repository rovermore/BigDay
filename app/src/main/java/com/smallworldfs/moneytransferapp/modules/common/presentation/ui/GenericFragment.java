package com.smallworldfs.moneytransferapp.modules.common.presentation.ui;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.braze.Braze;
import com.braze.models.outgoing.BrazeProperties;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsFragmentLifecycleCallback;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsSender;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenEvent;
import com.smallworldfs.moneytransferapp.modules.home.presentation.navigator.HomeNavigator;
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by luismiguel on 12/6/17
 */
@AndroidEntryPoint
public class GenericFragment extends Fragment {

    @Inject
    LoginRepository loginRepository;

    @Inject
    AnalyticsSender analyticsSender;

    @Inject
    AnalyticsFragmentLifecycleCallback analyticsFragmentLifecycleCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getChildFragmentManager().registerFragmentLifecycleCallbacks(analyticsFragmentLifecycleCallback, true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        getChildFragmentManager().unregisterFragmentLifecycleCallbacks(analyticsFragmentLifecycleCallback);
        super.onDestroy();
    }

    /**
     * Show Common error view
     */
    protected void showErrorView(String text, String subtitle, final View errorView, boolean disableStatusPadding) {
        errorView.setVisibility(View.VISIBLE);

        ((TextView) errorView.findViewById(R.id.error_title)).setText(text);
        ((TextView) errorView.findViewById(R.id.error_subtitle)).setText(subtitle);

        errorView.setTranslationY(-errorView.getHeight());
        errorView.animate().translationYBy(errorView.getHeight()).setDuration(300).start();

        // Set click listener to close view
        errorView.findViewById(R.id.close_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideErrorView(errorView);
            }
        });

        //Clickable text error
        if (loginRepository.getUser() != null) {
            TextView textViewSubtitle = errorView.findViewById(R.id.error_subtitle);
            if (subtitle.contains(getString(R.string.generic_error_view_action_subtitle))) {
                setClickableString(getString(R.string.generic_error_view_action_subtitle), subtitle, textViewSubtitle);
            }
        } else {
            //If contains contact support string - replace by generic
            if (subtitle.contains(getString(R.string.generic_error_view_action_subtitle))) {
                ((TextView) errorView.findViewById(R.id.error_subtitle)).setText(getString(R.string.generic_subtitle_error));
            }
        }

        if (disableStatusPadding) {
            errorView.findViewById(R.id.status_bar_padding).setVisibility(View.GONE);
        }
    }

    protected void showErrorView(String text, String subtitle, final View errorView) {
        showErrorView(text, subtitle, errorView, false);
    }

    //Redirect to Contact Customer Support
    private void setClickableString(String clickableValue, String wholeValue, TextView textView) {
        String value = wholeValue;
        SpannableString spannableString = new SpannableString(value);
        int startIndex = value.indexOf(clickableValue);
        int endIndex = startIndex + clickableValue.length();
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                HomeNavigator.navigateToContactSupportActivity(getActivity());
            }

            @Override
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setUnderlineText(true); // set to false to remove underline
            }
        }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setLinkTextColor(Color.WHITE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance()); // <-- important, onClick in ClickableSpan won't work without this
    }


    /**
     * Hide error view
     */
    protected void hideErrorView(final View mErrorView) {
        if (mErrorView.getVisibility() == View.VISIBLE) {
            mErrorView
                    .animate()
                    .translationYBy(-mErrorView.getHeight())
                    .setDuration(300)
                    .setListener(
                            new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(@NonNull Animator animator) {
                                }

                                @Override
                                public void onAnimationEnd(@NonNull Animator animator) {
                                    // This is a workaround until SendToFragment refactor
                                    if (mErrorView.getY() < 0) {
                                        mErrorView.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onAnimationCancel(@NonNull Animator animator) {
                                }

                                @Override
                                public void onAnimationRepeat(@NonNull Animator animator) {
                                }
                            }
                    );
        }
    }

    public void trackScreen(String screenName) {
        analyticsSender.trackScreen(screenName);
    }

    public void trackScreenBraze(String screenName, Map<String, String> properties) {
        analyticsSender.trackScreenBraze(screenName, properties);
    }

    public void trackEvent(AnalyticsEvent analyticsEvent) {
        analyticsSender.trackEvent(analyticsEvent);
    }

    public void trackEvent(BrazeEvent event){
        HashMap<String, String> map = new HashMap<>(event.getProperties());
        map.put("Platform", "Android");
        event.setProperties(map);
        Braze.getInstance(requireContext()).logCustomEvent(
                event.getName(),
                new BrazeProperties(
                        new JSONObject(
                                event.getProperties()
                        )
                )
        );
    }

    public String getHierarchy(String screenName) {
        ScreenEvent screenEvent;
        if (screenName != null && !screenName.isEmpty())
            screenEvent = analyticsSender.getScreenEventProperties(screenName);
        else
            screenEvent = analyticsSender.getScreenEventProperties(this.getClass().getSimpleName());
        return screenEvent.getHierarchy();
    }
}
