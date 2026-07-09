package com.windusk.ecosystem.subscribition

import android.os.Bundle
import kotlinx.parcelize.Parcelize

/**
 * Экосистема будет отправлять подписчику данные от всех подходящих поставщиков.
 */
@Parcelize
class MultiSubscribition private constructor(override val bundle: Bundle): Subscribition(bundle) {
    companion object : CreationTools<MultiSubscribition>(::MultiSubscribition)
}