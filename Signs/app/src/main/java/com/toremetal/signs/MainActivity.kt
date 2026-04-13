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
 *     MainActivity.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: MainActivity.kt
 *      Last Modified: 9/13/25, 5:28 PM
 *   ************************************************************************
 */

package com.toremetal.signs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
/*import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability*/
import com.toremetal.signs.MainActivity.MyNavView.mainActivity
import com.toremetal.signs.MainActivity.MyNavView.showMessage
import com.toremetal.signs.data.APP_VERSION
import com.toremetal.signs.data.IN_DEBUG_MODE
import com.toremetal.signs.data.checkUpdate
import com.toremetal.signs.data.signList
import com.toremetal.signs.data.signListInt
import com.toremetal.signs.data.sliderDataArrayList
import com.toremetal.signs.data.textToTranslStr
import com.toremetal.signs.data.translate
import com.toremetal.signs.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.Locale

/**
 * External function: [setUserInputText] is used to pass user input
 * to the Internal function: [MainActivity.setInputText]
 * to set the text in the web-search text-box.
 */
fun setUserInputText(str: String) {
    mainActivity.setInputText(str)
}

/**
 * Turns the translator buttons & text box back on.
 */
fun showTranslator() {
    mainActivity.showTheTranslator()
}

/**
 * Turns the translator buttons & text box off (hides).
 */
fun hideTranslator() {
    mainActivity.hideTranslator()
}

/**
 * [MainActivity] The primary container for the application.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var appBarConfiguration2: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: BottomNavigationView
    //private lateinit var appUpdateManager: AppUpdateManager

    /**
     * App Initialization.
     *  sharedPreferences
     *  "nightMode" const: MODE_NIGHT_NO, MODE_NIGHT_YES, MODE_NIGHT_FOLLOW_SYSTEM
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val textHome = findViewById<EditText>(R.id.textHome)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        AppCompatDelegate.setDefaultNightMode(
            sharedPreferences.getInt(
                "nightMode",
                MODE_NIGHT_FOLLOW_SYSTEM
            )
        )
        mainActivity = this
        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        bottomNav = findViewById(R.id.bottom_nav_view)
        bottomNav.let {
            appBarConfiguration2 = AppBarConfiguration(
                setOf(
                    R.id.nav_home,
                    R.id.nav_numbers,
                    R.id.nav_words
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration2)
            it.setupWithNavController(navController)
        }
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_numbers,
                R.id.nav_words
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val copyrightStr =
            getString(R.string.copyright, Calendar.getInstance().get(Calendar.YEAR).toString())

        binding.tradeText.text = copyrightStr
        binding.textView3.text = getString(R.string.version_display_name, APP_VERSION)
        textHome.setSelectAllOnFocus(true)
        textHome.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
        textHome.setOnKeyListener { view, keyCode, _ ->
            try {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (translate) {
                        translate()
                        /*textToTranslStr =
                            findViewById<EditText>(view.id).text.toString()//textHome.text.toString()
                        findNavController(R.id.nav_host_fragment_content_main).navigate(
                            R.id.nav_web
                        )*/
                    } else {
                        val query = textHome.text.toString()
                        if (query != "") {
                            textHome.setText("")
                            view.context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(getString(R.string.searchPrefix, query))
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                errorReport(
                    errStr = "[MA-349]${e.message}",
                    errorDetails = e.stackTraceToString()
                )
            }
            return@setOnKeyListener false
        }

        binding.appBarMain.fab.setOnClickListener {
            if (translate) {
                translate()
            }
        }

        /* Search */
        binding.appBarMain.fab1.setOnClickListener { view: View ->
            try {
                val query = textHome.text.toString()
                if (query == "") {
                    showMessage(getString(R.string.searchHint), true)
                } else if (!translate) {
                    textHome.setText("")
                    view.context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.searchPrefix, query))
                        )
                    )

                }
            } catch (e: Exception) {
                errorReport(
                    errStr = "[MA-417]${e.message}",
                    errorDetails = e.stackTraceToString()
                )
            }
        }
        binding.appBarMain.fabSpc.setOnClickListener {
            val oTxt = "${textHome.text} "
            textHome.setText(oTxt)
            textHome.setSelection(textHome.text.length)//textHome.setSelection(textHome.text.lastIndex.plus(1))
        }
        binding.appBarMain.fabClrAll.setOnClickListener {
            if (textHome.text.isNotEmpty()) {
                textHome.text.clear()
            }
        }
        binding.appBarMain.fabClr.setOnClickListener {
            if (textHome.text.isNotEmpty()) {
                textHome.setText(
                    textHome.text.toString().substring(0, textHome.editableText.lastIndex)
                )
                textHome.setSelection(textHome.text.length)
            }
        }
        binding.webText.setOnClickListener {
            binding.webText.isEnabled = false
            try {
                findNavController(R.id.nav_host_fragment_content_main).navigate(
                    R.id.nav_policy
                )
                binding.drawerLayout.close()
            } catch (e: Exception) {
                errorReport(
                    errStr = "[MA-436]${e.message}",
                    errorDetails = e.stackTraceToString()
                )
            }
            binding.webText.isEnabled = true
        }
        binding.trade.setOnClickListener {
            binding.trade.isEnabled = false
            translate = false
            hideTranslator()
            try {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_web)
                binding.drawerLayout.close()
            } catch (e: Exception) {
                errorReport(
                    errStr = "[MA-451]${e.message}",
                    errorDetails = e.stackTraceToString()
                )
            }
            binding.trade.isEnabled = true
        }

        binding.appBarMain.translatorSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.isEnabled = false
            translate = !isChecked
            showTranslator()
            buttonView.isEnabled = true
        }
        binding.appBarMain.translatorSwitch.isChecked = !translate
        if (checkUpdate) {
            checkUpdate = false
           /* if (sharedPreferences.getBoolean("checkUpdate", true)) {
                this.appUpdateManager = AppUpdateManagerFactory.create(this)
                val appUpdateInfoTask = appUpdateManager.appUpdateInfo
                // Action when the platform has an update. //AppUpdateType.FLEXIBLE AppUpdateType.IMMEDIATE
                appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                            AppUpdateType.IMMEDIATE
                        )
                    ) {
                        val listener = { state: InstallState ->
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
                        appUpdateManager.registerListener(listener)
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
            } */
        }

    }

    /**
     * companion object [MyNavView]
     * Provides the fragments access to the MainActivity Controls.
     */
    companion object MyNavView {

        /**
         * [navView] Nav Menu assigned as a global var to allow fragments to disable options.
         */
        lateinit var navView: NavigationView

        /**
         * Var [mainActivity] creates a handle for the application.
         */
        lateinit var mainActivity: MainActivity

        /**
         * User Optional Error Reporting.
         * Errors Logged in a Google Sheet by a Google AppScript.
         */
        fun errorReport(errStr: String, errorDetails: String = "") {
            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this.navView.context /* Activity context */)
            if (sharedPreferences.getBoolean("sendErrors", false)) {
                var details = ""
                if (sharedPreferences.getBoolean("detail", false)) {
                    details = "-Detail=$errorDetails"
                }
                try {
                    val queue = Volley.newRequestQueue(this.navView.context)

                    /*val url =
                        "https://script.google.com/macros/s/AKfycbzgPsTwAQQkPG5YC_gjZPhGU1dYVYycMNOfFs25TXypg1FJSUpOET_6NnoAe7LL0ixo/exec?error=$errStr$details&app=SignsA-Z"*/
                    val url = /*"https://toremetal.com/log/?error=$errStr$details&app=SignsA-Z"*/
                        "https://script.google.com/macros/s/AKfycbzitCr-NZAshO4Ak0n0VbSh1LoL31ugMqq_80c2_7JoFgwgDoZqCrGoqyMsvBH0oNgn/exec?app=SignsA-Z&error=$errStr$details"
                    // Request a string response from the provided URL.
                    val stringRequest = StringRequest(
                        Request.Method.POST, url,
                        { response ->
                            val vx: CharSequence = "Not Logged"
                            if (response.contains(vx, ignoreCase = false)) {
                                if (IN_DEBUG_MODE) {
                                    showMessage("[Log Error:] $response")
                                }
                            } else {
                                if (IN_DEBUG_MODE) {
                                    showMessage(response)
                                }
                            }
                        }, {
                            this.errorReport(
                                errStr = "[MA-239]${it.message}",
                                errorDetails = it.stackTraceToString()
                            )
                        })
                    queue.add(stringRequest)
                } catch (e: Exception) {
                    this.errorReport(
                        errStr = "[MA-245]${e.message}",
                        errorDetails = e.stackTraceToString()
                    )
                }
            }
        }

        /**
         * Function: [showMessage] displays a popup message to the user.
         *
         *   note: SnackBar does not work when passing messages BETWEEN fragments on view change,
         *
         *      Must use:
         *        Toast.makeText(this.navView.context, mes, Toast.LENGTH_SHORT).show()
         *
         *     To use this showMessage on View Changing events. Static-view messages display
         *     fine. However, when the view is switching there is no View?. for the SnackBar
         *     whereas the Toast uses App {context} not an Active {view} which doesn't
         *     dispose() during View Changing events. (theoretical, but logical.)
         *
         *       note2:
         *       (theory) Could Global function this outside the class & pass the context to it.
         *          this would eliminate the need for a companion object.
         *          fun showMessage(mes: String, context: Context) {}
         *
         *          toUse: showMessage("", this.requireContext())
         */
        fun showMessage(mes: String, fancy: Boolean = false) {
            if (fancy) {
                this.mainActivity.showFancyMsg(mes)
            } else {
                Toast.makeText(
                    this.navView.context,
                    mes,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Ensures Proper Controls are displayed based on user last selection.
     */
    override fun onResume() {
        binding.appBarMain.translatorSwitch.isChecked = !translate
        showTranslator()
        super.onResume()
    }

    /**
     * Create side option menu.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /** Menu Options selected response. */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            /*
                        R.id.action_feedback -> {
                            try {
                                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_feedback)
                            } catch (e: Exception) {
                                errorReport(
                                    errStr = "[MA-869]${e.message}",
                                    errorDetails = e.stackTraceToString()
                                )
                                try {
                                    Intent.createChooser(
                                        intent,
                                        getString(R.string.trademark)
                                    ).apply {
                                        startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(getString(R.string.feedbackLink))
                                            )
                                        )
                                    }
                                } catch (e1: Exception) {
                                    errorReport(
                                        errStr = "[MA-885]${e1.message}",
                                        errorDetails = e1.stackTraceToString()
                                    )
                                }
                            }
                            return true
                        }

                        R.id.action_settings -> {
                            try {
                                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_settings)
                            } catch (e: Exception) {
                                errorReport(
                                    errStr = "[MA-730:R.id.action_account_settings]${e.message}",
                                    errorDetails = e.stackTraceToString()
                                )
                            }
                            return true
                        }

                        R.id.action_privacy -> {
                            try {
                                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_policy)
                            } catch (e: Exception) {
                                errorReport(
                                    errStr = "[MA-742:R.id.action_privacy]${e.message}",
                                    errorDetails = e.stackTraceToString()
                                )
                            }
                            return true
                        }
            */
            R.id.action_play -> {
                try {
                    Intent.createChooser(
                        intent,
                        getString(R.string.trademark)
                    ).apply {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.playStoreLink))
                            )
                        )
                    }
                } catch (e: Exception) {
                    errorReport(
                        errStr = "[MA-906]${e.message}",
                        errorDetails = e.stackTraceToString()
                    )
                }
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Navigation support.
     */
    override fun onSupportNavigateUp(): Boolean {
        showTheTranslator()
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Assigns the image values to the Slider Display.
     */
    private fun translate() {
        try {
            val textHome = findViewById<EditText>(R.id.textHome)
            val query = textHome.text.toString()
            if (query == "") {
                showMessage(getString(R.string.searchHint), true)
            } else if (translate) {
                sliderDataArrayList.clear()
                sliderDataArrayList.add(
                    ResourcesCompat.getDrawable(
                        this.resources,
                        R.drawable.ic_launcher_foreground, null
                    )
                )
                textToTranslStr = textHome.text.toString()
                    .replace(",", " ")
                    .replace(".", " ")
                    .replace("  ", " ")
                textHome.setText("")
                val translateList = textToTranslStr.split(" ")
                var signNum: Int
                var foundSign: Boolean
                var foundSigns = false
                translateList.forEach { sign ->
                    signNum = 0
                    foundSign = false
                    signList.forEach {
                        if (!foundSign && sign.lowercase(Locale.getDefault()) == it.lowercase(
                                Locale.getDefault()
                            )
                        ) {
                            sliderDataArrayList.add(
                                ResourcesCompat.getDrawable(
                                    this.resources,
                                    signListInt[signNum],
                                    null
                                )
                            )
                            foundSign = true
                            foundSigns = true
                        }
                        signNum += 1
                    }
                    var signImg: String
                    if (!foundSign) {
                        sign.forEach { signChr ->
                            signImg = signChr.toString()
                            signNum = 0
                            foundSign = false
                            signList.forEach {
                                if (!foundSign && signImg.lowercase(Locale.getDefault()) == it.lowercase(
                                        Locale.getDefault()
                                    )
                                ) {
                                    sliderDataArrayList.add(
                                        ResourcesCompat.getDrawable(
                                            this.resources,
                                            signListInt[signNum],
                                            null
                                        )
                                    )
                                    foundSign = true
                                    foundSigns = true
                                }
                                signNum += 1
                            }
                        }
                    }
                }
                if (foundSigns) {
                    hideTranslator()
                    findNavController(R.id.nav_host_fragment_content_main).navigate(
                        R.id.nav_slider
                    )
                } else {
                    showFancyMsg("Could Not Translate")
                }
            }
        } catch (e: Exception) {
            errorReport(
                errStr = "[MA-352]${e.message}",
                errorDetails = e.stackTraceToString()
            )
        }
    }

    /** [setInputText]
     * #### Internal function to set the text in the translate/web-search text-box.
     */
    fun setInputText(newText: String) {
        val textHome = findViewById<EditText>(R.id.textHome)
        val txt = "${textHome.text}$newText"
        textHome.setText(txt)
        textHome.setSelection(textHome.text.length)
    }

    /**
     * Turns the translator buttons & text box back on (shows).
     */
    fun showTheTranslator() {
        binding.appBarMain.translatorSwitch.isChecked = !translate
        val textHome = this.findViewById<EditText>(R.id.textHome)
        textHome.isVisible = true
        textHome.isEnabled = true
        bottomNav.isVisible = true
        binding.appBarMain.translatorSwitch.isVisible = true
        binding.appBarMain.toolbar.menu.forEach {
            it.isVisible = true
        }
        if (translate) {
            binding.appBarMain.fab.isVisible = true // translate
            textHome.hint = getString(R.string.text_box_hint_translate)
            binding.appBarMain.fab1.isVisible = false // Search
            binding.appBarMain.fabClr.isVisible = true
            binding.appBarMain.fabClrAll.isVisible = true
            binding.appBarMain.fabSpc.isVisible = true
        } else {
            binding.appBarMain.fab1.isVisible = true // Search
            textHome.hint = getString(R.string.buttonText)
            binding.appBarMain.fab.isVisible = false // translate
            binding.appBarMain.fabClr.isVisible = false
            binding.appBarMain.fabClrAll.isVisible = false
            binding.appBarMain.fabSpc.isVisible = false
        }
    }

    /**
     * Turns the translator buttons & text box off (hides).
     */
    fun hideTranslator() {
        val textHome = this.findViewById<EditText>(R.id.textHome)
        textHome.isVisible = false
        textHome.isEnabled = false
        bottomNav.isVisible = false
        binding.appBarMain.translatorSwitch.isVisible = false
        binding.appBarMain.toolbar.menu.forEach {
            it.isVisible = false
        }
        binding.appBarMain.fab.isVisible = false
        binding.appBarMain.fab1.isVisible = false
        binding.appBarMain.fabClr.isVisible = false
        binding.appBarMain.fabClrAll.isVisible = false
        binding.appBarMain.fabSpc.isVisible = false
    }

    /** [showFancyMsg] ([theMessage]: String)
     * #### displays a modified snack-bar message to the user.
     */
    fun showFancyMsg(/** The Fancy Message to Display */ theMessage: String) {
        Snackbar.make(
            binding.root,
            theMessage,
            Snackbar.LENGTH_LONG
        ).apply {
            setAction("Ok") {
                //showMessage("thanks")
            }//,null)
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