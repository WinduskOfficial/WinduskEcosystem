package com.windusk.ecosystemBasics.sharing.componentInfo

import android.os.Bundle
import com.windusk.ecosystemBasics.sharing.special.SpecialSharingConsts

/**
 * Стандартные константы для информации компонента.
 * Передаются с помощью поставщиков данных с типом [SpecialSharingConsts.DATA_TYPE]
 *
 * Тег: [TAG]
 *
 * Возможные ключи: [KEY_MAIN]
 *
 * Что должно быть внутри [Bundle]:
 * * Составляется с помощью [ComponentInfo]
 */
object ComponentInfoSharingConsts {
    /**
     * Тег, который определяет информацию компонента
     */
    const val TAG = "component_info"

    /**
     * Ключ, по которому передается/получается основная информация компонента
     */
    const val KEY_MAIN = "main"
}