package br.edu.ifpb.sleepwell.view.screens

//import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


/**
 * Representa uma funcionalidade do app, exibida como card na grade.
 */
data class FeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector,     // Ícone específico
    val iconBgColor: Color,    // Cor de fundo do ícone
    val count: Int = 0,
    val onClick: () -> Unit = {}
)


/**
 * HomeScreen - Exemplo de layout baseado no mockup, agora com um BottomAppBar
 * e um FAB central.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    efficiency: Float = 0.76f,
    onTabSelected: (String) -> Unit = {},   // "Visão Geral" ou "Histórico de Sono"
    onAlarmClick: () -> Unit = {},     // Ação para ir para a tela de alarme
    onNavigateToDiary: () -> Unit = {},     // Ação para ir para a tela do diário
    onTipsClick: () -> Unit = {}, // Ação para ir para a tela dos lembretes
    onFabClick: () -> Unit = {}             // Ação do FAB (botão azul central)
) {
    // Exemplo de lista de funcionalidades
    val features = listOf(
        FeatureItem(
            title = "Diário dos Sonhos",
            description = "Nunca esqueça",
            icon = Icons.Default.AccountBox,
            iconBgColor = Color(0xFFE0E0E0), // Cinza claro, por exemplo
            onClick = onNavigateToDiary
        ),
        FeatureItem(
            title = "Dicas",
            description = "Durma com os anjos",
            icon = Icons.Default.DateRange,
            iconBgColor = Color(0xFF76FF03), // Verde claro
            onClick = onTipsClick
        ),
        FeatureItem(
            title = "Monitoramento de Ciclos",
            description = "Durateston",
            icon = Icons.Default.DateRange,
            iconBgColor = Color(0xFF76FF03) // Amarelo claro
        ),
        FeatureItem(
            title = "Despertadores",
            description = "Na hora certa",
            icon = Icons.Default.Notifications,
            iconBgColor = Color(0xFF00E5FF), // Ciano claro
            onClick = onAlarmClick
        )
    )

    // Captura somente o primeiro nome, com a inicial maiúscula
    val firstName = userName
        .split(" ")
        .firstOrNull()
        ?.lowercase()
        ?.replaceFirstChar { it.uppercase() } ?: ""

    Scaffold(){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Saudação com o nome do usuário logado
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

            // Abas "Visão Geral" e "Histórico de Sono"
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
            // Card para "Eficiência de Sono"
            SleepEfficiencyCard(efficiency)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Funcionalidades",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Grid com as funcionalidades
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

/**
 * Card que exibe a "Eficiência de Sono"
 */
@Composable
fun SleepEfficiencyCard(efficiency: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Eficiência do Sono",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Da sua última noite",
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
 * Card para cada funcionalidade (ex.: Diário, Lembretes, etc.)
 */
@Composable
fun FeatureCard(featureItem: FeatureItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 172.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick
    ) {
        // Uma coluna que preenche o card, com os itens espaçados entre si
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Bloco do ícone (ficará na parte superior)
            Surface(
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.small, // ou use CircleShape para um círculo perfeito
                color = featureItem.iconBgColor
            ) {
                Icon(
                    imageVector = featureItem.icon,
                    contentDescription = featureItem.title,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(4.dp)
                )
            }
            // Bloco de textos (ficará na parte inferior)
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 24.dp),
                //horizontalAlignment = Alignment.End,
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
