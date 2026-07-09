package com.windusk.componentBasics.sharing.lib.bite

import com.windusk.ecosystem.sharing.SharingInfo
import com.windusk.ecosystemBiteUi.view.BlockView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class BiteConstructor(
    private val scope: CoroutineScope
) {
    companion object {
        fun create(
            scope: CoroutineScope,
            constructor: BiteConstructor.() -> Unit
        ) = BiteConstructor(scope).apply { constructor() }
    }

    private val views = mutableListOf<BiteBlockViewFlowConstructor>()

    fun view(
        key: String,
        info: Flow<SharingInfo>,
        constructor: BiteBlockViewFlowConstructor.(BlockView) -> Unit
    ) = views.add(
        BiteBlockViewFlowConstructor.create(scope, key, info, constructor)
    )

    fun buildSharings() = views.map {
        it.buildSharing()
    }
}