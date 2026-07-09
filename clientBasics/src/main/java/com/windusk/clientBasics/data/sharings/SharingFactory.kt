package com.windusk.clientBasics.data.sharings

import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import com.windusk.clientBasics.component.Component
import com.windusk.clientBasics.data.prioritySharings.PrioritySharingRepository
import com.windusk.clientBasics.domain.FullSharingAssociation
import com.windusk.componentBasics.sharing.Sharing
import com.windusk.ecosystem.CallbackMessenger
import com.windusk.clientBasics.service.EcosystemClient
import com.windusk.clientBasics.data.sharings.helper.MultiSubscriberHelper
import com.windusk.clientBasics.data.sharings.helper.PrioritySubscriberHelper
import com.windusk.clientBasics.data.sharings.saver.ConflictSaver
import com.windusk.clientBasics.data.sharings.saver.PrioritySaver
import com.windusk.clientBasics.data.sharings.saver.SharingSaver
import com.windusk.clientBasics.data.sharings.saver.SubscriberSaver
import com.windusk.clientBasics.data.sharings.search.PrioritySharingSearch
import com.windusk.clientBasics.data.sharings.search.SearchContext
import com.windusk.clientBasics.scope.EcosystemScope
import com.windusk.ecosystem.logger.EcosystemLogger.Companion.collectWithLog
import com.windusk.ecosystem.logger.LogInterface
import com.windusk.ecosystem.logger.LogOutput
import com.windusk.ecosystem.sharing.SharingAssociation
import com.windusk.ecosystem.sharing.SharingOutput
import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber
import com.windusk.ecosystem.sharing.subscriber.SharingMultiSubscriber
import com.windusk.ecosystem.subscribition.MultiSubscribition
import com.windusk.ecosystem.subscribition.PrioritySubscribition
import com.windusk.ecosystem.subscribition.Subscribition.Companion.canMatchWith
import com.windusk.ecosystem.subscribition.SubscribitionOutput
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.emptyList

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class SharingFactory @Inject constructor(
    ecosystemScope: EcosystemScope,
    repository: PrioritySharingRepository
) {
    private val whiteListComponents = MutableStateFlow(emptyList<ComponentName>())

    init {
        ecosystemScope.scope.launch {
            whiteListComponents.runningFold(emptyList<ComponentName>()) { old, new ->
                val dead = old.filter { it !in new }
                dead.forEach { onComponentDead(it) }

                new
            }.collect()
        }
    }

    private val logger = EcosystemClient.logger.sub("SHARINGS_SERVER")

    private val conflictTriggerMutable = MutableSharedFlow<PrioritySubscribition>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val sharingSaver = SharingSaver()
    val subscriberSaver = SubscriberSaver()
    val prioritySaver = PrioritySaver(
        repository,
        onSaved = { association ->
            updatePriority(association)
        }
    )

    val conflictSaver = ConflictSaver(
        onChoiced = { subscribers ->
            updateChoiced(subscribers)
        }
    )

    private val searchContext = SearchContext(sharingSaver, prioritySaver, conflictSaver)

    private fun updateChoiced(
        subscribers: Set<PrioritySubscriberHelper>
    ) {
        subscribers.forEach { subscriber ->
            val search = PrioritySharingSearch(subscriber, searchContext)
            subscriber.tryUpdate(search.generateOutput(true))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun connectStandart(
        standartSharing: List<Sharing>
    ) {
        val crutchMessenger = getCrutchMessenger(
            onUpdate = { association, output ->
                sharingSaver.onUpdate(association, output)
            }
        )

        standartSharing
            .map { it.getOutputWithId() }.asFlow()
            .flattenMerge(standartSharing.size)
            .collectWithLog(logger, "STANDART SHARINGS") { (association, output) ->
                crutchMessenger.updateSharing(association, output.export())
                LogOutput(LogInterface.INFO, "SAVED OUTPUT\nASSOCIATION: $association\nOUTPUT: ${output.export()}")
            }
    }

    suspend fun connectConflictTrigger(
        onConflict: suspend (PrioritySubscribition) -> Unit
    ) {
        conflictTriggerMutable.collect {
            onConflict(it)
        }
    }

    fun getCrutchMessenger(
        onUpdate: (
            fullAssociation: FullSharingAssociation,
            sharingOutput: SharingOutput
        ) -> Unit
    ) = object : CallbackMessenger.Stub() {
        override fun updateSharing(association: SharingAssociation, sharingOutput: Bundle) {
            val association = FullSharingAssociation.fromAssociation(null, association)
            onUpdate(association, SharingOutput.import(sharingOutput))
        }

        override fun subscribeToSharing(subscriber: PrioritySharingSubscriber, subscribition: Bundle) {}
        override fun unsubscribeFromSharing(subscriber: PrioritySharingSubscriber) {}
        override fun multiSubscribeToSharing(subscriber: SharingMultiSubscriber, subscribition: Bundle) {}
        override fun multiUnsubscribeFromSharing(subscriber: SharingMultiSubscriber) {}
        override fun updateSubscriberCall(subscriber: PrioritySharingSubscriber) {}
    }

    fun updateSharing(
        component: Component,
        association: SharingAssociation,
        sharingOutput: Bundle
    ) {
        val fullAssociation = FullSharingAssociation.fromAssociation(component.name, association)
        val output = SharingOutput.import(sharingOutput)

        updateSharingOutput(fullAssociation, output)
    }

    fun updateSharingOutput(
        association: FullSharingAssociation,
        output: SharingOutput
    ) {
        sharingSaver.onUpdate(association, output)

        val subscribersForUpdate = subscriberSaver.getPriority()
            .filter { subscriber ->
                val match = subscriber.subscribition.canMatchWith(association.toAssociation())
                val updateManually = subscriber.subscribition.updateManually.get()

                match && !updateManually
            }
            .map { subscriber ->
                subscriber to PrioritySharingSearch(subscriber, searchContext).generateOutput()
            }

        val multiSubscribersForUpdate = subscriberSaver.getMulti()
            .filter { subscriber ->
                subscriber.subscribition.canMatchWith(association.toAssociation())
            }

        subscribersForUpdate.forEach {
            it.first.tryUpdate(it.second)
        }

        multiSubscribersForUpdate.forEach {
            it.tryUpdate(association to output)
        }
    }

    private fun updatePriority(
        association: FullSharingAssociation
    ) {
        subscriberSaver.getPriority().forEach { subscriber ->
            val search = PrioritySharingSearch(subscriber, searchContext)
            val status = search.predictStatusFor(association, true)

            if(status != SubscribitionOutput.SharingStatus.PRIORITY_SHARING) return@forEach
            if(search.priority != association) return@forEach

            subscriber.tryUpdate(search.generateOutput())
        }
    }

    private fun onComponentDead(componentName: ComponentName) {
        sharingSaver.onComponentDead(componentName)
        subscriberSaver.onComponentDead(componentName)
        conflictSaver.onComponentDead(componentName)
    }

    fun setWhiteList(components: List<ComponentName>) {
        whiteListComponents.tryEmit(components)
    }

    fun addSubscriber(
        component: Component?,
        subscriber: PrioritySharingSubscriber,
        subscribition: PrioritySubscribition
    ) {
        val helper = subscriberSaver.onPrioritySubscribed(component, subscriber, subscribition)
        updateSubscriberFromSaved(helper)
    }

    fun addMultiSubscriber(
        component: Component?,
        subscriber: SharingMultiSubscriber,
        subscribition: MultiSubscribition
    ) {
        val helper = subscriberSaver.onMultiSubscribed(component, subscriber, subscribition)
        updateMultiSubscriberFromSaved(helper)
    }

    fun removeSubscriber(
        subscriber: PrioritySharingSubscriber
    ) {
        subscriberSaver.onPriorityUnsubscribed(subscriber)
    }

    fun removeMultiSubscriber(
        subscriber: SharingMultiSubscriber
    ) {
        subscriberSaver.onMultiUnsubscribed(subscriber)
    }

    fun processSubscriberCallback(
        subscriber: PrioritySharingSubscriber
    ) {
        val helper = subscriberSaver.getPriority()
            .firstOrNull { it.subscribersEquals(subscriber) }
            ?: return

        Log.w("", "callback!!! $helper")

        val subscribition = helper.subscribition

        Log.w("", "callback!!! ${subscribition.export()}")

        updateSubscriberFromSaved(
            helper,
            canStartConflictsActivity = true
        )
    }

    fun updateSubscriberFromSaved(
        subscriber: PrioritySubscriberHelper,
        mustTakeChoised: Boolean = false,
        canStartConflictsActivity: Boolean = false
    ) {
        val search = PrioritySharingSearch(subscriber, searchContext)
        val output = search.generateOutput(mustTakeChoised)

        if(output.status.get() == SubscribitionOutput.SharingStatus.NEED_CALLBACK && canStartConflictsActivity) {
            conflictTriggerMutable.tryEmit(subscriber.subscribition)
            return
        }

        subscriber.tryUpdate(output)
    }

    fun updateMultiSubscriberFromSaved(subscriber: MultiSubscriberHelper) {
        sharingSaver.getSaved()
            .filter {
                subscriber.subscribition.canMatchWith(it.key.toAssociation())
            }
            .forEach {
                subscriber.tryUpdate(it.toPair())
            }
    }

//    @TestOnly
//    fun logSubscriberData(
//        subscriber: PrioritySubscriberHelper
//    ) {
//        val search = searchContext.getSearch(subscriber)
//
//        logger.d(
//            "SUBSCRIBER LOGS (TYPE: ${subscriber.subscribition.dataType.get()}, TAG: ${subscriber.subscribition.tag.get()})",
//            "COMPONENT: ${subscriber.componentName}",
//            "",
//            "DEFINED: ${search.getChoiced()}",
//            "DEFINED (MUST TAKE CHOISED): ${search.getChoiced(true)}")
//    }

    @TestOnly
    fun sendFakeData(
        subscriber: PrioritySubscriberHelper
    ) {
        val testOutput = SubscribitionOutput.new()
            .apply {
                status.set(SubscribitionOutput.SharingStatus.NEED_CALLBACK)
            }

        subscriber.tryUpdate(testOutput)
    }

    @TestOnly
    fun logMultiSubscriberData(
        multiSubscriber: MultiSubscriberHelper
    ) {
        val defined = sharingSaver.getSaved().filter { multiSubscriber.subscribition.canMatchWith(it.key.toAssociation()) }.toList()

        logger.d(
            "LOG ABOUT SUBSCRIBER (DATATYPE: ${multiSubscriber.subscribition.dataType.get()}, TAG: ${multiSubscriber.subscribition.tag.get()})",
            "COMPONENT: ${multiSubscriber.componentName}",
            "",
            "DEFINED",
            defined.joinToString("\n") {
                "|- SHARING (COMPONENT: ${it.first.getComponentName()}, KEY: ${it.first.key})\n|\n| ENABLED: ${it.second.enabled}\n| OUTPUT: ${it.second.data}\n|-"
            }
        )
    }
}