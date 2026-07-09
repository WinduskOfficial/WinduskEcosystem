package com.windusk.componentBasics.sharing.lib.stringList

import android.os.Bundle
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.ecosystem.subscribition.Subscribition
import com.windusk.ecosystemBasics.sharing.stringList.StringListSharingConsts

class StringListSubscriberDelegate(
    private val tag: String
): SubscriberDelegate<List<String>> {
    override fun getSubscribition() = Subscribition.new().apply {
        dataType.set(StringListSharingConsts.DATA_TYPE)
        tag.set(this@StringListSubscriberDelegate.tag)
    }

    override fun formatData(data: Bundle?) = data
        ?.getStringArray(StringListSharingConsts.BUNDLE_EXTRA_STRINGS)
        ?.toList()
}