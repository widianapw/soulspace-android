package com.soulspace.app.presentation.auth.biometric

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import android.content.Intent
import android.provider.Settings
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.soulspace.app.presentation.psychologist.psychologist.PsychologistRoute

@Serializable
object BiometricRoute


@Composable
fun BiometricScreen(
    navController: NavController,
    promptManager: BiometricPromptManager
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val biometricResult by promptManager.promptResults.collectAsState(
        initial = null
    )
    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            println("Activity result: $it")
        }
    )
    // Automatically call biometric prompt when screen starts
    LaunchedEffect(Unit) {
        promptManager.showBiometricPrompt(
            title = "Authentication",
            description = "Authenticate to continue"
        )
    }

    fun navigateToPsychologist() {
        navController.navigate(PsychologistRoute){
            popUpTo(0) {
                inclusive = true
            }
        }
    }

    LaunchedEffect(biometricResult) {
        when(biometricResult){
            is BiometricPromptManager.BiometricResult.AuthenticationError ->{
                val result = biometricResult as BiometricPromptManager.BiometricResult.AuthenticationError
                snackbarHostState.showSnackbar(
                    message = "Biometric authentication ${result.error}",
                    duration = SnackbarDuration.Short
                )
            }
            is BiometricPromptManager.BiometricResult.AuthenticationNotSet ->{
                snackbarHostState.showSnackbar(
                    message = "Biometric authentication not set",
                    duration = SnackbarDuration.Short
                )
                navigateToPsychologist()
            }
            is BiometricPromptManager.BiometricResult.AuthenticationFailed ->{
                snackbarHostState.showSnackbar(
                    message = "Biometric authentication failed",
                    duration = SnackbarDuration.Short
                )
            }
            is BiometricPromptManager.BiometricResult.AuthenticationSuccess ->{
                snackbarHostState.showSnackbar(
                    message = "Biometric authentication success",
                    duration = SnackbarDuration.Short
                )
                navigateToPsychologist()
            }
            is BiometricPromptManager.BiometricResult.FeatureUnavailable ->{
                snackbarHostState.showSnackbar(
                    message = "Biometric feature unavailable",
                    duration = SnackbarDuration.Short
                )
                navigateToPsychologist()
            }
            is BiometricPromptManager.BiometricResult.HardwareUnavailable ->{
                snackbarHostState.showSnackbar(
                    message = "Biometric hardware unavailable",
                    duration = SnackbarDuration.Short
                )
                navigateToPsychologist()
            }

            else -> {
                // Do nothing
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

        }
    }
}
