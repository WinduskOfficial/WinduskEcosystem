package com.windusk.componentBasics.sharing.lib.interaction

import android.os.Bundle
import com.windusk.componentBasics.sharing.Sharing
import com.windusk.ecosystem.sharing.SharingInfo
import com.windusk.ecosystemBasics.sharing.interaction.InteractionSharingConsts
import com.windusk.ecosystemBiteUi.InteractionMessenger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class InteractionSharing(
    override val tag: String,
    override val key: String,
    override val info: Flow<SharingInfo>,
    override val enabled: Flow<Boolean> = flowOf(true),
    val interactionFlow: Flow<InteractionMessenger.Stub?>
): Sharing() {
    override val dataType = InteractionSharingConsts.DATA_TYPE
    override val data by lazy {
        interactionFlow.map {
            Bundle().apply {
                putBinder(InteractionSharingConsts.BUNDLE_EXTRA_MESSENGER, it)
            }
        }
    }
}