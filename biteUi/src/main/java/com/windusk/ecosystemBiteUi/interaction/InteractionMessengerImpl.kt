package com.windusk.ecosystemBiteUi.interaction

import com.windusk.ecosystemBiteUi.InteractionMessenger

abstract class InteractionMessengerImpl: InteractionMessenger.Stub() {
    abstract val info: InteractionInfo
    override fun getInteractionInfo() = info.export()
}