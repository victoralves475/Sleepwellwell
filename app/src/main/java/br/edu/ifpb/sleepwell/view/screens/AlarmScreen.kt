package br.edu.ifpb.sleepwell.view.screens

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.sleepwell.alarm.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AlarmScreen(context: Context) {
    var selectedTime by remember { mutableStateOf("") }
    var alarmTime by remember { mutableStateOf(0L) }
    val cycleDuration = 90 * 60 * 1000L // 90 minutos em milissegundos
    var suggestedTimes by remember { mutableStateOf<List<Long>>(emptyList()) }
    var selectedSuggestedTime by remember { mutableStateOf<Long?>(null) }

    // Layout principal com fundo do tema
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Título da tela
            Text(
                text = "Ajuste seu",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Despertador",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botão para selecionar horário via TimePicker
            Button(
                onClick = {
                    showTimePicker(context) { hour, minute ->
                        val calendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                        }
                        suggestedTimes = calculateSleepCycles(calendar.timeInMillis, cycleDuration)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary), shape = RoundedCornerShape(8.dp)
            ) {
                Text("Definir", color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Botão para teste do alarme (+5s)
            Button(
                onClick = {
                    val now = Calendar.getInstance().apply { add(Calendar.SECOND, 5) }
                    alarmTime = now.timeInMillis
                    selectedTime = SimpleDateFormat("hh:mm:ss a dd MMM yyyy", Locale.getDefault()).format(now.time)
                    setAlarm(context, alarmTime, "Start")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary), shape = RoundedCornerShape(8.dp)
            ) {
                Text("Testar (+5s)", color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de horários sugeridos, exibida em uma LazyColumn dentro de um Card
            if (suggestedTimes.isNotEmpty() && selectedSuggestedTime == null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(suggestedTimes) { time ->
                            val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(time))
                            OutlinedButton(
                                onClick = {
                                    selectedSuggestedTime = time
                                    selectedTime = formattedTime
                                    setAlarm(context, time, "Start")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Suggested: $formattedTime")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Linha com botões para Cancel e Stop Alarm
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { cancelAlarm(context) },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults
                        .buttonColors(containerColor = MaterialTheme.colorScheme.secondary), shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSecondary, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { stopAlarm(context) },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults
                        .buttonColors(containerColor = MaterialTheme.colorScheme.secondary), shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Parar", color = MaterialTheme.colorScheme.onSecondary, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Exibe o tempo do alarme configurado
            if (selectedTime.isNotEmpty()) {
                Text(
                    text = "Alarm Set For: $selectedTime",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun showTimePicker(context: Context, onTimeSelected: (Int, Int) -> Unit) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hour, minute -> onTimeSelected(hour, minute) },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    ).show()
}

fun calculateSleepCycles(targetTime: Long, cycleSize: Long): List<Long> {
    val currentTime = System.currentTimeMillis()
    val wakeUpTimes = mutableListOf<Long>()
    var alarmTime = targetTime

    if (alarmTime < currentTime) {
        alarmTime += 24 * 60 * 60 * 1000L // Ajuste para o próximo dia
    }

    var time = currentTime
    while (time <= alarmTime) {
        time += cycleSize
        wakeUpTimes.add(time)
    }

    return wakeUpTimes.takeLast(4)
}

@RequiresApi(Build.VERSION_CODES.S)
fun setAlarm(context: Context, millisTime: Long, str: String) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("Service1", "Start")
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, 2407, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (str == "Start" && alarmManager.canScheduleExactAlarms()) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millisTime, pendingIntent)
        Toast.makeText(context, "Alarm set", Toast.LENGTH_SHORT).show()
    }
}

fun cancelAlarm(context: Context) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("Service1", "Stop")
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, 2407, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
    Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show()
}

fun stopAlarm(context: Context) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("Service1", "Stop")
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, 2407, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    )
    if (pendingIntent != null) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
    context.sendBroadcast(intent)
    Toast.makeText(context, "Alarm stopped", Toast.LENGTH_SHORT).show()
}
