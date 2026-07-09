package com.windusk.clientBasics.data

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.windusk.clientBasics.tools.AppChangeReciever
import com.windusk.ecosystem.INTENT_FILTER_COMPONENT
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@SuppressLint("UnspecifiedRegisterReceiverFlag")
class ComponentNamesFactory @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val allMutable = MutableStateFlow(getLocalComponentsNames())
    val all = allMutable.asStateFlow()

    private var appChangeReciever = AppChangeReciever { _, _ ->
        allMutable.tryEmit(getLocalComponentsNames())
    }

    init {
        appChangeReciever.register(context)
    }

    private fun getLocalComponentsNames(
        intent: Intent = Intent()
    ) = context.packageManager.queryIntentServices(
        intent.setAction(INTENT_FILTER_COMPONENT),
        0
    ).map {
        ComponentName(it.serviceInfo.packageName, it.serviceInfo.name)
    }
}