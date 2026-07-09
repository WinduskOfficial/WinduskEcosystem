package com.windusk.ecosystemBiteUi.interaction.emptyInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionMessengerImpl

class EmptyInteractionMessenger(
    override val info: EmptyInteractionInfo = EmptyInteractionInfo.new(),
    val onInteract: (EmptyInteractionOutput) -> Unit
): InteractionMessengerImpl() {
    override fun interact(interactionOutput: Bundle) = onInteract(
        EmptyInteractionOutput.import(interactionOutput)
    )
}