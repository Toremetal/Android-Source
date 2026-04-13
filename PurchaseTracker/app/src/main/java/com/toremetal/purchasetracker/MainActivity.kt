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
 *     MainActivity.kt : Copyright (c) 2022 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 8/23/22, 6:04 AM
 *      Program Name: PurchaseTracker.main
 *      File: MainActivity.kt
 *      Last Modified: 8/23/22, 5:57 AM
 *   ************************************************************************
 */

package com.toremetal.purchasetracker

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled
import com.facebook.FacebookSdk.setAutoLogAppEventsEnabled
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.RequestConfiguration.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.ump.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.toremetal.purchasetracker.WebActivity.Companion.MY_TITLE
import com.toremetal.purchasetracker.data.checkUpdate
import com.toremetal.purchasetracker.data.grandTotal
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.NumberFormat

/**
 * [mainActivity] Provides a handle to the root-view for functions access
 */
lateinit var mainActivity: MainActivity
//var gdprApplies: Int = 0

/** Main Class for App Activity. */
class MainActivity : AppCompatActivity() {
    /*
    // Test Crash function for crash analytics.
    fun crash() {
        // Force App-Crash
        throw RuntimeException("Test Crash")
    }
    */

    private lateinit var analytics: FirebaseAnalytics

    /**
     * function [getTestDeviceIdFromLogOutPut]
     * (AdMob UserMessagingPlatform Consent Form.)
     * Extracts the device test ID# to run in debug mode to retrieve
     * the Data Consent Form otherwise an error is returned stating:
     *
     *          No available form can be built.
     *
     * (This functions works automatically using)
     *
     * { it: FormError
     *    // Handle the error.
     *
     *    if (it.message.toString() == "No available form can be built.") {
     *       getTestDeviceIdFromLogOutPut()
     *    }
     * }
     * */
    private fun getTestDeviceIdFromLogOutPut(): String {
        var testDeviceId = ""
        try {
            val process = Runtime.getRuntime().exec("logcat")
            val bufferedReader = BufferedReader(
                InputStreamReader(process.inputStream)
            )
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                if (line.toString().length != line.toString().replace(
                        "ConsentDebugSettings.Builder().addTestDeviceHashedId(",
                        ""
                    ).length
                ) {
                    val newStr =
                        line.toString()
                    val removeA =
                        "I/UserMessagingPlatform: Use new ConsentDebugSettings.Builder().addTestDeviceHashedId("
                    val removeB = ") to set this as a debug device."
                    var remove = ""
                    for (chr in newStr) {
                        if (chr == '"') {
                            testDeviceId = line.toString().replace(remove, "").replace(removeA, "")
                                .replace(removeB, "").replace('"', '-').replace("-", "")
                            break
                        } else {
                            remove += chr
                        }
                    }
                }
                if (testDeviceId != "") {
                    break
                }
            }
        } catch (e: IOException) {
            Toast.makeText(
                this,
                "No available form can be found. ${e.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (testDeviceId != "") {
            val debugSettings = ConsentDebugSettings.Builder(this)
                .setDebugGeography(
                    ConsentDebugSettings
                        .DebugGeography
                        .DEBUG_GEOGRAPHY_EEA
                )
                .addTestDeviceHashedId(testDeviceId)
                .build()
            val mParams = ConsentRequestParameters
                .Builder()
                .setConsentDebugSettings(debugSettings)
                .build()
            consentInformation.requestConsentInfoUpdate(
                this,
                mParams,
                {
                    // Attempt to loadForm after setting test ID.
                    if (consentInformation.isConsentFormAvailable) {
                        loadForm()
                    }
                },
                {
                    // Handle the error.
                    Toast.makeText(
                        this,
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
        return testDeviceId
        // String return can be enabled if the device ID is needed for future reference.
        // Its not required for the function to work. To enable uncomment the
        // (:String) declaration & the (return testDeviceId).
    }

    private var en = 0

    private var itemCountStr = "1"

    private val entrees = mutableListOf<String>()

    private lateinit var mAdView: AdView

    private lateinit var consentInformation: ConsentInformation

    private var consentForm: ConsentForm? = null

    /**   Capture local currency symbol to translate for math formulas.
     *  (Capture device local currency to allow International translation).
     *      Implied Algorithm:.
     *    '{formatCurrency.replace(currencySymbol).replace("commas").replace(".00").toDouble()}'. */
    private val moneySym: String =
        NumberFormat.getCurrencyInstance().format(0.0).toString().replace(".", "").replace("0", "")

    private fun showForm() {
        UserMessagingPlatform.loadConsentForm(
            this,
            { consentForm ->
                this.consentForm = consentForm
                consentForm.show(this) {
                    loadForm()
                }
            },
            {
                //if (findViewById<TextInputEditText>(R.id.quantity).text.toString()=="-69") {
                if (BuildConfig.DEBUG) {
                    if (it.message.toString() == "No available form can be built.") {
                        messageUser(
                            mes = "Activating Consent-Form Retrieval-Mode. Attempting to setTestDeviceId.",
                            4000
                        )
                        Handler(Looper.getMainLooper()).postDelayed({
                            getTestDeviceIdFromLogOutPut()
                        }, 5000)
                    }
                    //} else {
                    //messageUser("Admob Consent Form doesn't work on published apps!")
                    //}
                } else {
                    messageUser("Consent Form ERROR! [${it.message}]", 5000)
                }

            }
        )
    }

    private fun loadForm() {
        UserMessagingPlatform.loadConsentForm(
            this,
            { consentForm ->
                this.consentForm = consentForm
                when (consentInformation.consentStatus) {
                    ConsentInformation.ConsentStatus.REQUIRED -> {
                        consentForm.show(this) {
                            if (it?.message.toString().isEmpty()) {
                                loadForm()
                            } else {
                                FacebookSdk.setAutoInitEnabled(true)
                                loadAds("1")
                            }
                        }
                    }
                    ConsentInformation.ConsentStatus.UNKNOWN -> {
                        consentForm.show(this) {
                            if (it?.message.toString().isEmpty()) {
                                loadForm()
                            } else {
                                FacebookSdk.setAutoInitEnabled(true)
                                loadAds("1")
                            }
                        }
                    }
                    ConsentInformation.ConsentStatus.NOT_REQUIRED -> {
                        consentForm.show(this) {
                            if (it?.message.toString().isEmpty()) {
                                loadForm()
                            } else {
                                setAdvertiserIDCollectionEnabled(true)
                                FacebookSdk.setAutoInitEnabled(true)
                                loadAds()
                            }
                        }
                    }
                    ConsentInformation.ConsentStatus.OBTAINED -> {
                        setAdvertiserIDCollectionEnabled(true)
                        FacebookSdk.setAutoInitEnabled(true)
                        loadAds()
                    }
                    else -> {
                        consentForm.show(this) {
                            if (it?.message.toString().isEmpty()) {
                                loadForm()
                            } else {
                                loadAds("1")
                            }
                        }
                    }
                }
            },
            {
                loadAds("1")
            }
        )
    }

    private fun getConsentFromMyUser() {
        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        val params = ConsentRequestParameters
            .Builder()
            .build()
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                when (consentInformation.consentStatus) {
                    ConsentInformation.ConsentStatus.REQUIRED -> {
                        if (consentInformation.isConsentFormAvailable) {
                            loadForm()
                        } else {
                            FacebookSdk.setAutoInitEnabled(true)
                            loadAds("1")
                        }
                    }
                    ConsentInformation.ConsentStatus.UNKNOWN -> {
                        if (consentInformation.isConsentFormAvailable) {
                            loadForm()
                        } else {
                            FacebookSdk.setAutoInitEnabled(true)
                            loadAds("1")
                        }
                    }
                    ConsentInformation.ConsentStatus.NOT_REQUIRED -> {
                        setAutoLogAppEventsEnabled(true) // If disabled by manifest
                        setAdvertiserIDCollectionEnabled(true)
                        FacebookSdk.setAutoInitEnabled(true)
                        if (consentInformation.isConsentFormAvailable) {
                            loadForm()
                        } else {
                            loadAds()
                        }
                    }
                    ConsentInformation.ConsentStatus.OBTAINED -> {
                        //if (consentInformation.consentStatus) {}
                        setAutoLogAppEventsEnabled(true)
                        setAdvertiserIDCollectionEnabled(true)
                        FacebookSdk.setAutoInitEnabled(true)
                        loadAds()
                    }
                    else -> {
                        if (consentInformation.isConsentFormAvailable) {
                            loadForm()
                        } else {
                            loadAds("1")
                        }
                    }
                }
            },
            {
                loadAds("1")
            }
        )
    }

    /**
     * [onDestroy] is an attempt to release any memory resources used by the activity.
     */
    override fun onDestroy() {
        mainActivity.releaseInstance()
        fbAdView?.destroy()
        super.onDestroy()
    }

    private fun analyticsEvent(eventId: String, eventType: String) {
        analytics.logEvent(eventType) {
            param(FirebaseAnalytics.Param.ITEM_ID, eventId)
        }
    }

    private var fbAdView: com.facebook.ads.AdView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkUpdate) {
            checkUpdate = false
            //if (sharedPreferences.getBoolean("checkUpdate", true)) {
            /** Requests the update availability for the current app, an intent to start
             * an update flow, and, if applicable, the state of updates currently in progress.*/
            val appUpdateManager = AppUpdateManagerFactory.create(this)
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            // Action when the platform has an update. //AppUpdateType.FLEXIBLE AppUpdateType.IMMEDIATE
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                        AppUpdateType.IMMEDIATE
                    )
                ) {
                    // AppUpdateType.FLEXIBLE
                    // Register the listener to initiate the FLEXIBLE update.
                    /*val listener = { state: InstallState ->
                        state.installStatus()
                        if (state.installStatus() == InstallStatus.DOWNLOADED) {
                            //if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            // After the update is downloaded, show a notification
                            // and request user confirmation to restart the app.
                            // Display the snackbar notification and call to action.
                            Snackbar.make(
                                binding.root,
                                getString(R.string.updateComplete),
                                Snackbar.LENGTH_INDEFINITE
                            ).apply {
                                setAction(getString(R.string.restart)) { appUpdateManager.completeUpdate() }
                                setBackgroundTint(
                                    ContextCompat.getColor(
                                        this.context,
                                        R.color.SnackBarBackground
                                    )
                                )
                                setActionTextColor(
                                    ContextCompat.getColor(
                                        this.context,
                                        R.color.SnackBarActionText
                                    )
                                )
                                setTextColor(
                                    ContextCompat.getColor(
                                        this.context,
                                        R.color.SnackBarText
                                    )
                                )
                                animationMode = ANIMATION_MODE_SLIDE
                                show()
                            }
                        }

                    }
                    appUpdateManager.registerListener(listener)*/
                    // Don't use for IMMEDIATE. (doubles the update screen on finish.)
                    // .setAllowAssetPackDeletion(true).build()
                    // AppUpdateType.IMMEDIATE AppUpdateType.FLEXIBLE
                    appUpdateManager.startUpdateFlow(
                        appUpdateInfo, this, AppUpdateOptions.newBuilder(
                            AppUpdateType.IMMEDIATE
                        )
                            .build()
                    )
                }
            }
            //}
        }
        analytics = Firebase.analytics
        analyticsEvent(packageName, FirebaseAnalytics.Event.APP_OPEN)
        /*analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
            param(FirebaseAnalytics.Param.ITEM_ID, packageName)
        }*/
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        //gdprApplies = sharedPreferences.getInt("IABTCF_gdprApplies", 0)
        //sharedPreferences.getString("IABTCF_TCString", "1111").toString()
        if (sharedPreferences.getInt("nightMode", MODE_NIGHT_NO) == MODE_NIGHT_NO) {
            setDefaultNightMode(
                MODE_NIGHT_NO
            )
        } else if (sharedPreferences.getInt("nightMode", MODE_NIGHT_NO) == MODE_NIGHT_YES) {
            setDefaultNightMode(
                MODE_NIGHT_YES
            )
        }
        setContentView(R.layout.activity_main)
        mainActivity = this
        findViewById<MaterialTextView>(R.id.service_question).setOnClickListener {
            findViewById<LinearLayout>(R.id.options_layout).isGone =
                !findViewById<LinearLayout>(R.id.options_layout).isGone
            findViewById<SwitchMaterial>(R.id.round_up_switch).isGone =
                !findViewById<SwitchMaterial>(R.id.round_up_switch).isGone
            findViewById<MaterialButton>(R.id.calculate_button).isGone =
                !findViewById<MaterialButton>(R.id.calculate_button).isGone
            findViewById<MaterialTextView>(R.id.tip_result).isGone =
                !findViewById<MaterialTextView>(R.id.tip_result).isGone
            findViewById<LinearLayout>(R.id.options_layout).isGone =
                !findViewById<LinearLayout>(R.id.options_layout).isGone
            /* Testing Crash-analytics
                crash()
             * Testing Crash-analytics */
        }
        val vName =
            packageManager.getPackageInfo(packageName, 0).versionName.replace("android-", "")
        findViewById<MaterialTextView>(R.id.version).text =
            getString(R.string.version_display_name, vName)
        val c = java.util.Calendar.getInstance()
        val copyright = getString(
            R.string.app_copyright,
            c.get(java.util.Calendar.YEAR).toString()
        )
        findViewById<MaterialTextView>(R.id.copyright).text = copyright
        findViewById<MaterialTextView>(R.id.copyright).setOnClickListener { goToWebsite() }
        findViewById<AppCompatButton>(R.id.calculate_button).setOnClickListener { calculateTip() }
        findViewById<SwitchCompat>(R.id.round_up_switch).setOnClickListener { calculateTip() }
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            addIt(
                findViewById<FloatingActionButton>(R.id.floatingActionButton)
            )
        }
        findViewById<FloatingActionButton>(R.id.saveTheTax).setOnClickListener { saveTax() }
        findViewById<FloatingActionButton>(R.id.refScreen).setOnClickListener { resetScreen() }
        findViewById<MaterialTextView>(R.id.list).setOnClickListener {
            if (entrees.size > 0) {
                if (en < (entrees.size - 1)) {
                    en++
                } else {
                    en = 0
                }
                findViewById<MaterialTextView>(R.id.list).text = entrees[en]
            }
        }
        /*   Retrieve saved data. */
        if (getFileStreamPath("itemCount.file").isFile) {
            val openFile = openFileInput("itemCount.file")
            itemCountStr = try {
                openFile.reader().readText().trimMargin()
            } catch (e: Exception) {
                "1"
            }
            openFile.close()
        }
        if (getFileStreamPath("taxRate.file").isFile) {
            val openFile = openFileInput("taxRate.file")
            findViewById<TextInputEditText>(R.id.editTextTaxRate).setText(
                openFile.reader().readText()
            )
            openFile.close()
        }
        /*  format the viewable subtotal as currency. */
        val subTotalText = findViewById<MaterialTextView>(R.id.subTotal)
        var results = ""
        if (getFileStreamPath("subtotal.file").isFile) {
            val openFile = openFileInput("subtotal.file")
            results = openFile.reader().readText()
            openFile.close()
        }
        if (results == "") {
            results = "0.0"
        }
        var formattedTip = money(results)
        subTotalText.text = getString(R.string.subtotal, formattedTip)
        /* format the viewable tax as currency. */
        val taxText = findViewById<MaterialTextView>(R.id.tax)
        results = ""
        if (getFileStreamPath("tax.file").isFile) {
            val openFile = openFileInput("tax.file")
            results = openFile.reader().readText()
            openFile.close()
        }
        if (results == "") {
            results = "0.0"
        }
        formattedTip = money(results)
        taxText.text = getString(R.string.tax, formattedTip)
        /* format the viewable total as currency. */
        val total = findViewById<MaterialTextView>(R.id.total)
        results = ""
        if (getFileStreamPath("total.file").isFile) {
            val openFile = openFileInput("total.file")
            results = openFile.reader().readText()
            openFile.close()
        }
        if (results == "") {
            results = "0.0"
        }
        formattedTip = money(results)
        total.text = getString(R.string.total, formattedTip)
        if (getFileStreamPath("list.file").isFile) {
            val openAFile = openFileInput("list.file")
            for (lines in openAFile.reader().readLines()) {
                entrees.add(lines)
            }
            openAFile.close()
            findViewById<MaterialTextView>(R.id.list).text =
                entrees[en]
        }
        findViewById<TextInputEditText>(R.id.price).requestFocus()
        if (sharedPreferences.getBoolean("showConsent", true)) {
            findViewById<Toolbar>(R.id.toolbar1).isVisible = true
            findViewById<ImageButton>(R.id.rated_g).setOnClickListener {
                sharedPreferences.edit().putString("contentRating", MAX_AD_CONTENT_RATING_G)
                    .apply()
                findViewById<Toolbar>(R.id.toolbar1).isVisible = false
                sharedPreferences.edit().putBoolean("showConsent", false).apply()
            }
            findViewById<ImageButton>(R.id.rated_pg).setOnClickListener {
                sharedPreferences.edit().putString("contentRating", MAX_AD_CONTENT_RATING_PG)
                    .apply()
                findViewById<Toolbar>(R.id.toolbar1).isVisible = false
                sharedPreferences.edit().putBoolean("showConsent", false).apply()
            }
            findViewById<ImageButton>(R.id.rated_t).setOnClickListener {
                sharedPreferences.edit().putString("contentRating", MAX_AD_CONTENT_RATING_T)
                    .apply()
                findViewById<Toolbar>(R.id.toolbar1).isVisible = false
                sharedPreferences.edit().putBoolean("showConsent", false).apply()
            }
            findViewById<ImageButton>(R.id.rated_ma).setOnClickListener {
                sharedPreferences.edit().putString("contentRating", MAX_AD_CONTENT_RATING_MA)
                    .apply()
                findViewById<Toolbar>(R.id.toolbar1).isVisible = false
                sharedPreferences.edit().putBoolean("showConsent", false).apply()
            }
            findViewById<Button>(R.id.rated_none).setOnClickListener {
                sharedPreferences.edit().putString("contentRating", "")
                    .apply()
                findViewById<Toolbar>(R.id.toolbar1).isVisible = false
                sharedPreferences.edit().putBoolean("showConsent", false).apply()
            }
        }
        if (sharedPreferences.getBoolean("showConsentIAB", true)) {
            InfoFragment1().show(supportFragmentManager, "Consent")
        }
        val mAdView2: View = findViewById(R.id.adView2)
        when (getRnd(ad)) {
            0 -> {
                adLinkStr =
                    "https://play.google.com/store/apps/details?id=com.toremetal.signsfree"
                mAdView2.background = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.ad_signs_gp
                )
                mAdView2.setOnClickListener { goToAdLink(ad.toString()) }
            }
            1 -> {
                mAdView2.setOnClickListener { goToAdLink(ad.toString()) }
                adLinkStr =
                    "https://play.google.com/store/apps/dev?id=7952290850776080706"
                mAdView2.background = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.ad_dev_pg
                )
            }
            2 -> {
                mAdView2.setOnClickListener { goToAdLink(ad.toString()) }
                adLinkStr = "https://toremetal.com"
                mAdView2.background = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.ad_tm
                )
            }
            3 -> {
                mAdView2.setOnClickListener { goToAdLink(ad.toString()) }
                adLinkStr =
                    "https://shareasale.com/r.cfm?b=43&u=2789938&m=47&urllink=&afftrack="
                mAdView2.background = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.ad_shareasale
                )
            }
            4 -> {
                mAdView2.setOnClickListener { goToAdLink(ad.toString()) }
                adLinkStr =
                    "https://play.google.com/store/apps/details?id=com.toremetal.pos"
                mAdView2.background = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.ad_pos
                )
            }
            5 -> {
                mAdView2.setOnClickListener { goToAdLink(ad.toString()) }
                adLinkStr =
                    "https://play.google.com/store/apps/dev?id=7952290850776080706"
                mAdView2.background = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.ad_apps_on_gp
                )
            }
            6 -> {
                mAdView2.setOnClickListener { goToAdLink(ad.toString()) }
                adLinkStr =
                    "https://play.google.com/store/apps/dev?id=7952290850776080706"
                mAdView2.background = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.ad_tm_slogan
                )
            }
            7 -> {
                mAdView2.setOnClickListener { goToAdLink(ad.toString()) }
                adLinkStr =
                    "https://play.google.com/store/apps/details?id=com.toremetal.signs"
                mAdView2.background = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.ad_signsplus_gp
                )
            }
        }
        getConsentFromMyUser()
    }

    private fun setDarkMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPreferences.getInt("nightMode", MODE_NIGHT_NO) == MODE_NIGHT_NO) {

            sharedPreferences.edit().putInt("nightMode", MODE_NIGHT_YES).apply()
            setDefaultNightMode(
                MODE_NIGHT_YES
            )
        } else if (sharedPreferences.getInt("nightMode", MODE_NIGHT_NO) == MODE_NIGHT_YES) {

            sharedPreferences.edit().putInt("nightMode", MODE_NIGHT_NO).apply()
            setDefaultNightMode(
                MODE_NIGHT_NO
            )
        }
    }

    /** Top-Navigation-Bar Menu. */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        //mMenu = menu
        return true
    }

    /** Top-Navigation-Bar Menu Options. */
    @SuppressLint("ApplySharedPref")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_bar_switch -> {
                setDarkMode()
                return true
            }
            R.id.action_info -> {
                InfoFragment().show(supportFragmentManager, "info")
                return true
            }
            R.id.action_settings -> {
                showForm()
                return true
            }
            R.id.action_privacy_policy -> {
                goToPrivacyPolicy()
                return true
            }

            R.id.action_website -> {
                goToWebsite()
                return true
            }
            R.id.action_feedback -> {
                feedback()
                return true
            }
            R.id.action_app_store -> {
                appStore()
                return true
            }
            R.id.action_view_total -> {
                viewGtotal()
                return true
            }
            R.id.action_save_total -> {
                saveGtotal()
                return true
            }
            R.id.action_reset_total -> {
                resetGtotal()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * function [resetGtotal] resets the users saved total value stored in the settings.
     */
    @SuppressLint("ApplySharedPref")
    fun resetGtotal() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        grandTotal = 0F
        sharedPreferences.edit().putFloat("grandTotal", 0F).commit()
        messageUser("Grand Total Reset")
    }

    /**
     * function [saveGtotal] stores the users saved total value in the settings.
     */
    @SuppressLint("ApplySharedPref")
    fun saveGtotal() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (grandTotal != 0F) {
            val new = grandTotal.plus(
                sharedPreferences.getFloat("grandTotal", 0F)
            )
            sharedPreferences.edit().putFloat("grandTotal", new).commit()
            grandTotal = 0F
            messageUser("Added to Grand Total")
        }
    }

    /**
     * function [viewGtotal] displays the users saved total value stored in the settings.
     */
    fun viewGtotal() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val value = NumberFormat.getCurrencyInstance()
            .format(sharedPreferences.getFloat("grandTotal", 0F)).toString()
        val mes = "Total Spent $value"
        messageUser(mes, 5000)
        /*Snackbar.make(
            findViewById(R.id.snackBar),
            mes,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setTextColor(
                ContextCompat.getColor(
                    this.context,
                    R.color.colorOnPrimary
                )
            )
            animationMode = ANIMATION_MODE_SLIDE
            setAction("🆗") {
                // Waste-gate to allow closing the SnackBar.
            }
            show()
        }*/
    }

    /** ❌ ❎
     * class [InfoFragment] is the main container for the G.Total control-box dialog.
     */
    class InfoFragment : DialogFragment() {

        /**
         * [onCreateDialog] is the initialization of the G.Total control-box dialog.
         */
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(requireContext())
                .setMessage(R.string.main_message)
                .setIcon(android.R.drawable.ic_menu_help)
                .setPositiveButtonIcon(getDrawable(requireContext(), R.drawable.ic_end_sale))
                .setPositiveButton(R.string.view_total) { _, _ ->
                    mainActivity.viewGtotal()
                }
                .setNegativeButtonIcon(getDrawable(requireContext(), R.drawable.ic_save))
                .setNegativeButton(R.string.save_total) { _, _ ->
                    mainActivity.saveGtotal()
                }
                .setNeutralButtonIcon(getDrawable(requireContext(), R.drawable.ic_clear))
                .setNeutralButton(R.string.reset_total) { _, _ ->
                    mainActivity.resetGtotal()
                }
                .show()
        }
    }

    /**
     * class [InfoFragment1] is the main container for the IABUSPrivacy control-box dialog.
     */
    class InfoFragment1 : DialogFragment() {

        /**
         * [onCreateDialog] is the initialization of the IABUSPrivacy control-box dialog.
         */
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this.requireContext())
            return AlertDialog.Builder(requireContext())
                .setView(R.layout.message_popup)
                .setNeutralButtonIcon(getDrawable(requireContext(), R.drawable.ic_remove_24))
                .setNeutralButton("Gov. Area: Opt-Out") { _, _ ->
                    sharedPreferences.edit().putString("IABUSPrivacy_String", "1YYY").apply()
                    sharedPreferences.edit().putBoolean("showConsentIAB", false).apply()
                }
                .setNegativeButtonIcon(
                    getDrawable(
                        requireContext(),
                        R.drawable.ic_twotone_public_off_16
                    )
                )
                .setNegativeButton("Non-Personal: Opt-out") { _, _ ->
                    sharedPreferences.edit().putString("IABUSPrivacy_String", "1YYN").apply()
                    sharedPreferences.edit().putBoolean("showConsentIAB", false).apply()
                }
                .setPositiveButtonIcon(
                    getDrawable(
                        requireContext(),
                        R.drawable.ic_twotone_public_16
                    )
                )
                .setPositiveButton("Personalized: Opt-in") { _, _ ->
                    sharedPreferences.edit().putString("IABUSPrivacy_String", "1YNN").apply()
                    sharedPreferences.edit().putBoolean("showConsentIAB", false).apply()
                }
                .show()
        }
    }

    /** calculate tip amount. */
    private fun calculateTip() {
        val stringInTextField =
            stripMoney(findViewById<MaterialTextView>(R.id.subTotal).text.toString())
        findViewById<MaterialTextView>(R.id.tip_result).text = ""
        if (stringInTextField != "") {
            val cost = stringInTextField.toDouble()
            val tipPercentage =
                when (findViewById<RadioGroup>(R.id.tip_options).checkedRadioButtonId) {
                    R.id.option_twenty_percent -> 0.20
                    R.id.option_eighteen_percent -> 0.18
                    else -> 0.15
                }
            var tip = tipPercentage * cost
            val roundUp = findViewById<SwitchCompat>(R.id.round_up_switch).isChecked
            if (roundUp) {
                tip = kotlin.math.ceil(tip)
            }
            val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
            findViewById<MaterialTextView>(R.id.tip_result).text =
                getString(R.string.tip_amount, formattedTip)
        }

    }

    /** fresh screen Load Sequence. */
    private fun screenLoad() {
        //findViewById<TextInputEditText>(R.id.price).setText("")
        //findViewById<TextInputEditText>(R.id.quantity).setText("")
        //recreate()
        setContentView(R.layout.activity_main)
        mainActivity = this
        loadAds()
        val c = java.util.Calendar.getInstance()
        val copyright = getString(R.string.app_copyright, c.get(java.util.Calendar.YEAR).toString())
        val vName =
            packageManager.getPackageInfo(packageName, 0).versionName.replace("android-", "")
        findViewById<MaterialTextView>(R.id.version).text =
            getString(R.string.version_display_name, vName)
        findViewById<MaterialTextView>(R.id.copyright).text = copyright
        findViewById<AppCompatButton>(R.id.calculate_button).setOnClickListener { calculateTip() }
        findViewById<SwitchCompat>(R.id.round_up_switch).setOnClickListener { calculateTip() }
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            addIt(
                findViewById<FloatingActionButton>(R.id.floatingActionButton)
            )
        }
        findViewById<FloatingActionButton>(R.id.saveTheTax).setOnClickListener { saveTax() }
        findViewById<FloatingActionButton>(R.id.refScreen).setOnClickListener { resetScreen() }
        findViewById<MaterialTextView>(R.id.copyright).setOnClickListener { goToWebsite() }
        /*findViewById<MaterialTextView>(R.id.feedback).setOnClickListener { feedback() }
        findViewById<MaterialTextView>(R.id.appStore).setOnClickListener { appStore() }*/
        findViewById<MaterialTextView>(R.id.list).setOnClickListener {
            if (entrees.size > 0) {
                if (en < (entrees.size - 1)) {
                    en++
                } else {
                    en = 0
                }
                findViewById<MaterialTextView>(R.id.list).text = entrees[en]
            }
        }

        findViewById<MaterialTextView>(R.id.service_question).setOnClickListener {
            findViewById<LinearLayout>(R.id.options_layout).isGone =
                !findViewById<LinearLayout>(R.id.options_layout).isGone
            findViewById<SwitchMaterial>(R.id.round_up_switch).isGone =
                !findViewById<SwitchMaterial>(R.id.round_up_switch).isGone
            findViewById<MaterialButton>(R.id.calculate_button).isGone =
                !findViewById<MaterialButton>(R.id.calculate_button).isGone
            findViewById<MaterialTextView>(R.id.tip_result).isGone =
                !findViewById<MaterialTextView>(R.id.tip_result).isGone
            findViewById<LinearLayout>(R.id.options_layout).isGone =
                !findViewById<LinearLayout>(R.id.options_layout).isGone
        }
        /*   Retrieve saved data. */
        if (getFileStreamPath("itemCount.file").isFile) {
            val openFile = openFileInput("itemCount.file")
            itemCountStr = try {
                openFile.reader().readText().trimMargin()
            } catch (e: Exception) {
                "1"
            }
            openFile.close()
        }
        if (getFileStreamPath("taxRate.file").isFile) {
            val openFile = openFileInput("taxRate.file")
            findViewById<TextInputEditText>(R.id.editTextTaxRate).setText(
                openFile.reader().readText()
            )
            openFile.close()
        }
        /*  format the viewable subtotal as currency. */
        val subTotalText = findViewById<MaterialTextView>(R.id.subTotal)
        var results = ""
        if (getFileStreamPath("subtotal.file").isFile) {
            val openFile = openFileInput("subtotal.file")
            results = openFile.reader().readText()
            openFile.close()
        }
        if (results == "") {
            results = "0.0"
        }
        var formattedTip = money(results)
        subTotalText.text = getString(R.string.subtotal, formattedTip)
        /* format the viewable tax as currency. */
        val taxText = findViewById<MaterialTextView>(R.id.tax)
        results = ""
        if (getFileStreamPath("tax.file").isFile) {
            val openFile = openFileInput("tax.file")
            results = openFile.reader().readText()
            openFile.close()
        }
        if (results == "") {
            results = "0.0"
        }
        formattedTip = money(results)
        taxText.text = getString(R.string.tax, formattedTip)
        /* format the viewable total as currency. */
        val total = findViewById<MaterialTextView>(R.id.total)
        results = ""
        if (getFileStreamPath("total.file").isFile) {
            val openFile = openFileInput("total.file")
            results = openFile.reader().readText()
            openFile.close()
        }
        if (results == "") {
            results = "0.0"
        }
        formattedTip = money(results)
        total.text = getString(R.string.total, formattedTip)
        if (getFileStreamPath("list.file").isFile) {
            val openAFile = openFileInput("list.file")
            for (lines in openAFile.reader().readLines()) {
                entrees.add(lines)
            }
            openAFile.close()
            findViewById<MaterialTextView>(R.id.list).text =
                entrees[en]
        }
        /*try {
            val priceView = findViewById<TextInputEditText>(R.id.price)
            priceView.requestFocus()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(priceView, SHOW_IMPLICIT)
        } catch (e: Exception) {
            // messageUser(e.message.toString())
        }*/
    }

    /**
     * Variable [adLinkStr] holds the url to open on activation of the onClick method.
     */
    var adLinkStr: String = ""

    /**
     * Variable [ad] is a randomly generated number used to select which in-house ad to display.
     */
    var ad: Int = (0..7).random()//0

    /**
     * function [getRnd] is an attempt to prevent the random
     * number generation from repeating the same number consecutively.
     */
    fun getRnd(x: Int): Int {
        ad = (0..7).random()
        if (ad == x) {
            ad = (0..7).random()
        }
        return ad
    }

    private fun loadAds(npa: String = "0") {

        AudienceNetworkAds.initialize(this)
        AudienceNetworkAds.isInitialized(this)
        //  FacebookSdk.fullyInitialize()
        //if (applicationInfo.packageName == "com.toremetal.purchasetracker") {
        mAdView = findViewById(R.id.adView)//binding.adView

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val iab = sharedPreferences.getString("IABUSPrivacy_String", "")
        when (val contentRating =
            sharedPreferences.getString("contentRating", "")) {
            MAX_AD_CONTENT_RATING_G -> {
                val requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setTagForChildDirectedTreatment(
                        TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE

                    )
                    .setTagForUnderAgeOfConsent(TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
                    .setMaxAdContentRating(contentRating)
                    .build()
                MobileAds.setRequestConfiguration(requestConfiguration)
            }
            MAX_AD_CONTENT_RATING_PG -> {
                val requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setTagForChildDirectedTreatment(
                        TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE

                    )
                    .setTagForUnderAgeOfConsent(TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
                    .setMaxAdContentRating(contentRating)
                    .build()
                MobileAds.setRequestConfiguration(requestConfiguration)
            }
            MAX_AD_CONTENT_RATING_T -> {
                val requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setTagForChildDirectedTreatment(
                        TAG_FOR_CHILD_DIRECTED_TREATMENT_FALSE
                    )
                    .setTagForUnderAgeOfConsent(TAG_FOR_UNDER_AGE_OF_CONSENT_FALSE)
                    .setMaxAdContentRating(contentRating)
                    .build()
                MobileAds.setRequestConfiguration(requestConfiguration)
            }
            MAX_AD_CONTENT_RATING_MA -> {
                val requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setTagForChildDirectedTreatment(
                        TAG_FOR_CHILD_DIRECTED_TREATMENT_FALSE
                    )
                    .setTagForUnderAgeOfConsent(TAG_FOR_UNDER_AGE_OF_CONSENT_FALSE)
                    .setMaxAdContentRating(contentRating)
                    .build()
                MobileAds.setRequestConfiguration(requestConfiguration)
            }
            else -> {
                val requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setTagForChildDirectedTreatment(
                        TAG_FOR_CHILD_DIRECTED_TREATMENT_FALSE
                    )
                    .setTagForUnderAgeOfConsent(TAG_FOR_UNDER_AGE_OF_CONSENT_FALSE)
                    .build()
                MobileAds.setRequestConfiguration(requestConfiguration)
            }
        }
        val adRequest = if (npa == "1") {
            val extras = Bundle()
            extras.putString("npa", "1")
            if (iab != "") {
                extras.putString("IABUSPrivacy_String", iab)
            }
            AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()
        } else {
            if (iab != "") {
                val extras = Bundle()
                extras.putString("IABUSPrivacy_String", iab)
                AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                    .build()
            } else {
                AdRequest.Builder()
                    .build()
            }
        }
        try {
            MobileAds.initialize(this) {}
            MobileAds.getInitializationStatus()
            if (BuildConfig.DEBUG) {
                Builder().setTestDeviceIds(listOf("1BAFD9C7D9994166ED69DC8CFD188A32"))
            }
            mAdView.adListener = object : AdListener() {
                override fun onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Code to be executed when an ad request fails.
                    mAdView.isVisible = false
                    mAdView.isGone = true
                    fbAdView = com.facebook.ads.AdView(
                        this@MainActivity,
                        "563008208937993_563012032270944",
                        com.facebook.ads.AdSize.BANNER_HEIGHT_50
                    )
                    if (BuildConfig.DEBUG) {
                        AdSettings.addTestDevice("b8de912a-fc02-42fe-a3c6-e36eda296659")
                    }
                    val adContainer = findViewById<LinearLayout>(R.id.adView2)
                    adContainer.addView(fbAdView)
                    val adListener: com.facebook.ads.AdListener = object :
                        com.facebook.ads.AdListener {
                        override fun onError(ads: Ad, adError: AdError) {
                            //messageUser(adError.errorMessage.toString())
                            // Ad error callback
                            when (getRnd(ad)) {
                                0 -> {
                                    adContainer?.setOnClickListener { goToAdLink(ad.toString()) }
                                    adLinkStr =
                                        "https://play.google.com/store/apps/details?id=com.toremetal.signsfree"
                                    adContainer?.background = AppCompatResources.getDrawable(
                                        this@MainActivity,
                                        R.drawable.ad_signs_gp
                                    )
                                }
                                1 -> {
                                    adContainer?.setOnClickListener { goToAdLink(ad.toString()) }
                                    adLinkStr =
                                        "https://play.google.com/store/apps/dev?id=7952290850776080706"
                                    adContainer?.background = AppCompatResources.getDrawable(
                                        this@MainActivity,
                                        R.drawable.ad_dev_pg
                                    )
                                }
                                2 -> {
                                    adContainer?.setOnClickListener { goToAdLink(ad.toString()) }
                                    adLinkStr = "https://toremetal.com"
                                    adContainer?.background = AppCompatResources.getDrawable(
                                        this@MainActivity,
                                        R.drawable.ad_tm
                                    )
                                }
                                3 -> {
                                    adContainer?.setOnClickListener { goToAdLink(ad.toString()) }
                                    adLinkStr =
                                        "https://shareasale.com/r.cfm?b=43&u=2789938&m=47&urllink=&afftrack="
                                    adContainer?.background = AppCompatResources.getDrawable(
                                        this@MainActivity,
                                        R.drawable.ad_shareasale
                                    )
                                }
                                4 -> {
                                    adContainer?.setOnClickListener { goToAdLink(ad.toString()) }
                                    adLinkStr =
                                        "https://play.google.com/store/apps/details?id=com.toremetal.pos"
                                    adContainer?.background = AppCompatResources.getDrawable(
                                        this@MainActivity,
                                        R.drawable.ad_pos
                                    )
                                }
                                5 -> {
                                    adContainer?.setOnClickListener { goToAdLink(ad.toString()) }
                                    adLinkStr =
                                        "https://play.google.com/store/apps/dev?id=7952290850776080706"
                                    adContainer?.background = AppCompatResources.getDrawable(
                                        this@MainActivity,
                                        R.drawable.ad_apps_on_gp
                                    )
                                }
                                6 -> {
                                    adContainer?.setOnClickListener { goToAdLink(ad.toString()) }
                                    adLinkStr =
                                        "https://play.google.com/store/apps/dev?id=7952290850776080706"
                                    adContainer?.background = AppCompatResources.getDrawable(
                                        this@MainActivity,
                                        R.drawable.ad_tm_slogan
                                    )
                                }
                                7 -> {
                                    adContainer?.setOnClickListener { goToAdLink(ad.toString()) }
                                    adLinkStr =
                                        "https://play.google.com/store/apps/details?id=com.toremetal.signs"
                                    adContainer?.background = AppCompatResources.getDrawable(
                                        this@MainActivity,
                                        R.drawable.ad_signsplus_gp
                                    )
                                }
                            }
                            Handler(Looper.getMainLooper()).postDelayed({
                                fbAdView!!.loadAd(
                                    fbAdView!!.buildLoadAdConfig().withAdListener(this).build()
                                )
                            }, 25000)
                        }

                        override fun onAdLoaded(ad: Ad) {
                            // Ad loaded callback
                            adContainer?.background = null
                            adLinkStr = ""
                            fbAdView?.isVisible = true
                            fbAdView?.isGone = false
                            adContainer.isGone = false
                            adContainer.isVisible = true
                            mAdView.isVisible = false
                            mAdView.isGone = true
                        }

                        override fun onAdClicked(ad: Ad) {
                            // Ad clicked callback
                        }

                        override fun onLoggingImpression(ad: Ad) {
                            // Ad impression logged callback
                        }
                    }
                    // Request an ad
                    fbAdView!!.loadAd(
                        fbAdView!!.buildLoadAdConfig().withAdListener(adListener).build()
                    )
                    //fbAdView!!.loadAd()
                    adContainer.isGone = false
                    adContainer.isVisible = true
                }

                override fun onAdImpression() {
                    // Code to be executed when an impression is recorded
                    // for an ad.
                }

                override fun onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    // mAdView.setOnClickListener(null)
                    // mAdView.isClickable = false
                    // mAdView.background = null
                    adLinkStr = ""
                    val mAdView2: View = findViewById(R.id.adView2)
                    mAdView2.isVisible = false
                    mAdView2.isGone = true
                    mAdView.isGone = false
                    mAdView.isVisible = true
                }

                override fun onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }
            }
            mAdView.loadAd(adRequest)
        } catch (e: Exception) {

        }
    }

    /** Open a browser to the selected ads link. */
    private fun goToAdLink(adId: String) {
        if (adLinkStr != "") {
            analyticsEvent("H-Ad:$adId", FirebaseAnalytics.Event.SELECT_CONTENT)
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(adLinkStr)
                    )
                )
            } catch (e: Exception) {
                //messageUser(e.message.toString())
            }
        }
    }

    /** reset Screen Sequence. */
    private fun resetScreen() {
        itemCountStr = "1"
        entrees.clear()
        val out = OutputStreamWriter(openFileOutput("subtotal.file", 0))
        out.write("0")
        out.close()
        val out1 = OutputStreamWriter(openFileOutput("tax.file", 0))
        out1.write("0")
        out1.close()
        val out2 = OutputStreamWriter(openFileOutput("total.file", 0))
        out2.write("0")
        out2.close()
        if (getFileStreamPath("list.file").isFile) {
            deleteFile("list.file")
        }
        if (getFileStreamPath("itemCount.file").isFile) {
            deleteFile("itemCount.file")
        }
        grandTotal = 0F
        screenLoad()
    }

    /** saveTax Rate for reuse. */
    private fun saveTax() {
        try {
            val taxRateStr = findViewById<TextInputEditText>(R.id.editTextTaxRate).text.toString()
            if (taxRateStr != "") {
                val out = OutputStreamWriter(openFileOutput("taxRate.file", 0))
                out.write(findViewById<TextInputEditText>(R.id.editTextTaxRate).text.toString())
                out.close()
            } else {
                if (getFileStreamPath("taxRate.file").isFile) deleteFile("taxRate.file")
            }
            messageUser(getString(R.string.taxRateSaved))
        } catch (e: Exception) {
            // messageUser(e.message.toString())
        }
    }

    /** Add Doubles. (x,y) */
    private fun add(x: Double, y: Double): Double {
        return try {
            x.plus(y)
        } catch (e: Exception) {
            0.0
        }
    }

    /** Add Int. (x, 1) */
    private fun incrementCount(x: String): Int {
        return try {
            x.toInt().plus(1)
        } catch (e: Exception) {
            0
        }
    }

    /** Multiply Doubles. (x,y) */
    private fun multiply(x: Double, y: Double): Double {
        return try {
            x.times(y)
        } catch (e: Exception) {
            0.0
        }
    }

    /** Enter-key press-event action. */
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        /* android.view.inputmethod.EditorInfo.IME_ACTION_DONE */
        return if (applicationInfo.targetSdkVersion >= Build.VERSION_CODES.ECLAIR) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                try {
                    if (findViewById<TextInputEditText>(R.id.price).text.toString() != "") {
                        addIt(findViewById<FloatingActionButton>(R.id.floatingActionButton))
                    }
                    true
                } catch (e: Exception) {
                    super.onKeyUp(keyCode, event)
                }
            } else if (keyCode == KeyEvent.KEYCODE_BACK && event!!.isTracking && !event.isCanceled) {
                onBackPressed()
                true
            } else {
                super.onKeyUp(keyCode, event)
            }
        } else {
            false
        }
    } // End Enter-key press-event action.

    /** remove currency formatting from a value. (p) */
    private fun stripMoney(p: String): String {
        /* use try method in-case (p) is not the right format */
        return try {
            p.replace(",", "").replace(moneySym, "").replace(".00", "")
                .replace(getString(R.string.tax).replace(" %s", ""), "")
                .replace(getString(R.string.subtotal).replace(" %s", ""), "")
                .replace(getString(R.string.total).replace(" %s", ""), "")
        } catch (e: Exception) {
            /* return (p) unmodified if error occurred */
            p
        }
    }

    /** format a value as currency. (p) */
    private fun money(p: String): String {
        /* use try method in-case (p) is not the right format */
        return try {
            NumberFormat.getCurrencyInstance()
                .format(p.replace(moneySym, "").replace(",", "").replace(".00", "").toDouble())
        } catch (e: Exception) {
            /* return (p) unmodified if error occurred */
            p
        }
    }

    /** Add a value to the purchase. */
    private fun addIt(view: View) = try {
        view.isEnabled = false
        if (findViewById<TextInputEditText>(R.id.price).text.toString() != "") {
            en = 0
            var qt = findViewById<TextInputEditText>(R.id.quantity).text.toString()
            if (qt == "") {
                qt = "1"
            }
            var p = findViewById<TextInputEditText>(R.id.price).text.toString().replace(",", "")
            if (p == "") {
                p = "0"
            }
            try {
                /* Save price entry history for user convenience */
                var theList = "Item($itemCountStr) $qt @ ${money(p)}"
                var index = 0
                while (index < entrees.size) {
                    theList += "\n${entrees[index]}"
                    index++
                }
                val out3 = OutputStreamWriter(openFileOutput("list.file", 0))
                out3.write(
                    theList
                )
                out3.close()
                itemCountStr = incrementCount(itemCountStr).toString()
                val out4 = OutputStreamWriter(openFileOutput("itemCount.file", 0))
                out4.write(itemCountStr)
                out4.close()
            } catch (e: Exception) {
            }
            var yy = findViewById<TextInputEditText>(R.id.editTextTaxRate).text.toString()
            if (yy == "") {
                yy = "0"
            }

            /* Convert tax rate percent to decimal. */
            val t: Double = multiply(yy.toDouble(), 0.01)

            /* ******************************************. */

            var currentTax = findViewById<MaterialTextView>(R.id.tax).text.toString()
            if (currentTax == "") {
                currentTax = "0"
            }
            currentTax = stripMoney(currentTax)
            var sub = findViewById<MaterialTextView>(R.id.subTotal).text.toString()
            if (sub == "") {
                sub = "0"
            }
            sub = stripMoney(sub)

            if (qt.toInt() != 1) { /* calculating against one is redundant*/
                p = multiply(qt.toDouble(), p.toDouble()).toString()
            }

            /* calculate new total from subtotal and tax */
            val newSub = add(sub.toDouble(), p.toDouble())
            val newTax = add(multiply(p.toDouble(), t), currentTax.toDouble())
            val newTotal = add(newSub, newTax)

            /* write values to memory */
            val out = OutputStreamWriter(openFileOutput("subtotal.file", 0))
            out.write(
                newSub.toString()
            )
            out.close()
            val out1 = OutputStreamWriter(openFileOutput("tax.file", 0))
            out1.write(
                newTax.toString()
            )
            out1.close()
            val out2 = OutputStreamWriter(openFileOutput("total.file", 0))
            out2.write(
                newTotal.toString()
            )
            out2.close()
            view.isEnabled = true
            entrees.clear()
            val nt: Float = newTotal.toFloat()
            grandTotal = nt
            messageUser("$grandTotal")
            /* refresh app to read the new data and assign values */
            screenLoad()
        } else {
            view.isEnabled = true
        }
    } catch (e: Exception) {
        view.isEnabled = true
        messageUser(e.message.toString())
    }

    /** Show the Website. */
    private fun goToWebsite() {
        try {
            val myIntent = Intent(this, WebActivity::class.java).apply {
                putExtra(MY_TITLE, getString(R.string.app_creator))
                data = "https://${getString(R.string.website)}".toUri().normalizeScheme()
            }
            startActivity(myIntent)
        } catch (e: Exception) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://${getString(R.string.website)}")
                )
            )
        }
    }

    /** Show the Privacy Policy. */
    private fun goToPrivacyPolicy() {
        try {
            val myIntent = Intent(this, WebActivity::class.java).apply {
                putExtra(MY_TITLE, getString(R.string.privacy))
                data = getString(R.string.privacy_policy).toUri().normalizeScheme()
            }
            startActivity(myIntent)
        } catch (e: Exception) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.privacy_policy))
                )
            )
        }
    }

    /** Open a browser to the Google Play Store Dev Page. */
    private fun appStore() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.dev_page))
                )
            )
        } catch (e: Exception) {
            //   messageUser(e.message.toString())
        }
    }

    /** Open the feedback form */
    private fun feedback() {
        try {
            val myIntent = Intent(this, WebActivity::class.java).apply {
                putExtra(MY_TITLE, getString(R.string.privacy))
                data = getString(R.string.feedback_link).toUri().normalizeScheme()
            }
            startActivity(myIntent)
        } catch (e: Exception) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.feedback_link))
                )
            )
        }
    }

    /**
     * Display a SnackBar popup message.
     *
     * * Usage Options.
     * * * Snackbar.LENGTH_SHORT = -1
     * Snackbar.LENGTH_LONG = 0
     * Snackbar.LENGTH_INDEFINITE = -2
     *
     * * Usage Methods:
     * * * messageUser("message")
     * messageUser("message", 0)
     * messageUser("message", -2)
     * * * *
     * messageUser("message", Snackbar.LENGTH_LONG)
     * messageUser("message", Snackbar.LENGTH_INDEFINITE)
     */
    private fun messageUser(mes: String, snackBarLENGTH: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(
            findViewById(R.id.snackBar),
            mes,
            snackBarLENGTH
        ).apply {
            setTextColor(
                ContextCompat.getColor(
                    this.context,
                    R.color.colorOnPrimary
                )
            )
            animationMode = ANIMATION_MODE_SLIDE
            setAction("🆗") {
                /* Waste-gate to allow closing the SnackBar */
            }
            show()
        }
    }
} /* End MainActivity() */