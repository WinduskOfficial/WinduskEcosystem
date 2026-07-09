package com.windusk.testclientapp

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import com.windusk.ecosystem.subscribition.PrioritySubscribition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestSharingChoiserActivity: ComponentActivity() {
    companion object {
        private const val INTENT_EXTRA_DATA_TYPE = "dataType"
        private const val INTENT_EXTRA_TAG = "tag"
        private const val INTENT_SUPPORT_PRIORITY = "supportPriority"

        fun Context.startChoiserActivity(
            subscribition: PrioritySubscribition
        ) {
            startActivity(
                Intent(this, TestSharingChoiserActivity::class.java)
                    .putExtra(INTENT_EXTRA_DATA_TYPE, subscribition.dataType.get())
                    .putExtra(INTENT_EXTRA_TAG, subscribition.tag.get())
                    .putExtra(INTENT_SUPPORT_PRIORITY, subscribition.supportPriority.get())
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

//    private val viewModel: EcosystemViewModel by viewModels()
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private val items by lazy {
//        val dataType = intent.getStringExtra(INTENT_EXTRA_DATA_TYPE)
//        val tag = intent.getStringExtra(INTENT_EXTRA_TAG)
//
//        viewModel.sharings
//            .map { map ->
//                map
//                    .filterKeys {
//                        it.dataType == dataType && it.tag == tag
//                    }
//                    .map { (association, output) ->
//                        ChoiserActivityItem.from(association, output)
//                    }
//            }
//            .stateIn(
//                lifecycleScope,
//                started = SharingStarted.Eagerly,
//                initialValue = emptyList()
//            )
//    }
//
//    private val supportPriority by lazy {
//        intent.getBooleanExtra(INTENT_SUPPORT_PRIORITY, false)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            SharingsList()
//        }
//    }
//
//    @Composable
//    private fun SharingsList() {
//        val selectedSharingAssociation = if(supportPriority) remember {
//            mutableStateOf<FullSharingAssociation?>(null)
//        } else null
//
//        val allowedSharingsState = items.collectAsState()
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1F),
//                verticalArrangement = Arrangement.spacedBy(20.dp)
//            ) {
//                items(allowedSharingsState.value) { item ->
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        SharingListItem(choiserActivityItem = item) {
//                            // Logic to select or handle click
//                        }
//                    }
//                }
//            }
//
//            ChoiseButtons(selectedSharingAssociation = selectedSharingAssociation)
//        }
//    }
//
//    @Composable
//    private fun SharingListItem(
//        modifier: Modifier = Modifier,
//        choiserActivityItem: ChoiserActivityItem,
//        onClick: () -> Unit
//    ) {
//        val icon = choiserActivityItem.icon.collectAsState(null)
//        val name = choiserActivityItem.association.getComponentName()?.packageName ?: "Unknown"
//
//        SharingListItemLabel(icon = icon.value, name = name)
//    }
//
//    @Composable
//    private fun SharingListItemLabel(
//        icon: Bitmap?,
//        name: String
//    ) {
//        Row {
//            icon?.let {
//                Image(
//                    modifier = Modifier
//                        .size(100.dp),
//                    bitmap = it.asImageBitmap(),
//                    contentDescription = null
//                )
//            }
//
//            Text(
//                text = name,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
//                fontSize = 25.sp
//            )
//        }
//    }
//
//    @Composable
//    private fun ChoiseButtons(
//        modifier: Modifier = Modifier,
//        selectedSharingAssociation: MutableState<FullSharingAssociation?>?,
//    ) {
//        if(selectedSharingAssociation == null) return
//
//        Column(
//            modifier,
//            verticalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            // Add buttons if needed
//        }
//    }
}
