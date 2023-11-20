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
import com.neo.speaktouch.R
import com.neo.speaktouch.model.Type
import com.neo.speaktouch.utils.extension.ifEmptyOrNull
import com.neo.speaktouch.utils.extension.iterator
import com.neo.speaktouch.utils.extension.toText
import javax.inject.Inject

class Reader @Inject constructor(
    private val context: Context
) {

    fun readContent(
        node: AccessibilityNodeInfoCompat,
        options: Options = Options()
    ) = with(node) {

        val content = contentDescription.ifEmptyOrNull {
            text.ifEmptyOrNull {
                hintText.ifEmptyOrNull {
                    readChildren(node)
                }
            }
        }

        buildList {
            add(content)

            if (options.mustReadType) {
                node.toText()?.let {
                    add(it.resolved(context))
                }
            }

            if (options.mustReadSelection && node.isSelected) {
                add(context.getString(R.string.text_selected))
            }

        }.joinToString(
            separator = ", ",
            postfix = "."
        )
    }

    fun readChildren(
        node: AccessibilityNodeInfoCompat
    ): CharSequence {
        return buildList {
            for (child in node) {

                if (!NodeValidator.isValidForAccessibility(child)) continue
                if (!NodeValidator.isReadableAsChild(child)) continue

                add(
                    readContent(
                        node = child,
                        options = Options(
                            mustReadSelection = false,
                            mustReadType = Type.get(child) is Type.Checkable
                        )
                    )
                )
            }
        }.joinToString(
            separator = ", ",
            postfix = "."
        )
    }

    data class Options(
        val mustReadSelection: Boolean = true,
        val mustReadType: Boolean = true,
    )
}
