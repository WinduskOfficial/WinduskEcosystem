package com.windusk.ecosystemSharedFile

import android.os.Bundle
import com.windusk.ecosystemSharedFile.FileSharingConsts.BUNDLE_EXTRA_MESSENGER
import com.windusk.ecosystemSharedFile.FileSharingConsts.DATA_TYPE
import com.windusk.ecosystemSharedFile.FileSharingConsts.IMAGE_TAG
import com.windusk.ecosystemSharedFile.FileSharingConsts.KEY_COMPONENT_ICON

/**
 * Стандартные константы для FileSharing
 *
 * Тип данных: [DATA_TYPE]
 *
 * Возможные теги: [IMAGE_TAG]
 *
 * Возможные ключи: [KEY_COMPONENT_ICON]
 *
 * Что должно быть внутри [Bundle]:
 * * [BUNDLE_EXTRA_MESSENGER] — [com.windusk.ecosystemSharedFile.SharedFileMessenger]
 */
object FileSharingConsts {
    /**
     * Тип данных, который определяет FileSharing
     */
    const val DATA_TYPE = "shared_file"

    /**
     * Ключ, по которому передаётся/получается [com.windusk.ecosystemSharedFile.SharedFileMessenger]
     */
    const val BUNDLE_EXTRA_MESSENGER = "shared_file_messenger"

    /**
     * Тег, который определяет FileSharing, передающий файл растрового изображения
     */
    const val IMAGE_TAG = "image"

    /**
     * FileSharing c тегом [IMAGE_TAG] и ключом [KEY_COMPONENT_ICON] должен передавать основную иконку компонента, желательно в формате 1:1.
     */
    const val KEY_COMPONENT_ICON = "main_component_icon"
}