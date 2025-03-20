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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.sleepwell.alarm.AlarmDisableReceiver
import br.edu.ifpb.sleepwell.alarm.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

/**
 * AlarmScreen exibe a interface para o usuário configurar o alarme.
 *
 * Nessa tela, o usuário pode:
 * - Selecionar um horário via TimePicker para definir o alarme.
 * - Testar o alarme com um tempo de +5 segundos.
 * - Visualizar uma lista de horários sugeridos (baseada nos ciclos de sono).
 * - Cancelar ou parar o alarme.
 *
 * O layout utiliza o fundo definido pelo tema e espaçamentos consistentes.
 */
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AlarmScreen(context: Context) {
    // Estado que guarda o tempo selecionado (formato de exibição)
    var selectedTime by remember { mutableStateOf("") }
    // Estado que guarda o tempo do alarme (em milissegundos)
    var alarmTime by remember { mutableStateOf(0L) }
    // Duração de um ciclo de sono (90 minutos)
    val cycleDuration = 90 * 60 * 1000L
    // Lista de horários sugeridos (baseada na data selecionada e no ciclo de sono)
    var suggestedTimes by remember { mutableStateOf<List<Long>>(emptyList()) }
    // Estado para saber se o usuário já selecionou um horário sugerido
    var selectedSuggestedTime by remember { mutableStateOf<Long?>(null) }

    // Layout principal com fundo e padding
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fundo definido pelo tema (geralmente preto)
            .padding(16.dp)
    ) {
        // Organiza os elementos em uma coluna vertical, alinhados à esquerda e centralizados verticalmente
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Título da tela: "Ajuste seu" em fonte menor
            Text(
                text = "Ajuste seu",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Título principal: "Despertador" em fonte grande e negrito
            Text(
                text = "Despertador",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botão "Definir" para selecionar o horário via TimePicker
            Button(
                onClick = {
                    // Abre o TimePicker; quando o horário é selecionado, calcula os horários sugeridos
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Definir", color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Botão "Testar (+5s)" para testar o alarme com um atraso de 5 segundos
            Button(
                onClick = {
                    val now = Calendar.getInstance().apply { add(Calendar.SECOND, 5) }
                    alarmTime = now.timeInMillis
                    // Formata a hora atual para exibição
                    selectedTime = SimpleDateFormat("hh:mm:ss a dd MMM yyyy", Locale.getDefault()).format(now.time)
                    // Configura o alarme
                    setAlarm(context, alarmTime, "Start")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Testar (+5s)", color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Exibe a lista de horários sugeridos em um Card, se houver e se nenhum tiver sido selecionado ainda
            if (suggestedTimes.isNotEmpty() && selectedSuggestedTime == null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(suggestedTimes) { time ->
                            // Formata o horário sugerido no padrão "hh:mm a"
                            val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(time))
                            OutlinedButton(
                                onClick = {
                                    // Ao selecionar, salva o horário sugerido e configura o alarme
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

            // Linha com botões para "Cancelar" e "Parar" o alarme
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { cancelAlarm(context) },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSecondary, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
//                Button(
//                    onClick = { stopAlarm(context) },
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(40.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.secondary
//                    ),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Text("Parar", color = MaterialTheme.colorScheme.onSecondary, fontSize = 16.sp)
//                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Exibe o tempo do alarme configurado, se disponível
            if (selectedTime.isNotEmpty()) {
                Text(
                    text = "Alarme criado: $selectedTime",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

/**
 * Exibe um TimePickerDialog para selecionar um horário.
 *
 * @param context Contexto para exibir o diálogo.
 * @param onTimeSelected Callback que recebe a hora e minuto selecionados.
 */
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

/**
 * Calcula os horários sugeridos com base no tempo alvo e na duração do ciclo de sono.
 *
 * O método adiciona o tamanho do ciclo repetidamente até atingir ou ultrapassar o tempo alvo,
 * retornando os últimos 4 horários calculados.
 *
 * @param targetTime Tempo alvo em milissegundos.
 * @param cycleSize Duração do ciclo em milissegundos.
 * @return Lista de horários sugeridos (em milissegundos).
 */
fun calculateSleepCycles(targetTime: Long, cycleSize: Long): List<Long> {
    val currentTime = System.currentTimeMillis()
    val wakeUpTimes = mutableListOf<Long>()
    var alarmTime = targetTime

    // Se o tempo alvo já passou, ajusta para o próximo dia.
    if (alarmTime < currentTime) {
        alarmTime += 24 * 60 * 60 * 1000L
    }

    var time = currentTime
    while (time <= alarmTime) {
        time += cycleSize
        wakeUpTimes.add(time)
    }
    // Retorna os últimos 4 horários sugeridos.
    return wakeUpTimes.takeLast(4)
}

/**
 * Configura um alarme utilizando o AlarmManager.
 *
 * @param context Contexto para acessar o AlarmManager.
 * @param millisTime Tempo do alarme em milissegundos.
 * @param str String que indica a ação ("Start" para iniciar o alarme).
 */
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

/**
 * Cancela o alarme configurado.
 *
 * @param context Contexto para acessar o AlarmManager.
 */
fun cancelAlarm(context: Context) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("Service1", "Stop")
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, 2407, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
    Toast.makeText(context, "Alarme cancelado", Toast.LENGTH_SHORT).show()
}

/**
 * Para o alarme que está tocando.
 *
 * @param context Contexto para acessar o AlarmManager e enviar broadcast para parar o alarme.
 */
fun stopAlarm(context: Context) {
    val intent = Intent(context, AlarmDisableReceiver::class.java).apply {
        action = "br.edu.ifpb.sleepwell.ALARM_DISABLE"
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, 2407, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    )
    if (pendingIntent != null) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
    context.sendBroadcast(intent)
    Toast.makeText(context, "Alarme parado", Toast.LENGTH_SHORT).show()
}
