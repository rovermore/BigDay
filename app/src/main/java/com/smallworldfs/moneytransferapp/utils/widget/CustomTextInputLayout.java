package com.smallworldfs.moneytransferapp.utils.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.util.AttributeSet;

/**
 * Created by ddi-pc-52 on 06.03.18.
 */

public class CustomTextInputLayout extends TextInputLayout {
    public CustomTextInputLayout(Context context) {
        super(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        super.setError(error);
       requestFocus();

    }
}
