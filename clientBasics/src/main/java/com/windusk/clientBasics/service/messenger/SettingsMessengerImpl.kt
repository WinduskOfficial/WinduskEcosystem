package com.windusk.clientBasics.service.messenger

import com.windusk.ecosystemBasics.SettingsMessenger

class SettingsMessengerImpl(
    private val onOpenPage: (String) -> Unit
): SettingsMessenger.Stub() {
    override fun openComponentPage(name: String) = onOpenPage(name)
}