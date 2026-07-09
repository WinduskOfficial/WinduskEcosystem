package com.windusk.ecosystemBasics.sharing.componentInfo

import android.os.Bundle
import com.windusk.lazybundle.BundleWrapper
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Декоративные данные компонента.
 */
@Parcelize
class ComponentInfo private constructor(
    override val bundle: Bundle
): BundleWrapper(bundle) {
    companion object : CreationTools<ComponentInfo>(::ComponentInfo) {
        fun create(
            name: String,
            description: String? = null,
            status: String? = null
        ) = new().apply {
            this.name.set(name)
            description?.let { this.description.set(it) }
            status?.let { this.status.set(it) }
        }
    }

    /**
     * Название компонента, должно быть заполнено.
     */
    @IgnoredOnParcel
    val name = StringProperty(
        this,
        key = "name",
        default = "Компонент"
    )

    /**
     * Описание компонента, не обязательно.
     */
    @IgnoredOnParcel
    val description = StringProperty(
        this,
        key = "description"
    )

    /**
     * Актуальный статус компонента, не обязателен.
     */
    @IgnoredOnParcel
    val status = StringProperty(
        this,
        key = "status"
    )
}