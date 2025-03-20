package br.edu.ifpb.sleepwell

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
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
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.edu.ifpb.sleepwell.model.SessionManager
import br.edu.ifpb.sleepwell.ui.theme.SleepwellTheme
import br.edu.ifpb.sleepwell.utils.DicaWorker
import br.edu.ifpb.sleepwell.view.screens.AddDreamScreen
import br.edu.ifpb.sleepwell.view.screens.AlarmScreen
import br.edu.ifpb.sleepwell.view.screens.BottomAppBar
import br.edu.ifpb.sleepwell.view.screens.CheckSleepHistoryScreen
import br.edu.ifpb.sleepwell.view.screens.DreamDiaryScreen
import br.edu.ifpb.sleepwell.view.screens.HomeScreen
import br.edu.ifpb.sleepwell.view.screens.LoginScreen
import br.edu.ifpb.sleepwell.view.screens.SignUpScreen
import br.edu.ifpb.sleepwell.view.screens.SleepHistoryScreen
import br.edu.ifpb.sleepwell.view.screens.SleepQualityScreen
import br.edu.ifpb.sleepwell.view.screens.SplashScreen
import br.edu.ifpb.sleepwell.view.screens.TipsScreen
import kotlinx.coroutines.delay
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.edu.ifpb.sleepwell.utils.DicaWorker
import java.util.concurrent.TimeUnit

// Transição de fundo preto para efeito visual
@Composable
fun BlackTransitionScreen(
    onTransitionFinished: () -> Unit
) {
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

// Função para agendar notificações diárias (já resolvida)
fun agendarNotificacaoDiaria(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val workRequest = PeriodicWorkRequestBuilder<DicaWorker>(
        24, TimeUnit.HOURS
    )
        .setConstraints(constraints)
        .setInitialDelay(calcularDelayPara22h(), TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "notificacao_diaria",
        ExistingPeriodicWorkPolicy.UPDATE,
        workRequest
    )
}

/**
 * Calcula o delay até as 22h do próximo dia (ou hoje, se ainda não passou)
 */
fun calcularDelayPara22h(): Long {
    val agora = System.currentTimeMillis()

    val calendario = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 22)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)

        if (timeInMillis <= agora) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }
    return calendario.timeInMillis - agora
}

// Classe Application para criar o canal de notificações
class SleepWellApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "sleep_tips_channel",
                "Dicas de Sono",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificações com dicas para melhorar seu sono."
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleepwellTheme {
                SleepWellAppContent(context = this)
            }
            agendarNotificacaoDiaria(this)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SleepWellAppContent(context: Context) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentRoute !in listOf("login", "signup", "splash", "transition", "add-dream", "sleep-quality", "check-sleep")) {
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
                    // Após a transição, verificar o registro de sono do dia anterior
                    navController.navigate("check-sleep") {
                        popUpTo("transition") { inclusive = true }
                    }
                }
            }
            // Tela para checar o histórico do sono do dia anterior
            composable("check-sleep") {
                CheckSleepHistoryScreen(
                    userId = SessionManager.currentUser?.id ?: "user123",
                    onNavigateToSleepQuality = { navController.navigate("sleep-quality") },
                    onNavigateToHome = { navController.navigate("home") }
                )
            }
            // Tela para registrar a qualidade do sono (se não houver registro)
            composable("sleep-quality") {
                SleepQualityScreen(
                    userId = SessionManager.currentUser?.id ?: "user123",
                    onSleepRecorded = { navController.navigate("home") }
                )
            }
            // Tela Principal (Home) com botão para acessar o histórico do sono
            composable("home") {
                HomeScreen(
                    userName = SessionManager.currentUser?.nome ?: "Usuário",
                    onAlarmClick = { navController.navigate("alarm") },
                    onNavigateToDiary = { navController.navigate("dream-diary") },
                    onTipsClick = { navController.navigate("tips") },
                    onNavigateToSleepHistory = { navController.navigate("sleep-history") }
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
            // Tela de Histórico do Sono – exibe barra de porcentagem e lista de registros
            composable("sleep-history") {
                SleepHistoryScreen(userId = SessionManager.currentUser?.id ?: "user123")
            }
        }
    }
}
