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
 *     PositionSavedState.java : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: PositionSavedState.java
 *      Last Modified: 9/13/25, 5:36 PM
 *   ************************************************************************
 */

package com.toremetal.signs.IndicatorView.draw.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class PositionSavedState extends View.BaseSavedState {

    private int selectedPosition;
    private int selectingPosition;
    private int lastSelectedPosition;

    public PositionSavedState(Parcelable superState) {
        super(superState);
    }

    private PositionSavedState(Parcel in) {
        super(in);
        this.selectedPosition = in.readInt();
        this.selectingPosition = in.readInt();
        this.lastSelectedPosition = in.readInt();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectingPosition() {
        return selectingPosition;
    }

    public void setSelectingPosition(int selectingPosition) {
        this.selectingPosition = selectingPosition;
    }

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }

    public void setLastSelectedPosition(int lastSelectedPosition) {
        this.lastSelectedPosition = lastSelectedPosition;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(this.selectedPosition);
        out.writeInt(this.selectingPosition);
        out.writeInt(this.lastSelectedPosition);
    }

    public static final Creator<PositionSavedState> CREATOR = new Creator<PositionSavedState>() {
        public PositionSavedState createFromParcel(Parcel in) {
            return new PositionSavedState(in);
        }

        public PositionSavedState[] newArray(int size) {
            return new PositionSavedState[size];
        }
    };
}
