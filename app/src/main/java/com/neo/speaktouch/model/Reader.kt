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

package com.neo.speaktouch.model

import android.content.Context
import com.neo.speaktouch.R
import com.neo.speaktouch.utils.extension.filterNotNullOrEmpty
import com.neo.speaktouch.utils.extension.ifEmptyOrNull
import com.neo.speaktouch.utils.extension.iterator
import com.neo.speaktouch.utils.`object`.NodeValidator
import com.neo.speaktouch.utils.`typealias`.NodeInfo

class Reader(
    private val context: Context
) {
    fun getContent(
        nodeInfo: NodeInfo
    ) = getContent(
        nodeInfo = nodeInfo,
        level = Level.TEXT(
            mustReadSelection = true,
            mustReadType = true,
            mustReadCheckable = true
        )
    )

    private fun getContent(
        nodeInfo: NodeInfo,
        level: Level
    ): String {
        return when (level) {
            is Level.TEXT -> with(nodeInfo) {
                val content = contentDescription.ifEmptyOrNull {
                    text.ifEmptyOrNull {
                        hintText.ifEmptyOrNull {
                            getContent(
                                nodeInfo,
                                Level.CHILDREN
                            )
                        }
                    }
                }

                listOf(
                    content,
                    getType(nodeInfo, level.mustReadType),
                    getCheckable(nodeInfo, level.mustReadCheckable),
                    getSelection(nodeInfo, level.mustReadSelection)
                ).filterNotNullOrEmpty()
            }

            is Level.CHILDREN -> buildList {
                for (child in nodeInfo) {

                    if (!NodeValidator.isValidForAccessible(child)) continue

                    if (NodeValidator.isChildReadable(child)) {
                        add(
                            getContent(
                                child,
                                Level.TEXT(
                                    mustReadSelection = false,
                                    mustReadCheckable = true,
                                    mustReadType = listOf(
                                        Type.SWITCH,
                                        Type.TOGGLE,
                                        Type.RADIO,
                                        Type.CHECKBOX
                                    ).any { it == Type.get(child) }
                                )
                            )
                        )
                    }
                }
            }
        }.joinToString(", ")
    }

    private fun getType(
        node: NodeInfo,
        mustRead: Boolean
    ): String? {

        if (!mustRead) return null

        return when (Type.get(node)) {
            Type.NONE -> null
            Type.IMAGE -> context.getString(R.string.text_image_type)
            Type.SWITCH -> context.getString(R.string.text_switch_type)
            Type.TOGGLE -> context.getString(R.string.text_toggle_type)
            Type.RADIO -> context.getString(R.string.text_radio_type)
            Type.CHECKBOX -> context.getString(R.string.text_checkbox_type)
            Type.BUTTON -> context.getString(R.string.text_button_type)
            Type.EDITFIELD -> context.getString(R.string.text_editfield_type)
            Type.OPTIONS -> context.getString(R.string.text_options_type)
            Type.LIST -> context.getString(R.string.text_list_type)
            Type.TITLE -> context.getString(R.string.text_title_type)
        }
    }

    private fun getCheckable(
        nodeInfo: NodeInfo,
        mustRead: Boolean
    ): CharSequence? {
        if (!mustRead) return null
        if (!nodeInfo.isCheckable) return null

        return if (nodeInfo.isCheckable) {
            context.getString(R.string.text_enabled)
        } else {
            context.getString(R.string.text_enabled)
        }
    }

    private fun getSelection(
        node: NodeInfo,
        mustRead: Boolean
    ) = if (mustRead && node.isSelected) {
        context.getString(R.string.text_selected)
    } else {
        null
    }

    sealed interface Level {

        data class TEXT(
            val mustReadSelection: Boolean,
            val mustReadType: Boolean,
            val mustReadCheckable: Boolean
        ) : Level

        object CHILDREN : Level
    }
}