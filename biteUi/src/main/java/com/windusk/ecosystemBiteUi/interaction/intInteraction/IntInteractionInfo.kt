package com.windusk.ecosystemBiteUi.interaction.intInteraction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.interaction.InteractionInfo
import com.windusk.lazybundle.IntProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class IntInteractionInfo private constructor(
    override val bundle: Bundle
): InteractionInfo(bundle) {
    companion object: CreationTools<IntInteractionInfo>(::IntInteractionInfo) {
        const val TYPE = "int"
    }

    init {
        type.set(TYPE)
    }

    @IgnoredOnParcel
    val default = IntProperty(this, BUNDLE_DEFAULT_KEY)

    @IgnoredOnParcel
    val min = IntProperty(this, "min")

    @IgnoredOnParcel
    val max = IntProperty(this, "max")

    fun getRatio() = min.get()..max.get()
    fun setRatio(range: IntRange) {
        min.set(range.start)
        max.set(range.endInclusive)
    }
}