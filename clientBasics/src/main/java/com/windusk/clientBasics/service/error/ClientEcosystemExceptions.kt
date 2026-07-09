package com.windusk.clientBasics.service.error

/**
 * Ошибки, которые могут выбрасыаться клиентским приложением при работе с компонентами.
 *
 * Это исключение не должно приводить к вылету клиентского приложения. Вместо этого, его нужно обработать, например с помощью [stackTraceToComponentErrorString]
 */
object ClientEcosystemExceptions {
    /**
     * Исключение выбрасывается, если компонент отправил неуместный запрос.
     *
     * Подробнее об исключениях: [ClientEcosystemExceptions]
     */
    class InappropriateRequestException: IllegalStateException()

    /**
     * Исключение выбрасывается, если компонент не совместим с экосистемой клиентского приложения.
     * Подробнее об исключениях: [ClientEcosystemExceptions]
     */
    class ComponentCheckSupportException : IllegalStateException()
}