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
 *     BindingAdapters.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: BindingAdapters.kt
 *      Last Modified: 8/7/24, 11:51 PM
 *   ************************************************************************
 */

package com.toremetal.signs

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.toremetal.signs.network.SignsPhoto
import com.toremetal.signs.ui.home.SignsApiStatus

/**
 * Updates the data shown in the [RecyclerView].
 * ### URL List handler [SignsPhoto].
 */
@Keep
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<SignsPhoto>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}

/**
 * Updates the data shown in the [RecyclerView].
 * ### URL List handler [SignsPhotos].
 */
@Keep
@BindingAdapter("listDatas")
fun bindsRecyclerView(recyclerView: RecyclerView, data: List<SignsPhotos>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter2
    adapter.submitList(data)
}

/**
 * Uses the Coil library to load an image by URL into an [ImageView]
 */
@Keep
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

/**
 * Returns the drawable matching the given [id].
 */
@Keep
private fun getRes(id: Int): Drawable {
    return ResourcesCompat.getDrawable(MainActivity.mainActivity.resources, id, null)!!.current
}

/**
 * Uses the Coil library to load an image by [Drawable] into an [ImageView]
 */
@Keep
@BindingAdapter("srcCompat")
fun bindImage(imgView: ImageView, img: Int?) {
    img?.let {
        imgView.load(getRes(img)) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

/**
 * This binding adapter displays the [SignsApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@Keep
@BindingAdapter("signsApiStatus")
fun bindStatus(statusImageView: ImageView, status: SignsApiStatus?) {
    when (status) {
        SignsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }

        SignsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }

        SignsApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }

        else -> {
            statusImageView.visibility = View.GONE
        }
    }
}
