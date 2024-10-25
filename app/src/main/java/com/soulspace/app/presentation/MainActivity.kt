package com.soulspace.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.soulspace.app.common.TokenManager
import com.soulspace.app.presentation.auth.biometric.BiometricPromptManager
import com.soulspace.app.presentation.auth.biometric.BiometricRoute
import com.soulspace.app.presentation.auth.biometric.BiometricScreen
import com.soulspace.app.presentation.auth.login.LoginRoute
import com.soulspace.app.presentation.auth.login.LoginScreen
import com.soulspace.app.presentation.auth.register.RegisterRoute
import com.soulspace.app.presentation.auth.register.RegisterScreen
import com.soulspace.app.presentation.chat.ChatRoute
import com.soulspace.app.presentation.chat.ChatScreen
import com.soulspace.app.presentation.intro.SplashRoute
import com.soulspace.app.presentation.intro.SplashScreen
import com.soulspace.app.presentation.psychologist.navigation.NavigationRoute
import com.soulspace.app.presentation.psychologist.navigation.NavigationScreen
import com.soulspace.app.presentation.psychologist.psychologist.PsychologistRoute
import com.soulspace.app.presentation.psychologist.psychologist.PsychologistScreen
import com.soulspace.app.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
//        val startDestination = NavigationRoute(
//            "0", "0"
//        )
        val startDestination = LoginRoute

        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable<LoginRoute> {
                        val promptManager by lazy {
                            BiometricPromptManager(this@MainActivity)
                        }
                        LoginScreen(
                            navController = navController,
                            promptManager = promptManager
                        )
                    }
                    composable<RegisterRoute> {
                        RegisterScreen(
                            navController = navController
                        )
                    }

                    composable<BiometricRoute> {
                         val promptManager by lazy {
                            BiometricPromptManager(this@MainActivity)
                        }
                        BiometricScreen(
                            navController = navController,
                            promptManager = promptManager
                        )
                    }

                    composable<PsychologistRoute> {
                        PsychologistScreen(
                            navController = navController
                        )
                    }

                    composable<ChatRoute> {
                        ChatScreen(
                            navController = navController
                        )
                    }

                    composable<NavigationRoute> {
                        val args = it.toRoute<NavigationRoute>()
                        NavigationScreen(
                            navController = navController,
                            args = args
                        )
                    }
                }
            }
        }
    }
}

