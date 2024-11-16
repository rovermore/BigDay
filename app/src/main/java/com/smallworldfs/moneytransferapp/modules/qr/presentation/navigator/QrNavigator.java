package com.smallworldfs.moneytransferapp.modules.qr.presentation.navigator;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.presentation.mtn.MTNActivity;

public class QrNavigator {

    public static void navigateToTransactionActivity(GenericActivity activity, @NonNull String url) {
        Intent intent = new Intent(activity, MTNActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }
}
