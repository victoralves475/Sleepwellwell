package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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
            text = "Dicas para melhorar seu sono",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dicas) { dica ->
                TipCard(dica.titulo, dica.descricao)
            }
        }
    }
}

@Composable
fun TipCard(title: String, description:String, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { expanded = !expanded } // Alterna o estado ao clicar no card
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f) // Faz o título ocupar o máximo possível do espaço disponível
                )

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
                    text = description,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Justify,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
