package com.windusk.ecosystemBiteUi.interaction.stringInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionMessengerImpl

class StringInteractionMessenger(
    override val info: StringInteractionInfo,
    val onInteract: (StringInteractionInfo) -> Unit
): InteractionMessengerImpl() {
    override fun interact(interactionOutput: Bundle) = onInteract(
         StringInteractionInfo.import(interactionOutput)
    )
}