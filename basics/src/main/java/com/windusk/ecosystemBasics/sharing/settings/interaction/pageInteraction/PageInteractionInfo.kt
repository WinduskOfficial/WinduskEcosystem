package com.windusk.ecosystemBasics.sharing.settings.interaction.pageInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionInfo
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class PageInteractionInfo private constructor(
    override val bundle: Bundle
): InteractionInfo(bundle) {
    companion object: CreationTools<PageInteractionInfo>(::PageInteractionInfo) {
        const val TYPE = "page"
    }

    init {
        type.set(TYPE)
    }

    @IgnoredOnParcel
    val destination = StringProperty(this, "destin")

}