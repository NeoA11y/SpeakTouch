package com.neo.test.checkable

import android.content.Context
import android.util.AttributeSet
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Checkable
import androidx.appcompat.widget.AppCompatTextView

class CustomCheckable(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatTextView(context, attrs), Checkable {

    private var checked = false

    private val CHECKED_STATE_SET = intArrayOf(
        android.R.attr.state_checked
    )

    init {
        isClickable = true
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)

        info.isCheckable = true
        info.isChecked = isChecked
    }

    override fun setChecked(checked: Boolean) {
        this.checked = checked

        refreshDrawableState()
    }

    override fun isChecked(): Boolean {
        return checked
    }

    override fun toggle() {
        isChecked = !isChecked
    }

    override fun performClick(): Boolean {
        toggle()

        return super.performClick()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)

        if (isChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }

        return drawableState
    }
}
