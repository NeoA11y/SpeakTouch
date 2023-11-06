/*
 * App version representation.
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

data class VersionConfig(
    var major: Int = -1, // 0..Infinity
    var minor: Int = -1, // 0..9
    var patch: Int = -1, // 0..9
    var type: Type? = null
) {

    fun getCode(): Int {

        checkIfIsValid()

        val majorValue = major * MAJOR_WEIGHT
        val minorValue = minor * MINOR_WEIGHT
        val patchValue = patch * PATH_WEIGHT

        return majorValue + minorValue + patchValue
    }

    fun getName(): String {

        checkIfIsValid()

        val versionName = "$major.$minor.$patch"

        return when (type!!) {
            Type.RELEASE -> versionName
            Type.DEV -> versionName + "-dev"
        }
    }

    private fun checkIfIsValid() {
        if (major !in 0..Int.MAX_VALUE) {
            throw Exception("Invalid major version")
        }

        if (minor !in 0..9) {
            throw Exception("Invalid minor version")
        }

        if (patch !in 0..9) {
            throw Exception("Invalid patch version")
        }

        if (type == null) {
            throw Exception("Version type not defined")
        }
    }

    enum class Type {
        DEV,
        RELEASE
    }

    companion object {
        private const val MAJOR_WEIGHT = 100
        private const val MINOR_WEIGHT = 10
        private const val PATH_WEIGHT = 1
    }
}