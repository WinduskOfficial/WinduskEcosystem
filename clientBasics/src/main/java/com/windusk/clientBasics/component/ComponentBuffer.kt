package com.windusk.clientBasics.component

import kotlinx.coroutines.Job

interface ComponentBuffer {
    val defaultBlockName: String

    fun <T>run(
        blockName: String = defaultBlockName,
        crashOnError: Boolean = true,
        block: () -> T
    ): T?

    fun <T>run(
        blockName: String = defaultBlockName,
        crashIf: (e: Throwable) -> Boolean,
        block: () -> T
    ): T?

    fun successRun(
        blockName: String = defaultBlockName,
        crashOnError: Boolean = true,
        block: () -> Unit
    ): Boolean

    fun successRun(
        blockName: String = defaultBlockName,
        crashIf: (e: Throwable) -> Boolean,
        block: () -> Unit
    ): Boolean

    fun launch(
        blockName: String = defaultBlockName,
        crashOnError: Boolean = true,
        block: suspend () -> Unit
    ): Job

    fun launch(
        blockName: String = defaultBlockName,
        crashIf: (e: Throwable) -> Boolean,
        block: suspend () -> Unit
    ): Job

    fun throwable(
        blockName: String = defaultBlockName,
        crash: Boolean = true
    )
}