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
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Switch
import android.widget.ToggleButton
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.speaktouch.R
import com.neo.speaktouch.utils.extension.`is`

sealed class Type {

    object Image : Type()

    sealed class Checkable : Type() {
        object Switch : Checkable()

        object Toggle : Checkable()

        object Radio : Checkable()

        object Checkbox : Checkable()

        object TextView : Checkable()

        object Custom : Checkable()
    }

    object Button : Type()

    object EditField : Type()

    object DropdownList : Type()

    object List : Type()

    object Title : Type()

    companion object {
        fun get(node: AccessibilityNodeInfoCompat): Type? {

            val className = node.className ?: return null

            /* ImageView */

            // View -> ImageView -> ImageButton
            if (className `is` ImageButton::class.java) return Button

            // View -> ImageView
            if (className `is` ImageView::class.java) {
                return if (node.isClickable) Button else Image
            }

            /* TextView */

            // View -> TextView -> EditText
            if (className `is` EditText::class.java) return EditField

            // View -> TextView -> CheckedTextView
            if (className `is` CheckedTextView::class.java) return Checkable.TextView

            /* Button */

            // View -> TextView -> Button -> CompoundButton -> Switch
            if (className `is` Switch::class.java) return Checkable.Switch

            // View -> TextView -> Button -> CompoundButton -> ToggleButton
            if (className `is` ToggleButton::class.java) return Checkable.Toggle

            // View -> TextView -> Button -> CompoundButton -> RadioButton
            if (className `is` RadioButton::class.java) return Checkable.Radio

            // View -> TextView -> Button -> CompoundButton -> CheckBox
            if (className `is` CheckBox::class.java) return Checkable.Checkbox

            if (node.isCheckable) return Checkable.Custom

            // View -> TextView -> Button
            if (className `is` android.widget.Button::class.java) return Button

            /* AdapterView */

            // View -> ViewGroup -> AdapterView -> AbsListView
            if (className `is` AbsListView::class.java) return List

            // View -> ViewGroup -> AdapterView -> AbsSpinner
            if (className `is` AbsSpinner::class.java) return DropdownList

            /* Independent of inheritance */

            if (node.collectionInfo != null) return List

            if (node.isHeading) return Title

            return null
        }
    }
}

fun Type.toTypeText() = when (this) {
    Type.Button -> Text(R.string.text_button_type)
    Type.DropdownList -> Text(R.string.text_options_type)
    Type.EditField -> Text(R.string.text_editfield_type)
    Type.Image -> Text(R.string.text_image_type)
    Type.List -> Text(R.string.text_list_type)
    Type.Title -> Text(R.string.text_title_type)
    Type.Checkable.Checkbox -> Text(R.string.text_checkbox_type)
    Type.Checkable.Radio -> Text(R.string.text_radio_type)
    Type.Checkable.Switch -> Text(R.string.text_switch_type)
    Type.Checkable.Toggle -> Text(R.string.text_toggle_type)
    Type.Checkable.TextView -> null /* don't speak type */
    Type.Checkable.Custom -> null /* don't speak type */
}