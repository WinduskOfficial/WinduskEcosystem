package com.windusk.ecosystemBiteUi.interaction.intInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionOutput
import com.windusk.lazybundle.FloatProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class IntInteractionOutput private constructor(
    override val bundle: Bundle
): InteractionOutput(bundle) {
    companion object: CreationTools<IntInteractionOutput>(::IntInteractionOutput)

    @IgnoredOnParcel
    val newFloat = FloatProperty(this, "new")
}