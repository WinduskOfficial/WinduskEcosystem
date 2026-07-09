package com.windusk.clientBasics.component

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.windusk.ecosystem.BinderMessenger
import com.windusk.ecosystem.logger.EcosystemLogger

class ComponentConection(
    private val buffer: ComponentBuffer,
    private val onMessengerChanged: (ComponentMessenger?) -> Unit,
    private val onConnectionDied: () -> Unit
) : ServiceConnection {
    private val logger = EcosystemLogger("ComponentConection")

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        logger.i("Service connected. Component name: $name")

        val componentMessenger = buffer.run("Получение управления от компонента") {
            ComponentMessenger(
                buffer,
                BinderMessenger.Stub.asInterface(service)
            )
        } ?: return

        onMessengerChanged(componentMessenger)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        logger.i("Service disconnected. Component name: $name")
        onMessengerChanged(null)
    }

    override fun onBindingDied(name: ComponentName) {
        super.onBindingDied(name)
        logger.i("Binding died. Component name: $name")
        onConnectionDied()
    }

    override fun onNullBinding(name: ComponentName?) {
        logger.i("Service cannot bind becose messenger is null. Component name: $name")
        onMessengerChanged(null)
    }
}