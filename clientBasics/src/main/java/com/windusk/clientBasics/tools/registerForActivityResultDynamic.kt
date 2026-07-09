package com.windusk.clientBasics.tools

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import java.util.UUID

fun <I, O> ActivityResultRegistry.registerForActivityResultDynamic(
    singleContract: ActivityResultContract<I, O>,
    input: I,

    callback: ActivityResultCallback<O>
) {
    var launcher: ActivityResultLauncher<I>? = null
    
    launcher = registerForActivityResultLauncher(singleContract) {
        callback.onActivityResult(it)
        launcher!!.unregister()
    }
    
    launcher.launch(input)
}

fun <I, O> ActivityResultRegistry.registerForActivityResultLauncher(
    contract: ActivityResultContract<I, O>,
    callback: ActivityResultCallback<O>
): ActivityResultLauncher<I> {
    val key = UUID.randomUUID().toString()
    return register(key, contract, callback)
}

fun <I, O> ComponentActivity.registerForActivityResultDynamic(
    singleContract: ActivityResultContract<I, O>,
    input: I,

    callback: ActivityResultCallback<O>
) = activityResultRegistry.registerForActivityResultDynamic(singleContract, input, callback)

fun <I, O> ComponentActivity.registerForActivityResultLauncher(
    contract: ActivityResultContract<I, O>,
    callback: ActivityResultCallback<O>
) = activityResultRegistry.registerForActivityResultLauncher(contract, callback)