package com.windusk.biteuirender.spec

import androidx.compose.runtime.Composable
import com.windusk.ecosystemBiteUi.element.block.Block
import com.windusk.ecosystemBiteUi.element.part.Part
import com.windusk.ecosystemBiteUi.view.ViewInfo

interface ThrowRenderSpec {
    fun renderPart(e: Exception, part: Part): (@Composable () -> Unit)
    fun renderBlock(e: Exception, block: Block): (@Composable () -> Unit)

    fun renderBlockWrapper(e: Exception): (@Composable (@Composable () -> Unit) -> Unit)

    fun renderViewWrapper(
        e: Exception,
        viewInfo: ViewInfo
    ): (@Composable (@Composable () -> Unit) -> Unit)
}