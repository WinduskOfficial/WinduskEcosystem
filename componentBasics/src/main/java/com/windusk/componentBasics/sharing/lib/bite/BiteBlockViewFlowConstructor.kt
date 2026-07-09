package com.windusk.componentBasics.sharing.lib.bite

import com.windusk.ecosystem.sharing.SharingInfo
import com.windusk.ecosystemBasics.sharing.settings.SettingsViewSharingConsts
import com.windusk.ecosystemBiteUi.view.BlockView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class BiteBlockViewFlowConstructor private constructor(
    private val key: String,
    private val info: Flow<SharingInfo>,
    private val constructor: BiteBlockViewFlowConstructor.(BlockView) -> Unit
) {
    private lateinit var scope: CoroutineScope

    companion object {
        fun create(
            scope: CoroutineScope,
            key: String,
            info: Flow<SharingInfo>,
            constructor: BiteBlockViewFlowConstructor.(BlockView) -> Unit
        ) = BiteBlockViewFlowConstructor(key, info, constructor).apply {
            this.scope = scope
        }
    }

    private val flows = mutableMapOf<String, StateFlow<*>>()

    fun <T>rememberStateFlowValue(key: String, flow: StateFlow<T>): T {
        val stateFlow = flows.getOrPut(key) { flow }

        @Suppress("UNCHECKED_CAST")
        return stateFlow.value as T
    }

    fun <T>rememberFlowValue(key: String, flow: Flow<T>, initialValue: T): T {
        val stateFlow = flows.getOrPut(key) {
            flow.stateIn(
                scope,
                SharingStarted.Eagerly,
                initialValue
            )
        }

        @Suppress("UNCHECKED_CAST")
        return stateFlow.value as T
    }

    fun getKey() = key

    fun buildSharing(): BiteSharing {
        BlockView.build { constructor(this) }

        val sharingFlows = (flows.values + flowOf(Unit)).toTypedArray()

        return BiteSharing(
            SettingsViewSharingConsts.TAG,
            key,
            info,
            blockViewFlow = combine(*sharingFlows) {
                BlockView.build { constructor(this) }
            }
        )
    }
}