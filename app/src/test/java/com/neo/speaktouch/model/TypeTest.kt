package com.neo.speaktouch.model

import android.app.Activity
import android.content.Context
import android.view.View
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
import androidx.recyclerview.widget.RecyclerView
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
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

        val imageButton = ImageButton(activity)

        activity.setContentView(imageButton)

        val node = imageButton.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Button, type)
    }

    @Test
    fun `should return Image from non-clickable ImageView`() {

        val activity = controller.get()

        val imageView = ImageView(activity)

        activity.setContentView(imageView)

        val node = imageView.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Image, type)
    }

    @Test
    fun `should return Button from clickable ImageView`() {

        val activity = controller.get()

        val imageView = ImageView(activity)

        imageView.isClickable = true

        activity.setContentView(imageView)

        val node = imageView.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Button, type)
    }

    @Test
    fun `should return EditField from EditText`() {

        val activity = controller.get()

        val editText = EditText(activity)

        activity.setContentView(editText)

        val node = editText.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.EditField, type)
    }

    @Test
    fun `should return Checkable_TextView from CheckedTextView`() {

        val activity = controller.get()

        val checkedTextView = CheckedTextView(activity)

        activity.setContentView(checkedTextView)

        val node = checkedTextView.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Checkable.TextView, type)
    }

    @Test
    fun `should return Checkable_Switch from Switch`() {

        val activity = controller.get()

        val switch = Switch(activity)

        activity.setContentView(switch)

        val node = switch.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Checkable.Switch, type)
    }

    @Test
    fun `should return Checkable_Toggle from ToggleButton`() {

        val activity = controller.get()

        val toggleButton = ToggleButton(activity)

        activity.setContentView(toggleButton)

        val node = toggleButton.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Checkable.Toggle, type)
    }

    @Test
    fun `should return Checkable_Radio from RadioButton`() {

        val activity = controller.get()

        val radioButton = RadioButton(activity)

        activity.setContentView(radioButton)

        val node = radioButton.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Checkable.Radio, type)
    }

    @Test
    fun `should return Checkable_Checkbox from CheckBox`() {

        val activity = controller.get()

        val checkBox = CheckBox(activity)

        activity.setContentView(checkBox)

        val node = checkBox.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Checkable.Checkbox, type)
    }

    @Test
    fun `should return Checkable_Custom from custom checkable`() {

        val activity = controller.get()

        val customCheckable = CustomCheckable(activity)

        activity.setContentView(customCheckable)

        val node = customCheckable.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Checkable.Custom, type)
    }

    @Test
    fun `should return Type_Button from Button`() {

        val activity = controller.get()

        val button = Button(activity)

        activity.setContentView(button)

        val node = button.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Button, type)
    }

    @Test
    fun `should return Type_List from RecyclerView`() {

        val activity = controller.get()

        val recyclerView = RecyclerView(activity)

        activity.setContentView(recyclerView)

        val node = recyclerView.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.List, type)
    }

    @Test
    fun `should return Type_List from ListView`() {

        val activity = controller.get()

        val listView = ListView(activity)

        activity.setContentView(listView)

        val node = listView.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.List, type)
    }

    @Test
    fun `should return Type_List from custom list`() {

        val activity = controller.get()

        val customList = CustomList(activity)

        activity.setContentView(customList)

        val node = customList.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.List, type)
    }

    @Test
    fun `should return Type_DropdownList from Spinner`() {

        val activity = controller.get()

        val spinner = Spinner(activity)

        activity.setContentView(spinner)

        val node = spinner.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.DropdownList, type)
    }

    @Test
    fun `should return Type_Heading from heading`() {

        val activity = controller.get()

        val heading = TextView(activity).apply {
            isAccessibilityHeading = true
        }

        activity.setContentView(heading)

        val node = heading.createAccessibilityNodeInfo()

        val type = Type.get(AccessibilityNodeInfoCompat.wrap(node))

        assertEquals(Type.Heading, type)
    }

    class CustomList(
        context: Context
    ) : View(context) {

        override fun onInitializeAccessibilityNodeInfo(
            info: AccessibilityNodeInfo
        ) {
            super.onInitializeAccessibilityNodeInfo(info)

            info.collectionInfo = AccessibilityNodeInfo.CollectionInfo(
                1,
                1,
                false
            )
        }
    }

    class CustomCheckable(
        context: Context
    ) : View(context) {

        override fun onInitializeAccessibilityNodeInfo(
            info: AccessibilityNodeInfo
        ) {
            super.onInitializeAccessibilityNodeInfo(info)

            info.isCheckable = true
        }
    }
}