package com.smallworldfs.moneytransferapp.modules.tracking.ui.navigator;

import android.app.Activity;
import android.content.Intent;

import androidx.core.app.ActivityOptionsCompat;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.qr.presentation.ui.QrActivity;
import com.smallworldfs.moneytransferapp.presentation.mtn.MTNActivity;

public class TrackingNavigator {

    public static void navigateToQrTrackingActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, QrActivity.class);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivity(intent, compat.toBundle());
        }
    }

    public static void navigateToMtnTrackingActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, MTNActivity.class);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left);
            activity.startActivity(intent, compat.toBundle());
        }
    }
}
