package com.windusk.clientBasics.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.windusk.ecosystem.error.ComponentError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComponentErrorReceiver(
    private val name: ComponentName,
    private val onError: (errorTrace: String) -> Unit
) : BroadcastReceiver() {
    val intentFilter = IntentFilter(ComponentError.ERROR_INTENT)

    override fun onReceive(context: Context, intent: Intent) {
        if(name.packageName != intent.`package`) return

        CoroutineScope(Dispatchers.Default).launch {
            val errorTrace = intent.getStringExtra(ComponentError.ERROR_TRACE).orEmpty()
            onError(errorTrace)
        }
    }
}