package com.windusk.clientBasics.data.factory

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface EcosystemFactoryEntryPoint {
    fun getEcosystemFactory(): EcosystemFactory
}