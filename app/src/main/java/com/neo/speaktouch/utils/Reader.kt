/*
 * Read node content.
 *
 * Copyright (C) 2023 Irineu A. Silva.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.neo.speaktouch.utils

import android.content.Context
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.speaktouch.model.Type
import com.neo.speaktouch.model.toTypeText
import com.neo.speaktouch.utils.extension.getContent
import com.neo.speaktouch.utils.extension.isNotNullOrEmpty
import com.neo.speaktouch.utils.extension.iterator
import com.neo.speaktouch.utils.extension.toStateText
import javax.inject.Inject

class Reader @Inject constructor(
    private val context: Context
) {

    fun read(
        node: AccessibilityNodeInfoCompat,
        options: Options = Options()
    ) = with(node) {

        val type = Type.get(node)

        val content = getContent(type) ?: readChildren(node)

        buildList {

            if (content.isNotNullOrEmpty()) {
                add(content)
            }

            if (options.mustReadType && type != null) {
                type.toTypeText()?.let {
                    add(it.resolved(context))
                }
            }

            if (options.mustReadState) {
                node.toStateText(type)?.let {
                    add(it.resolved(context))
                }
            }
        }.joinToString(
            separator = ", "
        )
    }

    fun readChildren(
        node: AccessibilityNodeInfoCompat
    ): CharSequence {
        return buildList {
            for (child in node) {

                if (!NodeValidator.isReadableAsChild(child)) continue

                val type = Type.get(child)

                add(
                    read(
                        node = child,
                        options = Options(
                            // Announce state only checkable children
                            mustReadState = type is Type.Checkable,
                            // Should not announce the type image of children
                            mustReadType = type !is Type.Image
                        )
                    )
                )
            }
        }.joinToString(
            separator = ", "
        )
    }

    data class Options(
        val mustReadState: Boolean = true,
        val mustReadType: Boolean = true,
    )
}
