package com.neo.screenreader.intercepter

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import com.neo.screenreader.intercepter.interfece.Interceptor
import com.neo.screenreader.utils.extensions.*
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

            isImageType-> {
                "imagem"
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

        fun getInstance(context: Context): SpeechInterceptor {

            var speechInterceptor: SpeechInterceptor? = null

            speechInterceptor = SpeechInterceptor(
                TextToSpeech(context) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        speechInterceptor!!.speak(
                            "Screen Reader ativado"
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
