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
 *     SettingsFragment.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: SettingsFragment.kt
 *      Last Modified: 8/7/24, 11:51 PM
 *   ************************************************************************
 */

package com.toremetal.signs.ui.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.toremetal.signs.*

/**
 * The SettingsFragment is used to maintain user preferred app state
 * for control selection state between app use and screen refresh.
 * As well as night mode (on/off) and debug data share preferences
 * DEBUG data: send/Don'tSend | Basic/detailed.
 */
class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var sharedPreferences: SharedPreferences// = PreferenceManager.getDefaultSharedPreferences(requireContext())

    /**
     * Initialize Settings UI.
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    /**
     * public open fun onResume():
     * Called when the fragment is visible to the user and actively running.
     * Overrides: [onResume] in class Fragment.
     *  Hide the translator function while displaying the settings menu.
     *   note: Works consistently placed in onResume.
     */
    override fun onResume() {
        hideTranslator()
        super.onResume()
    }

    /**
     * Reshow the translator functions.
     */
    @SuppressLint("ApplySharedPref")
    override fun onPause() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        if (sharedPreferences.getBoolean("nightModeBool", false)) {
            if (sharedPreferences.getInt(
                    "nightMode",
                    MODE_NIGHT_FOLLOW_SYSTEM
                ) == MODE_NIGHT_FOLLOW_SYSTEM
            ) {
                sharedPreferences.edit().putInt("nightMode", MODE_NIGHT_YES).commit()
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            }
        } else if (sharedPreferences.getInt(
                "nightMode",
                MODE_NIGHT_FOLLOW_SYSTEM
            ) != MODE_NIGHT_FOLLOW_SYSTEM
        ) {
            sharedPreferences.edit().putInt("nightMode", MODE_NIGHT_FOLLOW_SYSTEM).commit()
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
        }
        //showTranslator()
        super.onPause()
    }
}
