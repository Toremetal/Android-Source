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
 *     WormDrawer.java : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: WormDrawer.java
 *      Last Modified: 9/13/25, 5:36 PM
 *   ************************************************************************
 */

package com.toremetal.signs.IndicatorView.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import com.toremetal.signs.IndicatorView.animation.data.Value;
import com.toremetal.signs.IndicatorView.animation.data.type.WormAnimationValue;
import com.toremetal.signs.IndicatorView.draw.data.Indicator;
import com.toremetal.signs.IndicatorView.draw.data.Orientation;

public class WormDrawer extends BaseDrawer {

    public RectF rect;

    public WormDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);
        rect = new RectF();
    }

    public void draw(
            @NonNull Canvas canvas,
            @NonNull Value value,
            int coordinateX,
            int coordinateY) {

        if (!(value instanceof WormAnimationValue)) {
            return;
        }

        WormAnimationValue v = (WormAnimationValue) value;
        int rectStart = v.getRectStart();
        int rectEnd = v.getRectEnd();

        int radius = indicator.getRadius();
        int unselectedColor = indicator.getUnselectedColor();
        int selectedColor = indicator.getSelectedColor();

        if (indicator.getOrientation() == Orientation.HORIZONTAL) {
            rect.left = rectStart;
            rect.right = rectEnd;
            rect.top = coordinateY - radius;
            rect.bottom = coordinateY + radius;

        } else {
            rect.left = coordinateX - radius;
            rect.right = coordinateX + radius;
            rect.top = rectStart;
            rect.bottom = rectEnd;
        }

        paint.setColor(unselectedColor);
        canvas.drawCircle(coordinateX, coordinateY, radius, paint);

        paint.setColor(selectedColor);
        canvas.drawRoundRect(rect, radius, radius, paint);
    }
}
