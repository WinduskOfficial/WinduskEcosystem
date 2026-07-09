package com.windusk.biteuirender

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.windusk.biteuirender.spec.BasicRenderSpec
import com.windusk.ecosystemBiteUi.InteractionMessenger

class TestMaterialRenderSpec: BasicRenderSpec() {
    override val bigTitleStyle = TextStyle(fontSize = 40.sp)
    override val mediumTitleStyle = TextStyle(fontSize = 25.sp)
    override val smallTitleStyle = TextStyle(fontSize = 20.sp)
    override val bigTextStyle = TextStyle(fontSize = 16.sp)
    override val mediumTextStyle = TextStyle(fontSize = 14.sp)
    override val smallTextStyle = TextStyle(fontSize = 12.sp)

    @Composable
    override fun Switch(modifier: Modifier, checked: Boolean) {
        Switch(
            modifier = modifier,
            checked = checked,
            onCheckedChange = {}
        )
    }

    @Composable
    override fun Checkbox(modifier: Modifier, checked: Boolean) {
        Checkbox(
            modifier = modifier,
            checked = checked,
            onCheckedChange = {}
        )
    }

    @Composable
    override fun RadioButton(modifier: Modifier, checked: Boolean) {
        RadioButton(
            modifier = modifier,
            selected = checked,
            onClick = {}
        )
    }

    @Composable
    override fun BlockWrapper(content: @Composable (() -> Unit)) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black.copy(alpha = 0.2F))
                .padding(5.dp)
        ) {
            content()
        }
    }

    @Composable
    override fun ColumnWrapper(
        modifier: Modifier,
        verticalArrangement: Arrangement.Vertical,
        horizontalAlignment: Alignment.Horizontal,
        content: @Composable (() -> Unit)
    ) {
        Column(
            modifier,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }

    @Composable
    override fun RowWrapper(
        modifier: Modifier,
        horizontalArrangement: Arrangement.Horizontal,
        verticalAlignment: Alignment.Vertical,
        content: @Composable (() -> Unit)
    ) {
        Row(
            modifier,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            content()
        }
    }

    @Composable
    fun Error(modifier: Modifier, string: String) {
        TODO("Not yet implemented")
    }

    override fun Modifier.enabledModifier(enabled: Boolean) = this
        .alpha(if(enabled) 1F else 0.5F)

    override fun Modifier.interactionModifier(
        enabled: Boolean,
        onClick: InteractionMessenger?,
        onLongPress: InteractionMessenger?
    ): Modifier = this
}