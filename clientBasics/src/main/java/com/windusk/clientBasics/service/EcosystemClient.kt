package com.windusk.clientBasics.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Intent
import com.windusk.clientBasics.component.Component
import com.windusk.clientBasics.data.factory.EcosystemFactoryEntryPoint
import com.windusk.clientBasics.service.messenger.CallbackMessengerImpl
import com.windusk.componentBasics.sharing.Sharing
import com.windusk.ecosystem.logger.EcosystemLogger
import com.windusk.ecosystem.subscribition.PrioritySubscribition
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@SuppressLint("UnspecifiedRegisterReceiverFlag")
abstract class EcosystemClient : Service() {
    companion object {
        val logger = EcosystemLogger("CLIENT")
    }

    abstract val foregroundNotification: Notification
    open val standartSharings: List<Sharing> = emptyList()

    abstract fun onConflict(subscribition: PrioritySubscribition)

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun getCallbackMessenger(component: Component) = CallbackMessengerImpl(component, factory)

    val factory by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            EcosystemFactoryEntryPoint::class.java
        ).getEcosystemFactory()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        startForeground(1, foregroundNotification)
        factory.attachClient(this)
    }

    override fun onDestroy() {
        factory.attachClient(null)
        scope.cancel()
    }

    override fun onBind(intent: Intent) = null
}