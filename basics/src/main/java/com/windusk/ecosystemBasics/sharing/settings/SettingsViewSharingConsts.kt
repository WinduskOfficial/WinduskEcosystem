package com.windusk.ecosystemBasics.sharing.settings

import com.windusk.ecosystemBasics.sharing.bite.BiteSharingConsts
import com.windusk.ecosystemBasics.sharing.settings.SettingsViewSharingConsts.KEY_PAGE_MAIN

/**
 * Стандартные константы для страниц настроек компонента.
 * Передаются с помощью поставщиков данных с типом [BiteSharingConsts.DATA_TYPE]
 *
 * Тег: [TAG]
 *
 * Возможные ключи: [KEY_PAGE_MAIN]
 */
object SettingsViewSharingConsts {
    /**
     * Тег, который определяет страницу настроек
     */
    const val TAG = "settings_block_view"

    /**
     * Ключ, по которому передаётся/получается начальная страница настроек компонента
     */
    const val KEY_PAGE_MAIN = "main"
}