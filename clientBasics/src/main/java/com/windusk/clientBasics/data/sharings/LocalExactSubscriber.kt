package com.windusk.clientBasics.data.sharings

import android.content.ComponentName
import android.util.Log
import androidx.annotation.CallSuper
import com.windusk.clientBasics.domain.FullSharingAssociation
import com.windusk.clientBasics.data.sharings.saver.SharingSaver
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.componentBasics.sharing.subscriber.BasicSubscriber
import com.windusk.ecosystem.logger.EcosystemLogger
import com.windusk.ecosystem.logger.EcosystemLogger.Companion.collectWithLog
import com.windusk.ecosystem.logger.LogInterface
import com.windusk.ecosystem.logger.LogOutput
import com.windusk.ecosystem.sharing.SharingAssociation
import com.windusk.ecosystem.subscribition.SubscribitionOutput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LocalExactSubscriber<T>(
    private val sharingSaver: SharingSaver,
    private val scope: CoroutineScope,
    private val componentName: ComponentName?,
    private val key: String
): BasicSubscriber<T>() {
    companion object {
        fun <T>create(
            sharingSaver: SharingSaver,
            scope: CoroutineScope,
            delegate: SubscriberDelegate<T>,
            componentName: ComponentName?,
            key: String
        ) = LocalExactSubscriber<T>(
            sharingSaver,
            scope,
            componentName,
            key
        ).apply { this.delegate = delegate }
    }

    private var subscribing: Job? = null

    private val targetAssociation by lazy {
        FullSharingAssociation.fromAssociation(
            componentName,
            SharingAssociation(subscribition.dataType.get(), subscribition.tag.get(), key)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val targetSharing by lazy {
        getOutputFlow(sharingSaver)
    }

    override fun sendUpdateCall() {}

    @OptIn(ExperimentalCoroutinesApi::class)
    @CallSuper
    override fun init() {
        subscribing = scope.launch {
            try {
                targetSharing.collectWithLog(EcosystemLogger("LocalExactSubscriber"), "SAVED SHARING") { it ->
                    val newStatus = if(it == null) SubscribitionOutput.SharingStatus.HAVENT_SHARING else SubscribitionOutput.SharingStatus.PRIORITY_SHARING
                    val newData = it?.data?.get()

                    onUpdate(
                        SubscribitionOutput.new().apply {
                            status.set(newStatus)
                            newData?.let { data.set(it) }
                        }.export()
                    )

                    return@collectWithLog LogOutput(LogInterface.INFO, "GET NEW SHARING OUTPUT\nPACKAGE: ${componentName?.packageName}\nCLASS: ${componentName?.className}\n\nSTATUS: $newStatus\nDATA: $newData")
                }
            }
            catch(e: NullPointerException) {
                Log.e("", "${e.cause} ${subscribition.dataType.get()} ${subscribition.tag.get()}")
                Log.e("", e.stackTraceToString())
                throw e
            }
        }
    }

    @CallSuper
    override fun close() {
        subscribing?.cancel()
        subscribing = null
    }

    private fun getOutputFlow(sharingSaver: SharingSaver) = sharingSaver.getSavedFlow().map {
        it.getOrDefault(targetAssociation, null)
    }
}