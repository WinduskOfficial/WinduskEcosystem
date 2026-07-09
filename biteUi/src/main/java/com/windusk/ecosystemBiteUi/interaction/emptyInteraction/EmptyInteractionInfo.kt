package com.windusk.ecosystemBiteUi.interaction.emptyInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionInfo
import kotlinx.parcelize.Parcelize

@Parcelize
class EmptyInteractionInfo private constructor(
    override val bundle: Bundle
): InteractionInfo(bundle) {
    companion object: CreationTools<EmptyInteractionInfo>(::EmptyInteractionInfo) {
        const val TYPE = "empty"
    }

    init {
        type.set(TYPE)
    }
}