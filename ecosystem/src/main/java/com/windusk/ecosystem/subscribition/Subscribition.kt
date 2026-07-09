package com.windusk.ecosystem.subscribition

import android.os.Bundle
import com.windusk.ecosystem.sharing.SharingAssociation
import com.windusk.lazybundle.BundleWrapper
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Запрос к экосистеме для сохранения подписчика. Даёт клиенту понять, какие данные нужно отправлять.
 *
 * @property dataType Тип данных, которые передаются поставщиком.
 * @property tag Назначение данных поставщика.
 */
@Parcelize
open class Subscribition protected constructor(
    override val bundle: Bundle
): BundleWrapper(bundle) {
    companion object : CreationTools<Subscribition>(::Subscribition) {
        fun Subscribition.canMatchWith(association: SharingAssociation) = dataType.get() == association.dataType && tag.get() == association.dataType
    }

    @IgnoredOnParcel
    val dataType = StringProperty(this, "type")

    @IgnoredOnParcel
    val tag = StringProperty(this, "tag")

    fun toAssociation(key: String) = SharingAssociation(dataType.get(), tag.get(), key)
}