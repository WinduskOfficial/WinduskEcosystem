package com.windusk.ecosystemBiteUi.interaction.floatInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionMessengerImpl

class FloatInteractionMessenger(
    override val info: FloatInteractionInfo,
    val onInteract: (FloatInteractionOutput) -> Unit
): InteractionMessengerImpl() {
    override fun interact(interactionOutput: Bundle) = onInteract(
        FloatInteractionOutput.import(interactionOutput)
    )
}