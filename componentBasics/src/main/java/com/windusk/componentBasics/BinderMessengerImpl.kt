package com.windusk.componentBasics

import com.windusk.ecosystem.BinderMessenger
import com.windusk.ecosystem.CENTAUR_ECOSYSTEM_VERSION
import com.windusk.ecosystem.CallbackMessenger
import kotlinx.coroutines.flow.MutableStateFlow

class BinderMessengerImpl(
    private val componentState: MutableStateFlow<Boolean>,
    private val componentCallbackMessenger: MutableStateFlow<CallbackMessenger?>
) : BinderMessenger.Stub() {
    override fun getEcosystemVersion() = CENTAUR_ECOSYSTEM_VERSION

    override fun setClientMessenger(messenger: CallbackMessenger) {
        componentCallbackMessenger.tryEmit(messenger)
    }

    override fun changeState(state: Boolean) {
        componentState.tryEmit(state)
    }
}