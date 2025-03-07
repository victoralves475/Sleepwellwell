package br.edu.ifpb.sleepwell.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * AlarmReceiver é um BroadcastReceiver que é disparado pelo AlarmManager quando o alarme é acionado.
 * Ele inicia o serviço de alarme (AlarmService) em primeiro plano, garantindo que ele seja executado
 * mesmo em Android 8.0 (Oreo) ou superior, onde os serviços em background têm restrições.
 */
class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        // Cria uma Intent para iniciar o AlarmService
        val serviceIntent = Intent(context, AlarmService::class.java)
        // Inicia o serviço em primeiro plano (necessário para Android 8+)
        context.startForegroundService(serviceIntent)
    }
}
