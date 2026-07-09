package com.windusk.componentBasics.sharing.lib.componentInfo

import com.windusk.componentBasics.sharing.Sharing
import com.windusk.ecosystem.sharing.SharingInfo
import com.windusk.ecosystemBasics.sharing.componentInfo.ComponentInfo
import com.windusk.ecosystemBasics.sharing.componentInfo.ComponentInfoSharingConsts
import com.windusk.ecosystemBasics.sharing.special.SpecialSharingConsts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ComponentInfoSharing(
    override val key: String,
    override val enabled: Flow<Boolean> = flowOf(true),
    val infoFlow: Flow<ComponentInfo>
): Sharing() {
    override val info = flowOf(SharingInfo("", null))
    
    override val dataType = SpecialSharingConsts.DATA_TYPE
    override val tag = ComponentInfoSharingConsts.TAG

    override val data by lazy {
        infoFlow.map {
            it.export()
        }
    }
}