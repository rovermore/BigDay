package com.smallworldfs.moneytransferapp.utils.widget;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;

/**
 * Created by luis on 25/5/17
 */
public class DismissibleEditText extends androidx.appcompat.widget.AppCompatEditText {

    public DismissKeyboardListener mListener;
    private InputFilter[] nonLatinCharactersInputFilter = {(source, start, end, dest, dstart, dend) -> {
        String isLatin = "\\p{Latin}+";
        String digits = "[0-9 ]+";
        String symbols = "[:?!@#$%^&*(),.;\\-_'’+/=\"€£¥₩\\n]";
        for (int i = 0; i < source.length(); i++) {
            if (!String.valueOf(source.charAt(i)).matches(isLatin) && !String.valueOf(source.charAt(i)).matches(digits) && !String.valueOf(source.charAt(i)).matches(symbols)) {
                new DialogExt().showSingleActionErrorDialog(getContext(), getContext().getString(R.string.an_error_has_occurred), getContext().getString(R.string.incorrect_language), null);
                return "";
            }
        }
        return null;
    }};

    public interface DismissKeyboardListener {
        void onDismissKeyboard();
    }

    public DismissibleEditText(Context context) {
        super(context);
        setFilters(nonLatinCharactersInputFilter);
    }

    public DismissibleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFilters(nonLatinCharactersInputFilter);
    }

    public DismissibleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFilters(nonLatinCharactersInputFilter);
    }

    public DismissibleEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);//, defStyleAttr, defStyleRes);
        setFilters(nonLatinCharactersInputFilter);
    }

    public void setDismissListener(DismissKeyboardListener listener) {
        this.mListener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // Do your thing.
            if (mListener != null && hasFocus()) {
                mListener.onDismissKeyboard();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onEditorAction(int actionCode) {
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            if (mListener != null) {
                mListener.onDismissKeyboard();
            }
        }
        super.onEditorAction(actionCode);
    }
}
