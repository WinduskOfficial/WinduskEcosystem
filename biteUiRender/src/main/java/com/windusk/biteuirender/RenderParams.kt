package com.windusk.biteuirender

import android.os.Bundle
import androidx.compose.runtime.compositionLocalOf
import com.windusk.lazybundle.BooleanProperty
import com.windusk.lazybundle.BundleWrapper
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class RenderParams private constructor(override val bundle: Bundle): BundleWrapper(bundle) {
    companion object : CreationTools<RenderParams>(::RenderParams) {
        val local = compositionLocalOf<RenderParams> { error("RenderParams are not provided.") }
    }

    @IgnoredOnParcel
    val globalEnabled = BooleanProperty(this, "global_enabled", default = false)
}