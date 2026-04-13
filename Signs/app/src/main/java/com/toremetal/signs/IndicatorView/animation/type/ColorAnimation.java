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
 *     ColorAnimation.java : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: ColorAnimation.java
 *      Last Modified: 9/13/25, 5:36 PM
 *   ************************************************************************
 */

package com.toremetal.signs.IndicatorView.animation.type;

import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.toremetal.signs.IndicatorView.animation.controller.ValueController;
import com.toremetal.signs.IndicatorView.animation.data.type.ColorAnimationValue;

public class ColorAnimation extends BaseAnimation<ValueAnimator> {

    public static final String DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    public static final String DEFAULT_SELECTED_COLOR = "#ffffff";

    static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    static final String ANIMATION_COLOR = "ANIMATION_COLOR";

    private ColorAnimationValue value;

    int colorStart;
    int colorEnd;

    public ColorAnimation(@Nullable ValueController.UpdateListener listener) {
        super(listener);
        value = new ColorAnimationValue();
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
    public ColorAnimation progress(float progress) {
        if (animator != null) {
            long playTime = (long) (progress * animationDuration);

            if (animator.getValues() != null && animator.getValues().length > 0) {
                animator.setCurrentPlayTime(playTime);
            }
        }

        return this;
    }

    @NonNull
    public ColorAnimation with(int colorStart, int colorEnd) {
        if (animator != null && hasChanges(colorStart, colorEnd)) {

            this.colorStart = colorStart;
            this.colorEnd = colorEnd;

            PropertyValuesHolder colorHolder = createColorPropertyHolder(false);
            PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(true);

            animator.setValues(colorHolder, reverseColorHolder);
        }

        return this;
    }

    PropertyValuesHolder createColorPropertyHolder(boolean isReverse) {
        String propertyName;
        int colorStart;
        int colorEnd;

        if (isReverse) {
            propertyName = ANIMATION_COLOR_REVERSE;
            colorStart = this.colorEnd;
            colorEnd = this.colorStart;

        } else {
            propertyName = ANIMATION_COLOR;
            colorStart = this.colorStart;
            colorEnd = this.colorEnd;
        }

        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, colorStart, colorEnd);
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int colorStart, int colorEnd) {
        if (this.colorStart != colorStart) {
            return true;
        }

        if (this.colorEnd != colorEnd) {
            return true;
        }

        return false;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
        int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);

        value.setColor(color);
        value.setColorReverse(colorReverse);

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }
}
