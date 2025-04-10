package com.example.meucarro.services.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.meucarro.MainActivity
import com.example.meucarro.R
import com.example.meucarro.services.http.RetrofitClient
import com.example.meucarro.services.http.maintenance.MaintenanceService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class MaintenanceReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("MaintenanceWorker", "Worker iniciado às ${System.currentTimeMillis()}")
        Log.e("MaintenanceWorker", "Iniciando o worker de manutenção")

        val context = applicationContext

        try {
            val service = RetrofitClient.createService(
                MaintenanceService::class.java, context
            )
            val maintenances = service.getMaintenances()

            Log.d("MaintenanceWorker", "Total de manutenções encontradas: ${maintenances.size}")

            // Verifica próximas manutenções
            val now = Calendar.getInstance()
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val dueSoon = maintenances.filter {
                try {
                    val isoFormatter =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
                    val dueDate = Calendar.getInstance().apply {
                        time = isoFormatter.parse(it.nextDueAt)!!
                    }

                    val fifteenDaysAgo = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, -15)
                    }

                    val fifteenDaysAhead = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, 15)
                    }

                    !dueDate.before(fifteenDaysAgo) && !dueDate.after(fifteenDaysAhead)
                } catch (e: Exception) {
                    false
                }
            }

            Log.d("MaintenanceWorker", "Manutenções dentro do intervalo: ${dueSoon.size}")
            dueSoon.forEach {
                Log.d("MaintenanceWorker", "→ ${it.name} - ${it.nextDueAt}")
            }

            if (dueSoon.isNotEmpty()) {
                val titles = dueSoon.joinToString("\n") { "• ${it.name}" }
                sendNotification(
                    context,
                    "Você tem manutenções próximas",
                    titles
                )
            }

            Log.d("MaintenanceWorker", "Worker finalizado com sucesso.")
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

    private fun sendNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "maintenance_channel",
                "Lembretes de manutenção",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "home")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "maintenance_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))

        notificationManager.notify(1, builder.build())
    }
}