package com.windusk.clientBasics.data.sharings

import android.util.Log
import androidx.annotation.CallSuper
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.componentBasics.sharing.multiSubscriber.BasicMultiSubscriber
import com.windusk.componentBasics.sharing.multiSubscriber.SubscriberForMultiSubscriber
import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocalMultiSubscriber<T>(
    private val sharingFactory: SharingFactory,
    private val scope: CoroutineScope
): BasicMultiSubscriber<T>() {
    companion object {
        fun <T>create(
            sharingFactory: SharingFactory,
            scope: CoroutineScope,
            delegate: SubscriberDelegate<T>
        ) = LocalMultiSubscriber<T>(sharingFactory, scope).apply { this.delegate = delegate }
    }

    private val subscribersMutable = MutableStateFlow<List<SubscriberForMultiSubscriber>>(emptyList())
    private var subscribing: Job? = null

    @CallSuper
    override fun init() {
        subscribing = scope.launch {
            try {
                sharingFactory.addMultiSubscriber(null, this@LocalMultiSubscriber, subscribition)
            }
            catch(e: NullPointerException) {
                Log.e("", "${e.cause} ${subscribition.dataType.get()} ${subscribition.tag.get()}")
                Log.e("", e.stackTraceToString())
                throw e
            }
            finally {
                sharingFactory.removeMultiSubscriber(this@LocalMultiSubscriber)
            }
        }
    }

    @CallSuper
    override fun close() {
        subscribing?.cancel()
        subscribing = null
    }

    override fun killSubscriber(subscriber: PrioritySharingSubscriber?) {
        subscribersMutable.update { oldSubscribers ->
            oldSubscribers.filter { it != subscriber }
        }
    }
}