package com.windusk.biteuirender.spec

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.windusk.ecosystemBiteUi.element.block.Block
import com.windusk.ecosystemBiteUi.element.part.Part
import com.windusk.ecosystemBiteUi.view.ViewInfo

interface RenderSpec {
    fun renderPart(part: Part): (@Composable () -> Unit)?
    fun renderBlock(block: Block): (@Composable () -> Unit)?

    fun renderBlockWrapper(block: Block): (@Composable (@Composable () -> Unit) -> Unit)?

    fun renderViewWrapper(
        viewInfo: ViewInfo
    ): (@Composable (@Composable () -> Unit) -> Unit)?
}