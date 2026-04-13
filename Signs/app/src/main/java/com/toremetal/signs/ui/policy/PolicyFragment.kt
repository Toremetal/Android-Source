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
 *     PolicyFragment.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: PolicyFragment.kt
 *      Last Modified: 8/7/24, 11:51 PM
 *   ************************************************************************
 */

package com.toremetal.signs.ui.policy

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.toremetal.signs.MainActivity
import com.toremetal.signs.R
import com.toremetal.signs.databinding.FragmentPolicyBinding
import com.toremetal.signs.hideTranslator

/**
 * In-App Privacy Policy Fragment.
 */
class PolicyFragment : Fragment() {
    private lateinit var policyViewModel: PolicyViewModel
    private var _binding: FragmentPolicyBinding? = null
    private val binding get() = _binding!!

    /**
     * Create setup.
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        policyViewModel =
            ViewModelProvider(this)[PolicyViewModel::class.java]
        _binding = FragmentPolicyBinding.inflate(inflater, container, false)
        val webViewer: WebView = binding.webView
        webViewer.loadUrl(getString(R.string.privacyPolicyLink))
        webViewer.webViewClient = MyWebViewClient()
        webViewer.settings.javaScriptEnabled = true
        return binding.root
    }

    /**
     * public open fun onResume():
     * Unit Description copied from class: androidx.fragment.app.Fragment
     * Called when the fragment is visible to the user and actively running.
     * This is generally tied to Activity.onResume of the containing Activity's lifecycle.
     * Overrides: [onResume] in class Fragment.
     *
     * [In this app Usage:]
     *
     * Hides the translator functions while displaying the settings menu.
     * Works consistently placed here. Since the view is only created once when the function
     * is placed in the [onCreate] the function does not run when resuming causing
     * the translator to display when switching between light and dark mode.
     */
    override fun onResume() {
        hideTranslator()
        super.onResume()
    }

    /**
     * Turns the translator buttons & text box back on.
     */
    /*override fun onPause() {
        showTranslator()
        super.onPause()
    }*/

    /**
     * Release resources associated with the view.
     */
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

/**
 * Override non site URL loading for java security.
 */
private class MyWebViewClient : WebViewClient() {
    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (Uri.parse(url).host == "toremetal.github.io") {
            return false
        }
        if (Uri.parse(url).host == "toremetal.com") {
            return false
        }
        try {
            view?.context?.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
            )
            return true
        } catch (e: Exception) {
            MainActivity.errorReport(
                errStr = "[PF-104]${e.message}",
                errorDetails = e.stackTraceToString()
            )
        }
        return false
    }
}