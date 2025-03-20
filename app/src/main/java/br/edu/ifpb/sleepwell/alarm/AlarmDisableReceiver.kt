package br.edu.ifpb.sleepwell.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * AlarmDisableReceiver é um BroadcastReceiver que recebe a ação de desativar o alarme.
 * Quando acionado, ele interrompe o serviço do alarme.
 */
class AlarmDisableReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Verifica se a ação corresponde à de parar o alarme
        if (intent?.action == "ACTION_STOP_ALARM") {
            val serviceIntent = Intent(context, AlarmService::class.java)
            context.stopService(serviceIntent)
        }
    }
}
