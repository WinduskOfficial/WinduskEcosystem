package com.windusk.ecosystemBasics.sharing.bite

import android.os.Bundle
import com.windusk.ecosystemBasics.sharing.settings.SettingsViewSharingConsts

/**
 * Стандартные константы для BiteSharing
 *
 * Тип данных: [DATA_TYPE]
 *
 * Возможные теги: [SettingsViewSharingConsts.TAG]
 *
 * Что должно быть внутри [Bundle]:
 * * [BUNDLE_EXTRA_VIEW] — [com.windusk.ecosystemBiteUi.view.BlockView]
 */
object BiteSharingConsts {
    /**
     * Тип данных, который определяет BiteSharing
     */
    const val DATA_TYPE = "bite"

    /**
     * Ключ, по которому передаётся/получается [com.windusk.ecosystemBiteUi.view.BlockView]
     */
    const val BUNDLE_EXTRA_VIEW = "view"
}