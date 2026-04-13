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
 *     SignsViewModel.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: SignsViewModel.kt
 *      Last Modified: 8/7/24, 11:51 PM
 *   ************************************************************************
 */

package com.toremetal.signs.ui.signs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toremetal.signs.network.SignsApi
import com.toremetal.signs.network.SignsPhoto
import com.toremetal.signs.ui.home.SignsApiStatus
import kotlinx.coroutines.launch

/**
 *
 */
class SignsViewModel : ViewModel() {

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
    private val _photos = MutableLiveData<List<SignsPhoto>>()

    /**
     * The external LiveData interface is immutable, so only this class can modify it.
     */
    val photos: LiveData<List<SignsPhoto>> = _photos

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
                //_photos.value = SignsApi.retrofitService.getPhotos()[0] //<- single photo
                _photos.value = SignsApi.retrofitService.getPhotos()
                _status.value = SignsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = SignsApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }
}