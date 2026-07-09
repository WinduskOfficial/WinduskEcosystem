package com.windusk.ecosystemBiteUi.element.part.lib

import android.content.Context
import android.os.Bundle
import com.windusk.ecosystemBiteUi.element.part.Part
import com.windusk.ecosystemBiteUi.view.PartView
import com.windusk.ecosystemSharedFile.SharedFileMessenger
import com.windusk.ecosystemSharedFile.SharedFileMessengerImpl
import com.windusk.ecosystemSharedFile.fileDescriptor.ResourceDescriptorCreator
import com.windusk.lazybundle.BinderProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class Bitmap private constructor(
    override val bundle: Bundle
): Part(bundle) {
    companion object: CreationTools<Bitmap>(::Bitmap) {
        const val TYPE = "bitmap"

        fun PartView.bitmap(
            key: String,
            context: Context,
            resId: Int
        ) = addPart {
            new().apply {
                this.key.set(key)
                this.bitmap.set(SharedFileMessengerImpl(ResourceDescriptorCreator(context, resId, "bitmap:$key")))
            }
        }
    }

    init {
        type.set(TYPE)
    }

    @IgnoredOnParcel
    val bitmap = BinderProperty<SharedFileMessenger.Stub, SharedFileMessenger>(
        this,
        "bitmap",
        asInterface = { SharedFileMessenger.Stub.asInterface(it) }
    )
}