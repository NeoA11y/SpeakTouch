/*
 * Node Representation.
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

package com.neo.speaktouch.utils

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.speaktouch.utils.extension.getContent
import com.neo.speaktouch.utils.extension.iterator
import com.neo.speaktouch.utils.extension.map
import org.json.JSONArray
import org.json.JSONObject

data class Node(
    val nodeInfo: AccessibilityNodeInfoCompat,
    val content: CharSequence? = nodeInfo.getContent(),
    val children: List<Node> = nodeInfo.iterator().map { Node(it) }.toList()
) {
    fun geJson(): JSONObject {

        val json = JSONObject()

        json.put("content", content)
        json.put("className", nodeInfo.className)
        json.put("isReadableAsChild", NodeValidator.isReadableAsChild(nodeInfo))

        val childrenJson = JSONArray()

        children.forEachIndexed { index, node ->
            childrenJson.put(index, node.geJson())
        }

        json.put("children", childrenJson)

        return json
    }
}
