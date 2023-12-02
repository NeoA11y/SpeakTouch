/*
 * Project extensions.
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

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.Properties

fun Project.properties(
    file: File,
    onSuccess: (Properties) -> Unit
) {
    if (file.exists()) {
        onSuccess(
            Properties().apply {
                load(FileInputStream(file))
            }
        )
    }
}

fun Project.appVersion(
    type: VersionConfig.Type? = null,
    block: VersionConfig.() -> Unit
) {

    val version = VersionConfig(type = type).also(block)

    val defaultConfig = android().defaultConfig

    defaultConfig.apply {
        versionCode = version.getCode()
        versionName = version.getName()
    }

    tasks.register("appVersion") {
        doLast {
            println(
                "versionCode is ${defaultConfig.versionCode} " +
                        "from versionName ${defaultConfig.versionName}"
            )
        }
    }
}

fun Project.android(): BaseAppModuleExtension {
    return extensions.getByName("android") as BaseAppModuleExtension
}