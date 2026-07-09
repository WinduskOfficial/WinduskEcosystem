package com.windusk.clientBasics.service.error

/**
 * Выдаёт форматированный отчёт об ошибке, случившейся при работе компонента.
 *
 * @param errorComment Комментарий к ошибке. Должен быть понятен пользователю.
 * @return Текст в несколько строк, который можно отобразить пользователю без изменений.
 */
fun Throwable.stackTraceToComponentErrorString(errorComment: String) =
    "$errorComment:\n\n${stackTraceToString()}"