package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Data class que representa uma funcionalidade do aplicativo.
 *
 * Cada funcionalidade é exibida como um card e contém:
 * - title: o título da funcionalidade.
 * - description: uma breve descrição.
 * - icon: o ícone que representa a funcionalidade.
 * - iconBgColor: a cor de fundo para o ícone.
 * - count: um contador opcional (não utilizado aqui, mas pode ser usado para indicar quantidade de itens, por exemplo).
 * - onClick: uma ação a ser executada quando o card é clicado.
 */
data class FeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector,     // Ícone específico para a funcionalidade.
    val iconBgColor: Color,    // Cor de fundo usada no contêiner do ícone.
    val count: Int = 0,        // Contador opcional.
    val onClick: () -> Unit = {}  // Ação padrão ao clicar, que pode ser sobrescrita.
)

/**
 * HomeScreen exibe a tela principal do aplicativo.
 *
 * Ela exibe uma saudação ao usuário (apenas o primeiro nome) e apresenta:
 * - Abas para alternar entre "Visão Geral" e "Histórico de Sono".
 * - Um card que mostra a eficiência de sono.
 * - Um grid de funcionalidades (ex.: Diário dos Sonhos, Dicas, Monitoramento de Ciclos, Despertadores).
 *
 * Os callbacks (onAlarmClick, onNavigateToDiary, onTipsClick, onFabClick) são passados para
 * permitir a navegação para outras telas.
 *
 * @param userName Nome completo do usuário (será convertido para o primeiro nome).
 * @param efficiency Eficiência de sono (valor entre 0 e 1).
 * @param onTabSelected Callback para selecionar a aba ("Visão Geral" ou "Histórico de Sono").
 * @param onAlarmClick Callback para navegar à tela de alarme.
 * @param onNavigateToDiary Callback para navegar à tela do diário de sonhos.
 * @param onTipsClick Callback para navegar à tela de dicas.
 * @param onFabClick Callback para a ação do FAB central.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    efficiency: Float = 0.76f,
    onTabSelected: (String) -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onNavigateToDiary: () -> Unit = {},
    onTipsClick: () -> Unit = {},
    onFabClick: () -> Unit = {}
) {
    // Define uma lista de funcionalidades a serem exibidas no grid.
    val features = listOf(
        FeatureItem(
            title = "Diário dos Sonhos",
            description = "Nunca esqueça",
            icon = Icons.Default.AccountBox,
            iconBgColor = Color(0xFFE0E0E0), // Cor de fundo: cinza claro.
            onClick = onNavigateToDiary
        ),
        FeatureItem(
            title = "Dicas",
            description = "Durma com os anjos",
            icon = Icons.Default.Star,
            iconBgColor = Color(0xFFeac918), // Cor de fundo: amarelo.
            onClick = onTipsClick
        ),
        FeatureItem(
            title = "Monitoramento de Ciclos",
            description = "Durateston",
            icon = Icons.Default.DateRange,
            iconBgColor = Color(0xFF76FF03) // Cor de fundo: verde claro.
        ),
        FeatureItem(
            title = "Despertadores",
            description = "Na hora certa",
            icon = Icons.Default.Notifications,
            iconBgColor = Color(0xFF00E5FF), // Cor de fundo: ciano claro.
            onClick = onAlarmClick
        )
    )

    // Extrai o primeiro nome do usuário e converte a primeira letra para maiúscula.
    val firstName = userName
        .split(" ")
        .firstOrNull()
        ?.lowercase()
        ?.replaceFirstChar { it.uppercase() } ?: ""

    // Scaffold organiza a tela, onde o conteúdo principal é disposto em uma Column.
    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Saudação: "Olá, [primeiro nome]"
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
            SleepEfficiencyCard(efficiency)
            Spacer(modifier = Modifier.height(16.dp))

            // Título para a seção de funcionalidades
            Text(
                text = "Funcionalidades",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Grid para exibir as funcionalidades (cada funcionalidade é representada por um FeatureCard)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(features) { feature ->
                    // Cada FeatureCard é clicável e executa a ação definida em feature.onClick.
                    FeatureCard(featureItem = feature, onClick = feature.onClick)
                }
            }
        }
    }
}

/**
 * SleepEfficiencyCard exibe a "Eficiência de Sono" em um card.
 *
 * O card mostra:
 * - Um título ("Eficiência do Sono")
 * - Um subtítulo ("Da sua última noite")
 * - A porcentagem de eficiência (calculada a partir de um valor entre 0 e 1)
 * - Um LinearProgressIndicator para visualizar graficamente essa eficiência.
 *
 * @param efficiency Valor entre 0 e 1 que representa a eficiência do sono.
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
                text = "Eficiência de Sono",
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
 * FeatureCard exibe uma funcionalidade do app em um card.
 *
 * O card possui:
 * - Um bloco superior que exibe um ícone dentro de um Surface (com fundo colorido).
 * - Um bloco inferior que exibe o título e a descrição da funcionalidade.
 *   O bloco inferior é posicionado na parte inferior do card.
 *
 * Ao clicar no card, a ação definida em onClick é executada.
 *
 * @param featureItem Objeto FeatureItem que contém as informações da funcionalidade.
 * @param onClick Callback executado quando o card é clicado.
 * @param modifier Modifier opcional para personalização adicional.
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
        // Coluna que organiza o ícone e os textos com espaço uniforme entre eles.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Bloco do ícone, exibido na parte superior.
            Surface(
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.small, // Pode ser ajustado para CircleShape se preferir
                color = featureItem.iconBgColor
            ) {
                Icon(
                    imageVector = featureItem.icon,
                    contentDescription = featureItem.title,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(4.dp)
                )
            }
            // Bloco de textos, exibido na parte inferior com espaço de padding no topo para evitar sobreposição.
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
