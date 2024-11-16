package com.smallworldfs.moneytransferapp.utils;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by luismiguel on 8/6/17.
 */

public class AnimationUtils {
    /**
     * Rotate view
     * @param view
     * @param duration
     * @param angle
     */
    public static void rotateView(View view, int duration, int angle){
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotation", angle, angle + 180);
        anim.setDuration(duration);
        anim.start();
    }
}
