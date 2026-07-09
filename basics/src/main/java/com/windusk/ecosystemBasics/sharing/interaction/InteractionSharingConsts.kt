package com.windusk.ecosystemBasics.sharing.interaction

import android.os.Bundle
import com.windusk.ecosystemBiteUi.InteractionMessenger

/**
 * Стандартные константы для InteractionSharing.
 *
 * Тип данных: [DATA_TYPE]
 *
 * Что должно быть внутри [Bundle]:
 * * [BUNDLE_EXTRA_MESSENGER] — [InteractionMessenger]
 */
object InteractionSharingConsts {
    /**
     * Тип данных, который определяет массив строк
     */
    const val DATA_TYPE = "interaction"

    /**
     * Ключ, по которому передаётся/получается [InteractionMessenger]
     */
    const val BUNDLE_EXTRA_MESSENGER = "messenger"
}