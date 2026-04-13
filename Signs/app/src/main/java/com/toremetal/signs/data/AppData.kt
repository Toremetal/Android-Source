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
 *     AppData.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: AppData.kt
 *      Last Modified: 8/9/24, 2:50 AM
 *   ************************************************************************
 */

package com.toremetal.signs.data

import android.graphics.drawable.Drawable
import com.toremetal.signs.R

/**
 * ## [APP_VERSION]
 * ### Year . Month . TargetSdk . CodeVersion
 * */
const val APP_VERSION = com.toremetal.signs.BuildConfig.VERSION_NAME

/**
 * Global display Error-Message Switch for debugging.
 *
 * set: { const val [IN_DEBUG_MODE]: Boolean = false } for release.
 */
const val IN_DEBUG_MODE: Boolean = false//com.toremetal.signs.BuildConfig.DEBUG

/**
 * The database urls hosting app content.
 *   "https://toremetal.com/signs/" https://toremetal.github.io/
 */
const val BASE_URL: String = "https://toremetal.github.io/"

/**
 * Prevents the update manager from checking for updates everytime the main form loads.
 */
var checkUpdate: Boolean = true

/**
 * translateAction switch.
 */
var translate: Boolean = true

/**
 * String to translate.
 */
var textToTranslStr: String = ""

/**
 * List of Drawables to translate.
 */
val sliderDataArrayList: ArrayList<Drawable?> = ArrayList()

/**
 * [signList]
 * ### Contains the translatable List of Drawable-Names' <value: String>,
 * for comparing against the string to translate.
 * #### Note: [signList] & [signListInt] arrays MUST maintain equal values.
 * String value: {"a"} -> Resource id: {R.drawable.a}
 */
val signList: List<String> = listOf(
    "0",
    "1",
    "2",
    "3",
    "4",
    "5",
    "6",
    "7",
    "8",
    "9",
    "10",
    "11",
    "12",
    "13",
    "14",
    "15",
    "16",
    "17",
    "18",
    "19",
    "20",
    "a",
    "b",
    "c",
    "d",
    "e",
    "f",
    "g",
    "h",
    "i",
    "j",
    "k",
    "l",
    "m",
    "n",
    "o",
    "p",
    "q",
    "r",
    "s",
    "t",
    "u",
    "v",
    "w",
    "x",
    "y",
    "z",
    "zero",
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine",
    "ten",
    "eleven",
    "twelve",
    "thirteen",
    "fourteen",
    "fifteen",
    "sixteen",
    "seventeen",
    "eighteen",
    "nineteen",
    "twenty",
    "brother",
    "dad",
    "done",
    "eat",
    "finished",
    "grandma",
    "grandpa",
    "help",
    "left",
    "lr",
    "mom",
    "more",
    "open",
    "play",
    "please",
    "right",
    "sister",
    "stop",
    "thank_you",
    "thankyou",
    "fire",
    "restroom",
    "bathroom",
    "toilet",
    "yes",
    "no",
    "hello",
    "goodbye"
)

/**
 * [signListInt]
 * ### Contains the translatable List of Drawables' <Resource Id: Int>,
 * #### Note: [signList] & [signListInt] arrays MUST maintain equal values.
 * String value: {"a"} -> Resource id: {R.drawable.a}
 */
val signListInt: List<Int> = listOf(
    R.drawable.zero,
    R.drawable.one,
    R.drawable.two,
    R.drawable.three,
    R.drawable.four,
    R.drawable.five,
    R.drawable.six,
    R.drawable.seven,
    R.drawable.eight,
    R.drawable.nine,
    R.drawable.ten,
    R.drawable.eleven,
    R.drawable.twelve,
    R.drawable.thirteen,
    R.drawable.fourteen,
    R.drawable.fifteen,
    R.drawable.sixteen,
    R.drawable.seventeen,
    R.drawable.eighteen,
    R.drawable.nineteen,
    R.drawable.twenty,
    R.drawable.a,
    R.drawable.b,
    R.drawable.c,
    R.drawable.d,
    R.drawable.e,
    R.drawable.f,
    R.drawable.g,
    R.drawable.h,
    R.drawable.i,
    R.drawable.tj,
    R.drawable.k,
    R.drawable.l,
    R.drawable.m,
    R.drawable.n,
    R.drawable.o,
    R.drawable.p,
    R.drawable.q,
    R.drawable.r,
    R.drawable.s,
    R.drawable.t,
    R.drawable.u,
    R.drawable.v,
    R.drawable.w,
    R.drawable.x,
    R.drawable.y,
    R.drawable.tz,
    R.drawable.zero,
    R.drawable.one,
    R.drawable.two,
    R.drawable.three,
    R.drawable.four,
    R.drawable.five,
    R.drawable.six,
    R.drawable.seven,
    R.drawable.eight,
    R.drawable.nine,
    R.drawable.ten,
    R.drawable.eleven,
    R.drawable.twelve,
    R.drawable.thirteen,
    R.drawable.fourteen,
    R.drawable.fifteen,
    R.drawable.sixteen,
    R.drawable.seventeen,
    R.drawable.eighteen,
    R.drawable.nineteen,
    R.drawable.twenty,
    R.drawable.brother,
    R.drawable.dad,
    R.drawable.done,
    R.drawable.eat,
    R.drawable.finished,
    R.drawable.grandma,
    R.drawable.grandpa,
    R.drawable.help,
    R.drawable.left,
    R.drawable.lr,
    R.drawable.mom,
    R.drawable.more,
    R.drawable.open,
    R.drawable.play,
    R.drawable.please,
    R.drawable.right,
    R.drawable.sister,
    R.drawable.stop,
    R.drawable.thank_you,
    R.drawable.thankyou,
    R.drawable.fire,
    R.drawable.restroom,
    R.drawable.restroom,
    R.drawable.toilet,
    R.drawable.yes,
    R.drawable.no,
    R.drawable.hello,
    R.drawable.goodbye
)
