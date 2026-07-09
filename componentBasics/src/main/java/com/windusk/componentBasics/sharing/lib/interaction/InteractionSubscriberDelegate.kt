package com.windusk.componentBasics.sharing.lib.interaction

import android.os.Bundle
import android.os.IBinder
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.ecosystem.subscribition.Subscribition
import com.windusk.ecosystemBasics.sharing.interaction.InteractionSharingConsts
import com.windusk.ecosystemBiteUi.InteractionMessenger

class InteractionSubscriberDelegate(
    private val tag: String
): SubscriberDelegate<InteractionMessenger?> {
    override fun getSubscribition() = Subscribition.new().apply {
        dataType.set(InteractionSharingConsts.DATA_TYPE)
        tag.set(this@InteractionSubscriberDelegate.tag)
    }

    override fun formatData(data: Bundle?) = data
        ?.apply { classLoader = IBinder::class.java.classLoader }
        ?.getBinder(InteractionSharingConsts.BUNDLE_EXTRA_MESSENGER)
        ?.let { InteractionMessenger.Stub.asInterface(it) }
}