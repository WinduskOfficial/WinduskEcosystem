package com.windusk.ecosystemBiteUi.interaction.floatInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionOutput
import com.windusk.lazybundle.FloatProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class FloatInteractionOutput private constructor(
    override val bundle: Bundle
): InteractionOutput(bundle) {
    companion object: CreationTools<FloatInteractionOutput>(::FloatInteractionOutput)

    @IgnoredOnParcel
    val newFloat = FloatProperty(this, "new")
}