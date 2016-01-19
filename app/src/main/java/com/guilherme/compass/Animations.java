package com.guilherme.compass;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by Guilherme on 15/01/2016.
 */
public class Animations {
    public static void rotateBetweenTwoDegrees(View objectToAnimate, float currentDegree, float targetDegree){
        RotateAnimation animation = new RotateAnimation(
                currentDegree,
                targetDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        objectToAnimate.startAnimation(animation);

        animation.setDuration(100); // make the animation smoother
        animation.setFillAfter(true);
        objectToAnimate.startAnimation(animation);
    }
}
