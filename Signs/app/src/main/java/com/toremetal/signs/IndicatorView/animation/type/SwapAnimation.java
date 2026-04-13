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
 *     SwapAnimation.java : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: SwapAnimation.java
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
import com.toremetal.signs.IndicatorView.animation.data.type.SwapAnimationValue;

public class SwapAnimation extends BaseAnimation<ValueAnimator> {

    private static final String ANIMATION_COORDINATE = "ANIMATION_COORDINATE";
    private static final String ANIMATION_COORDINATE_REVERSE = "ANIMATION_COORDINATE_REVERSE";
    private static final int COORDINATE_NONE = -1;

    private int coordinateStart = COORDINATE_NONE;
    private int coordinateEnd = COORDINATE_NONE;

    private SwapAnimationValue value;

    public SwapAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
        value = new SwapAnimationValue();
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

    @Override
    public SwapAnimation progress(float progress) {
        if (animator != null) {
            long playTime = (long) (progress * animationDuration);

            if (animator.getValues() != null && animator.getValues().length > 0) {
                animator.setCurrentPlayTime(playTime);
            }
        }

        return this;
    }

    @NonNull
    public SwapAnimation with(int coordinateStart, int coordinateEnd) {
        if (animator != null && hasChanges(coordinateStart, coordinateEnd)) {
            this.coordinateStart = coordinateStart;
            this.coordinateEnd = coordinateEnd;

            PropertyValuesHolder holder = createColorPropertyHolder(ANIMATION_COORDINATE, coordinateStart, coordinateEnd);
            PropertyValuesHolder holderReverse = createColorPropertyHolder(ANIMATION_COORDINATE_REVERSE, coordinateEnd, coordinateStart);
            animator.setValues(holder, holderReverse);
        }

        return this;
    }

    private PropertyValuesHolder createColorPropertyHolder(String propertyName, int startValue, int endValue) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startValue, endValue);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int coordinate = (int) animation.getAnimatedValue(ANIMATION_COORDINATE);
        int coordinateReverse = (int) animation.getAnimatedValue(ANIMATION_COORDINATE_REVERSE);

        value.setCoordinate(coordinate);
        value.setCoordinateReverse(coordinateReverse);

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int coordinateStart, int coordinateEnd) {
        if (this.coordinateStart != coordinateStart) {
            return true;
        }

        if (this.coordinateEnd != coordinateEnd) {
            return true;
        }

        return false;
    }
}
