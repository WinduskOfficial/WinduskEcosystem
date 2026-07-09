package com.windusk.componentBasics.sharing.lib.stringList

import android.os.Bundle
import com.windusk.componentBasics.sharing.Sharing
import com.windusk.ecosystem.sharing.SharingInfo
import com.windusk.ecosystemBasics.sharing.stringList.StringListSharingConsts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class StringListSharing(
    override val tag: String,
    override val key: String,
    override val info: Flow<SharingInfo>,
    override val enabled: Flow<Boolean> = flowOf(true),
    val stringsFlow: Flow<List<String>>
): Sharing() {
    override val dataType = StringListSharingConsts.DATA_TYPE
    override val data: Flow<Bundle?> by lazy {
        stringsFlow.map {
            Bundle().apply {
                putStringArray(StringListSharingConsts.BUNDLE_EXTRA_STRINGS, it.toTypedArray())
            }
        }
    }
}