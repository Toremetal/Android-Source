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
 *     HomeViewModel.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: HomeViewModel.kt
 *      Last Modified: 8/7/24, 11:51 PM
 *   ************************************************************************
 */

package com.toremetal.signs.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toremetal.signs.R
import com.toremetal.signs.SignsPhotos
import kotlinx.coroutines.launch

/**
 * Image Loading Status values.
 */
enum class SignsApiStatus {
    /**
     * Image loading indicator.
     */
    LOADING,

    /**
     * Image load error indicator.
     */
    ERROR,

    /**
     * Image loaded successfully indicator.
     */
    DONE
}

/**
 * The [ViewModel] that is attached to the [HomeFragment].
 */
class HomeViewModel : ViewModel() {

    /**
     * The internal MutableLiveData that stores the status of the most recent request.
     */
    private val _status = MutableLiveData<SignsApiStatus>()

    /**
     * The external immutable LiveData for the request status.
     */
    val status: LiveData<SignsApiStatus> = _status

    /**
     * Internal MutableLiveData for updating the List of SignsPhoto with new values.
     */
    private val _photos = MutableLiveData<List<SignsPhotos>>()

    /**
     * The external LiveData interface is immutable, so only this class can modify it.
     */
    val photos: LiveData<List<SignsPhotos>> = _photos

    /**
     * Call getSignsPhotos() on init so we can display status immediately.
     */
    init {
        getSignsPhotos()
    }

    /**
     * Gets Signs photos information from the Signs API service and updates the
     * [SignsPhotos] [List] [LiveData].
     */
    private fun getSignsPhotos() {

        viewModelScope.launch {
            _status.value = SignsApiStatus.LOADING
            try {
                _photos.value = listOf(
                    SignsPhotos("11", R.drawable.a, "a"),
                    SignsPhotos("12", R.drawable.b, "b"),
                    SignsPhotos("13", R.drawable.c, "c"),
                    SignsPhotos("14", R.drawable.d, "d"),
                    SignsPhotos("15", R.drawable.e, "e"),
                    SignsPhotos("16", R.drawable.f, "f"),
                    SignsPhotos("17", R.drawable.g, "g"),
                    SignsPhotos("18", R.drawable.h, "h"),
                    SignsPhotos("19", R.drawable.i, "i"),
                    SignsPhotos("20", R.drawable.j, "j"),
                    SignsPhotos("21", R.drawable.k, "k"),
                    SignsPhotos("22", R.drawable.l, "l"),
                    SignsPhotos("23", R.drawable.m, "m"),
                    SignsPhotos("24", R.drawable.n, "n"),
                    SignsPhotos("25", R.drawable.o, "o"),
                    SignsPhotos("26", R.drawable.p, "p"),
                    SignsPhotos("27", R.drawable.q, "q"),
                    SignsPhotos("28", R.drawable.r, "r"),
                    SignsPhotos("29", R.drawable.s, "s"),
                    SignsPhotos("30", R.drawable.t, "t"),
                    SignsPhotos("31", R.drawable.u, "u"),
                    SignsPhotos("32", R.drawable.v, "v"),
                    SignsPhotos("33", R.drawable.w, "w"),
                    SignsPhotos("34", R.drawable.x, "x"),
                    SignsPhotos("35", R.drawable.y, "y"),
                    SignsPhotos("36", R.drawable.z, "z")
                )
                _status.value = SignsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = SignsApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }
}