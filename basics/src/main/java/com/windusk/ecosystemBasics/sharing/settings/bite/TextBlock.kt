package com.windusk.ecosystemBasics.sharing.settings.bite

import android.os.Bundle
import com.windusk.ecosystemBiteUi.element.block.Block
import com.windusk.ecosystemBiteUi.view.BlockView
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class TextBlock private constructor(
    override val bundle: Bundle
): Block(bundle) {
    companion object: CreationTools<TextBlock>(::TextBlock) {
        const val TYPE = "settings:text_block"

        fun BlockView.settingsTextBlock(
            key: String,

            name: String,
            description: String? = null
        ) = addBlock {
            new().apply {
                this.key.set(key)

                this.name.set(name)
                description?.let { this.description.set(it) }
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

}