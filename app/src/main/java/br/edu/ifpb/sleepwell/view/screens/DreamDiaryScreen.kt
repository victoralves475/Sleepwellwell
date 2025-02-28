package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.sleepwell.controller.DiarioDeSonhoController
import br.edu.ifpb.sleepwell.model.entity.DiarioDeSonho
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DreamDiaryScreen(
    onAddDreamClick: () -> Unit // Navega para a tela de adição de um novo sonho
) {
    val diarioController = remember { DiarioDeSonhoController() }
    var listaDiarios by remember { mutableStateOf(listOf<DiarioDeSonho>()) }
    var mensagemErro by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // Busca os diários ao carregar a tela
    LaunchedEffect(Unit) {
        isLoading = true // Inicia o carregamento
        diarioController.listarDiariosDeSonho(
            onSuccess = { diarios ->
                listaDiarios = diarios
                isLoading = false // Finaliza o carregamento
            },
            onFailure = { e ->
                mensagemErro = "Erro ao carregar diários: ${e.message}"
                isLoading = false // Finaliza o carregamento
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDreamClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Sonho")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título da tela
            Text(
                text = "Diário de Sonhos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Estado de Carregamento
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            // Mensagem de Erro (se houver)
            if (mensagemErro.isNotEmpty()) {
                Text(
                    text = mensagemErro,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            // Listagem dos Diários de Sonho
            if (listaDiarios.isEmpty() && !isLoading && mensagemErro.isEmpty()) {
                Text(
                    text = "Nenhum diário encontrado.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn {
                    items(listaDiarios) { diario ->
                        DreamCard(
                            diarioId = diario.id,
                            titulo = diario.titulo,
                            data = diario.data,
                            relato = diario.relato,

                            onDelete = {
                                diarioController.excluirDiarioDeSonho(
                                    diario.id,
                                    onSuccess = {
                                        listaDiarios = listaDiarios.filter { it.id != diario.id }
                                    },
                                    onFailure = { e ->
                                        mensagemErro = "Erro ao excluir: ${e.message}"
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DreamCard(
    diarioId: String,
    titulo: String,
    data: String,
    relato: String,
    onDelete: () -> Unit, // Callback para exclusão
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { expanded = !expanded } // Alterna o estado ao clicar no card
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Data: $data",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Recolher" else "Expandir"
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = relato,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Justify,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botão de Exclusão Centralizado
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Excluir", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}