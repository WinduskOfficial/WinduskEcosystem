package com.windusk.ecosystemBasics.sharing.settings.interaction.pageInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionOutput
import kotlinx.parcelize.Parcelize

@Parcelize
class PageInteractionOutput private constructor(
    override val bundle: Bundle
): InteractionOutput(bundle) {
    companion object: CreationTools<PageInteractionOutput>(::PageInteractionOutput)

    init {
        throw IllegalArgumentException("PageInteraction must be processed by client application. Don't use PageInteractionOutput.")
    }

}