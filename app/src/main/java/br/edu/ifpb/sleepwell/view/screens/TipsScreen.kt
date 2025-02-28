package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.sleepwell.controller.DicaController
import br.edu.ifpb.sleepwell.model.entity.Dica

@Composable
fun TipsScreen(modifier: Modifier = Modifier) {
    val controller = remember { DicaController() }
    var dicas by remember { mutableStateOf<List<Dica>>(emptyList()) }

    // Buscar dicas do Firestore ao abrir a tela
    LaunchedEffect(Unit) {
        controller.listarDicas { dicasRecebidas ->
            dicas = dicasRecebidas
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Dicas para Melhorar seu Sono",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dicas) { dica ->
                TipCard(text = dica.descricao)
            }
        }
    }
}

@Composable
fun TipCard(text: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = text, fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
