package com.windusk.ecosystem.subscribition

import android.os.Bundle
import com.windusk.ecosystem.subscribition.SubscribitionOutput.SharingStatus.CHOISED_SHARING
import com.windusk.ecosystem.subscribition.SubscribitionOutput.SharingStatus.HAVENT_SHARING
import com.windusk.ecosystem.subscribition.SubscribitionOutput.SharingStatus.NEED_CALLBACK
import com.windusk.ecosystem.subscribition.SubscribitionOutput.SharingStatus.PRIORITY_SHARING
import com.windusk.lazybundle.BundleProperty
import com.windusk.lazybundle.BundleWrapper
import com.windusk.lazybundle.EnumProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Подготовленные данные для подписчика.
 *
 * @property status Статус подписки на момент обновления данных.
 * @property data Данные подходящего поставщика. Если [status] равен [SharingStatus.HAVENT_SHARING] или [SharingStatus.NEED_CALLBACK], данные отсутствуют.
 */
@Parcelize
class SubscribitionOutput private constructor(
    override val bundle: Bundle
): BundleWrapper(bundle) {
    /**
     * Статус подписки на момент обновления данных.
     *
     * @property PRIORITY_SHARING Отправлены данные приоритетного поставщика.
     * @property CHOISED_SHARING Отправлены данные поставщика, который выбрал пользователь.
     * @property NEED_CALLBACK Поставщик не может быть выбран автоматически, требуется обратный вызов. Пользователь должен выбрать поставщика.
     * @property HAVENT_SHARING Подходящий поставщик не найден.
     */
    enum class SharingStatus {
        PRIORITY_SHARING, CHOISED_SHARING, NEED_CALLBACK, HAVENT_SHARING
    }

    companion object : CreationTools<SubscribitionOutput>(::SubscribitionOutput)

    @IgnoredOnParcel
    val status = EnumProperty(this, "status", SharingStatus::class.java)

    @IgnoredOnParcel
    val data = BundleProperty(this, "data")
}