package com.windusk.ecosystem.error

import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper

/**
 * Обработчик критических ошибок компонента.
 */
open class ComponentErrorHandle(
    private val context: Context
): Thread.UncaughtExceptionHandler {
    override fun uncaughtException(p0: Thread, throwable: Throwable) {
        handleError(throwable)
    }

    @CallSuper
    open fun handleError(throwable: Throwable) {
        sendComponentError(throwable)
    }

    private fun sendComponentError(throwable: Throwable) {
        val intent = Intent(ComponentError.ERROR_INTENT)
            .putExtra(
                ComponentError.ERROR_TRACE,
                throwable.stackTraceToString()
            )

        context.sendBroadcast(intent)
    }
}