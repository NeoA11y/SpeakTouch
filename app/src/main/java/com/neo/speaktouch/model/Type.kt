package com.neo.speaktouch.model

import android.widget.*
import com.neo.speaktouch.utils.extensions.NodeInfo
import com.neo.speaktouch.utils.extensions.instanceOf

enum class Type {
    NONE,
    IMAGE,
    SWITCH,
    TOGGLE,
    RADIO,
    CHECKBOX,
    CHECKABLE,
    BUTTON,
    EDITABLE,
    OPTIONS,
    LIST,
    TITLE;

    companion object {
        fun get(node: NodeInfo): Type {
            val className = node.className ?: return NONE

            /* ImageView */

            // View->ImageView->ImageButton
            if (className instanceOf ImageButton::class.java) return BUTTON

            // View->ImageView
            if (className instanceOf ImageView::class.java) return IMAGE

            /* TextView */

            // View->TextView->EditText
            if (className instanceOf EditText::class.java) return EDITABLE

            /* Button */

            // View->TextView->Button->CompoundButton->Switch
            if (className instanceOf Switch::class.java) return SWITCH

            // View->TextView->Button->CompoundButton->ToggleButton
            if (className instanceOf ToggleButton::class.java) return TOGGLE

            // View->TextView->Button->CompoundButton->RadioButton
            if (className instanceOf RadioButton::class.java) return RADIO

            // View->TextView->Button->CompoundButton->CheckBox
            if (className instanceOf CheckBox::class.java) return CHECKBOX

            // View->TextView->Button
            if (className instanceOf Button::class.java) return BUTTON

            /* AdapterView */

            // View->ViewGroup->AdapterView->AbsListView
            if (className instanceOf AbsListView::class.java) return LIST

            // View->ViewGroup->AdapterView->AbsSpinner
            if (className instanceOf AbsSpinner::class.java) return OPTIONS

            /* Additional */

            if (node.isCheckable) return CHECKABLE

            if (node.isEditable) return EDITABLE

            if (node.collectionInfo != null) return LIST

            if (node.isHeading)  return TITLE

            return NONE
        }
    }
}