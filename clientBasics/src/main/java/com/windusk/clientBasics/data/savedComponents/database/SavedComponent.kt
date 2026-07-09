package com.windusk.clientBasics.data.savedComponents.database

import android.content.ComponentName
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity(
    tableName = SavedComponent.TABLE_NAME,
    primaryKeys = [SavedComponent.COLUMN_PACKAGE, SavedComponent.COLUMN_CLASS]
)
@Serializable
data class SavedComponent(
    @ColumnInfo(name = COLUMN_PACKAGE) var packageName: String,
    @ColumnInfo(name = COLUMN_CLASS) var className: String,
    @ColumnInfo(name = COLUMN_ALLOWED) var allowed: Boolean,
    @ColumnInfo(name = COLUMN_ENABLED) var enabled: Boolean
) {
    companion object {
        const val TABLE_NAME = "saved_components"
        const val COLUMN_PACKAGE = "pkgName"
        const val COLUMN_CLASS = "clsName"
        const val COLUMN_ALLOWED = "allowed"
        const val COLUMN_ENABLED = "enabled"

        fun getDefault(name: ComponentName) = SavedComponent(
            name.packageName,
            name.className,
            allowed = false,
            enabled = false
        )

        fun SavedComponent.getComponentName() = ComponentName(packageName, className)
    }
}