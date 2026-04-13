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
 *     DetailActivity.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: DetailActivity.kt
 *      Last Modified: 8/7/24, 11:51 PM
 *   ************************************************************************
 */

package com.toremetal.signs

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.toremetal.signs.MainActivity.MyNavView.errorReport
import com.toremetal.signs.databinding.ActivityDetailBinding

/**
 * Detail Activity displays individual images to the user.
 */
class DetailActivity : AppCompatActivity() {
    companion object {
        /**
         * val LETTER is used to display a custom title to the user.
         */
        const val LETTER: String = "letter"
        const val IMG: String = "IMG"
    }

    private fun getRes(id: Int?): Drawable {
        return ResourcesCompat.getDrawable(
            MainActivity.mainActivity.resources,
            id!!,
            null
        )!!.current
    }

    /**
     * The Initial entry point for the detail activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val letterId = intent?.extras?.getString(LETTER).toString()
        val img = intent?.extras?.getInt(IMG)
        if (img == 0) {
            binding.imageView2.isGone = true
            val imgUrl = intent?.data.toString()
            val myWebView = binding.webView
            myWebView.loadUrl(imgUrl)
            myWebView.contentDescription = letterId
            myWebView.settings.setSupportZoom(true)
            myWebView.settings.builtInZoomControls = true
            myWebView.settings.displayZoomControls = true
        } else {
            binding.webView.stopLoading()
            binding.webView.isGone = true
            binding.imageView2.setImageDrawable(getRes(img))//.srcCompat=getRes(img)
            binding.imageView2.isGone = false
        }
        val searchButton: FloatingActionButton = binding.button
        val searchText = "${getString(R.string.buttonText)}: $letterId"
        val searchQry = getString(R.string.searchPrefix, letterId)
        searchButton.contentDescription = searchText
        searchButton.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    /*type =
                        ContactsContract.CommonDataKinds.Website.MIMETYPE*/
                    data =
                        Uri.parse(searchQry)
                }
                Intent.createChooser(intent, "™T©ReMeTaL").apply {
                    it.context.startActivity(intent)
                }
            } catch (e: Exception) {
                errorReport(
                    errStr = "[DA-88]${e.message}",
                    errorDetails = e.stackTraceToString()
                )
            }
        }
        title = getString(R.string.detail_prefix) + " " + letterId.uppercase()
    }
}