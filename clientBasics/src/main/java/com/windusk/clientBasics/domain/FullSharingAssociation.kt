package com.windusk.clientBasics.domain

import android.content.ComponentName
import android.os.Parcelable
import com.windusk.ecosystem.sharing.SharingAssociation
import com.windusk.ecosystem.subscribition.Subscribition
import kotlinx.parcelize.Parcelize

@Parcelize
data class FullSharingAssociation(
    val packageName: String?,
    val className: String?,
    val dataType: String,
    val tag: String,
    val key: String
): Parcelable {
    companion object {
        fun standart(
            sharingAssociation: SharingAssociation
        ) = fromAssociation(null, sharingAssociation)

        fun fromAssociation(
            name: ComponentName?,
            sharingAssociation: SharingAssociation
        ) = FullSharingAssociation(
            name?.packageName,
            name?.className,
            sharingAssociation.dataType,
            sharingAssociation.tag,
            sharingAssociation.key
        )

        fun Subscribition.toFullAssociation(
            name: ComponentName?,
            key: String,
        ) = fromAssociation(name, toAssociation(key))
    }

    fun isStandart() = getComponentName() == null

    fun getComponentName() = if(packageName != null && className != null) ComponentName(
        packageName,
        className
    ) else null

    fun toAssociation() = SharingAssociation(dataType, tag, key)
}