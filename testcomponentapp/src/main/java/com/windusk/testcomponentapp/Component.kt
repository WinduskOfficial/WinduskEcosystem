package com.windusk.testcomponentapp

import com.windusk.componentBasics.LazyComponent
import com.windusk.ecosystemBasics.sharing.componentInfo.ComponentInfo
import kotlinx.coroutines.flow.flowOf

class Component : LazyComponent() {
    override val info = flowOf(
        ComponentInfo.create(
            name = "Тестовый компонент",
            description = "This is a test component"
        )
    )

    override val iconRes = R.drawable.centaur_icon
}