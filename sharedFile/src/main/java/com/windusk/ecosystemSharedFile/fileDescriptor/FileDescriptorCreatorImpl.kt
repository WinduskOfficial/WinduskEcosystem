package com.windusk.ecosystemSharedFile.fileDescriptor

import android.os.ParcelFileDescriptor
import java.io.File

abstract class FileDescriptorCreatorImpl: FileDescriptorCreator {
    override fun create(): ParcelFileDescriptor = ParcelFileDescriptor.open(
        getFile(),
        ParcelFileDescriptor.MODE_READ_ONLY
    )

    abstract fun getFile(): File
}