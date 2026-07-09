package com.windusk.ecosystemBasics.sharing.stringList

import android.os.Bundle
import com.windusk.ecosystemBasics.sharing.stringList.StringListSharingConsts.BUNDLE_EXTRA_STRINGS
import com.windusk.ecosystemBasics.sharing.stringList.StringListSharingConsts.DATA_TYPE


/**
 * Стандартные константы для StringListSharing.
 *
 * Тип данных: [DATA_TYPE]
 *
 * Что должно быть внутри [Bundle]:
 * * [BUNDLE_EXTRA_STRINGS] — [Array] из [String]: массив строк
 */
object StringListSharingConsts {
    /**
     * Тип данных, который определяет массив строк
     */
    const val DATA_TYPE = "string_list"

    /**
     * Ключ, по которому передаётся/получается массив строк
     */
    const val BUNDLE_EXTRA_STRINGS = "strings"
}