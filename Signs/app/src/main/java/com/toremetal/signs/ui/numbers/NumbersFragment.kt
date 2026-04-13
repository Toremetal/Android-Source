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
 *     NumbersFragment.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: NumbersFragment.kt
 *      Last Modified: 8/7/24, 11:51 PM
 *   ************************************************************************
 */

package com.toremetal.signs.ui.numbers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.toremetal.signs.PhotoGridAdapter2
import com.toremetal.signs.databinding.FragmentNumbersBinding
import com.toremetal.signs.showTranslator

/**
 * The Numbers Fragment displays Number signs.
 */
class NumbersFragment : Fragment() {
    private val viewModel: NumbersViewModel by viewModels()
    private var _binding: FragmentNumbersBinding? = null
    private val binding get() = _binding!!

    /**
     * Override to provide access to the Grid Adapter on View Creation.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNumbersBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        // Sets the adapter of the photosGrid RecyclerView
        binding.photosGrid.adapter = PhotoGridAdapter2()

        return binding.root
    }

    /**
     *
     */
    override fun onResume() {
        showTranslator()
        super.onResume()
    }

    /**
     * Release all resources associated with the view.
     */
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}