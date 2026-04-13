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
 *     NumbersViewModel.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: NumbersViewModel.kt
 *      Last Modified: 8/8/24, 3:21 AM
 *   ************************************************************************
 */

package com.toremetal.signs.ui.numbers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toremetal.signs.R
import com.toremetal.signs.SignsPhotos
import com.toremetal.signs.network.SignsPhoto
import com.toremetal.signs.ui.home.SignsApiStatus
import kotlinx.coroutines.launch

/**
 *
 */
class NumbersViewModel : ViewModel() {

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
     * [SignsPhoto] [List] [LiveData].
     */
    private fun getSignsPhotos() {

        viewModelScope.launch {
            _status.value = SignsApiStatus.LOADING
            try {
                //_photos.value = SignsApi.retrofitService.getPhotos()[0]
                //_photos.value = NumbersApi.retrofitService.getPhotos()
                _photos.value = listOf(
                    SignsPhotos("0", R.drawable.zero, "zero"),
                    SignsPhotos("1", R.drawable.one, "one"),
                    SignsPhotos("2", R.drawable.two, "two"),
                    SignsPhotos("3", R.drawable.three, "three"),
                    SignsPhotos("4", R.drawable.four, "four"),
                    SignsPhotos("5", R.drawable.five, "five"),
                    SignsPhotos("6", R.drawable.six, "six"),
                    SignsPhotos("7", R.drawable.seven, "seven"),
                    SignsPhotos("8", R.drawable.eight, "eight"),
                    SignsPhotos("9", R.drawable.nine, "nine"),
                    SignsPhotos("10", R.drawable.ten, "ten"),
                    SignsPhotos("111", R.drawable.eleven, "eleven"),
                    SignsPhotos("112", R.drawable.twelve, "twelve"),
                    SignsPhotos("113", R.drawable.thirteen, "thirteen"),
                    SignsPhotos("114", R.drawable.fourteen, "fourteen"),
                    SignsPhotos("115", R.drawable.fifteen, "fifteen"),
                    SignsPhotos("116", R.drawable.sixteen, "sixteen"),
                    SignsPhotos("117", R.drawable.seventeen, "seventeen"),
                    SignsPhotos("118", R.drawable.eighteen, "eighteen"),
                    SignsPhotos("119", R.drawable.nineteen, "nineteen"),
                    SignsPhotos("120", R.drawable.twenty, "twenty")
                )
                _status.value = SignsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = SignsApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }
}