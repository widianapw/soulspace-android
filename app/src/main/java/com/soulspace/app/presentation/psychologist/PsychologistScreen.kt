package com.soulspace.app.presentation.psychologist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soulspace.app.R
import kotlinx.serialization.Serializable

@Serializable
object PsychologistRoute

@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PsychologistScreen() {
    Scaffold { padding ->
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
                )
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                items(10) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                    ) {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Psychologist Image"
                        )
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Psychologist Name", style = MaterialTheme.typography.titleMedium)
                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )
                            Text("Jalan Kaliurang no 16, Yogyakarta, Sleman, Mantap, asdsaj dlasjdkl askj dasdj")
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                FilledTonalButton(
                                    onClick = {},
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.dice_1),
                                        contentDescription = "Call",
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
                                    onClick = {},
                                    modifier = Modifier.weight(1f)
                                ) {
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
