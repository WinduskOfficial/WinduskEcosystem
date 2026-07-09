package com.windusk.testclientapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.windusk.clientBasics.viewModel.EcosystemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.FileInputStream

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: EcosystemViewModel by viewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        enableEdgeToEdge()
//        setContent {
//            val componentsState = components.collectAsState(emptyList())
//
//            Column(
//                modifier = Modifier
//                    .padding(20.dp)
//            ) {
//                Text(
//                    modifier = Modifier
//                        .size(100.dp)
//                        .background(Color.Yellow)
//                        .clickable {
//                            startForegroundService(Intent(this@MainActivity, TestEcosystemClient::class.java))
//                        },
//                    text = "Components"
//                )
//
//                componentsState.value.forEach { component ->
//                    val statusFlow = remember(component) {
//                        component.getStatus()
//                    }
//
//                    val status = statusFlow.collectAsState(null)
//
//                    Text(
//                        modifier = Modifier
//                            .clickable {
//                                runBlocking {
//                                    component.updateRegistration(allowed = true)
//                                    component.updateRegistration(enabled = true)
//                                }
//                            }
//                            .padding(20.dp),
//                        text = component.name.packageName + " " + status.value?.name
//                    )
//
//                    if(status.value == Component.Status.ENABLED) {
//                        val association = SharingAssociation(
//                            FileSharingConsts.DATA_TYPE,
//                            FileSharingConsts.IMAGE_TAG,
//                            FileSharingConsts.KEY_COMPONENT_ICON
//                        )
//                        val fullAssociation = FullSharingAssociation.fromAssociation(component.name, association)
//
//                        val icon = viewModel.sharings
//                            .map { map ->
//                                map.orEmpty().toList().firstOrNull {
//                                    it.first == fullAssociation
//                                }
//                            }
//                            .map { sharing ->
//                                null
//                            }
//                            .collectAsState(null)
//
//                        icon.value?.let {
//                            Image(
//                                bitmap = it,
//                                contentDescription = null
//                            )
//                        }
//                    }
//
//                    if(status.value == Component.Status.CRASHED) {
//                        Text(
//                            text = component.getErrorState().value.toString()
//                        )
//                    }
//                }
//            }
//        }
    }

    fun readImageFromParcelFileDescriptor(pfd: ParcelFileDescriptor): ImageBitmap {
        val inputStream = FileInputStream(pfd.fileDescriptor)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        return bitmap.asImageBitmap()
    }
}