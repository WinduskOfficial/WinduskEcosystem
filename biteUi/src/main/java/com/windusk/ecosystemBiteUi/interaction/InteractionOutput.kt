package com.windusk.ecosystemBiteUi.interaction

import android.os.Bundle
import com.windusk.lazybundle.BundleWrapper
import kotlinx.parcelize.Parcelize

@Parcelize
open class InteractionOutput protected constructor(
    override val bundle: Bundle
): BundleWrapper(bundle) {
    companion object: CreationTools<InteractionOutput>(::InteractionOutput)
}