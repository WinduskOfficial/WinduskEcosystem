package com.windusk.ecosystemSharedFile.fileDescriptor

import android.content.Context
import java.io.File

class ResourceDescriptorCreator(
    private val context: Context,
    private val resId: Int,
    private val key: String,
): FileDescriptorCreatorImpl() {
    override fun getFile(): File {
        val outFile = File(context.getDir("sharedRes", Context.MODE_PRIVATE), key)

        context.resources.openRawResource(resId).use { input ->
            outFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return outFile
    }
}