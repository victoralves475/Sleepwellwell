package br.edu.ifpb.sleepwell.alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import br.edu.ifpb.sleepwell.R

/**
 * AlarmService é um Service que gerencia a execução do alarme.
 * Ele é iniciado em primeiro plano e reproduz um som de alarme em loop.
 * Após 60 segundos, o serviço se encerra automaticamente.
 */
class AlarmService : Service() {

    // MediaPlayer para reproduzir o som do alarme
    private var mediaPlayer: MediaPlayer? = null
    // Handler para agendar o término do serviço
    private val handler = Handler(Looper.getMainLooper())

    /**
     * onCreate é chamado quando o serviço é criado.
     * Aqui, iniciamos o serviço em primeiro plano e agendamos o encerramento após 60 segundos.
     */
    override fun onCreate() {
        super.onCreate()
        // Inicia o serviço em primeiro plano com uma notificação
        startForegroundService()

        // Agenda a parada do serviço após 60 segundos (60 * 1000 milissegundos)
        handler.postDelayed({
            stopSelf()
        }, 60 * 1000)
    }

    /**
     * onStartCommand é chamado quando o serviço é iniciado.
     * Aqui, criamos e iniciamos o MediaPlayer para reproduzir o som do alarme.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Cria o MediaPlayer utilizando o som localizado em res/raw/alarm.mp3 (ou outro formato)
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
        // Configura para que o som toque em loop
        mediaPlayer?.isLooping = true
        // Inicia a reprodução do som
        mediaPlayer?.start()

        // Retorna START_STICKY para que o serviço seja reiniciado caso seja encerrado pelo sistema
        return START_STICKY
    }

    /**
     * onDestroy é chamado quando o serviço é encerrado.
     * Aqui, paramos e liberamos os recursos do MediaPlayer.
     */
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }

    /**
     * onBind não é utilizado para serviços iniciados (startService), portanto retornamos null.
     */
    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * startForegroundService configura e inicia o serviço em primeiro plano.
     * Para Android O (8.0) e superior, é necessário criar um canal de notificação.
     * Em seguida, uma notificação é construída e o serviço é iniciado em primeiro plano com ela.
     */
    private fun startForegroundService() {
        // Define o ID e o nome do canal de notificação
        val channelId = "alarm_service_channel"
        val channelName = "Alarm Service"

        // Para Android O e superior, crie o canal de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH // Alta importância para alertar o usuário
            )
            // Cria o canal usando o NotificationManager do sistema
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // Cria um PendingIntent para a ação "Desativar"
        val stopIntent = Intent(this, AlarmDisableReceiver::class.java).apply {
            action = "ACTION_STOP_ALARM"
        }
        val pendingStopIntent = PendingIntent.getBroadcast(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Constrói a notificação com a ação para desativar o alarme
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Alarme Ativo")
            .setContentText("O alarme está tocando.")
            .setSmallIcon(R.drawable.alarm) // Ícone definido na pasta res/drawable
            .addAction(android.R.drawable.ic_media_pause, "Desativar", pendingStopIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Inicia o serviço em primeiro plano com o ID 1 e a notificação criada
        startForeground(1, notification)
    }
}
