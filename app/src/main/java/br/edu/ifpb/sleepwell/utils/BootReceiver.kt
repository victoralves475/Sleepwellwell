package br.edu.ifpb.sleepwell.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.edu.ifpb.sleepwell.agendarNotificacaoDiaria

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            context?.let { agendarNotificacaoDiaria(it) }
        }
    }
}