package com.windusk.ecosystemSharedFile.fileDescriptor

import android.os.ParcelFileDescriptor

interface FileDescriptorCreator {
    fun create(): ParcelFileDescriptor
}