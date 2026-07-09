package com.windusk.ecosystemBiteUi.interaction.emptyInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionOutput
import kotlinx.parcelize.Parcelize

@Parcelize
class EmptyInteractionOutput private constructor(
    override val bundle: Bundle
): InteractionOutput(bundle) {
    companion object: CreationTools<EmptyInteractionOutput>(::EmptyInteractionOutput)
}