package com.windusk.ecosystem.sharing

import android.os.Bundle
import com.windusk.ecosystem.subscribition.PrioritySubscribition
import com.windusk.ecosystem.subscribition.Subscribition
import com.windusk.lazybundle.BooleanProperty
import com.windusk.lazybundle.BundleProperty
import com.windusk.lazybundle.BundleWrapper
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Подготовленные данные от поставщика. Используются экосистемой и подписчиками.
 *
 * @property name Декоративное имя поставщика.
 * @property description Декоративное описание поставщика.
 *
 * @property enabled Флаг, указывающий, включен ли поставщик.
 * @property data Данные поставщика.
 */
@Parcelize
class SharingOutput private constructor(
    override val bundle: Bundle
): BundleWrapper(bundle) {
    companion object: CreationTools<SharingOutput>(::SharingOutput)

    @IgnoredOnParcel
    private val name = StringProperty(this, "name")

    @IgnoredOnParcel
    private val description = StringProperty(this, "description")

    @IgnoredOnParcel
    val enabled = BooleanProperty(this, "enabled")

    @IgnoredOnParcel
    val data = BundleProperty(this, "data")

    fun setInfo(info: SharingInfo) = apply {
        name.set(info.name)
        if(info.description != null) description.set(info.description)
    }

    fun getInfo() = SharingInfo(
        name.get(),
        description.getOrNull()
    )
}