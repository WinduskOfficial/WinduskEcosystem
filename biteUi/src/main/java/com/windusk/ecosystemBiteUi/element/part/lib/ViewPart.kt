package com.windusk.ecosystemBiteUi.element.part.lib

import android.os.Bundle
import com.windusk.ecosystemBiteUi.element.part.Part
import com.windusk.ecosystemBiteUi.view.PartView
import com.windusk.lazybundle.ParcelableProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class ViewPart protected constructor(
    override val bundle: Bundle
): Part(bundle) {
    companion object: CreationTools<ViewPart>(::ViewPart) {
        const val TYPE = "view_part"

        fun PartView.viewPart(
            key: String,
            partView: PartView
        ) = addPart {
            new().apply {
                this.key.set(key)
                this.partView.set(partView)
            }
        }

        fun PartView.part(
            key: String,
            constructor: PartView.() -> Unit
        ) = viewPart(key, PartView.build(constructor))
    }

    init {
        type.set(TYPE)
    }

    @IgnoredOnParcel
    val partView = ParcelableProperty<PartView>(this, "view")
}