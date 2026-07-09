package com.windusk.ecosystemBiteUi.view

import android.os.Parcelable
import com.windusk.ecosystemBiteUi.element.block.Block
import kotlinx.parcelize.Parcelize

@Parcelize
class BlockView(
    val info: ViewInfo,
    var blocks: List<Block>
) : Parcelable {
    companion object {
        fun build(
            constructor: BlockView.() -> Unit
        ) = BlockView(
            ViewInfo.new(),
            emptyList()
        ).apply(constructor)
    }

    fun <T : Block>addBlock(constructor: () -> T) = constructor().apply {
        blocks = blocks + this
    }
}