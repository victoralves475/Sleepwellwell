package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TipsScreen(modifier: Modifier = Modifier) {
    val tips = listOf(
        "Evite cafeína antes de dormir",
        "Crie uma rotina de sono regular",
        "Reduza a exposição à luz azul antes de dormir",
        "Mantenha seu quarto escuro e silencioso",
        "Evite refeições pesadas antes de deitar",
        "Pratique exercícios físicos regularmente",
        "Mantenha uma temperatura agradável no quarto"
    )

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
            items(tips) { tip ->
                TipCard(text = tip)
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
