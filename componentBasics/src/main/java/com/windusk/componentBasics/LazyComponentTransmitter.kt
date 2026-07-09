package com.windusk.componentBasics

import com.windusk.ecosystem.logger.EcosystemLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class LazyComponentTransmitter<T: LazyComponent> {
    private val lazyComponent = MutableStateFlow<T?>(null)
    private val scope = CoroutineScope(Dispatchers.IO)

    val logger = EcosystemLogger("LazyComponentTransmitter")

    @OptIn(ExperimentalCoroutinesApi::class)
    val work = lazyComponent
        .flatMapLatest {
            it?.work ?: flowOf(false)
        }
        .stateIn(
            scope,
            SharingStarted.Lazily,
            initialValue = false
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val callbackMessenger = lazyComponent
        .flatMapLatest {
            it?.callbackMessenger ?: flowOf(null)
        }
        .stateIn(
            scope,
            SharingStarted.Lazily,
            initialValue = null
        )

    fun setComponent(newService: T?) = lazyComponent.tryEmit(newService)

    fun onInit(component: T) = lazyComponent.tryEmit(component)
    fun onDead() = lazyComponent.tryEmit(null)
}