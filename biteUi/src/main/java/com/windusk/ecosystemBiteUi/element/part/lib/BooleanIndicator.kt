package com.windusk.ecosystemBiteUi.element.part.lib

import android.os.Bundle
import com.windusk.ecosystemBiteUi.element.part.Part
import com.windusk.ecosystemBiteUi.view.PartView
import com.windusk.lazybundle.BooleanProperty
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class BooleanIndicator private constructor(
    override val bundle: Bundle
): Part(bundle) {
    companion object: CreationTools<BooleanIndicator>(::BooleanIndicator) {
        const val TYPE = "boolean_indicator"

        const val TYPE_SWITCH = "switch"
        const val TYPE_CHECKBOX = "checkbox"
        const val TYPE_RADIO = "radio"

        fun PartView.booleanIndicator(
            key: String,
            indicatorType: String,
            state: Boolean
        ) = addPart {
            new().apply {
                this.key.set(key)
                this.indicatorType.set(indicatorType)
                this.state.set(state)
            }
        }
    }

    init {
        type.set(TYPE)
    }

    @IgnoredOnParcel
    val indicatorType = StringProperty(this, "indicator_type")

    @IgnoredOnParcel
    val state = BooleanProperty(this, "state")
}