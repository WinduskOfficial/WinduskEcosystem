package com.windusk.clientBasics.component

import com.windusk.ecosystem.BinderMessenger
import com.windusk.ecosystem.CallbackMessenger

class ComponentMessenger(
    private val buffer: ComponentBuffer,
    private val binder: BinderMessenger
) {
    val ecosystemVersion by lazy {
        buffer.run("Получение версии экосистемы") {
            binder.ecosystemVersion
        }
    }

    fun setCallbackMessenger(
        newMessenger: CallbackMessenger
    ) {
        buffer.run("Передача компоненту управления экосистемой") {
            binder.setClientMessenger(newMessenger)
        }
    }

    fun setState(
        currentState: Boolean
    ) {
        buffer.run("Установка состояния компонента") {
            binder.changeState(currentState)
        }
    }
}