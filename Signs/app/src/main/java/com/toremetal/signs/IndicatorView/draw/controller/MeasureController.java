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
 *     MeasureController.java : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: MeasureController.java
 *      Last Modified: 9/13/25, 5:36 PM
 *   ************************************************************************
 */

package com.toremetal.signs.IndicatorView.draw.controller;

import androidx.annotation.NonNull;
import android.util.Pair;
import android.view.View;
import com.toremetal.signs.IndicatorView.animation.type.IndicatorAnimationType;
import com.toremetal.signs.IndicatorView.draw.data.Indicator;
import com.toremetal.signs.IndicatorView.draw.data.Orientation;

public class MeasureController {

    public Pair<Integer, Integer> measureViewSize(@NonNull Indicator indicator, int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        int count = indicator.getCount();
        int radius = indicator.getRadius();
        int stroke = indicator.getStroke();

        int padding = indicator.getPadding();
        int paddingLeft = indicator.getPaddingLeft();
        int paddingTop = indicator.getPaddingTop();
        int paddingRight = indicator.getPaddingRight();
        int paddingBottom = indicator.getPaddingBottom();

        int circleDiameterPx = radius * 2;
        int desiredWidth = 0;
        int desiredHeight = 0;

        int width;
        int height;

        Orientation orientation = indicator.getOrientation();
        if (count != 0) {
            int diameterSum = circleDiameterPx * count;
            int strokeSum = (stroke * 2) * count;

            int paddingSum = padding * (count - 1);
            int w = diameterSum + strokeSum + paddingSum;
            int h = circleDiameterPx + stroke;

            if (orientation == Orientation.HORIZONTAL) {
                desiredWidth = w;
                desiredHeight = h;

            } else {
                desiredWidth = h;
                desiredHeight = w;
            }
        }

        if (indicator.getAnimationType() == IndicatorAnimationType.DROP) {
            if (orientation == Orientation.HORIZONTAL) {
                desiredHeight *= 2;
            } else {
                desiredWidth *= 2;
            }
        }

        int horizontalPadding = paddingLeft + paddingRight;
        int verticalPadding = paddingTop + paddingBottom;

        if (orientation == Orientation.HORIZONTAL) {
            desiredWidth += horizontalPadding;
            desiredHeight += verticalPadding;

        } else {
            desiredWidth += horizontalPadding;
            desiredHeight += verticalPadding;
        }

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        if (width < 0) {
            width = 0;
        }

        if (height < 0) {
            height = 0;
        }

        indicator.setWidth(width);
        indicator.setHeight(height);

        return new Pair<>(width, height);
    }
}
