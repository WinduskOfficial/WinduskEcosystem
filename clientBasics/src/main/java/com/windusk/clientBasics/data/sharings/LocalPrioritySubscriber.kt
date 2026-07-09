package com.windusk.clientBasics.data.sharings

import android.util.Log
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.componentBasics.sharing.subscriber.BasicSubscriber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LocalPrioritySubscriber<T>(
    private val sharingFactory: SharingFactory,
    private val scope: CoroutineScope
): BasicSubscriber<T>() {
    companion object {
        fun <T>create(
            sharingFactory: SharingFactory,
            scope: CoroutineScope,
            delegate: SubscriberDelegate<T>
        ) = LocalPrioritySubscriber<T>(sharingFactory, scope).apply { this.delegate = delegate }
    }

    private var subscribing: Job? = null

    override fun sendUpdateCall() {
        sharingFactory.processSubscriberCallback(this)
    }

    override fun init() {
        subscribing = scope.launch {
            try {
                sharingFactory.addSubscriber(null, this@LocalPrioritySubscriber, subscribition)
            }
            catch(e: NullPointerException) {
                Log.e("", "${e.cause} ${subscribition.dataType.get()} ${subscribition.tag.get()}")
                Log.e("", e.stackTraceToString())
                throw e
            }
            finally {
                sharingFactory.removeSubscriber(this@LocalPrioritySubscriber)
            }
        }
    }

    override fun close() {
        subscribing?.cancel()
    }
}