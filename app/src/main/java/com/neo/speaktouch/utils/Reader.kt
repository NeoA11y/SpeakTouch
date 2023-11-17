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
import com.neo.speaktouch.utils.extension.filterNotNullOrEmpty
import com.neo.speaktouch.utils.extension.getLog
import com.neo.speaktouch.utils.extension.ifEmptyOrNull
import com.neo.speaktouch.utils.extension.iterator
import timber.log.Timber
import javax.inject.Inject

class Reader @Inject constructor(
    private val context: Context
) {
    fun getContent(
        nodeInfo: AccessibilityNodeInfoCompat
    ) = getContent(
        nodeInfo,
        Level.Text(
            mustReadSelection = true,
            mustReadType = true
        )
    )

    private fun getContent(
        node: AccessibilityNodeInfoCompat,
        level: Level
    ): String {
        Timber.d(
            node.getLog(
                "level: $level"
            )
        )

        return when (level) {
            is Level.Text -> with(node) {
                val content = contentDescription.ifEmptyOrNull {
                    text.ifEmptyOrNull {
                        hintText.ifEmptyOrNull {
                            getContent(
                                node,
                                Level.Children
                            )
                        }
                    }
                }

                listOf(
                    content,
                    getType(node, level.mustReadType),
                    getSelection(node, level.mustReadSelection)
                ).filterNotNullOrEmpty()
            }

            is Level.Children -> buildList {
                for (child in node) {

                    if (!NodeValidator.isValidForAccessibility(child)) continue
                    if (!NodeValidator.isReadableAsChild(child)) continue

                    add(
                        getContent(
                            child,
                            Level.Text(
                                mustReadSelection = false,
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
        }.joinToString(", ")
    }

    private fun getType(
        node: AccessibilityNodeInfoCompat,
        mustRead: Boolean
    ): String? {

        if (!mustRead) return null

        return when (Type.get(node)) {
            Type.NONE -> null
            Type.IMAGE -> context.getString(R.string.text_image_type)
            Type.SWITCH -> {
                val switchType = context.getString(R.string.text_switch_type)
                val selectionStatus = if (node.isChecked) {
                    node.stateDescription?.toString() ?: context.getString(R.string.text_enabled)
                } else {
                node.stateDescription?.toString() ?: context.getString(R.string.text_disabled)
            }
                "$switchType, $selectionStatus"
            }
            Type.TOGGLE -> {
                val toggleType = context.getString(R.string.text_toggle_type)
                val selectionStatus = if (node.isChecked) {
                    node.stateDescription?.toString() ?: context.getString(R.string.text_pressed)
                } else {
                node.stateDescription?.toString() ?: context.getString(R.string.text_not_pressed)
            }
                "$toggleType, $selectionStatus"
            }
            Type.RADIO -> {
                val radioButtonType = context.getString(R.string.text_radio_type)
                val selectionStatus = if (node.isChecked) {
                    node.stateDescription?.toString() ?: context.getString(R.string.text_selected)
                } else {
                    node.stateDescription?.toString() ?: context.getString(R.string.text_not_selected)
                }
                "$radioButtonType, $selectionStatus"
            }
            Type.CHECKBOX -> {
                val checkboxType = context.getString(R.string.text_checkbox_type)
                val selectionStatus = if (node.isChecked) {
                    node.stateDescription?.toString() ?: context.getString(R.string.text_checked)
                } else {
                    node.stateDescription?.toString() ?: context.getString(R.string.text_not_checked)
                }
                "$checkboxType, $selectionStatus"
            }
            Type.BUTTON -> context.getString(R.string.text_button_type)
            Type.EDITFIELD -> context.getString(R.string.text_editfield_type)
            Type.OPTIONS -> context.getString(R.string.text_options_type)
            Type.LIST -> context.getString(R.string.text_list_type)
            Type.TITLE -> context.getString(R.string.text_title_type)
            Type.CHECKEDTEXT -> {
                val selectionStatus = if (node.isChecked) {
                    node.stateDescription?.toString() ?: context.getString(R.string.text_selected)
                } else {
                    ""
                }
                "$selectionStatus"
            }
        }
    }

    private fun getSelection(
        node: AccessibilityNodeInfoCompat,
        mustRead: Boolean
    ) = if (mustRead && node.isSelected) {
        context.getString(R.string.text_selected)
    } else {
        null
    }

    sealed interface Level {

        data class Text(
            val mustReadSelection: Boolean,
            val mustReadType: Boolean,
        ) : Level

        data object Children : Level
    }
}
