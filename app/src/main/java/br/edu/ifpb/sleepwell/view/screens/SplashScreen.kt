package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // Lança um efeito que, após 3 segundos, chama onSplashFinished
    LaunchedEffect(true) {
        delay(3000)
        onSplashFinished()
    }

    // Layout que centraliza o texto
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "SLEEPWELL",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 40.sp,                // Ajuste conforme seu design
            fontWeight = FontWeight.Bold
        )
    }
}
