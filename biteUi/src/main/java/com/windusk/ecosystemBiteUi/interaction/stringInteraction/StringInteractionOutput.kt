package com.windusk.ecosystemBiteUi.interaction.stringInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionOutput
import com.windusk.lazybundle.FloatProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class StringInteractionOutput private constructor(
    override val bundle: Bundle
): InteractionOutput(bundle) {
    companion object: CreationTools<StringInteractionOutput>(::StringInteractionOutput)

    @IgnoredOnParcel
    val newFloat = FloatProperty(this, "new")
}