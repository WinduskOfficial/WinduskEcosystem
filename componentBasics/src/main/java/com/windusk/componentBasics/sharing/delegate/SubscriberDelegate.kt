package com.windusk.componentBasics.sharing.delegate

import android.os.Bundle
import com.windusk.ecosystem.subscribition.Subscribition

interface SubscriberDelegate<T> {
    fun getSubscribition(): Subscribition
    fun formatData(data: Bundle?): T?
}