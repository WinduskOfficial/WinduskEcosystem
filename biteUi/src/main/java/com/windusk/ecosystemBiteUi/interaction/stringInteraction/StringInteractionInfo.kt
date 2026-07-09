package com.windusk.ecosystemBiteUi.interaction.stringInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionInfo
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class StringInteractionInfo private constructor(
    override val bundle: Bundle
): InteractionInfo(bundle) {
    companion object: CreationTools<StringInteractionInfo>(::StringInteractionInfo) {
        const val TYPE = "string"
    }

    init {
        type.set(TYPE)
    }

    @IgnoredOnParcel
    val default = StringProperty(this, BUNDLE_DEFAULT_KEY)
}