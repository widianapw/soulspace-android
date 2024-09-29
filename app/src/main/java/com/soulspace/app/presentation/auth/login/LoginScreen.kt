package com.soulspace.app.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.soulspace.app.common.TokenManager
import com.soulspace.app.common.UiEvents
import com.soulspace.app.presentation.auth.biometric.BiometricPromptManager
import com.soulspace.app.presentation.auth.register.RegisterRoute
import com.soulspace.app.presentation.psychologist.psychologist.PsychologistRoute
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Fingerprint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController? = null,
    promptManager: BiometricPromptManager,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val biometricResult by promptManager.promptResults.collectAsState(
        initial = null
    )

    val scrollState = rememberScrollState()

    val userToken = TokenManager(context).getToken()


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    fun submitLogin() {
        viewModel.submitLogin(email, password)
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
                is UiEvents.SuccessEvent -> {
                    navController?.navigate(PsychologistRoute){
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                    snackbarHostState.showSnackbar(
                        message = "Login successful",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    fun showBioMetricPrompt() {
        promptManager.showBiometricPrompt(
            title = "Authentication",
            description = "Authenticate to continue"
        )
    }

    fun navigateToPsychologist() {
        navController?.navigate(PsychologistRoute){
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){ padding ->
        Column(
            modifier = Modifier.padding(padding).verticalScroll(scrollState).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
            ) {

                Text(
                    "SoulSpace",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 48.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    isError = state.isEmailError,
                    label = { Text("Email") },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )
                if (state.isEmailError) {
                    Text(
                        text = state.emailErrorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    isError = state.isPasswordError,
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (state.isPasswordError) {
                    Text(
                        text = state.passwordErrorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        submitLogin()
                        // Optionally navigate after successful login
                        // navController?.navigate(PsychologistRoute)
                    }) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .height(16.dp)
                                .aspectRatio(1f)
                        )
                    } else {
                        Text("Login")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "or",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if(userToken.isNotBlank()){
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                        showBioMetricPrompt()
                    }) {
                        Icon(
                            imageVector = FontAwesomeIcons.Solid.Fingerprint,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Login using Biometric")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController?.navigate(RegisterRoute)
                    }) {
                    Text("Register Account")
                }

            }
        }
    }
}