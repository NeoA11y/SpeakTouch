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
import com.neo.speaktouch.utils.extensions.*
import timber.log.Timber

class SpeechInterceptor(
    private val textToSpeech: TextToSpeech,
    private val context: Context
) : Interceptor {

    override fun handler(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            speak(NodeInfo.wrap(event.source ?: return))
        }
    }

    fun speak(text: CharSequence) {

        Timber.i("speak: $text")

        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    private fun speak(node: NodeInfo) {
        speak(getContent(node))
    }

    fun shutdown() {

        Timber.i("shutdown")

        textToSpeech.shutdown()
    }

    private fun getType(
        node: NodeInfo
    ): String {

        fun getState() = context.getText(node.isChecked)

        return when (Type.get(node)) {
            Type.NONE -> ""
            Type.IMAGE -> context.getString(R.string.text_image_type)
            Type.SWITCH -> context.getString(R.string.text_switch_type, getState())
            Type.TOGGLE -> context.getString(R.string.text_toggle_type, getState())
            Type.RADIO -> context.getString(R.string.text_radio_type, getState())
            Type.CHECKBOX -> context.getString(R.string.text_checkbox_type, getState())
            Type.CHECKABLE -> context.getString(R.string.text_checkable_type, getState())
            Type.BUTTON -> context.getString(R.string.text_button_type)
            Type.EDITABLE -> context.getString(R.string.text_editable_type)
            Type.OPTIONS -> context.getString(R.string.text_options_type)
            Type.LIST -> context.getString(R.string.text_list_type)
            Type.TITLE -> context.getString(R.string.text_title_type)
        }
    }

    private fun getChildrenContent(
        node: NodeInfo
    ): String {
        return buildList {
            for (index in 0 until node.childCount) {
                val nodeChild = node.getChild(index)

                if (nodeChild.isAvailableForAccessibility) {
                    add(getContent(nodeChild))
                }
            }
        }.joinToString(", ")
    }

    private fun getContent(
        node: NodeInfo
    ) = with(node) {
        Timber.d("getContent\n${node.getLog()}")

        val content = contentDescription.ifEmptyOrNull {
            text.ifEmptyOrNull {
                hintText.ifEmptyOrNull {
                    getChildrenContent(node)
                }
            }
        }

        listOf(
            content,
            getType(node),
            hintText?.takeIf { it != content },
            error
        ).filterNotNullOrEmpty()
            .joinToString(", ")
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
                    } else {
                        error(message = "TTS_INITIALIZATION_ERROR")
                    }
                },
                context = context
            )

            return speechInterceptor
        }
    }

}