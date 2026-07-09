package com.windusk.clientBasics.tools

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class AppChangeReciever(
    private val onChange: (Status, String) -> Unit
) : BroadcastReceiver() {
    enum class Status {
        ADDED, REMOVED, REPLACED
    }

    private val intentFilter = IntentFilter().apply {
        addAction(Intent.ACTION_PACKAGE_REMOVED)
        addAction(Intent.ACTION_PACKAGE_ADDED)
        addAction(Intent.ACTION_PACKAGE_REPLACED)
        addDataScheme("package")
    }

    override fun onReceive(context: Context, intent: Intent) {
        val packageName = intent.data?.schemeSpecificPart ?: return

        val status = when(intent.action) {
            Intent.ACTION_PACKAGE_ADDED -> Status.ADDED
            Intent.ACTION_PACKAGE_REMOVED -> Status.REMOVED
            Intent.ACTION_PACKAGE_REPLACED -> Status.REPLACED
            else -> null
        }

        status?.let { onChange(status, packageName) }
    }

    fun register(context: Context) = context.registerReceiver(this, intentFilter)
    fun unregister(context: Context) = context.unregisterReceiver(this)
}