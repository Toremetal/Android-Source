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
 *     SliderAdapter.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: SliderAdapter.kt
 *      Last Modified: 9/13/25, 5:37 PM
 *   ************************************************************************
 */

package com.toremetal.signs.ui.slider

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.toremetal.signs.R
import com.toremetal.signs.SliderViewAdapter

/**
 * [SliderAdapter]:
 * ### Image Change Manager to translate text to Image.
 */
class SliderAdapter(private val sliderList: List<Drawable?>) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    /**
     *  Create a class container for the slider view holder.
     */
    class SliderViewHolder(ItemView: View) : ViewHolder(ItemView) {
        val sliderIV: ImageView = itemView.findViewById(R.id.idIVSliderItem)
    }

    /**
     *  List Count getter().
     */
    override fun getCount(): Int {
        return sliderList.size
    }

    /**
     * Create the view Container.
     */
    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        val itemView: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.fragment_item, null)
        return SliderViewHolder(itemView)
    }

    /**
     * Load images to display in imageView,
     * from drawable or url, using the Glide library.
     */
    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        Glide.with(viewHolder!!.itemView).load(sliderList.get(position)).fitCenter()
            .into(viewHolder.sliderIV)
    }
}