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
 *     BasicDrawer.java : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: BasicDrawer.java
 *      Last Modified: 9/13/25, 5:36 PM
 *   ************************************************************************
 */

package com.toremetal.signs.IndicatorView.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import com.toremetal.signs.IndicatorView.animation.type.IndicatorAnimationType;
import com.toremetal.signs.IndicatorView.draw.data.Indicator;

public class BasicDrawer extends BaseDrawer {

    private Paint strokePaint;

    public BasicDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(indicator.getStroke());
    }

    public void draw(
            @NonNull Canvas canvas,
            int position,
            boolean isSelectedItem,
            int coordinateX,
            int coordinateY) {

        float radius = indicator.getRadius();
        int strokePx = indicator.getStroke();
        float scaleFactor = indicator.getScaleFactor();

        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int selectedPosition = indicator.getSelectedPosition();
        IndicatorAnimationType animationType = indicator.getAnimationType();

		if (animationType == IndicatorAnimationType.SCALE && !isSelectedItem) {
			radius *= scaleFactor;

		} else if (animationType == IndicatorAnimationType.SCALE_DOWN && isSelectedItem) {
			radius *= scaleFactor;
		}

        int color = unselectedColor;
        if (position == selectedPosition) {
            color = selectedColor;
        }

        Paint paint;
        if (animationType == IndicatorAnimationType.FILL && position != selectedPosition) {
            paint = strokePaint;
            paint.setStrokeWidth(strokePx);
        } else {
            paint = this.paint;
        }

        paint.setColor(color);
        canvas.drawCircle(coordinateX, coordinateY, radius, paint);
    }
}
