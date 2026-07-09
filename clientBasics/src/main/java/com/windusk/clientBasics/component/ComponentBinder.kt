package com.windusk.clientBasics.component

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.windusk.clientBasics.service.error.ClientEcosystemExceptions
import com.windusk.ecosystem.CENTAUR_ECOSYSTEM_VERSION
import com.windusk.ecosystem.CallbackMessenger
import com.windusk.ecosystem.INTENT_FILTER_COMPONENT
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ComponentBinder(
    private val name: ComponentName,
    private val context: Context,
    private val buffer: ComponentBuffer,
    private val callbackMessenger: CallbackMessenger,
) {
    private var job: Job? = null
    private val mutex = Mutex()

    private val connection = ComponentConection(
        buffer,
        onMessengerChanged = {
            buffer.launch { updateMessenger(it) }
        },
        onConnectionDied = {
            buffer.launch { updateMessenger(null) }
            buffer.throwable(crash = false)
        }
    )

    private val messengerMutable = MutableStateFlow<ComponentMessenger?>(null)
    val messenger = messengerMutable.asStateFlow()

    suspend fun rebind() {
        unbind()
        bind()
    }

    suspend fun bind() = mutex.withLock {
        buffer.run(
            blockName = "Подключение компонента"
        ) {
            context.bindService(
                Intent(INTENT_FILTER_COMPONENT).setComponent(name),
                connection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    suspend fun unbind() = mutex.withLock {
        buffer.run(
            blockName = "Отключение компонента перед повторным подключением",
            crashOnError = false
        ) { context.unbindService(connection) }
    }

    private suspend fun updateMessenger(
        newMessenger: ComponentMessenger?
    ) = mutex.withLock {
        job?.cancel()
        job = buffer.launch {
            messengerMutable.tryEmit(null)
            if(newMessenger == null) return@launch

            val supported = buffer.successRun("Проверка компонента на совместимость с экосистемой") {
                checkSupport(newMessenger)
            }

            if(!supported) return@launch

            messengerMutable.tryEmit(newMessenger)
        }
    }

    fun prepare() {
        buffer.successRun("Подготовка компонента") {
            prepare(messenger.value!!)
        }
    }

    private fun checkSupport(messenger: ComponentMessenger) {
        if(messenger.ecosystemVersion != CENTAUR_ECOSYSTEM_VERSION)
            throw ClientEcosystemExceptions.ComponentCheckSupportException()
    }

    private fun prepare(messenger: ComponentMessenger) {
        messenger.setCallbackMessenger(callbackMessenger)
    }
}