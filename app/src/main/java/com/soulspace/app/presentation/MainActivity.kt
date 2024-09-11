package com.soulspace.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soulspace.app.presentation.auth.login.LoginRoute
import com.soulspace.app.presentation.auth.login.LoginScreen
import com.soulspace.app.presentation.auth.RegisterRoute
import com.soulspace.app.presentation.auth.RegisterScreen
import com.soulspace.app.presentation.intro.SplashRoute
import com.soulspace.app.presentation.intro.SplashScreen
import com.soulspace.app.presentation.psychologist.PsychologistRoute
import com.soulspace.app.presentation.psychologist.PsychologistScreen
import com.soulspace.app.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = SplashRoute
                ) {
                    composable<SplashRoute> {
                        SplashScreen(navigateToLogin = {
                            navController.navigate(LoginRoute)
                        })
                    }
                    composable<LoginRoute> {
                        LoginScreen(
                            navController = navController
                        )
                    }
                    composable<RegisterRoute> {
                        RegisterScreen(
                            navController = navController
                        )
                    }

                    composable<PsychologistRoute> {
                        PsychologistScreen()
                    }
                }
            }
        }
    }
}

