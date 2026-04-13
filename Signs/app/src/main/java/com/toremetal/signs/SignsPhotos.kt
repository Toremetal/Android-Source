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
 *     SignsPhotos.kt : Copyright (c) 2025 ™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David Rick (™T©ReMeTaL)
 *      Date: 10/11/25, 4:10 AM
 *      Program Name: signs.main
 *      File: SignsPhotos.kt
 *      Last Modified: 8/7/24, 11:51 PM
 *   ************************************************************************
 */

package com.toremetal.signs

import androidx.annotation.Keep
import com.squareup.moshi.Json

/**
 * ### data class [SignsPhotos] defines a Signs photo object.
 * #### &nbsp;&nbsp; val [id]: String, object id.
 * #### &nbsp;&nbsp; var [imgSrcUrl]: Int, Drawable ResId.
 * #### &nbsp;&nbsp; val [imgVal]: String, object title/label.
 * #####  note: The property names of this data class are used to map the adaptor actions.
 */
@Keep
data class SignsPhotos(
    /**
     * used to map id from the JSON to id in our class
     */
    val id: String,

    /**
     * used to map img_src from the JSON to imgSrcUrl in our class
     */
    @Json(name = "img_src") var imgSrcUrl: Int,
    /**
     * used to map img_val from the JSON to img_val in our class
     */
    @Json(name = "img_val") val imgVal: String
)