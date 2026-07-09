package com.windusk.clientBasics.activity.sharingChoiser.ui

import android.graphics.Bitmap
import com.windusk.clientBasics.domain.FullSharingAssociation

@Suppress("unused")
class ChoiserActivityItem(
    val enabled: Boolean,
    val icon: Bitmap?,
    val name: String,
    val description: String?,
    val association: FullSharingAssociation,
    onSelect: (remember: Boolean) -> Unit
) {
//    suspend fun select(remember: Boolean) {
//        EcosystemFactory.instance.value
//            ?.sharingsModel
//            ?.let {
//                if(remember)
//                    it.conflictsSaver.onChoiced(association)
//                else
//                    it.prioritySaver.onSave(association)
//            }
//    }
}
