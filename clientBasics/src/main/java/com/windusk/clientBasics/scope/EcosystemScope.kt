package com.windusk.clientBasics.scope

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

@Singleton
class EcosystemScope @Inject constructor() {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun getChildScope(): CoroutineScope {
        val parentJob = scope.coroutineContext[Job]

        return CoroutineScope(
            SupervisorJob(parentJob) + Dispatchers.Main
        )
    }
}