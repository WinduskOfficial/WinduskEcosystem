package com.windusk.clientBasics.data.factory

import com.windusk.clientBasics.component.Component
import com.windusk.clientBasics.data.ComponentNamesFactory
import com.windusk.clientBasics.data.savedComponents.ComponentRepository
import com.windusk.clientBasics.data.sharings.SharingFactory
import com.windusk.clientBasics.scope.EcosystemScope
import com.windusk.clientBasics.service.EcosystemClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EcosystemFactory @Inject constructor(
    private val scopes: EcosystemScope,
    val componentNamesFactory: ComponentNamesFactory,
    val componentRepository: ComponentRepository,
    val sharingFactory: SharingFactory
) {
    private val instanceMutable = MutableStateFlow<EcosystemClient?>(null)
    val instance = instanceMutable.asStateFlow()

    fun attachClient(
        client: EcosystemClient?
    ) {
        instanceMutable.tryEmit(client)

        client?.scope?.launch {
            sharingFactory.connectStandart(client.standartSharings)
        }

        client?.scope?.launch {
            sharingFactory.connectConflictTrigger {
                client.onConflict(it)
            }
        }

        client?.scope?.launch {
            componentStates.collect { map ->
                sharingFactory.setWhiteList(
                    map.filter { it.value in listOf(Component.State.DISABLED, Component.State.ENABLED) }
                        .keys.toList()
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val components = combine(
        componentNamesFactory.all,
        instanceMutable
    ) { components, client ->
        components to client
    }.scan(emptyList<Component>()) { old, (componentNames, client) ->
        EcosystemClient.logger.i(
            "UPDATE COMPONENTS",
            "ALL COMPONENTS: ${componentNames.joinToString()}"
        )

        if(client == null) {
            old.forEach { it.close() }
            return@scan emptyList()
        }

        val oldMutable = old.toMutableList()
        val newMutable = mutableListOf<Component>()

        componentNames.forEach { componentName ->
            val existingIndex = oldMutable.indexOfFirst { it.name == componentName }
            val existing = if(existingIndex != -1) oldMutable.removeAt(existingIndex) else null

            newMutable += existing ?: Component(
                componentName,
                client,
                scopes.getChildScope(),
                this
            ).apply { init() }
        }

        oldMutable.forEach {
            EcosystemClient.logger.i("CLOSE OLD COMPONENT ${it.name}")
            it.close()
        }

        return@scan newMutable.toList()
    }.stateIn(
        scopes.scope,
        started = SharingStarted.Eagerly,
        emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val componentStates = components.flatMapLatest { list ->
        val states = list.map { component ->
            component.state.map { component.name to it }
        }

        combine(states) { it.toMap() }
    }
}