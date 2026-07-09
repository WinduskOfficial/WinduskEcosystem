package com.windusk.componentBasics.sharing.lib.bite

import android.os.Bundle
import com.windusk.componentBasics.sharing.Sharing
import com.windusk.ecosystem.sharing.SharingInfo
import com.windusk.ecosystemBasics.sharing.bite.BiteSharingConsts
import com.windusk.ecosystemBiteUi.view.BlockView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class BiteSharing(
    override val tag: String,
    override val key: String,
    override val info: Flow<SharingInfo>,
    override val enabled: Flow<Boolean> = flowOf(true),
    val blockViewFlow: Flow<BlockView>
): Sharing() {
    override val dataType: String = BiteSharingConsts.DATA_TYPE
    override val data: Flow<Bundle?> by lazy {
        blockViewFlow.map {
            Bundle().apply {
                putParcelable(BiteSharingConsts.BUNDLE_EXTRA_VIEW, it)
            }
        }
    }
}