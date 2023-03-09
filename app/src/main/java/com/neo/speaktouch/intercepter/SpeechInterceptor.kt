package com.neo.speaktouch.intercepter

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import com.neo.speaktouch.intercepter.interfece.Interceptor
import com.neo.speaktouch.model.Type
import com.neo.speaktouch.utils.extensions.*
import timber.log.Timber

class SpeechInterceptor(
    private val textToSpeech: TextToSpeech
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
        return when (Type.get(node)) {
            Type.NONE -> ""
            Type.IMAGE -> "image"
            Type.SWITCH -> "switch: ${if (node.isChecked) "enabled" else "disabled"}"
            Type.TOGGLE -> "toggle button: ${if (node.isChecked) "enabled" else "disabled"}"
            Type.RADIO -> "option button: ${if (node.isChecked) "enabled" else "disabled"}"
            Type.CHECKBOX -> "checkbox: ${if (node.isChecked) "enabled" else "disabled"}"
            Type.CHECKABLE -> "check button: ${if (node.isChecked) "enabled" else "disabled"}"
            Type.BUTTON -> "button"
            Type.EDITABLE -> "edit field"
            Type.OPTIONS -> "options"
            Type.LIST -> "list"
            Type.TITLE -> "title"
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
            stateDescription,
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
                TextToSpeech(context) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        speechInterceptor!!.speak(
                            "Speak Touch ativado"
                        )
                    } else {
                        error(message = "TTS_INITIALIZATION_ERROR")
                    }
                }
            )
            return speechInterceptor
        }
    }

}
