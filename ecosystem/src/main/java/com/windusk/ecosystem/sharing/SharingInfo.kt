package com.windusk.ecosystem.sharing

/**
 * Визуальная информация о поставщике данных.
 *
 * @property name Имя поставщика.
 * @property description Описание поставщика.
 */
data class SharingInfo(
    val name: String,
    val description: String? = null
)