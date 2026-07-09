package com.windusk.componentBasics.sharing.lib.bite

import android.os.Bundle
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.ecosystem.subscribition.Subscribition
import com.windusk.ecosystemBasics.sharing.bite.BiteSharingConsts
import com.windusk.ecosystemBiteUi.view.BlockView

class BiteSubscriberDelegate(
    private val tag: String
): SubscriberDelegate<BlockView> {
    override fun getSubscribition() = Subscribition.new().apply {
        dataType.set(BiteSharingConsts.DATA_TYPE)
        tag.set(this@BiteSubscriberDelegate.tag)
    }

    override fun formatData(data: Bundle?) = data
        ?.apply { classLoader = BlockView::class.java.classLoader }
        ?.getParcelable(BiteSharingConsts.BUNDLE_EXTRA_VIEW) as BlockView?
}