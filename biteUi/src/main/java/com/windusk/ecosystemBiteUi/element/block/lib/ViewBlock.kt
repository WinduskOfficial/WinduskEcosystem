package com.windusk.ecosystemBiteUi.element.block.lib

import android.os.Bundle
import com.windusk.ecosystemBiteUi.element.block.Block
import com.windusk.ecosystemBiteUi.view.BlockView
import com.windusk.ecosystemBiteUi.view.PartView
import com.windusk.lazybundle.ParcelableProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class ViewBlock private constructor(
    override val bundle: Bundle
): Block(bundle) {
    companion object: CreationTools<ViewBlock>(::ViewBlock) {
        const val TYPE = "view_block"

        fun BlockView.viewBlock(
            key: String,
            partView: PartView
        ) = addBlock {
            new().apply {
                this.key.set(key)
                this.partView.set(partView)
            }
        }

        fun BlockView.view(
            key: String,
            constructor: PartView.() -> Unit
        ) = viewBlock(key, PartView.build(constructor))
    }

    init {
        type.set(TYPE)
    }

    @IgnoredOnParcel
    val partView = ParcelableProperty<PartView>(this, "view")
}