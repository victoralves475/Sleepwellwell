package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.sleepwell.model.SessionManager
import br.edu.ifpb.sleepwell.utils.SleepViewModel

/**
 * Data class que representa uma funcionalidade do aplicativo.
 */
data class FeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val count: Int = 0,
    val onClick: () -> Unit = {}
)

/**
 * Exibe um card que mostra a eficiência do sono.
 */
@Composable
fun SleepEfficiencyCard(
    userId: String,
    sleepViewModel: SleepViewModel = viewModel()
) {
    var efficiency by remember { mutableStateOf(0f) }

    // Calcula a eficiência com base no histórico do usuário.
    LaunchedEffect(userId) {
        sleepViewModel.computeSleepQualityPercentage(userId) { perc ->
            efficiency = perc / 100f
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Eficiência de Sono",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "com base no seu histórico",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${(efficiency * 100).toInt()}%",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = efficiency,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Exibe um card representando uma funcionalidade do aplicativo.
 */
@Composable
fun FeatureCard(featureItem: FeatureItem, onClick: () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .defaultMinSize(minHeight = 172.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Ícone com fundo colorido
            Surface(
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.small,
                color = featureItem.iconBgColor
            ) {
                Icon(
                    imageVector = featureItem.icon,
                    contentDescription = featureItem.title,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(4.dp)
                )
            }
            // Título e descrição
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = featureItem.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = featureItem.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * HomeScreen exibe a tela principal do aplicativo.
 *
 * @param userName Nome completo do usuário (será convertido para o primeiro nome).
 * @param efficiency Eficiência de sono (valor entre 0 e 1).
 * @param onTabSelected Callback para selecionar a aba ("Visão Geral" ou "Histórico de Sono").
 * @param onAlarmClick Callback para navegar à tela de alarme.
 * @param onNavigateToDiary Callback para navegar à tela do diário de sonhos.
 * @param onTipsClick Callback para navegar à tela de dicas.
 * @param onFabClick Callback para a ação do FAB central.
 * @param onNavigateToSleepHistory Callback para navegar à tela do histórico do sono.
 */
@Composable
fun HomeScreen(
    userName: String,
    efficiency: Float = 0.76f,
    onTabSelected: (String) -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onNavigateToDiary: () -> Unit = {},
    onTipsClick: () -> Unit = {},
    onFabClick: () -> Unit = {},
    onNavigateToSleepHistory: () -> Unit = {}
) {
    // Lista de funcionalidades (cards) exibidos na tela
    val features = listOf(
        FeatureItem(
            title = "Diário dos Sonhos",
            description = "Nunca esqueça",
            icon = Icons.Default.AccountBox,
            iconBgColor = Color(0xFFE0E0E0),
            onClick = onNavigateToDiary
        ),
        FeatureItem(
            title = "Dicas",
            description = "Durma com os anjos",
            icon = Icons.Default.Star,
            iconBgColor = Color(0xFFeac918),
            onClick = onTipsClick
        ),
        FeatureItem(
            title = "Despertadores",
            description = "Na hora certa",
            icon = Icons.Default.Notifications,
            iconBgColor = Color(0xFF00E5FF),
            onClick = onAlarmClick
        ),
        FeatureItem(
            title = "Histórico do Sono",
            description = "Veja seu histórico",
            icon = Icons.Default.List,
            iconBgColor = Color(0xFFB0BEC5),
            onClick = onNavigateToSleepHistory
        )
    )

    // Extrai o primeiro nome do usuário
    val firstName = userName
        .split(" ")
        .firstOrNull()
        ?.lowercase()
        ?.replaceFirstChar { it.uppercase() } ?: ""

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Saudação
            Text(
                text = "Olá, ",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = firstName,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Abas para "Visão Geral" e "Histórico de Sono"
            Row {
                Button(
                    onClick = { onTabSelected("Visão Geral") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Visão Geral", color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { onTabSelected("Histórico de Sono") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Histórico de Sono", color = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Card que exibe a eficiência do sono
            SleepEfficiencyCard(userId = SessionManager.currentUser?.id ?: "user123")
            Spacer(modifier = Modifier.height(16.dp))

            // Título da seção de funcionalidades
            Text(
                text = "Funcionalidades",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Grid com as funcionalidades (cards)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(features) { feature ->
                    FeatureCard(featureItem = feature, onClick = feature.onClick)
                }
            }
        }
    }
}
