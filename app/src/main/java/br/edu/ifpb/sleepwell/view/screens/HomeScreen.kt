package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Representa uma funcionalidade do app, exibida como card na grade
 */
data class FeatureItem(
    val title: String,
    val description: String,
    val count: Int = 0 // Por exemplo, "5 entradas", "2 novos", etc.
)

/**
 * HomeScreen - Exemplo de layout baseado no mockup, que agora exibe "Olá, [nome do usuário]".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String, // O nome do usuário logado (sem valor default, pois deve ser fornecido)
    efficiency: Float = 0.76f,
    onTabSelected: (String) -> Unit = {},  // "Visão Geral" ou "Histórico de Sono"
    onFeatureClick: (FeatureItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    // Exemplo de lista de funcionalidades
    val features = listOf(
        FeatureItem("Diário", "5 Entradas"),
        FeatureItem("Lembretes", "2 Novos"),
        FeatureItem("Monitoramento de Ciclos", "3 Novos"),
        FeatureItem("Despertadores", "")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Início", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { /* Ação de abrir perfil */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Saudação com o nome do usuário logado
            Text(
                text = "Olá, $userName",
                style = MaterialTheme.typography.headlineMedium,
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
                modifier = Modifier.fillMaxHeight(0.5f),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(features) { feature ->
                    FeatureCard(featureItem = feature) {
                        onFeatureClick(feature)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Sair", color = MaterialTheme.colorScheme.onSecondary)
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
            .height(100.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = featureItem.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = featureItem.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
