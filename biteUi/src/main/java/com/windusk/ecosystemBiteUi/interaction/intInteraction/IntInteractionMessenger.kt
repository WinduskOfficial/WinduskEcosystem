package com.windusk.ecosystemBiteUi.interaction.intInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionMessengerImpl

class IntInteractionMessenger(
    override val info: IntInteractionInfo,
    val onInteract: (IntInteractionInfo) -> Unit
): InteractionMessengerImpl() {
    override fun interact(interactionOutput: Bundle) = onInteract(
        IntInteractionInfo.import(interactionOutput)
    )
}