package br.edu.ifpb.sleepwell.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Esquema de cores escuras baseado na imagem do mockup
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3A6FF7),    // Azul para destaques (FAB, botão "Visão Geral", barra de progresso)
    onPrimary = Color.White,

    secondary = Color(0xFF626262), // Cinza médio para destaques secundários
    onSecondary = Color.White,

    background = Color.Black,      // Fundo preto
    onBackground = Color.White,

    surface = Color(0xFF2C2C2C),   // Cartões e containers
    onSurface = Color.White
    // Se quiser, defina tertiary, error etc. conforme necessidade
)

@Composable
fun SleepwellTheme(content: @Composable () -> Unit) {
    // Removemos a lógica de cores dinâmicas para usar SEMPRE esse esquema
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
