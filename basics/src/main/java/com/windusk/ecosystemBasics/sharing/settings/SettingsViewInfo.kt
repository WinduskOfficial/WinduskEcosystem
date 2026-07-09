package com.windusk.ecosystemBasics.sharing.settings

import android.os.Bundle
import com.windusk.ecosystemBiteUi.view.ViewInfo
import kotlinx.parcelize.Parcelize

/**
 * Информация об окне настроек.
 *
 * @property type всегда равен [ViewInfo.COLUMN_TYPE] и не может быть изменён.
 */
@Parcelize
class SettingsViewInfo private constructor(override val bundle: Bundle): ViewInfo(bundle) {
    companion object : CreationTools<SettingsViewInfo>(::SettingsViewInfo)

    init {
        type.set(COLUMN_TYPE)
    }
}