package com.windusk.ecosystemBiteUi.view

import android.os.Bundle
import com.windusk.ecosystemBiteUi.InteractionMessenger
import com.windusk.lazybundle.BinderProperty
import com.windusk.lazybundle.BooleanProperty
import com.windusk.lazybundle.BundleWrapper
import com.windusk.lazybundle.IntProperty
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class ViewInfo protected constructor(
    override val bundle: Bundle
): BundleWrapper(bundle) {
    companion object: CreationTools<ViewInfo>(::ViewInfo) {
        const val COLUMN_TYPE = "column"
        const val ROW_TYPE = "row"

        const val ALIGN_START = "start"
        const val ALIGN_CENTER = "center"
        const val ALIGN_END = "end"
    }

    @IgnoredOnParcel
    val type = StringProperty(this, "type")

    @IgnoredOnParcel
    val horizontalAlignment = StringProperty(this, "horizontal_alignment", default = ALIGN_START)

    @IgnoredOnParcel
    val verticalAlignment = StringProperty(this, "vertical_alignment", default = ALIGN_START)

    @IgnoredOnParcel
    val contentPadding = IntProperty(this, "content_padding", default = 0)

    @IgnoredOnParcel
    val enabled = BooleanProperty(this, "enabled", default = true)

    @IgnoredOnParcel
    val clickInteraction = BinderProperty<InteractionMessenger.Stub, InteractionMessenger>(
        this,
        "onClick",
        asInterface = { InteractionMessenger.Stub.asInterface(it) }
    )

    @IgnoredOnParcel
    val longPressInteraction = BinderProperty<InteractionMessenger.Stub, InteractionMessenger>(
        this,
        "longPress",
        asInterface = { InteractionMessenger.Stub.asInterface(it) }
    )

    open fun onClick(
        messenger: InteractionMessenger.Stub
    ) = this.apply {
        clickInteraction.set(messenger)
    }

    open fun onLongPress(
        messenger: InteractionMessenger.Stub
    ) = this.apply {
        longPressInteraction.set(messenger)
    }
}