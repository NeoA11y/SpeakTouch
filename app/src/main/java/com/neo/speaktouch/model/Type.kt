/*
 * Type nodes.
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
            if (className instanceOf ImageView::class.java) {
                return if (node.isClickable) BUTTON else IMAGE
            }

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

            /* Independent of inheritance */

            if (node.isCheckable) return CHECKABLE

            if (node.isEditable) return EDITABLE

            if (node.collectionInfo != null) return LIST

            if (node.isHeading) return TITLE

            if (node.isClickable) return BUTTON

            return NONE
        }
    }
}