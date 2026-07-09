package com.windusk.clientBasics.data.savedComponents

import android.content.ComponentName
import com.windusk.clientBasics.data.savedComponents.database.SavedComponentDao
import com.windusk.clientBasics.data.savedComponents.database.SavedComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComponentRepository @Inject constructor(
    private val dao: SavedComponentDao
) {
    val all = dao.getAllFlow()

    fun getRegisteredFlow(name: ComponentName): Flow<SavedComponent?> = dao.getByNameFlow(name.packageName, name.className)
    suspend fun getRegistered(name: ComponentName) = dao.getByName(name.packageName, name.className)

    fun getRegisteredOrNewRegisterFlow(name: ComponentName) = getRegisteredFlow(name).map { getNewRegister(name) }
    suspend fun getRegisteredOrNewRegister(name: ComponentName) = getRegistered(name) ?: getNewRegister(name)

    suspend fun getNewRegister(name: ComponentName) = SavedComponent(
        packageName = name.packageName,
        className = name.className,
        allowed = false,
        enabled = false
    ).apply {
        dao.insert(this)
    }

    suspend fun insert(registration: SavedComponent) {
        dao.insert(registration)
    }

    suspend fun updateAllowed(name: ComponentName, allowed: Boolean) {
        dao.updateAllowed(name.packageName, name.className, allowed)
    }

    suspend fun updateEnabled(name: ComponentName, enabled: Boolean) {
        dao.updateEnabled(name.packageName, name.className, enabled)
    }

    suspend fun update(name: ComponentName, allowed: Boolean, enabled: Boolean) {
        dao.update(name.packageName, name.className, allowed, enabled)
    }

    suspend fun delete(registration: SavedComponent) {
        dao.delete(registration)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}