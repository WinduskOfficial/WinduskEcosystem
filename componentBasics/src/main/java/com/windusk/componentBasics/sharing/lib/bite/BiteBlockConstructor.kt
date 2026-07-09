package com.windusk.componentBasics.sharing.lib.bite

import com.windusk.ecosystemBiteUi.element.block.Block

class BiteBlockConstructor private constructor(
    private val key: String,
    private val constructor: Block.() -> Unit
) {
    companion object {
        fun create(
            key: String,
            constructor: Block.() -> Unit
        ) = BiteBlockConstructor(key, constructor)
    }

    fun build() = Block.new().apply {
        this.key.set(this@BiteBlockConstructor.key)
        constructor()
    }
}