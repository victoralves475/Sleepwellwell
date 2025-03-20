package br.edu.ifpb.sleepwell.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.edu.ifpb.sleepwell.model.data.repository.DicaRepository
import br.edu.ifpb.sleepwell.model.entity.Dica
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class DicaWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            // Usando o DicaRepository para buscar as dicas
            val dicas = runBlocking {
                DicaRepository().ListarDicas()
            }

            if (dicas.isNotEmpty()) {
                val dicaAleatoria = dicas[Random.nextInt(dicas.size)]
                enviarNotificacao(dicaAleatoria)
            }

            Result.success()
        } catch (e: Exception) {
            println("❌ Erro ao buscar dicas no DicaWorker: ${e.message}")
            Result.failure()
        }
    }

    private fun enviarNotificacao(dica: Dica) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, "sleep_tips_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Dica para Melhorar seu Sono")
            .setContentText(dica.titulo)
            .setStyle(NotificationCompat.BigTextStyle().bigText(dica.descricao))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}
