package br.edu.ifpb.sleepwell.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.edu.ifpb.sleepwell.model.entity.Dica
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class DicaWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("dicasGerais")

        collectionRef.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val dicas = documents.documents.mapNotNull { it.toObject(Dica::class.java) }
                    if (dicas.isNotEmpty()) {
                        val dicaAleatoria = dicas[Random.nextInt(dicas.size)]
                        enviarNotificacao(dicaAleatoria)
                    }
                }
            }
            .addOnFailureListener {
                // Log de erro (opcional)
            }

        return Result.success()
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
