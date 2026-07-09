package com.windusk.clientBasics.data.prioritySharings.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.windusk.clientBasics.domain.FullSharingAssociation
import kotlinx.serialization.Serializable

@Entity(
    tableName = PrioritySharing.TABLE_NAME,
    primaryKeys = [PrioritySharing.COLUMN_DATA_TYPE, PrioritySharing.COLUMN_TAG]
)
@Serializable
data class PrioritySharing(
    @ColumnInfo(name = COLUMN_DATA_TYPE) val dataType: String,
    @ColumnInfo(name = COLUMN_TAG) val tag: String,
    @ColumnInfo(name = COLUMN_PRIORITY_PACKAGE_NAME) val packageName: String?,
    @ColumnInfo(name = COLUMN_PRIORITY_CLASS_NAME) val className: String?,
    @ColumnInfo(name = COLUMN_PRIORITY_KEY) val key: String
) {
    companion object {
        const val TABLE_NAME = "saved_prioritySharings"
        const val COLUMN_DATA_TYPE = "dataType"
        const val COLUMN_TAG = "tag"
        const val COLUMN_PRIORITY_PACKAGE_NAME = "package"
        const val COLUMN_PRIORITY_CLASS_NAME = "clsName"
        const val COLUMN_PRIORITY_KEY = "key"

        fun fromAssociation(sharingAssociation: FullSharingAssociation) = PrioritySharing(
            sharingAssociation.dataType,
            sharingAssociation.tag,
            sharingAssociation.packageName,
            sharingAssociation.className,
            sharingAssociation.key
        )

        fun PrioritySharing.toAssociation() = FullSharingAssociation(
            packageName, className,
            dataType, tag, key
        )
    }
}