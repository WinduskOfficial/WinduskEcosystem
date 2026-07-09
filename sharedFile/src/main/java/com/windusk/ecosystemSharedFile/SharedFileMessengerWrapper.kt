package com.windusk.ecosystemSharedFile

import android.os.ParcelFileDescriptor

class SharedFileMessengerWrapper private constructor(
    private val sharedFileMessenger: SharedFileMessenger,
    private val onError: (Throwable) -> Unit
) {
    companion object {
        fun create(
            sharedFileMessenger: SharedFileMessenger,
            onError: (Throwable) -> Unit
        ) = SharedFileMessengerWrapper(sharedFileMessenger, onError)
    }

    fun tryGetDescriptor() = try {
        sharedFileMessenger.get()
    } catch (e: Exception) {
        onError(e)
        null
    }

    private fun tryGetFile(
        convert: (ParcelFileDescriptor) -> Unit
    ) = try {
        convert(tryGetDescriptor()!!)
    } catch (e: Exception) {
        onError(e)
    }
}