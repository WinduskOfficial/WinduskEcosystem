package com.windusk.ecosystemBiteUi.interaction.floatInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionInfo
import com.windusk.lazybundle.FloatProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class FloatInteractionInfo private constructor(
    override val bundle: Bundle
): InteractionInfo(bundle) {
    companion object: CreationTools<FloatInteractionInfo>(::FloatInteractionInfo) {
        const val TYPE = "int"
    }

    init {
        type.set(TYPE)
    }

    @IgnoredOnParcel
    val default = FloatProperty(this, BUNDLE_DEFAULT_KEY)

    @IgnoredOnParcel
    val min = FloatProperty(this, "min")

    @IgnoredOnParcel
    val max = FloatProperty(this, "max")

    fun getRatio() = min.get()..max.get()
    fun setRatio(range: ClosedFloatingPointRange<Float>) {
        min.set(range.start)
        max.set(range.endInclusive)
    }
}