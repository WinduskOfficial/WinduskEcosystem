package com.windusk.componentBasics.sharing.lib.sharedFile

import android.os.Bundle
import android.os.IBinder
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.ecosystem.subscribition.Subscribition
import com.windusk.ecosystemSharedFile.FileSharingConsts
import com.windusk.ecosystemSharedFile.SharedFileMessenger
import com.windusk.ecosystemSharedFile.SharedFileMessengerWrapper

class FileSubscriberDelegate(
    private val tag: String,
    private val onError: (Throwable) -> Unit
): SubscriberDelegate<SharedFileMessengerWrapper> {
    override fun getSubscribition() = Subscribition.new().apply {
        dataType.set(FileSharingConsts.DATA_TYPE)
        tag.set(this@FileSubscriberDelegate.tag)
    }

    override fun formatData(data: Bundle?) = data
        ?.apply { classLoader = IBinder::class.java.classLoader }
        ?.getBinder(FileSharingConsts.BUNDLE_EXTRA_MESSENGER)
        ?.let { SharedFileMessenger.Stub.asInterface(it) }
        ?.let { SharedFileMessengerWrapper.create(it, onError) }
}