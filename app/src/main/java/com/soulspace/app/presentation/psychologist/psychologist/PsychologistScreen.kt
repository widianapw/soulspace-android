package com.soulspace.app.presentation.psychologist.psychologist

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.soulspace.app.R
import com.soulspace.app.presentation.auth.login.LoginRoute
import com.soulspace.app.presentation.chat.ChatRoute
import kotlinx.serialization.Serializable

@Serializable
object PsychologistRoute

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PsychologistScreen(
    navController: NavController? = null,
    viewModel: PsychologistViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController?.navigate(ChatRoute)
                },
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp) // Optional padding for spacing
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_chat_24),
                        contentDescription = "Chat with AI"
                    )
                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )
                    Text("Chat dengan AI")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .background(color = MaterialTheme.colorScheme.primary),
                    title = { Text("SoulSpace") },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.logout()
                                navController?.navigate(LoginRoute) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Filled.ExitToApp,
                                contentDescription = "Logout"
                            )
                        }
                    }
                )
                if (state.isLoading) {
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.onPrimary),
                        contentPadding = PaddingValues(all = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(state.list) { psychologist ->
                            Card(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.onPrimary,
                                )

                            ) {
                                AsyncImage(
                                    model = psychologist.imageUrl,
                                    contentDescription = "Psychologist Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16f / 9f)
                                )
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        psychologist.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(
                                        modifier = Modifier.height(4.dp)
                                    )
                                    Text(psychologist.address)
                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                    ) {
                                        FilledTonalButton(
                                            onClick = {},
                                            modifier = Modifier.weight(1f),
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_map_24),
                                                contentDescription = "Navigation",
                                                modifier = Modifier.width(24.dp)
                                            )
                                            Spacer(
                                                modifier = Modifier.width(4.dp)
                                            )
                                            Text("Navigation")
                                        }
                                        Spacer(
                                            modifier = Modifier.width(16.dp)
                                        )
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                                    data =
                                                        Uri.parse("tel:${psychologist.phoneNumber}") // Ubah nomor telepon dengan nomor tujuan
                                                }
                                                context.startActivity(intent)
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_contacts_24),
                                                contentDescription = "Navigation",
                                                modifier = Modifier.width(24.dp)
                                            )
                                            Spacer(
                                                modifier = Modifier.width(4.dp)
                                            )
                                            Text("Contact")
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
