package com.neo.screenreader.manager

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.screenreader.utils.extensions.ifEmptyOrNull
import com.neo.screenreader.utils.extensions.isButtonType
import com.neo.screenreader.utils.extensions.isReadable

class SpeechManager(
    private val tts: TextToSpeech
) {

    fun speak(text: CharSequence) = tts.speak(
        text,
        TextToSpeech.QUEUE_FLUSH,
        null,
        null
    )

    fun shutdown() = tts.shutdown()

    fun handlerAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            speak(AccessibilityNodeInfoCompat.wrap(event.source ?: return))
        }
    }

    private fun speak(node: AccessibilityNodeInfoCompat) {
        // (content or text or hint) : type : error : hint
    }

    private fun getChildrenContent(
        node: AccessibilityNodeInfoCompat
    ) = with(node) {
        buildList {
            for (index in 0 until childCount) {
                val nodeChild = getChild(index)

                if (nodeChild.isReadable) {
                    add(getContent(nodeChild))
                }
            }
        }.joinToString(", ")
    }

    private fun getContent(
        node: AccessibilityNodeInfoCompat
    ) = with(node) {
        contentDescription.ifEmptyOrNull {
            text.ifEmptyOrNull {
                hintText ?: ""
            }
        }
    }

    companion object {

        fun getInstance(context: Context): SpeechManager {

            var speechManager: SpeechManager? = null

            speechManager = SpeechManager(
                TextToSpeech(context) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        speechManager!!.speak(
                            "Screen Reader ativado"
                        )
                    } else {
                        error("TTS_INITIALIZATION_ERROR")
                    }
                }

            )
            return speechManager
        }
    }

}