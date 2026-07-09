package com.windusk.ecosystem.sharing

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Идентификатор поставщика данных. Должен быть уникален для каждого поставщика компонента.
 *
 * @property dataType Тип данных, которые передаются поставщиком.
 * @property tag Назначение данных поставщика.
 * @property key Уникальный идентификатор поставщика.
 */
@Parcelize
data class SharingAssociation(
    val dataType: String,
    val tag: String,
    val key: String
): Parcelable