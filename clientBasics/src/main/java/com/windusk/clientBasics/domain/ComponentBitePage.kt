package com.windusk.clientBasics.domain

import com.windusk.clientBasics.component.Component
import com.windusk.ecosystemBiteUi.view.BlockView

data class ComponentBitePage(
    val key: String,
    val name: String?,
    val description: String?,
    val error: Component.Error?,
    val view: BlockView?
)