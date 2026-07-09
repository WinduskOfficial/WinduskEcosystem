package com.windusk.clientBasics.domain

import android.content.ComponentName
import android.graphics.Bitmap
import com.windusk.clientBasics.component.Component
import com.windusk.ecosystemBasics.sharing.componentInfo.ComponentInfo

data class ComponentItem(
    val componentName: ComponentName,
    val state: Component.State,
    val error: Component.Error?,
    val info: ComponentInfo?,
    val icon: Bitmap?,
    val pages: List<ComponentBitePage>
)