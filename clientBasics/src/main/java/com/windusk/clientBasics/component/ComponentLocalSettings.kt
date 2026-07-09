package com.windusk.clientBasics.component

import android.content.ComponentName
import com.windusk.clientBasics.data.savedComponents.ComponentRepository
import com.windusk.clientBasics.data.savedComponents.database.SavedComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ComponentLocalSettings(
    private val name: ComponentName,
    private val buffer: ComponentBuffer,
    private val repository: ComponentRepository
) {
    private val mutex = Mutex()

    private val mutableSaved = MutableStateFlow(
        SavedComponent(
            name.packageName,
            name.className,
            allowed = false,
            enabled = false
        )
    )

    val saved = mutableSaved.asStateFlow()

    init {
        buffer.launch("Получение локальных настроек компонента") {
            mutableSaved.tryEmit(repository.getRegisteredOrNewRegister(name))
        }
    }

    suspend fun setAllowed(state: Boolean) = set(allowed = state)
    suspend fun setEnabled(state: Boolean) = set(enabled = state)

    suspend fun set(allowed: Boolean? = null, enabled: Boolean? = null) {
        mutex.withLock {
            val new = mutableSaved.value.copy(
                allowed = allowed ?: mutableSaved.value.allowed,
                enabled = enabled ?: mutableSaved.value.enabled
            )

            new.apply {
                mutableSaved.tryEmit(this)
                repository.insert(this)
            }
        }
    }
}