package com.windusk.clientBasics.service

import com.windusk.ecosystem.BinderMessenger
import com.windusk.clientBasics.service.error.stackTraceToComponentErrorString
import com.windusk.clientBasics.service.messenger.CallbackMessengerImpl

class BinderMessengerWrapper(
    private val binderMessenger: BinderMessenger,
    private val onError: (String) -> Unit
) {
    val ecosystemVersion = try {
        binderMessenger.ecosystemVersion
    } catch (e: Throwable) {
        onError(e.stackTraceToComponentErrorString("Клиентскому приложению не удалось получить версию экосистемы"))
        null
    }

    fun trySetClientMessenger(
        messenger: CallbackMessengerImpl
    ) = try {
        binderMessenger.setClientMessenger(messenger)
    } catch (e: Throwable) {
        onError(e.stackTraceToComponentErrorString("Клиентскому приложению не удалось наладить общение с экосистемой"))
    }

    fun tryChangeState(state: Boolean) = try {
        binderMessenger.changeState(state)
    } catch (e: Throwable) {
        onError(e.stackTraceToComponentErrorString("Клиентскому приложению не удалось изменить состояние компонента"))
    }
}