package com.windusk.ecosystemBasics.sharing.settings.interaction.pageInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionMessengerImpl

class PageInteractionMessenger(
    val destination: String
): InteractionMessengerImpl() {
    override val info = PageInteractionInfo.new().apply {
        this.destination.set(this@PageInteractionMessenger.destination)
    }

    override fun interact(interactionOutput: Bundle) {}
}