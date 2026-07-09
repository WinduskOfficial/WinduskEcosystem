package com.windusk.clientBasics.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.windusk.clientBasics.component.Component
import com.windusk.clientBasics.data.factory.EcosystemFactory
import com.windusk.clientBasics.domain.ComponentItem
import com.windusk.clientBasics.tools.readImageFromParcelFileDescriptor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class EcosystemViewModel @Inject constructor(
    private val factory: EcosystemFactory
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val components = factory.components

    @OptIn(ExperimentalCoroutinesApi::class)
    private val sharings = factory.sharingFactory.sharingSaver.getSavedFlow()

//    @OptIn(ExperimentalCoroutinesApi::class)
//    val targets = combine(
//        components,
//        factory.sharingFactory.subscribersSaver.getPriorityFlow(),
//        factory.sharingFactory.subscribersSaver.getMultiFlow()
//    ) { components, priority, multi ->
//        val priorityCounts = priority.groupBy { it.componentName }.mapValues { it.value.size }
//        val multiCounts = multi.groupBy { it.componentName }.mapValues { it.value.size }
//        val keys = (priorityCounts.keys + multiCounts.keys).toSet()
//
//        val counts = keys.associateWith {
//            priorityCounts.getOrDefault(it, 0) + multiCounts.getOrDefault(it, 0)
//        }
//
//        counts.mapNotNull { (componentName, subscribersSize) ->
//            val component = components.firstOrNull { it.name == componentName } ?: return@mapNotNull null
//            ChoiserActivityTarget(component, subscribersSize)
//        }
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val items = components.flatMapLatest { components ->
        components.toItems()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun setAllowed(componentItem: ComponentItem, state: Boolean) {
        getComponent(componentItem)?.setAllowed(state)
    }

    fun setEnabled(componentItem: ComponentItem, state: Boolean) {
        getComponent(componentItem)?.setEnabled(state)
    }

    fun set(componentItem: ComponentItem, allowed: Boolean, enabled: Boolean) {
        getComponent(componentItem)?.set(allowed, enabled)
    }

    fun clearError(componentItem: ComponentItem) {
        getComponent(componentItem)?.clearError()
    }

    private fun getComponent(componentItem: ComponentItem) = factory.components.value
        .firstOrNull { it.name == componentItem.componentName }

    private fun List<Component>.toItems() = combine(
        map { it.getItem() }
    ) { it.toList() }

    @OptIn(FlowPreview::class)
    private fun Component.getItem() = combine(
        state,
        error,
        info,
        getSafetyIcon(),
        pages.debounce(0.2.seconds)
    ) { currentStatus, currentError, currentInfo, currentIcon, currentPages ->
        ComponentItem(
            name,
            currentStatus,
            currentError,
            currentInfo,
            currentIcon,
            currentPages
        )
    }

    private fun Component.getSafetyIcon() = icon.map { sharedFile ->
        sharedFile?.tryGetDescriptor()?.let { readImageFromParcelFileDescriptor(it) }
    }
}
