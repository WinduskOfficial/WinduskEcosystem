package com.windusk.ecosystemBiteUi.interaction

import android.os.Bundle
import com.windusk.lazybundle.BundleWrapper
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class InteractionInfo protected constructor(override val bundle: Bundle): BundleWrapper(bundle) {
    companion object: CreationTools<InteractionInfo>(::InteractionInfo) {
        const val BUNDLE_DEFAULT_KEY = "key"
    }
    
    @IgnoredOnParcel
    val type = StringProperty(this, "type")
}