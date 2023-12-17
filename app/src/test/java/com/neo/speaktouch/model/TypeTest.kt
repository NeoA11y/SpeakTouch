/*
 * Local tests of the Type.
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

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neo.speaktouch.view.CustomCheckable
import com.neo.speaktouch.view.CustomList
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class TypeTest {

    private lateinit var controller: ActivityController<Activity>

    @Before
    fun setUp() {
        controller = Robolectric.buildActivity(Activity::class.java).setup()
    }

    @After
    fun tearDown() {
        controller.close()
    }

    @Test
    fun `should return Button from ImageButton`() {

        // given

        val activity = controller.get()

        val imageButton = ImageButton(activity).also {
            activity.setContentView(it)
        }

        val node = imageButton.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Button, type)
    }

    @Test
    fun `should return Image from non-clickable ImageView`() {

        // given

        val activity = controller.get()

        val imageView = ImageView(activity).also {
            activity.setContentView(it)
        }

        val node = imageView.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Image, type)
    }

    @Test
    fun `should return Button from clickable ImageView`() {

        // given

        val activity = controller.get()

        val imageView = ImageView(activity).apply {
            isClickable = true
        }.also {
            activity.setContentView(it)
        }

        val node = imageView.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Button, type)
    }

    @Test
    fun `should return EditField from EditText`() {

        // given

        val activity = controller.get()

        val editText = EditText(activity).also {
            activity.setContentView(it)
        }

        val node = editText.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.EditField, type)
    }

    @Test
    fun `should return Checkable_TextView from CheckedTextView`() {

        // given

        val activity = controller.get()

        val checkedTextView = CheckedTextView(activity).also {
            activity.setContentView(it)
        }

        val node = checkedTextView.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Checkable.TextView, type)
    }

    @Test
    fun `should return Checkable_Switch from Switch`() {

        // given

        val activity = controller.get()

        val switch = Switch(activity).also {
            activity.setContentView(it)
        }

        val node = switch.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Checkable.Switch, type)
    }

    @Test
    fun `should return Checkable_Toggle from ToggleButton`() {

        // given

        val activity = controller.get()

        val toggleButton = ToggleButton(activity).also {
            activity.setContentView(it)
        }

        val node = toggleButton.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Checkable.Toggle, type)
    }

    @Test
    fun `should return Checkable_Radio from RadioButton`() {

        // given

        val activity = controller.get()

        val radioButton = RadioButton(activity).also {
            activity.setContentView(it)
        }

        val node = radioButton.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Checkable.Radio, type)
    }

    @Test
    fun `should return Checkable_Checkbox from CheckBox`() {

        // given

        val activity = controller.get()

        val checkBox = CheckBox(activity).also {
            activity.setContentView(it)
        }

        val node = checkBox.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Checkable.Checkbox, type)
    }

    @Test
    fun `should return Checkable_Custom from custom checkable`() {

        // given

        val activity = controller.get()

        val customCheckable = CustomCheckable(activity).also {
            activity.setContentView(it)
        }

        val node = customCheckable.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Checkable.Custom, type)
    }

    @Test
    fun `should return Type_Button from Button`() {

        // given

        val activity = controller.get()

        val button = Button(activity).also {
            activity.setContentView(it)
        }

        val node = button.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Button, type)
    }

    @Test
    fun `should return Type_List from RecyclerView`() {

        // given

        val activity = controller.get()

        val recyclerView = RecyclerView(activity).also {
            activity.setContentView(it)
        }.apply {
            // needed to trigger LayoutManager.onInitializeAccessibilityNodeInfo
            layoutManager = LinearLayoutManager(activity)
        }

        // needed to trigger RecyclerView.onLayout
        controller.visible()

        val node = recyclerView.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.List, type)
    }

    @Test
    fun `should return Type_List from ListView`() {

        // given

        val activity = controller.get()

        val listView = ListView(activity).also {
            activity.setContentView(it)
        }

        val node = listView.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.List, type)
    }

    @Test
    fun `should return Type_List from custom list`() {

        // given

        val activity = controller.get()

        val customList = CustomList(activity).also {
            activity.setContentView(it)
        }

        val node = customList.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.List, type)
    }

    @Test
    fun `should return Type_DropdownList from Spinner`() {

        // given

        val activity = controller.get()

        val spinner = Spinner(activity).also {
            activity.setContentView(it)
        }

        val node = spinner.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.DropdownList, type)
    }

    @Test
    fun `should return Type_Heading from heading`() {

        // given

        val activity = controller.get()

        val heading = TextView(activity).apply {
            isAccessibilityHeading = true
        }.also {
            activity.setContentView(it)
        }

        val node = heading.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        // then

        assertEquals(Type.Heading, type)
    }
}