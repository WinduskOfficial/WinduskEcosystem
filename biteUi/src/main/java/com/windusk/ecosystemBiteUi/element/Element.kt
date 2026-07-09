package com.windusk.ecosystemBiteUi.element

import android.os.Bundle
import com.windusk.ecosystemBiteUi.InteractionMessenger
import com.windusk.lazybundle.BinderProperty
import com.windusk.lazybundle.BooleanProperty
import com.windusk.lazybundle.BundleWrapper
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Единица интерфейса в BiteUI. Используется для составления интерфейса.
 *
 * @property key Ключ элемента. Необходим для определения элемента внутри View
 * @property type Тип элемента. Опеределяет, как будет выглядеть элемент внутри View
 */
@Parcelize
open class Element protected constructor(override val bundle: Bundle): BundleWrapper(bundle) {
    companion object: CreationTools<Element>(::Element)

    @IgnoredOnParcel
    val key = StringProperty(this, "key")

    @IgnoredOnParcel
    val type = StringProperty(this, "type")

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
        "onLongPress",
        asInterface = { InteractionMessenger.Stub.asInterface(it) }
    )

    open fun setEnabled(newEnabled: Boolean) = this.apply {
        this.enabled.set(newEnabled)
    }

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