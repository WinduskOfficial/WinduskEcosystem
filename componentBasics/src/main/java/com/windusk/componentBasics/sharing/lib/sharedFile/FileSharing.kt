package com.windusk.componentBasics.sharing.lib.sharedFile

import android.os.Bundle
import com.windusk.componentBasics.sharing.Sharing
import com.windusk.ecosystem.sharing.SharingInfo
import com.windusk.ecosystemSharedFile.FileSharingConsts
import com.windusk.ecosystemSharedFile.SharedFileMessengerImpl
import com.windusk.ecosystemSharedFile.fileDescriptor.FileDescriptorCreator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FileSharing(
    override val tag: String,
    override val key: String,
    override val info: Flow<SharingInfo>,
    override val enabled: Flow<Boolean> = flowOf(true),
    val fileFlow: Flow<FileDescriptorCreator>
): Sharing() {
    override val dataType = FileSharingConsts.DATA_TYPE
    override val data by lazy {
        fileFlow.map {
            Bundle().apply {
                putBinder(FileSharingConsts.BUNDLE_EXTRA_MESSENGER, SharedFileMessengerImpl(it))
            }
        }
    }
}