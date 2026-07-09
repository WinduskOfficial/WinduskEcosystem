package com.windusk.componentBasics.sharing.lib.componentInfo

import android.os.Bundle
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.ecosystem.subscribition.Subscribition
import com.windusk.ecosystemBasics.sharing.componentInfo.ComponentInfo
import com.windusk.ecosystemBasics.sharing.componentInfo.ComponentInfoSharingConsts
import com.windusk.ecosystemBasics.sharing.special.SpecialSharingConsts

class ComponentInfoSubscriberDelegate(): SubscriberDelegate<ComponentInfo> {
    override fun getSubscribition() = Subscribition.new().apply {
        dataType.set(SpecialSharingConsts.DATA_TYPE)
        tag.set(ComponentInfoSharingConsts.TAG)
    }

    override fun formatData(data: Bundle?) = data?.let {
        ComponentInfo.import(it)
    }
}