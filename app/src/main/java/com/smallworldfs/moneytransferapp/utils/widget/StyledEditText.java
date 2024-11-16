package com.smallworldfs.moneytransferapp.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;

/**
 * Created by luismiguel on 28/6/16.
 */

public class StyledEditText extends androidx.appcompat.widget.AppCompatEditText {

    public StyledEditText(Context context) {
        this(context, null);
    }

    public StyledEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StyledEditText(Context context, AttributeSet attrs, int defStyle) {
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

        InputFilter[] nonLatinCharactersInputFilter = {new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String isLatin = "\\p{Latin}+";
                String digits = "[0-9 ]+";
                String symbols = "[:?!@#$%^&*(),.;\\-_'’+/=\"€£¥₩\\n]";
                for (int i = 0; i < source.length(); i++) {
                    if (!String.valueOf(source.charAt(i)).matches(isLatin) && !String.valueOf(source.charAt(i)).matches(digits) &&
                            !String.valueOf(source.charAt(i)).matches(symbols)) {
                        new DialogExt().showSingleActionErrorDialog(getContext(), getContext().getString(R.string.an_error_has_occurred),
                                getContext().getString(R.string.incorrect_language), null);
                        return "";
                    }
                }
                return null;
            }
        }};
        setFilters(nonLatinCharactersInputFilter);
    }

}
