package br.edu.ifpb.pdm.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.ifpb.sleepwell.alarm.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AlarmScreen(context: Context) {
    var selectedTime by remember { mutableStateOf("") }
    var alarmTime by remember { mutableStateOf(0L) }
    val cycleDuration = 90 * 60 * 1000L // 90 minutos em milissegundos
    var suggestedTimes by remember { mutableStateOf<List<Long>>(emptyList()) }
    var selectedSuggestedTime by remember { mutableStateOf<Long?>(null) } // Para armazenar o horário sugerido selecionado

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botões de Set Alarm e Test Alarm
        Button(onClick = {
            showTimePicker(context) { hour, minute ->
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }
                suggestedTimes = calculateSleepCycles(calendar.timeInMillis, cycleDuration)
            }
        }) {
            Text("Set Alarm")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val now = Calendar.getInstance()
            now.add(Calendar.SECOND, 5) // Adiciona 5 segundos ao tempo atual
            alarmTime = now.timeInMillis
            selectedTime = SimpleDateFormat("hh:mm:ss a dd MMM yyyy").format(now.time)
            setAlarm(context, alarmTime, "Start")
        }) {
            Text("Test Alarm (+5s)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exibir os horários sugeridos após a seleção
        if (suggestedTimes.isNotEmpty() && selectedSuggestedTime == null) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(suggestedTimes) { time ->
                    val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(time))
                    Button(
                        onClick = {
                            selectedSuggestedTime = time // Salva o horário selecionado
                            selectedTime = formattedTime // Formata o horário selecionado
                            setAlarm(context, time, "Start")
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Text("Suggested: $formattedTime")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botões de Cancel Alarm e Stop Alarm (sempre visíveis)
        Button(onClick = { cancelAlarm(context) }) {
            Text("Cancel Alarm")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { stopAlarm(context) }) {
            Text("Stop Alarm")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar o tempo do alarme após a seleção
        if (selectedTime.isNotEmpty()) {
            Text("Alarm Set For: $selectedTime")
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
        alarmTime += + 24 * 60 * 60 * 1000L // Ajuste para o próximo dia
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
