/*
 * Speak focused content.
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

package com.neo.speaktouch.intercepter

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import com.neo.speaktouch.R
import com.neo.speaktouch.intercepter.interfece.Interceptor
import com.neo.speaktouch.model.Type
import com.neo.speaktouch.utils.extension.filterNotNullOrEmpty
import com.neo.speaktouch.utils.extension.getString
import com.neo.speaktouch.utils.extension.getText
import com.neo.speaktouch.utils.extension.ifEmptyOrNull
import com.neo.speaktouch.utils.extension.iterator
import com.neo.speaktouch.utils.`object`.NodeValidator
import com.neo.speaktouch.utils.`typealias`.NodeInfo
import timber.log.Timber

class SpeechInterceptor(
    private val textToSpeech: TextToSpeech,
    private val context: Context
) : Interceptor {

    override fun handle(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            speak(NodeInfo.wrap(event.source ?: return))
        }
    }

    fun speak(text: CharSequence) {

        Timber.i("speak:\"$text\"")

        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    private fun speak(nodeInfo: NodeInfo) {

        Timber.i("speak:${nodeInfo.getString()}")

        speak(
            getContent(
                nodeInfo = nodeInfo,
                mustReadSelection = true,
                mustReadType = true,
                mustReadCheckable = true
            )
        )
    }

    fun shutdown() {

        Timber.i("shutdown")

        textToSpeech.shutdown()
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

    private fun getChildrenContent(
        nodeInfo: NodeInfo
    ): String {
        return buildList {
            for (nodeChild in nodeInfo) {

                if (!NodeValidator.isValidForAccessible(nodeChild)) continue

                if (NodeValidator.isChildReadable(nodeChild)) {
                    add(
                        getContent(
                            nodeInfo = nodeChild,
                            mustReadCheckable = true,
                            mustReadType = listOf(
                                Type.SWITCH,
                                Type.TOGGLE,
                                Type.RADIO,
                                Type.CHECKBOX
                            ).any { it == Type.get(nodeChild) }
                        )
                    )
                }
            }
        }.joinToString(", ")
    }

    private fun getContent(
        nodeInfo: NodeInfo,
        mustReadSelection: Boolean = false,
        mustReadType: Boolean = false,
        mustReadCheckable: Boolean = false
    ) = with(nodeInfo) {
        val content = contentDescription.ifEmptyOrNull {
            text.ifEmptyOrNull {
                hintText.ifEmptyOrNull {
                    getChildrenContent(nodeInfo)
                }
            }
        }

        listOf(
            content,
            getType(nodeInfo, mustReadType),
            getCheckable(nodeInfo, mustReadCheckable),
            getSelection(nodeInfo, mustReadSelection)
        ).filterNotNullOrEmpty()
            .joinToString(", ")
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

    companion object {

        fun getInstance(context: Context): SpeechInterceptor {

            var speechInterceptor: SpeechInterceptor? = null

            speechInterceptor = SpeechInterceptor(
                textToSpeech = TextToSpeech(context) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        speechInterceptor!!.speak(
                            "Speak Touch ${context.getText(true)}"
                        )
                    }
                },
                context = context
            )

            return speechInterceptor
        }
    }

}
