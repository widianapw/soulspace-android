package com.soulspace.app.presentation.chat

import android.Manifest
import android.app.Application
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.soulspace.app.common.UiEvents
import com.soulspace.app.domain.model.ChatResponseItem
import com.soulspace.app.domain.voice_to_text.VoiceToTextParser
import com.soulspace.app.presentation.chat.components.UserHead
import com.soulspace.app.presentation.permission.LocationPermissionTextProvider
import com.soulspace.app.presentation.permission.PermissionDialog
import com.soulspace.app.presentation.permission.PermissionViewModel
import com.soulspace.app.presentation.permission.openAppSettings
import com.soulspace.app.presentation.psychologist.psychologist.PsychologistRoute
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Microphone
import compose.icons.fontawesomeicons.solid.Stop
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object ChatRoute


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ChatScreen(
    navController: NavController? = null,
    viewModel: ChatViewModel = hiltViewModel()
) {
    var canRecord by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.value

    val context = LocalContext.current

    val permissionViewModel = viewModel<PermissionViewModel>()
    val permissionsToRequest = Manifest.permission.RECORD_AUDIO
    val dialogQueue = permissionViewModel.visiblePermissionDialogQueue


    val voiceState by viewModel.voiceState.collectAsState()

    val voicePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionViewModel.onPermissionResult(
                permission = permissionsToRequest,
                isGranted = isGranted
            )

            canRecord = isGranted
//            if (isGranted) {
//                voiceToTextParser.startListening()
//            }
        }
    )

    LaunchedEffect(Unit) {
        voicePermissionLauncher.launch(permissionsToRequest)
    }

    fun startListening() {
//        voicePermissionLauncher.launch(permissionsToRequest)
        if(canRecord) {
            viewModel.startListening()
        }else{
            voicePermissionLauncher.launch(permissionsToRequest)
        }
    }

    fun stopListening() {
        viewModel.stopListening()
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(key1 = state.messages.size) {
        if (state.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(state.messages.size - 1)
            }
        }
    }

    LaunchedEffect(voiceState.spokenText, voiceState.isSpeaking) {
        if (!voiceState.isSpeaking) {
            viewModel.setMessage(voiceState.spokenText)
        }
    }

    LaunchedEffect(voiceState.error) {
        voiceState.error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
        }
    }

    dialogQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = LocationPermissionTextProvider(),
                onDismiss = permissionViewModel::dismissDialog,
                onOkClick = {
                    permissionViewModel.dismissDialog()
                    voicePermissionLauncher.launch(
                        permission
                    )
                },
                onGoToAppSettingsClick = {
                    context.openAppSettings()
                }
            )
        }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                actions = {
                    IconButton(onClick = {
                        viewModel.resetChat()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Reset"
                        )
                    }
                },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(onClick = {
                            navController?.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        UserHead(
                            id = "1",
                            firstName = "AI",
                            lastName = "Psikolog",
                            size = 36.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "AI Psikolog",
                            modifier = Modifier.weight(1f)
                        ) // Replace with dynamic name
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White) // Customize color as needed
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(padding),
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.messages) { item ->
                        ChatItem(item)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (voiceState.isSpeaking) {
                        stopListening()
                    } else {
                        startListening()
                    }
                }
                ) {
                    AnimatedContent(
                        targetState = voiceState.isSpeaking,
                    ) {  isSpeaking ->
                        if (!isSpeaking){
                            Icon(
                                imageVector = FontAwesomeIcons.Solid.Microphone,
                                modifier = Modifier.width(14.dp),
                                contentDescription = "Refresh"
                            )
                        }else{
                            Icon(
                                imageVector = FontAwesomeIcons.Solid.Stop,
                                modifier = Modifier.width(14.dp),
                                contentDescription = "Refresh"
                            )

                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = state.message,
                    singleLine = true,
                    onValueChange = {
                        viewModel.setMessage(it)
                    },
                    label = { Text("Message") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (state.isLoadingSendMessage) {
                    Box(
                        modifier = Modifier.width(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            viewModel.postMessage(state.message)
                        },
                        enabled = state.message.isNotEmpty()
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send"
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ChatItem(item: ChatResponseItem) {
// Different alignment and background color for user and bot messages
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if (item.senderType == "user") Arrangement.End else Arrangement.Start
    ) {
        // Box to contain the text bubble
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (item.senderType == "user") Color(0xFFDFFFD6) // Green bubble for user
                    else Color.White // White bubble for bot
                )
                .padding(12.dp)
        ) {
            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
