/*
 *    ™T©ReMeTaL Signs A-Z.
 *    Copyright (C) 2025 ™T©ReMeTaL.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *
 *    You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *   ************************************************************************
 *     ScaleAnimation.java : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: ScaleAnimation.java
 *      Last Modified: 9/13/25, 5:36 PM
 *   ************************************************************************
 */

package com.toremetal.signs.IndicatorView.animation.type;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.toremetal.signs.IndicatorView.animation.controller.ValueController;
import com.toremetal.signs.IndicatorView.animation.data.type.ScaleAnimationValue;

public class ScaleAnimation extends ColorAnimation {

    public static final float DEFAULT_SCALE_FACTOR = 0.7f;
    public static final float MIN_SCALE_FACTOR = 0.3f;
    public static final float MAX_SCALE_FACTOR = 1;

    static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";
    static final String ANIMATION_SCALE = "ANIMATION_SCALE";

    int radius;
    float scaleFactor;

    private ScaleAnimationValue value;

    public ScaleAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
        value = new ScaleAnimationValue();
    }

    @NonNull
    @Override
    public ValueAnimator createAnimator() {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(BaseAnimation.DEFAULT_ANIMATION_TIME);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(animation);
            }
        });

        return animator;
    }

    @NonNull
    public ScaleAnimation with(int colorStart, int colorEnd, int radius, float scaleFactor) {
        if (animator != null && hasChanges(colorStart, colorEnd, radius, scaleFactor)) {

            this.colorStart = colorStart;
            this.colorEnd = colorEnd;

            this.radius = radius;
            this.scaleFactor = scaleFactor;

            PropertyValuesHolder colorHolder = createColorPropertyHolder(false);
            PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(true);

            PropertyValuesHolder scaleHolder = createScalePropertyHolder(false);
            PropertyValuesHolder scaleReverseHolder = createScalePropertyHolder(true);

            animator.setValues(colorHolder, reverseColorHolder, scaleHolder, scaleReverseHolder);
        }

        return this;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
        int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);

        int radius = (int) animation.getAnimatedValue(ANIMATION_SCALE);
        int radiusReverse = (int) animation.getAnimatedValue(ANIMATION_SCALE_REVERSE);

        value.setColor(color);
        value.setColorReverse(colorReverse);

        value.setRadius(radius);
        value.setRadiusReverse(radiusReverse);

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }

    @NonNull
    protected PropertyValuesHolder createScalePropertyHolder(boolean isReverse) {
        String propertyName;
        int startRadiusValue;
        int endRadiusValue;

        if (isReverse) {
            propertyName = ANIMATION_SCALE_REVERSE;
            startRadiusValue = radius;
            endRadiusValue = (int) (radius * scaleFactor);
        } else {
            propertyName = ANIMATION_SCALE;
            startRadiusValue = (int) (radius * scaleFactor);
            endRadiusValue = radius;
        }

        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startRadiusValue, endRadiusValue);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int colorStart, int colorEnd, int radiusValue, float scaleFactorValue) {
        if (this.colorStart != colorStart) {
            return true;
        }

        if (this.colorEnd != colorEnd) {
            return true;
        }

        if (radius != radiusValue) {
            return true;
        }

        if (scaleFactor != scaleFactorValue) {
            return true;
        }

        return false;
    }
}

