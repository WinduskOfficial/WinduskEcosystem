package com.windusk.ecosystemBasics.sharing.settings.bite

import android.os.Bundle
import com.windusk.ecosystemBiteUi.InteractionMessenger
import com.windusk.ecosystemBiteUi.element.block.Block
import com.windusk.ecosystemBiteUi.interaction.emptyInteraction.EmptyInteractionMessenger
import com.windusk.ecosystemBiteUi.view.BlockView
import com.windusk.lazybundle.BinderProperty
import com.windusk.lazybundle.BooleanProperty
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class BooleanIndicatorBlock private constructor(
    override val bundle: Bundle
): Block(bundle) {
    companion object: CreationTools<BooleanIndicatorBlock>(::BooleanIndicatorBlock) {
        const val TYPE = "settings:boolean_indicator_block"

        const val INDICATOR_SWITCH = "switch"
        const val INDICATOR_CHECKBOX = "checkbox"
        const val INDICATOR_RADIO = "radio"

        fun BlockView.settingsBooleanIndicatorBlock(
            key: String,

            name: String,
            description: String? = null,

            indicator: String,
            checked: Boolean
        ) = addBlock {
            new().apply {
                this.key.set(key)

                this.name.set(name)
                description?.let { this.description.set(it) }

                this.indicator.set(indicator)
                this.checked.set(checked)
            }
        }

        fun BooleanIndicatorBlock.onIndicatorClicked(
            onClick: () -> Unit,
            onLongPress: (() -> Unit)? = null
        ) = this.apply {
            this.indicatorClickInteraction.set(
                EmptyInteractionMessenger { onClick() }
            )

            onLongPress?.let {
                this.indicatorLongPressInteraction.set(
                    EmptyInteractionMessenger { onLongPress() }
                )
            }
        }
    }

    init {
        type.set(TYPE)
    }

    @IgnoredOnParcel
    val name = StringProperty(this, "name")

    @IgnoredOnParcel
    val description = StringProperty(this, "description")

    @IgnoredOnParcel
    val indicator = StringProperty(this, "indicator")

    @IgnoredOnParcel
    val checked = BooleanProperty(this, "checked")

    @IgnoredOnParcel
    val indicatorClickInteraction = BinderProperty<InteractionMessenger.Stub, InteractionMessenger>(
        this,
        "onIndicatorClick",
        asInterface = { InteractionMessenger.Stub.asInterface(it) }
    )

    @IgnoredOnParcel
    val indicatorLongPressInteraction = BinderProperty<InteractionMessenger.Stub, InteractionMessenger>(
        this,
        "onIndicatorLongPress",
        asInterface = { InteractionMessenger.Stub.asInterface(it) }
    )

}