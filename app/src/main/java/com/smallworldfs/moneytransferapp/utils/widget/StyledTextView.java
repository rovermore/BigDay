package com.smallworldfs.moneytransferapp.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;


/**
 * Created by luismiguel on 28/6/16.
 */

public class StyledTextView extends AppCompatTextView {
    public StyledTextView(Context context) {
        this(context, null);
    }

    public StyledTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StyledTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        if (this.isInEditMode()) {
            return;
        }
        int customFont = Typefaces.NORMAL;
        int[] attrsArray = new int[]{android.R.attr.textStyle};
        TypedArray t = context.obtainStyledAttributes(attrs, attrsArray);

        // Cambia la fuente si tiene estilo
        if (t.getInt(0, 0) == Typeface.BOLD) {
            customFont = Typefaces.BOLD;
        } else if (t.getInt(0, 0) == Typeface.ITALIC) {
            customFont = Typefaces.SEMIBOLD;
        }
        // Set custom font
        this.setTypeface(Typefaces.get(context, customFont));
        t.recycle();
    }

    public void tint(@ColorRes int tintColor) {
        this.setTextColor(ContextCompat.getColor(getContext(), tintColor));
        for (Drawable drawable : this.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(), tintColor), PorterDuff.Mode.SRC_IN));
            }
        }
    }

}
