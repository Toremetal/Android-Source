/*
 *    Copyright (C) 2022 ™T©ReMeTaL.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *
 *    Some functionality created with modified (code) lessons provided by:
 *    The Android Open Source Project.
 *    Copyright (C) 2022 The Android Open Source Project.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *   ************************************************************************
 *     WebActivity.kt : Copyright (c) 2022 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 8/23/22, 6:04 AM
 *      Program Name: PurchaseTracker.main
 *      File: WebActivity.kt
 *      Last Modified: 8/23/22, 5:57 AM
 *   ************************************************************************
 */

package com.toremetal.purchasetracker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.toremetal.purchasetracker.databinding.ActivityWebBinding

/**
 * [WebActivity] is a Full-Screen web viewer  for viewing the privacy policy and website.
 */
@Keep
class WebActivity : AppCompatActivity() {

    private var _binding: ActivityWebBinding? = null
    private val binding get() = _binding!!

    /**
     * [onDestroy] is an attempt to release any memory resources used by the activity.
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        /**
         * val [MY_TITLE] is used to display a custom title to the user.
         */
        const val MY_TITLE: String = "Privacy Policy"
    }

    /**
     * [onCreate] is the initialization of the web viewer activity.
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.root.rootView.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            @Suppress("DEPRECATION")
            binding.root.rootView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        val pgTitle = intent?.extras?.getString(MY_TITLE).toString()
        val imgUrl = intent?.data.toString()
        title = pgTitle
        val webViewer: WebView = binding.webView//findViewById(R.id.webView)
        webViewer.loadUrl(imgUrl)
        webViewer.webViewClient = MyWebViewClient()
        webViewer.settings.javaScriptEnabled = true
    }
}

@Keep
private class MyWebViewClient : WebViewClient() {

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (Uri.parse(url).host == "www.toremetal.com") {
            // This is my web site, so do not override; let my WebView load the page
            return false
        }
        if (Uri.parse(url).host == "toremetal.com") {
            // This is my web site, so do not override; let my WebView load the page
            return false
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        view?.context?.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
        return true
    }
}