package br.edu.ifpb.sleepwell

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.edu.ifpb.sleepwell.model.SessionManager
import br.edu.ifpb.sleepwell.ui.theme.SleepwellTheme
import br.edu.ifpb.sleepwell.view.screens.AlarmScreen
import br.edu.ifpb.sleepwell.view.screens.AddDreamScreen
import br.edu.ifpb.sleepwell.view.screens.BottomAppBar
import br.edu.ifpb.sleepwell.view.screens.DreamDiaryScreen
import br.edu.ifpb.sleepwell.view.screens.HomeScreen
import br.edu.ifpb.sleepwell.view.screens.LoginScreen
import br.edu.ifpb.sleepwell.view.screens.SignUpScreen
import br.edu.ifpb.sleepwell.view.screens.SplashScreen
import br.edu.ifpb.sleepwell.view.screens.TipsScreen
import kotlinx.coroutines.delay

@Composable
fun BlackTransitionScreen(
    onTransitionFinished: () -> Unit
) {
    // Mostra um fundo preto por 500 ms
    LaunchedEffect(Unit) {
        delay(500)
        onTransitionFinished()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleepwellTheme {
                SleepWellApp(context = this)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SleepWellApp(context: Context) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentRoute !in listOf("login", "signup", "splash", "transition", "add-dream")) {
                BottomAppBar(
                    onProfileClick = { navController.navigate("profile") },
                    onHomeClick = { navController.navigate("home") },
                    onLogout = {
                        SessionManager.currentUser = null
                        navController.navigate("login")
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tela de Splash
            composable("splash") {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }
            // Tela de Login
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { navController.navigate("transition") },
                    onNavigateToSignUp = { navController.navigate("signup") }
                )
            }
            // Tela de Cadastro
            composable("signup") {
                SignUpScreen(
                    onSignUpSuccess = { navController.popBackStack() },
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }
            // Tela de Transição (fundo preto por 500ms)
            composable("transition") {
                BlackTransitionScreen {
                    navController.navigate("home") {
                        popUpTo("transition") { inclusive = true }
                    }
                }
            }
            // Tela Principal (Home)
            composable("home") {
                HomeScreen(
                    userName = SessionManager.currentUser?.nome ?: "Usuário",
                    onAlarmClick = { navController.navigate("alarm") },
                    onNavigateToDiary = { navController.navigate("dream-diary") },
                    onTipsClick = { navController.navigate("tips") }
                )
            }
            // Tela de Alarme
            composable("alarm") {
                AlarmScreen(context = context)
            }
            // Tela de Dicas
            composable("tips") {
                TipsScreen()
            }
            // Tela do Diário de Sonhos
            composable("dream-diary") {
                DreamDiaryScreen(
                    onAddDreamClick = { navController.navigate("add-dream") }
                )
            }
            // Tela de Adição de Sonho
            composable("add-dream") {
                AddDreamScreen(
                    onSaveSuccess = {
                        navController.navigate("dream-diary") {
                            popUpTo("add-dream") { inclusive = true }
                        }
                    },
                    onCancelClick = {
                        navController.navigate("dream-diary") {
                            popUpTo("add-dream") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
