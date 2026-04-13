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
 *     PhotoGridAdapter.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: PhotoGridAdapter.kt
 *      Last Modified: 8/7/24, 11:51 PM
 *   ************************************************************************
 */

package com.toremetal.signs

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.toremetal.signs.DetailActivity.Companion.LETTER
import com.toremetal.signs.data.translate
import com.toremetal.signs.databinding.GridViewItemBinding
import com.toremetal.signs.network.SignsPhoto

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class PhotoGridAdapter :
    ListAdapter<SignsPhoto, PhotoGridAdapter.SignsPhotosViewHolder>(DiffCallback) {

    /**
     * The SignsPhotosViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [SignsPhoto] information.
     */
    class SignsPhotosViewHolder(
        private var binding: GridViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        /**
         *
         */
        fun bind(signsPhoto: SignsPhoto) {
            binding.photo = signsPhoto
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of
     * [SignsPhoto] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<SignsPhoto>() {
        /**
         *
         */
        override fun areItemsTheSame(oldItem: SignsPhoto, newItem: SignsPhoto): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         *
         */
        override fun areContentsTheSame(oldItem: SignsPhoto, newItem: SignsPhoto): Boolean {
            return oldItem.imgVal == newItem.imgVal
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SignsPhotosViewHolder {
        return SignsPhotosViewHolder(
            GridViewItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: SignsPhotosViewHolder, position: Int) {
        val signsPhoto = getItem(position)
        holder.bind(signsPhoto)
        holder.itemView.contentDescription = signsPhoto.imgVal
        holder.itemView.setOnClickListener {
            if (!translate) {
                val intent = Intent(it.context, DetailActivity::class.java).apply {
                    putExtra(LETTER, signsPhoto.imgVal)
                    //putExtra(IMG, signsPhoto.imgSrcUrl)
                    data = signsPhoto.imgSrcUrl.toUri()
                }
                it.context.startActivity(intent)
            } else {
                setUserInputText(signsPhoto.imgVal)
            }
        }
    }
}
