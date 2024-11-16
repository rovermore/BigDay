package com.smallworldfs.moneytransferapp.modules.scroll;


import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import android.view.View;

public class NestedScrollViewFocusRemover implements NestedScrollView.OnScrollChangeListener {
    private Activity mActivity;

    public NestedScrollViewFocusRemover(@NonNull final Activity activity) {
        mActivity = activity;
    }


    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        final View currentFocus = mActivity.getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
    }
}
