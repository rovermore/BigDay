package com.smallworldfs.moneytransferapp.utils.widget;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.smallworldfs.moneytransferapp.R;

import java.util.Hashtable;

public class Typefaces {
    public static final int NORMAL = R.font.nunito_semi_bold;
    public static final int BOLD = R.font.nunito_black;
    public static final int SEMIBOLD = R.font.nunito_bold;

    private static final Hashtable<Integer, Typeface> cache = new Hashtable<>();

    public static Typeface get(Context c, int name) {
        synchronized (cache) {
            if (!cache.containsKey(name)) {
                Typeface t = ResourcesCompat.getFont(c, name);

                cache.put(name, t);
            }
            return cache.get(name);
        }
    }

}