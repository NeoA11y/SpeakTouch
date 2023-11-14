/*
 * Coroutines DI.
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

package com.vanuatu.aiqfome.di.modules

import com.neo.speaktouch.di.qualifier.IODispatcher
import com.neo.speaktouch.di.qualifier.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @MainDispatcher
    @Singleton
    @Provides
    fun providesMainCoroutineScope(): CoroutineScope {

        return CoroutineScope(SupervisorJob() + Dispatchers.Main)

    }

    @IODispatcher
    @Singleton
    @Provides
    fun providesIOCoroutineScope(): CoroutineScope {

        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
}
