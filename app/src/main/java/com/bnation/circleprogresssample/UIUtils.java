package com.bnation.circleprogresssample;

import android.view.View;

/**
 * Created by ahmed.bah7ini on Mar 15, 2017.
 */

public class UIUtils {

    public static void animateProgress(View progressBar, float to){
        ProgressBarAnimation animation = new ProgressBarAnimation(progressBar,0,to);
        animation.setDuration(1500);
        progressBar.startAnimation(animation);
    }
}
