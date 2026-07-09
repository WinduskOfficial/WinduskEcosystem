package com.windusk.ecosystemBiteUi.element.block

import android.os.Bundle
import com.windusk.ecosystemBiteUi.InteractionMessenger
import com.windusk.ecosystemBiteUi.element.Element
import com.windusk.lazybundle.BooleanProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class Block protected constructor(
    override val bundle: Bundle
): Element(bundle) {
    companion object: CreationTools<Block>(::Block)

    override fun setEnabled(newEnabled: Boolean) = this.apply {
        this.enabled.set(newEnabled)
    }

    override fun onClick(
        messenger: InteractionMessenger.Stub
    ) = this.apply {
        clickInteraction.set(messenger)
    }

    override fun onLongPress(
        messenger: InteractionMessenger.Stub
    ) = this.apply {
        longPressInteraction.set(messenger)
    }
    
}