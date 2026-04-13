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
 *     FillDrawer.java : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: FillDrawer.java
 *      Last Modified: 9/13/25, 5:36 PM
 *   ************************************************************************
 */

package com.toremetal.signs.IndicatorView.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import com.toremetal.signs.IndicatorView.animation.data.Value;
import com.toremetal.signs.IndicatorView.animation.data.type.FillAnimationValue;
import com.toremetal.signs.IndicatorView.draw.data.Indicator;

public class FillDrawer extends BaseDrawer {

    private Paint strokePaint;

    public FillDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
    }

    public void draw(
            @NonNull Canvas canvas,
            @NonNull Value value,
            int position,
            int coordinateX,
            int coordinateY) {

        if (!(value instanceof FillAnimationValue)) {
            return;
        }

        FillAnimationValue v = (FillAnimationValue) value;
        int color = indicator.getUnselectedColor();
        float radius = indicator.getRadius();
        int stroke = indicator.getStroke();

        int selectedPosition = indicator.getSelectedPosition();
        int selectingPosition = indicator.getSelectingPosition();
        int lastSelectedPosition = indicator.getLastSelectedPosition();

        if (indicator.isInteractiveAnimation()) {
            if (position == selectingPosition) {
                color = v.getColor();
                radius = v.getRadius();
                stroke = v.getStroke();

            } else if (position == selectedPosition) {
                color = v.getColorReverse();
                radius = v.getRadiusReverse();
                stroke = v.getStrokeReverse();
            }

        } else {
            if (position == selectedPosition) {
                color = v.getColor();
                radius = v.getRadius();
                stroke = v.getStroke();

            } else if (position == lastSelectedPosition) {
                color = v.getColorReverse();
                radius = v.getRadiusReverse();
                stroke = v.getStrokeReverse();
            }
        }

        strokePaint.setColor(color);
        strokePaint.setStrokeWidth(indicator.getStroke());
        canvas.drawCircle(coordinateX, coordinateY, indicator.getRadius(), strokePaint);

        strokePaint.setStrokeWidth(stroke);
        canvas.drawCircle(coordinateX, coordinateY, radius, strokePaint);
    }
}
