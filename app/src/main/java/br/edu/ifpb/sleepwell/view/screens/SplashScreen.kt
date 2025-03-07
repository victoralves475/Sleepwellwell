package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.edu.ifpb.sleepwell.R
import kotlinx.coroutines.delay

/**
 * SplashScreen exibe uma tela de apresentação do aplicativo "SLEEPWELL".
 *
 * Essa tela é mostrada por 3 segundos e, em seguida, aciona o callback onSplashFinished()
 * para redirecionar o usuário para a próxima tela (geralmente a tela de login).
 *
 * A tela apresenta:
 * - Uma imagem de fundo (onda azul) que preenche toda a tela.
 * - Um overlay translúcido (escuro) para melhorar a legibilidade dos elementos.
 * - Um texto centralizado exibindo "SLEEPWELL" em fonte grande e negrito.
 *
 * @param onSplashFinished Callback acionado após 3 segundos, geralmente para navegação.
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // LaunchedEffect inicia uma coroutine que espera 3 segundos antes de chamar onSplashFinished.
    LaunchedEffect(true) {
        delay(3000)
        onSplashFinished()
    }

    // Box principal que preenche toda a tela.
    Box(modifier = Modifier.fillMaxSize()) {
        // Exibe a imagem de fundo que preenche toda a tela.
        Image(
            painter = painterResource(id = R.drawable.wave_background), // Imagem de fundo (onda azul) na pasta res/drawable.
            contentDescription = null, // Descrição opcional para acessibilidade.
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Garante que a imagem preencha a tela, cortando as partes excedentes.
        )

        // Overlay escuro translúcido para melhorar a legibilidade sobre a imagem de fundo.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
        ) {
            // Box que centraliza o texto na tela.
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Texto "SLEEPWELL" exibido no centro da tela.
                Text(
                    text = "SLEEPWELL",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
