package com.windusk.ecosystemBasics.sharing.settings.bite

import android.os.Bundle
import com.windusk.ecosystemBiteUi.element.block.Block
import com.windusk.ecosystemBiteUi.view.BlockView
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class SubtitleBlock private constructor(
    override val bundle: Bundle
): Block(bundle) {
    companion object: CreationTools<SubtitleBlock>(::SubtitleBlock) {
        const val TYPE = "settings:subtitle_block"

        fun BlockView.settingsSubtitleBlock(
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