package br.edu.ifpb.sleepwell.view.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.sleepwell.utils.SleepViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CheckSleepHistoryScreen(
    userId: String,
    sleepViewModel: SleepViewModel = viewModel(),
    onNavigateToSleepQuality: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var checking by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        sleepViewModel.checkYesterdaySleepRecord(userId) { exists ->
            checking = false
            if (exists) {
                onNavigateToHome()
            } else {
                onNavigateToSleepQuality()
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (checking) {
            CircularProgressIndicator()
        }
    }
}
