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

import android.widget.AbsListView
import android.widget.AbsSpinner
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Switch
import android.widget.ToggleButton
import com.neo.speaktouch.utils.`typealias`.NodeInfo
import com.neo.speaktouch.utils.extension.`is`

enum class Type {
    NONE,
    IMAGE,
    SWITCH,
    TOGGLE,
    RADIO,
    CHECKBOX,
    BUTTON,
    EDITFIELD,
    OPTIONS,
    LIST,
    TITLE;

    companion object {
        fun get(node: NodeInfo): Type {
            val className = node.className ?: return NONE

            /* ImageView */

            // View->ImageView->ImageButton
            if (className `is` ImageButton::class.java) return BUTTON

            // View->ImageView
            if (className `is` ImageView::class.java) {
                return if (node.isClickable) BUTTON else IMAGE
            }

            /* TextView */

            // View->TextView->EditText
            if (className `is` EditText::class.java) return EDITFIELD

            /* Button */

            // View->TextView->Button->CompoundButton->Switch
            if (className `is` Switch::class.java) return SWITCH

            // View->TextView->Button->CompoundButton->ToggleButton
            if (className `is` ToggleButton::class.java) return TOGGLE

            // View->TextView->Button->CompoundButton->RadioButton
            if (className `is` RadioButton::class.java) return RADIO

            // View->TextView->Button->CompoundButton->CheckBox
            if (className `is` CheckBox::class.java) return CHECKBOX

            // View->TextView->Button
            if (className `is` Button::class.java) return BUTTON

            /* AdapterView */

            // View->ViewGroup->AdapterView->AbsListView
            if (className `is` AbsListView::class.java) return LIST

            // View->ViewGroup->AdapterView->AbsSpinner
            if (className `is` AbsSpinner::class.java) return OPTIONS

            /* Independent of inheritance */

            if (node.collectionInfo != null) return LIST

            if (node.isHeading) return TITLE

            return NONE
        }
    }
}
