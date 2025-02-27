package br.edu.ifpb.sleepwell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.ifpb.sleepwell.model.SessionManager
import br.edu.ifpb.sleepwell.ui.theme.SleepwellTheme
import br.edu.ifpb.sleepwell.view.screens.HomeScreen
import br.edu.ifpb.sleepwell.view.screens.LoginScreen
import br.edu.ifpb.sleepwell.view.screens.SignUpScreen
import br.edu.ifpb.sleepwell.view.screens.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleepwellTheme {
                SleepWellApp()
            }
        }
    }
}

@Composable
fun SleepWellApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tela inicial: Splash
            composable("splash") {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }
            // Tela de login
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { navController.navigate("home") },
                    onNavigateToSignUp = { navController.navigate("signup") }
                )
            }
            // Tela de cadastro
            composable("signup") {
                SignUpScreen(
                    onSignUpSuccess = { navController.popBackStack() },
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }
            // Tela principal
            composable("home") {
                HomeScreen(
                    userName = SessionManager.currentUser?.nome ?: "Usuário",
                    onLogout = {
                        // Opcional: limpar a sessão no logout
                        SessionManager.currentUser = null
                        navController.navigate("login")
                    }
                )
            }

        }
    }
}