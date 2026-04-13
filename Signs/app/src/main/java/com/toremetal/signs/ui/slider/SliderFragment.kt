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
 *     SliderFragment.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: SliderFragment.kt
 *      Last Modified: 9/13/25, 5:36 PM
 *   ************************************************************************
 */

package com.toremetal.signs.ui.slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.toremetal.signs.SliderView
import com.toremetal.signs.data.sliderDataArrayList
import com.toremetal.signs.databinding.FragmentSliderBinding
import com.toremetal.signs.hideTranslator

class SliderFragment : Fragment() {
    val viewModel: SliderViewModel by viewModels()
    private var _binding: FragmentSliderBinding? = null

    /* This property is only valid between onCreateView and onDestroyView. */
    private val binding get() = _binding!!

    /**
     * Create the Translator view to display sign Images.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSliderBinding.inflate(inflater)
        val adapter = SliderAdapter(sliderDataArrayList)
        binding.sliderView.setSliderAdapter(adapter)
        binding.sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        binding.sliderView.scrollTimeInSec = 1
        binding.sliderView.isAutoCycle = true
        binding.sliderView.startAutoCycle()
        return binding.root
    }

    /**
     * Turns the translator buttons & text box back on.
     */
    /*override fun onPause() {
        showTranslator()
        super.onPause()
    }*/

    /**
     *
     */
    override fun onResume() {
        hideTranslator()
        super.onResume()
    }

    /**
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}