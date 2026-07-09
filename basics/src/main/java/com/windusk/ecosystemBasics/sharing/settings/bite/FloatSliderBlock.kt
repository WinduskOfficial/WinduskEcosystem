package com.windusk.ecosystemBasics.sharing.settings.bite

import android.os.Bundle
import com.windusk.ecosystemBiteUi.InteractionMessenger
import com.windusk.ecosystemBiteUi.element.block.Block
import com.windusk.ecosystemBiteUi.interaction.floatInteraction.FloatInteractionInfo
import com.windusk.ecosystemBiteUi.interaction.floatInteraction.FloatInteractionMessenger
import com.windusk.ecosystemBiteUi.view.BlockView
import com.windusk.lazybundle.BinderProperty
import com.windusk.lazybundle.BooleanProperty
import com.windusk.lazybundle.FloatProperty
import com.windusk.lazybundle.IntProperty
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class FloatSliderBlock private constructor(
    override val bundle: Bundle
): Block(bundle) {
    companion object: CreationTools<FloatSliderBlock>(::FloatSliderBlock) {
        const val TYPE = "settings:slider_block"

        fun BlockView.settingsFloatSliderBlock(
            key: String,

            name: String,
            description: String? = null,

            ratio: ClosedFloatingPointRange<Float>,
            position: Float,

            steps: Int,
            showSteps: Boolean,

            onChange: (Float) -> Unit
        ) = addBlock {
            new().apply {
                this.key.set(key)

                this.name.set(name)
                description?.let { this.description.set(it) }

                sliderInteraction.set(
                    FloatInteractionMessenger(
                        FloatInteractionInfo.new().apply {
                            setRatio(ratio)
                        },
                        onInteract = { info ->
                            info.newFloat.getOrNull()?.let { onChange(it) }
                        }
                    )
                )

                this.position.set(position)

                this.steps.set(steps)
                this.showSteps.set(showSteps)
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
    val sliderInteraction = BinderProperty<InteractionMessenger.Stub, InteractionMessenger>(
        this,
        "onSlider",
        asInterface = { InteractionMessenger.Stub.asInterface(it) }
    )

    @IgnoredOnParcel
    val position = FloatProperty(this, "sliderPosition")

    @IgnoredOnParcel
    val steps = IntProperty(this, "sliderSteps", default = 10)

    @IgnoredOnParcel
    val showSteps = BooleanProperty(this, "sliderShowSteps", default = true)

}