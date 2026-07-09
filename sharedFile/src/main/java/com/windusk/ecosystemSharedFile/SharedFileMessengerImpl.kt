package com.windusk.ecosystemSharedFile

import android.os.ParcelFileDescriptor
import com.windusk.ecosystemSharedFile.fileDescriptor.FileDescriptorCreator

class SharedFileMessengerImpl(
    private val creator: FileDescriptorCreator
): SharedFileMessenger.Stub() {
    override fun get(): ParcelFileDescriptor = creator.create()
}