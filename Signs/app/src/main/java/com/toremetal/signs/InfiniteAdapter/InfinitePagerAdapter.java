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
 *     InfinitePagerAdapter.java : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: InfinitePagerAdapter.java
 *      Last Modified: 9/13/25, 5:36 PM
 *   ************************************************************************
 */

package com.toremetal.signs.InfiniteAdapter;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.toremetal.signs.SliderViewAdapter;


/**
 * Its just a wrapper adapter class for providing infinite behavior
 * for slider.
 */
public class InfinitePagerAdapter extends PagerAdapter {

    // Warning: it should be an even number.
    public static final int INFINITE_SCROLL_LIMIT = 32400;
    private static final String TAG = "InfinitePagerAdapter";
    private SliderViewAdapter adapter;

    public InfinitePagerAdapter(SliderViewAdapter adapter) {
        this.adapter = adapter;
    }

    public PagerAdapter getRealAdapter() {
        return this.adapter;
    }

    @Override
    public int getCount() {
        if (getRealCount() < 1) {
            return 0;
        }
        // warning: infinite scroller actually is not infinite!
        // very big number will be cause memory problems.
        return getRealCount() * INFINITE_SCROLL_LIMIT;
    }

    /**
     * @return the {@link #getCount()} result of the wrapped adapter
     */
    public int getRealCount() {
        try {
            return getRealAdapter().getCount();
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * @param item real position of item
     * @return virtual mid point
     */
    public int getMiddlePosition(int item) {
       int midpoint = Math.max(0, getRealCount()) * (InfinitePagerAdapter.INFINITE_SCROLL_LIMIT / 2);
        return item + midpoint;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int virtualPosition) {
        // prevent division by zer
        if (getRealCount() < 1) {
            return adapter.instantiateItem(container, 0);
        }
        //Log.i(TAG, "instantiateItem: real virtualPosition: " + virtualPosition);
        //Log.i(TAG, "instantiateItem: virtual virtualPosition: " + virtualPosition);

        // only expose virtual virtualPosition to the inner adapter
        return adapter.instantiateItem(container, getRealPosition(virtualPosition));
    }

    @Override
    public void destroyItem(ViewGroup container, int virtualPosition, Object object) {
        // prevent division by zero
        if (getRealCount() < 1) {
            adapter.destroyItem(container, 0, object);
            return;
        }
        //Log.i(TAG, "destroyItem: real position: " + position);
        //Log.i(TAG, "destroyItem: virtual position: " + virtualPosition);

        // only expose virtual position to the inner adapter
        adapter.destroyItem(container, getRealPosition(virtualPosition), object);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        adapter.startUpdate(container);
    }

    /*
     * Delegate rest of methods directly to the inner adapter.
     */
    @Override
    public void finishUpdate(ViewGroup container) {
        adapter.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return adapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        adapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return adapter.saveState();
    }

    @Override
    public CharSequence getPageTitle(int virtualPosition) {
        return adapter.getPageTitle(getRealPosition(virtualPosition));
    }

    @Override
    public float getPageWidth(int position) {
        return adapter.getPageWidth(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        adapter.setPrimaryItem(container, position, object);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        adapter.unregisterDataSetObserver(observer);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public int getItemPosition(Object object) {
        return adapter.getItemPosition(object);
    }

    public int getRealPosition(int virtualPosition) {
        if (getRealCount() > 0) {
            return virtualPosition % getRealCount();
        }
        return 0;
    }
}