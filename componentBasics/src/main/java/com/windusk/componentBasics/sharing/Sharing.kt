package com.windusk.componentBasics.sharing

import android.os.Bundle
import com.windusk.ecosystem.sharing.SharingAssociation
import com.windusk.ecosystem.sharing.SharingInfo
import com.windusk.ecosystem.sharing.SharingOutput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

abstract class Sharing {
    abstract val dataType: String
    abstract val tag: String
    abstract val key: String

    abstract val info: Flow<SharingInfo>
    abstract val enabled: Flow<Boolean>
    abstract val data: Flow<Bundle?>

    fun getAssociation() = SharingAssociation(dataType, tag, key)

    fun getOutput() = combine(
        info,
        enabled,
        data
    ) { newInfo, newEnabled, newData ->
        SharingOutput.new().apply {
            setInfo(newInfo)
            enabled.set(newEnabled)
            if (newData != null) data.set(newData)
        }
    }

    fun getOutputWithId() = getOutput().map { getAssociation() to it }
}