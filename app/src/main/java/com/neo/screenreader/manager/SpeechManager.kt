package com.neo.screenreader.manager

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.screenreader.utils.extensions.*
import timber.log.Timber

class SpeechManager(
    private val textToSpeech: TextToSpeech
) {

    fun handlerAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            speak(AccessibilityNodeInfoCompat.wrap(event.source ?: return))
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

    private fun speak(node: AccessibilityNodeInfoCompat) {
        speak(getContent(node))
    }

    fun shutdown() {

        Timber.i("shutdown")

        textToSpeech.shutdown()
    }

    private fun getType(
        node: AccessibilityNodeInfoCompat
    ) = with(node) {
        when {
            isCheckable -> {
                val stateText = if (node.isChecked) {
                    "ativado"
                } else {
                    "desativado"
                }

                "interruptor $stateText"
            }

            isButtonType -> {
                "botão"
            }

            isEditable -> {
                "campo de edição"
            }

            isHeading -> {
                "título"
            }

            else -> ""
        }
    }

    private fun getChildrenContent(
        node: AccessibilityNodeInfoCompat
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
        node: AccessibilityNodeInfoCompat
    ) = with(node) {
        Timber.d("getContent()\n${node.getLog()}")

        val value = contentDescription.ifEmptyOrNull {
            text.ifEmptyOrNull {
                hintText.ifEmptyOrNull {
                    getChildrenContent(node)
                }
            }
        }

        listOf(
            value,
            getType(node),
            hintText?.takeIf { it != value },
            error
        ).filter {
            !it.isNullOrEmpty()
        }.joinToString(", ")
    }

    companion object {

        private const val TTS_INITIALIZATION_ERROR = "TTS_INITIALIZATION_ERROR"

        fun getInstance(context: Context): SpeechManager {

            var speechManager: SpeechManager? = null

            speechManager = SpeechManager(
                TextToSpeech(context) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        speechManager!!.speak(
                            "Screen Reader ativado"
                        )
                    } else {
                        error(TTS_INITIALIZATION_ERROR)
                    }
                }
            )
            return speechManager
        }
    }

}
