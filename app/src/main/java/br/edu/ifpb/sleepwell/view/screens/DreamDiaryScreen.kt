package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.sleepwell.controller.DiarioDeSonhoController
import br.edu.ifpb.sleepwell.model.entity.DiarioDeSonho

/**
 * Função que exibe a tela do diário de sonhos.
 *
 * Essa tela mostra o título "Diário dos Sonhos", exibe um indicador de carregamento enquanto os diários são buscados,
 * e, se houver, lista os diários em uma LazyColumn. Caso não haja diários ou ocorra um erro, exibe mensagens apropriadas.
 *
 * @param onAddDreamClick Callback que é acionado quando o usuário deseja adicionar um novo sonho.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DreamDiaryScreen(
    onAddDreamClick: () -> Unit // Callback para navegar para a tela de adição de um novo sonho.
) {
    // Instancia o controller responsável por gerenciar os diários de sonho.
    val diarioController = remember { DiarioDeSonhoController() }
    // Estado para armazenar a lista de diários recuperados do banco de dados.
    var listaDiarios by remember { mutableStateOf(listOf<DiarioDeSonho>()) }
    // Estado para armazenar mensagens de erro, se ocorrer alguma falha.
    var mensagemErro by remember { mutableStateOf("") }
    // Estado para indicar se os dados ainda estão sendo carregados.
    var isLoading by remember { mutableStateOf(true) }

    // Busca os diários de sonho quando a tela é carregada.
    LaunchedEffect(Unit) {
        isLoading = true // Inicia o carregamento
        diarioController.listarDiariosDeSonho(
            onSuccess = { diarios ->
                listaDiarios = diarios
                isLoading = false // Finaliza o carregamento após o sucesso
            },
            onFailure = { e ->
                mensagemErro = "Erro ao carregar diários: ${e.message}"
                isLoading = false // Finaliza o carregamento mesmo em caso de erro
            }
        )
    }

    // Scaffold organiza a tela e adiciona um FloatingActionButton para adicionar um novo sonho.
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDreamClick, // Callback para navegar para a tela de adição de sonho.
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Sonho")
            }
        }
    ) { padding ->
        // Coluna principal que organiza o conteúdo verticalmente.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Título da tela: "Diário dos" (linha menor)
            Text(
                text = "Diário dos",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Título principal: "Sonhos" (linha maior e em destaque)
            Text(
                text = "Sonhos",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Exibe um CircularProgressIndicator se os dados ainda estiverem sendo carregados.
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            // Se houver mensagem de erro, exibe-a centralizada.
            if (mensagemErro.isNotEmpty()) {
                Text(
                    text = mensagemErro,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            // Se não houver diários, o carregamento terminou e não houve erro, exibe uma mensagem.
            if (listaDiarios.isEmpty() && !isLoading && mensagemErro.isEmpty()) {
                Text(
                    text = "Nenhum diário encontrado.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            } else {
                // Lista os diários de sonho utilizando LazyColumn para exibição eficiente de listas.
                LazyColumn {
                    items(listaDiarios) { diario ->
                        DreamCard(
                            diarioId = diario.id,
                            titulo = diario.titulo,
                            data = diario.data, // A data será formatada na função formatDate
                            relato = diario.relato,
                            onDelete = {
                                // Callback para excluir o diário, removendo-o da lista se a operação for bem-sucedida.
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

/**
 * DreamCard exibe as informações de um Diário de Sonho em um cartão interativo.
 *
 * O cartão mostra o título e a data do diário; ao ser clicado, expande para revelar o relato completo
 * e um botão de exclusão para remover o diário.
 *
 * @param diarioId ID do diário.
 * @param titulo Título do diário.
 * @param data Data do diário (deve ser formatada antes da exibição).
 * @param relato Relato do diário.
 * @param onDelete Callback acionado quando o usuário clica para excluir o diário.
 * @param modifier Modifier opcional para personalização adicional.
 */
@Composable
fun DreamCard(
    diarioId: String,
    titulo: String,
    data: String,
    relato: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado que controla se o cartão está expandido para mostrar o relato completo.
    var expanded by remember { mutableStateOf(false) }

    // OutlinedCard é usado para exibir o conteúdo do diário com bordas e elevação.
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // Ao clicar no cartão, alterna o estado expandido.
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Linha superior: exibe o título e a data do diário.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Coluna que contém o título e a data.
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Data: ${formatDate(data)}", // Chama a função utilitária para formatar a data.
                        color = Color.Gray,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                // Botão para expandir ou recolher o cartão.
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Recolher" else "Expandir"
                    )
                }
            }
            // Se o cartão estiver expandido, exibe o relato completo e o botão de exclusão.
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
                // Botão para excluir o diário, alinhado à direita.
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Excluir", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Função utilitária que formata uma string de data para o formato "dd/MM/yyyy".
 *
 * Se o input contiver exatamente 8 dígitos (ex: "12122025"), a função insere as barras
 * nos locais corretos para retornar "12/12/2025". Caso contrário, retorna o input original.
 *
 * @param input String contendo a data sem formatação.
 * @return Data formatada no padrão "dd/MM/yyyy" ou o input original se não possuir 8 dígitos.
 */
fun formatDate(input: String): String {
    // Filtra somente os dígitos do input
    val digits = input.filter { it.isDigit() }
    // Se houver exatamente 8 dígitos, formata para dd/MM/yyyy
    return if (digits.length == 8) {
        "${digits.substring(0, 2)}/${digits.substring(2, 4)}/${digits.substring(4, 8)}"
    } else {
        input
    }
}
