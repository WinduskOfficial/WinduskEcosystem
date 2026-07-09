package com.windusk.ecosystem.logger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

class EcosystemLogger(
    private val subtag: String
) {
    companion object {
        suspend fun <T>Flow<T>.collectWithLog(
            logger: EcosystemLogger,
            name: String,
            collectAction: suspend (T) -> LogOutput
        ) {
            logger.i("COLLECT FLOW\nFLOW NAME: $name")

            collect {
                logger.i("NEW DATA EMITED\nFLOW NAME: $name\nDATA: $it")

                val output = collectAction(it)

                logger.send(output)
            }
        }

        suspend fun <T>Flow<T>.collectLatestWithLog(
            logger: EcosystemLogger,
            name: String,
            collectAction: suspend (T) -> LogOutput
        ) {
            logger.i("COLLECT FLOW\nFLOW NAME: $name")

            collectLatest {
                logger.i("NEW DATA EMITED\nFLOW NAME: $name\nDATA: $it")

                val output = collectAction(it)

                logger.send(output)
            }
        }
    }

    val tag = "WINDUSK_ECOSYSTEM: $subtag"

    fun sub(subtag: String) = EcosystemLogger("${this.subtag} > $subtag")

    fun send(output: LogOutput) = output.logInterface.send(
        tag,
        output.message
    )

    fun i(vararg strings: String) = send(
        LogOutput(LogInterface.INFO, strings.joinToString("\n"))
    )
    fun e(vararg strings: String) = send(
        LogOutput(LogInterface.ERROR, strings.joinToString("\n"))
    )
    fun d(vararg strings: String) = send(
        LogOutput(LogInterface.DEBUG, strings.joinToString("\n"))
    )
    fun v(vararg strings: String) = send(
        LogOutput(LogInterface.VERBOSE, strings.joinToString("\n"))
    )
    fun w(vararg strings: String) = send(
        LogOutput(LogInterface.WARNING, strings.joinToString("\n"))
    )
    fun wtf(vararg strings: String) = send(
        LogOutput(LogInterface.WTF, strings.joinToString("\n"))
    )

    fun i(message: String) = send(
        LogOutput(LogInterface.INFO, message)
    )
    fun e(message: String) = send(
        LogOutput(LogInterface.ERROR, message)
    )
    fun d(message: String) = send(
        LogOutput(LogInterface.DEBUG, message)
    )
    fun v(message: String) = send(
        LogOutput(LogInterface.VERBOSE, message)
    )
    fun w(message: String) = send(
        LogOutput(LogInterface.WARNING, message)
    )
    fun wtf(message: String) = send(
        LogOutput(LogInterface.WTF, message)
    )
}