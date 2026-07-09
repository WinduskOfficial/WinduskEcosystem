package com.windusk.ecosystem.logger

import android.util.Log

enum class LogInterface(
    private val sendAction: (tag: String, message: String) -> Unit
) {
    INFO(
        { tag, message ->
            Log.i(tag, message)
        }
    ),
    ERROR(
        { tag, message ->
            Log.e(tag, message)
        }
    ),
    DEBUG(
        { tag, message ->
            Log.d(tag, message)
        }
    ),
    VERBOSE(
        { tag, message ->
            Log.v(tag, message)
        }
    ),
    WARNING(
        { tag, message ->
            Log.w(tag, message)
        }
    ),
    WTF(
        { tag, message ->
            Log.wtf(tag, message)
        }
    );

    fun send(tag: String, message: String) = sendAction(tag, message)
}