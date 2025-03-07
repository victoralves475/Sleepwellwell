package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*         // Importa funções para layout (Column, Row, Spacer, etc.)
import androidx.compose.foundation.lazy.LazyColumn  // Importa LazyColumn para listas roláveis
import androidx.compose.foundation.lazy.items       // Importa items para LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells  // Importa GridCells (não utilizado nesta versão)
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid  // Importa LazyVerticalGrid (não utilizado nesta versão)
import androidx.compose.foundation.lazy.grid.items   // Importa items para LazyVerticalGrid (não utilizado nesta versão)
import androidx.compose.foundation.shape.RoundedCornerShape  // Importa RoundedCornerShape para bordas arredondadas
import androidx.compose.material.icons.Icons        // Importa ícones padrão
import androidx.compose.material.icons.filled.KeyboardArrowDown  // Importa ícone de seta para baixo
import androidx.compose.material.icons.filled.KeyboardArrowUp    // Importa ícone de seta para cima
import androidx.compose.material3.*                 // Importa Material3 (componentes, temas, etc.)
import androidx.compose.runtime.*                 // Importa funções para estado (remember, mutableStateOf, etc.)
import androidx.compose.ui.Alignment              // Importa Alignment para alinhar os elementos
import androidx.compose.ui.Modifier               // Importa Modifier para customização dos layouts
import androidx.compose.ui.graphics.Color         // Importa Color para cores
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign     // Importa TextAlign para alinhamento de texto
import androidx.compose.ui.unit.dp                  // Importa dp para dimensões
import androidx.compose.ui.unit.sp                  // Importa sp para tamanhos de fonte
import br.edu.ifpb.sleepwell.controller.DicaController  // Importa o controlador de dicas
import br.edu.ifpb.sleepwell.model.entity.Dica   // Importa a entidade Dica

/**
 * TipsScreen exibe a tela de dicas do aplicativo.
 *
 * Essa tela busca as dicas do Firestore utilizando o DicaController, armazena-as em um estado,
 * e exibe os dados em uma lista rolável (LazyColumn). Também apresenta títulos e espaçamentos
 * de acordo com o design proposto.
 *
 * @param modifier Modifier opcional para customização do layout da tela.
 */
@Composable
fun TipsScreen(modifier: Modifier = Modifier) {
    // Cria uma instância do DicaController e a mantém enquanto a tela estiver ativa.
    val controller = remember { DicaController() }
    // Declara um estado para armazenar a lista de dicas, inicializada como uma lista vazia.
    var dicas by remember { mutableStateOf<List<Dica>>(emptyList()) }

    // Ao iniciar a tela, executa o bloco dentro de LaunchedEffect para buscar as dicas.
    LaunchedEffect(Unit) {
        controller.listarDicas { dicasRecebidas ->
            // Atualiza o estado 'dicas' com as dicas recebidas do Firestore.
            dicas = dicasRecebidas
        }
    }

    // Layout principal da tela, organizado em uma Column com padding e alinhamento à esquerda.
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()        // Preenche toda a tela.
            .padding(16.dp)       // Aplica um padding de 16dp em todos os lados.
    ) {
        // Exibe um texto de cabeçalho para a seção.
        Text(
            text = "Melhore seu sono",
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onBackground
        )
        // Espaçamento vertical de 8dp entre os textos.
        Spacer(modifier = Modifier.height(8.dp))
        // Exibe o título principal da tela ("Dicas").
        Text(
            text = "Dicas",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp),
            color = MaterialTheme.colorScheme.onBackground
        )
        // Espaçamento vertical de 24dp antes da lista.
        Spacer(modifier = Modifier.height(24.dp))

        // LazyColumn para exibir as dicas de forma rolável, com um espaçamento de 8dp entre os itens.
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Para cada dica na lista 'dicas', chama TipCard passando título e descrição.
            items(dicas) { dica ->
                TipCard(dica.titulo, dica.descricao)
            }
        }
    }
}

/**
 * TipCard exibe uma dica em formato de card interativo.
 *
 * O card apresenta o título da dica e um ícone para alternar sua expansão.
 * Quando expandido, exibe a descrição completa da dica.
 *
 * @param title Título da dica.
 * @param description Descrição completa da dica.
 * @param modifier Modifier opcional para customização adicional do layout do card.
 */
@Composable
fun TipCard(title: String, description: String, modifier: Modifier = Modifier) {
    // Estado para controlar se o card está expandido (exibindo a descrição) ou recolhido.
    var expanded by remember { mutableStateOf(false) }

    // OutlinedCard cria um card com bordas e elevação.
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // Ao clicar no card, alterna o estado de expansão.
        onClick = { expanded = !expanded }
    ) {
        // Coluna que organiza o conteúdo interno do card com padding e alinhamento central.
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Row que exibe o título e o botão de ícone para expandir/recolher o card.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Texto que exibe o título da dica, ocupando o máximo de espaço disponível na linha.
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                // Botão de ícone que alterna o estado do card (expandir ou recolher).
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Recolher" else "Expandir"
                    )
                }
            }
            // Se o card estiver expandido, exibe a descrição da dica.
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Left,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
