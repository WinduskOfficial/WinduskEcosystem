package com.windusk.biteuirender

import androidx.compose.runtime.Composable
import com.windusk.biteuirender.spec.RenderSpec
import com.windusk.biteuirender.spec.ThrowRenderSpec
import com.windusk.ecosystemBiteUi.element.block.Block
import com.windusk.ecosystemBiteUi.element.block.lib.ViewBlock
import com.windusk.ecosystemBiteUi.element.part.Part
import com.windusk.ecosystemBiteUi.element.part.lib.ViewPart
import com.windusk.ecosystemBiteUi.view.BlockView
import com.windusk.ecosystemBiteUi.view.PartView
import java.lang.IllegalStateException

open class Renderer(
    private val specs: List<RenderSpec>,
    private val throwSpec: ThrowRenderSpec
) {
    @Composable
    open fun RenderViewBlock(block: ViewBlock) {
        RenderPartView(block.partView.get())
    }

    @Composable
    open fun RenderViewPart(part: ViewPart) {
        RenderPartView(part.partView.get())
    }

    @Composable
    open fun RenderBlockView(view: BlockView) {
        val wrapper = renderProcess(
            { renderViewWrapper(view.info) },
            { renderViewWrapper(it, view.info) }
        )

        val blocks = view.blocks.map { block ->
            renderProcess(
                {
                    @Composable { RenderBlock(block) }
                },
                {
                    @Composable { RenderBlock(block) }
                }
            )
        }

        wrapper {
            blocks.forEach { it() }
        }
    }

    @Composable
    open fun RenderPartView(view: PartView) {
        val wrapper = renderProcess(
            { renderViewWrapper(view.info) },
            { renderViewWrapper(it, view.info) }
        )
        val parts = view.parts.map { part ->
            renderProcess(
                {
                    @Composable { RenderPart(part) }
                },
                {
                    @Composable { RenderPart(part) }
                }
            )
        }

        wrapper {
            parts.forEach { it() }
        }
    }

    @Composable
    open fun RenderBlock(block: Block) {
        val wrapper = renderProcess(
            { renderBlockWrapper(block) },
            { renderBlockWrapper(it) }
        )

        wrapper {
            if(block.type.getOrNull() == ViewBlock.TYPE)
                RenderViewBlock(block.exportAs(ViewBlock::class))
            else
                renderProcess(
                    { renderBlock(block) },
                    { renderBlock(it, block) }
                ).invoke()
        }
    }

    @Composable
    open fun RenderPart(part: Part) {
        if(part.type.getOrNull() == ViewPart.TYPE)
            return RenderViewPart(part.exportAs(ViewPart::class))
        else
            renderProcess(
                { renderPart(part) },
                { renderPart(it, part) }
            ).invoke()
    }

    @Composable
    fun <T>renderProcess(
        renderAction: RenderSpec.() -> T?,
        throwRenderAction: ThrowRenderSpec.(Exception) -> T
    ) = try {
        specs.firstNotNullOfOrNull { spec ->
            spec.renderAction()
        } ?: throw IllegalStateException("None of the RenderSpecs support this process.")
    } catch (e: Exception) {
        throwSpec.throwRenderAction(e)
    }
}