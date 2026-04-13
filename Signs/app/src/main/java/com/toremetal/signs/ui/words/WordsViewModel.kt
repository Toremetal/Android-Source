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
 *     WordsViewModel.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: WordsViewModel.kt
 *      Last Modified: 8/9/24, 2:55 AM
 *   ************************************************************************
 */

package com.toremetal.signs.ui.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toremetal.signs.R
import com.toremetal.signs.SignsPhotos
import com.toremetal.signs.network.SignsPhoto
import com.toremetal.signs.ui.home.SignsApiStatus
import kotlinx.coroutines.launch

class WordsViewModel : ViewModel() {

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
                    SignsPhotos("57", R.drawable.hello, "hello"),
                    SignsPhotos("58", R.drawable.goodbye, "goodbye"),
                    SignsPhotos("59", R.drawable.yes, "yes"),
                    SignsPhotos("60", R.drawable.no, "no"),
                    SignsPhotos("37", R.drawable.help, "help"),
                    SignsPhotos("54", R.drawable.fire, "fire"),
                    SignsPhotos("38", R.drawable.stop, "stop"),
                    SignsPhotos("39", R.drawable.please, "please"),
                    SignsPhotos("40", R.drawable.thankyou, "thankyou"),
                    SignsPhotos("41", R.drawable.more, "more"),
                    SignsPhotos("42", R.drawable.done, "done"),
                    SignsPhotos("43", R.drawable.eat, "eat"),
                    SignsPhotos("44", R.drawable.open, "open"),
                    SignsPhotos("45", R.drawable.play, "play"),
                    SignsPhotos("46", R.drawable.left, "left"),
                    SignsPhotos("47", R.drawable.right, "right"),
                    SignsPhotos("55", R.drawable.restroom, "restroom"),
                    SignsPhotos("56", R.drawable.toilet, "toilet"),
                    SignsPhotos("48", R.drawable.mom, "mom"),
                    SignsPhotos("49", R.drawable.dad, "dad"),
                    SignsPhotos("50", R.drawable.sister, "sister"),
                    SignsPhotos("51", R.drawable.brother, "brother"),
                    SignsPhotos("52", R.drawable.grandma, "grandma"),
                    SignsPhotos("53", R.drawable.grandpa, "grandpa")
                )
                _status.value = SignsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = SignsApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }
}