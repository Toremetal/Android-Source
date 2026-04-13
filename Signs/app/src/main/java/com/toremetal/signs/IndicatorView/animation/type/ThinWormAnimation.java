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
 *     ThinWormAnimation.java : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: ThinWormAnimation.java
 *      Last Modified: 9/13/25, 5:36 PM
 *   ************************************************************************
 */

package com.toremetal.signs.IndicatorView.animation.type;

import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.toremetal.signs.IndicatorView.animation.controller.ValueController;
import com.toremetal.signs.IndicatorView.animation.data.type.ThinWormAnimationValue;

public class ThinWormAnimation extends WormAnimation {

    private ThinWormAnimationValue value;

    public ThinWormAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
        value = new ThinWormAnimationValue();
    }

    @Override
    public ThinWormAnimation duration(long duration) {
        super.duration(duration);
        return this;
    }

    @Override
    public WormAnimation with(int coordinateStart, int coordinateEnd, int radius, boolean isRightSide) {
        if (hasChanges(coordinateStart, coordinateEnd, radius, isRightSide)) {
            animator = createAnimator();

            this.coordinateStart = coordinateStart;
            this.coordinateEnd = coordinateEnd;

            this.radius = radius;
            this.isRightSide = isRightSide;

            int height = radius * 2;
            rectLeftEdge = coordinateStart - radius;
            rectRightEdge = coordinateStart + radius;

            value.setRectStart(rectLeftEdge);
            value.setRectEnd(rectRightEdge);
            value.setHeight(height);

            RectValues rec = createRectValues(isRightSide);
            long sizeDuration = (long) (animationDuration * 0.8);
            long reverseDelay = (long) (animationDuration * 0.2);

            long heightDuration = (long) (animationDuration * 0.5);
            long reverseHeightDelay = (long) (animationDuration * 0.5);

            ValueAnimator straightAnimator = createWormAnimator(rec.fromX, rec.toX, sizeDuration, false, value);
            ValueAnimator reverseAnimator = createWormAnimator(rec.reverseFromX, rec.reverseToX, sizeDuration, true, value);
            reverseAnimator.setStartDelay(reverseDelay);

            ValueAnimator straightHeightAnimator = createHeightAnimator(height, radius, heightDuration);
            ValueAnimator reverseHeightAnimator = createHeightAnimator(radius, height, heightDuration);
            reverseHeightAnimator.setStartDelay(reverseHeightDelay);

            animator.playTogether(straightAnimator, reverseAnimator, straightHeightAnimator, reverseHeightAnimator);
        }
        return this;
    }

    private ValueAnimator createHeightAnimator(int fromHeight, int toHeight, long duration) {
        ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(animation);
            }
        });

        return anim;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        value.setHeight((int) animation.getAnimatedValue());

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }

    @Override
    public ThinWormAnimation progress(float progress) {
        if (animator != null) {
            long progressDuration = (long) (progress * animationDuration);
            int size = animator.getChildAnimations().size();

            for (int i = 0; i < size; i++) {
                ValueAnimator anim = (ValueAnimator) animator.getChildAnimations().get(i);

                long setDuration = progressDuration - anim.getStartDelay();
                long duration = anim.getDuration();

                if (setDuration > duration) {
                    setDuration = duration;

                } else if (setDuration < 0) {
                    setDuration = 0;
                }

                if (i == size - 1 && setDuration <= 0) {
                    continue;
                }

                if (anim.getValues() != null && anim.getValues().length > 0) {
                    anim.setCurrentPlayTime(setDuration);
                }
            }
        }

        return this;
    }
}