package com.windusk.ecosystem.error

object ComponentError {
    /**
     * Наменование интента, который отправляется компонентом при возникновении критической ошибки.
     */
    const val ERROR_INTENT = "com.windusk.ecosystem.COMPONENT_ERROR"

    /**
     * Ключ, в котором хранится дорожка критической ошибки.
     */
    const val ERROR_TRACE = "com.windusk.ecosystem.COMPONENT_ERROR_TRACE"
}