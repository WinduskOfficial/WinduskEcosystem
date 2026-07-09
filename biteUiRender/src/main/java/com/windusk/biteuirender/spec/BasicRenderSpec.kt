package com.windusk.biteuirender.spec

import androidx.annotation.CallSuper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.windusk.ecosystemBiteUi.InteractionMessenger
import com.windusk.ecosystemBiteUi.element.block.Block
import com.windusk.ecosystemBiteUi.element.part.Part
import com.windusk.ecosystemBiteUi.element.part.lib.BooleanIndicator
import com.windusk.ecosystemBiteUi.element.part.lib.Text
import com.windusk.ecosystemBiteUi.view.ViewInfo

abstract class BasicRenderSpec: RenderSpec {
    @CallSuper
    override fun renderPart(part: Part): @Composable (() -> Unit)? {
        val clickInteraction = part.clickInteraction.getInterfaceOrNull()
        val longPressInteraction = part.longPressInteraction.getInterfaceOrNull()

        val modifier = Modifier
            .enabledModifier(part.enabled.getOrNull() != false)
            .clickableInteractionModifierOrNull(
                part.enabled.getOrNull() != false,
                clickInteraction,
                longPressInteraction
            )

        when (part.type.getOrNull()) {
            BooleanIndicator.TYPE -> {
                val booleanIndicator = part.exportAs(BooleanIndicator::class)

                val state = booleanIndicator.state.getOrNull() ?: throw IllegalArgumentException("BooleanIndicator.state is null")
                val indicatorType = booleanIndicator.indicatorType.getOrNull() ?: throw IllegalArgumentException("BooleanIndicator.indicatorType is null")

                return {
                    when(indicatorType) {
                        BooleanIndicator.TYPE_SWITCH -> {
                            Switch(
                                modifier,
                                state
                            )
                        }
                        BooleanIndicator.TYPE_CHECKBOX -> {
                            Checkbox(
                                modifier,
                                state
                            )
                        }
                        BooleanIndicator.TYPE_RADIO -> {
                            RadioButton(
                                modifier,
                                state
                            )
                        }
                    }
                }
            }

            Text.TYPE -> {
                val text = part.exportAs(Text::class)
                val string = text.text.getOrNull() ?: throw IllegalArgumentException("Text.text is null")

                val style = when(text.style.getOrNull()) {
                    Text.BIG_TITLE_STYLE -> bigTitleStyle
                    Text.MEDIUM_TITLE_STYLE -> mediumTitleStyle
                    Text.SMALL_TITLE_STYLE -> smallTitleStyle
                    Text.BIG_TEXT_STYLE -> bigTextStyle
                    Text.MEDIUM_TEXT_STYLE -> mediumTextStyle
                    Text.SMALL_TEXT_STYLE -> smallTextStyle
                    else -> throw IllegalArgumentException("Text.style is null")
                }

                return {
                    BasicText(
                        modifier = modifier
                            .wrapContentWidth(Alignment.Start),
                        text = string,
                        style = style
                    )
                }
            }

            else -> return null
        }
    }

    @CallSuper
    override fun renderBlock(block: Block): @Composable (() -> Unit)? = when(block.type.get()) {
        else -> null
    }

    override fun renderBlockWrapper(block: Block): @Composable ((@Composable (() -> Unit)) -> Unit)? = { content ->
        BlockWrapper(content)
    }

    override fun renderViewWrapper(viewInfo: ViewInfo): @Composable ((@Composable (() -> Unit)) -> Unit)? {
        val clickInteraction = viewInfo.clickInteraction.getInterfaceOrNull()
        val longPressInteraction = viewInfo.longPressInteraction.getInterfaceOrNull()

        val modifier = Modifier
            .enabledModifier(viewInfo.enabled.getOrNull() != false)
            .clickableInteractionModifierOrNull(
                viewInfo.enabled.getOrNull() != false,
                clickInteraction,
                longPressInteraction
            )

        when (viewInfo.type.getOrNull()) {
            ViewInfo.COLUMN_TYPE -> {
                return { content ->
                    ColumnWrapper(
                        modifier,
                        getVerticalArrangment(viewInfo),
                        getHorizontalAlignment(viewInfo)
                    ) {
                        content()
                    }
                }
            }
            ViewInfo.ROW_TYPE -> {
                return { content ->
                    RowWrapper(
                        modifier,
                        getHorizontalArrangment(viewInfo),
                        getVerticalAlignment(viewInfo)
                    ) {
                        content()
                    }
                }
            }
            else -> return null
        }
    }

    open fun getHorizontalAlignment(viewInfo: ViewInfo) = when(viewInfo.horizontalAlignment.getOrNull()) {
        ViewInfo.ALIGN_START -> Alignment.Start
        ViewInfo.ALIGN_CENTER -> Alignment.CenterHorizontally
        ViewInfo.ALIGN_END -> Alignment.End
        else -> Alignment.Start
    }

    open fun getVerticalAlignment(viewInfo: ViewInfo) = when(viewInfo.verticalAlignment.getOrNull()) {
        ViewInfo.ALIGN_START -> Alignment.Top
        ViewInfo.ALIGN_CENTER -> Alignment.CenterVertically
        ViewInfo.ALIGN_END -> Alignment.Bottom
        else -> Alignment.Top
    }

    open fun getHorizontalArrangment(viewInfo: ViewInfo): Arrangement.Horizontal {
        return Arrangement.spacedBy(
            viewInfo.contentPadding.getOrNull()?.dp ?: 0.dp,
            getHorizontalAlignment(viewInfo)
        )
    }

    open fun getVerticalArrangment(viewInfo: ViewInfo) = Arrangement.spacedBy(
        viewInfo.contentPadding.getOrNull()?.dp ?: 0.dp,
        getVerticalAlignment(viewInfo)
    )

    abstract val bigTitleStyle: TextStyle
    abstract val mediumTitleStyle: TextStyle
    abstract val smallTitleStyle: TextStyle

    abstract val bigTextStyle: TextStyle
    abstract val mediumTextStyle: TextStyle
    abstract val smallTextStyle: TextStyle

    @Composable
    abstract fun Switch(
        modifier: Modifier,
        checked: Boolean
    )

    @Composable
    abstract fun Checkbox(
        modifier: Modifier,
        checked: Boolean
    )

    @Composable
    abstract fun RadioButton(
        modifier: Modifier,
        checked: Boolean
    )

    @Composable
    abstract fun BlockWrapper(
        content: @Composable () -> Unit
    )

    @Composable
    abstract fun ColumnWrapper(
        modifier: Modifier,
        verticalArrangement: Arrangement.Vertical,
        horizontalAlignment: Alignment.Horizontal,
        content: @Composable () -> Unit,
    )

    @Composable
    abstract fun RowWrapper(
        modifier: Modifier,
        horizontalArrangement: Arrangement.Horizontal,
        verticalAlignment: Alignment.Vertical,
        content: @Composable () -> Unit,
    )

    fun Modifier.clickableInteractionModifierOrNull(
        enabled: Boolean,
        onClickInteraction: InteractionMessenger?,
        onLongPressInteraction: InteractionMessenger?
    ) = if(onClickInteraction == null && onLongPressInteraction == null)
        this
    else
        interactionModifier(
            enabled,
            onClickInteraction,
            onLongPressInteraction
        )

    abstract fun Modifier.enabledModifier(
        enabled: Boolean
    ): Modifier

    abstract fun Modifier.interactionModifier(
        enabled: Boolean,
        onClick: InteractionMessenger?,
        onLongPress: InteractionMessenger?
    ): Modifier
}