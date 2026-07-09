package com.windusk.clientBasics.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.ParcelFileDescriptor
import java.io.FileInputStream

fun readImageFromParcelFileDescriptor(
        pfd: ParcelFileDescriptor
): Bitmap {
        val inputStream = FileInputStream(pfd.fileDescriptor)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        return bitmap
}