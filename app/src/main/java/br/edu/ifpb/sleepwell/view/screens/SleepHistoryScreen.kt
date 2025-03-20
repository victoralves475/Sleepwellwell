package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.sleepwell.data.model.SleepRecord
import br.edu.ifpb.sleepwell.utils.SleepViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SleepHistoryScreen(
    userId: String,
    sleepViewModel: SleepViewModel = viewModel()
) {
    var percentage by remember { mutableStateOf(0) }
    var records by remember { mutableStateOf<List<SleepRecord>>(emptyList()) }

    LaunchedEffect(userId) {
        sleepViewModel.computeSleepQualityPercentage(userId) { perc ->
            percentage = perc
        }
        sleepViewModel.fetchSleepRecords(userId) { recs ->
            records = recs
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Histórico do",
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onBackground
        )
        // Espaçamento vertical de 8dp entre os textos.
        Spacer(modifier = Modifier.height(8.dp))
        // Exibe o título principal da tela ("Dicas").
        Text(
            text = "Sono",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = percentage / 100f,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Qualidade do sono: $percentage%",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(records) { record ->
                SleepRecordCard(record = record)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SleepRecordCard(record: SleepRecord) {
    val dateString = formatDate(record.date)
    val qualityString = when (record.quality) {
        true -> "Bom"
        false -> "Ruim"
        else -> "Não registrado"
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = dateString,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (qualityString == "Bom") {
                Text(
                    text = qualityString,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    color = Color(0xFFBBDEFB)
                )
            }
            else {
                Text(
                    text = qualityString,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    color = Color(0xFFFFCDD2)
                )
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
