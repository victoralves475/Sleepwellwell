package br.edu.ifpb.sleepwell.view.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.sleepwell.data.model.SleepRecord
import br.edu.ifpb.sleepwell.utils.SleepViewModel
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SleepQualityScreen(
    userId: String,
    sleepViewModel: SleepViewModel = viewModel(),
    onSleepRecorded: () -> Unit
) {
    // Cores pastel definidas para feedback visual:
    val pastelGreen = Color(0xFFC8E6C9)  // Bom
    val pastelRed = Color(0xFFFFCDD2)    // Ruim
    val unselectedBackground = MaterialTheme.colorScheme.onPrimary

    var selectedOption by remember { mutableStateOf<Boolean?>(null) }

    // Layout principal dentro de um Card para destacar o conteúdo
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center)
    {
        Column (
            modifier = Modifier.padding(start = 16.dp),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = "Como foi o seu",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sono?",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 50.sp),
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Botão "Bom"
                    Button(
                        onClick = { selectedOption = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedOption == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Bom",
                            color = if (selectedOption == true) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                        )
                    }
                    // Botão "Ruim"
                    Button(
                        onClick = { selectedOption = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedOption == false) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Ruim",
                            color = if (selectedOption == false) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (selectedOption != null) {
                    Button(
                        onClick = {
                            val yesterday = LocalDate.now().minusDays(1)
                            val zone = ZoneId.systemDefault()
                            val epoch = yesterday.atStartOfDay(zone).toEpochSecond() * 1000
                            val record = SleepRecord(
                                recordId = "",
                                userId = userId,
                                date = epoch,
                                quality = selectedOption
                            )
                            sleepViewModel.addSleepRecord(record) { success ->
                                if (success) {
                                    onSleepRecorded()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Registrar", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
