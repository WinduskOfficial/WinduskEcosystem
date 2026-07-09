package com.windusk.ecosystem.subscribition

import android.os.Bundle
import com.windusk.lazybundle.BooleanProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Экосистема будет отправлять подписчику данные от единственного подходящего поставщика.
 * Если подходящих поставщиков несколько, то экосистема даст пользователю выбрать нужного поставщика и сохранит выбор в памяти, если [supportPriority] равно true.
 *
 * @property supportPriority Поставщик поддерживает сохранение выбора пользователя, если поставщиков несколько.
 * @property updateManually Экосистема отправляет данные только по запросу подписчика или после того, как пользователь выберет нужного поставщика.
 */
@Parcelize
class PrioritySubscribition private constructor(override val bundle: Bundle): Subscribition(bundle) {
    companion object : CreationTools<PrioritySubscribition>(::PrioritySubscribition)

    @IgnoredOnParcel
    val supportPriority = BooleanProperty(this, "support_priority")

    @IgnoredOnParcel
    val updateManually = BooleanProperty(this, "update_manually")
}