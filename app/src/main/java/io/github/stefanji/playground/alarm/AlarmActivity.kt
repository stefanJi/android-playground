package io.github.stefanji.playground.alarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.github.stefanji.playground.R
import java.util.*

/**
 * Create by jy on 2020-01-10
 */
class AlarmActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frame = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        frame.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        frame.addView(Button(this).apply {
            text = "enable"
            setOnClickListener { enableAlarm(this@AlarmActivity) }
        }, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        frame.addView(Button(this).apply {
            text = "disable"
            setOnClickListener { disable() }
        }, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        setContentView(frame)
    }

    private fun disable() {
        val mgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.cancel(createPendingIntent(this))
    }
}

private const val CHANNEL_ID = "playground.alarm.AlarmActivity_channel"

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "playground.alarm.AlarmActivity" -> {
                Log.d("ALARM", "receive alarm broadcast")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel(context)
                }
                val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("title")
                    .setContentText("content ${System.currentTimeMillis()}")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(context)) {
                    notify(1, builder.build())
                    enableAlarm(context)
                }
            }

            Intent.ACTION_BOOT_COMPLETED -> {
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "playground notification"
        val descriptionText = "$name description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply { description = descriptionText }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

private fun enableAlarm(context: Context) {
    // Set the alarm to start at approximately 2:00 p.m.
    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        add(Calendar.MINUTE, 1)
    }

    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent = createPendingIntent(context)

    // With setInexactRepeating(), you have to use one of the AlarmManager interval
    // constants--in this case, AlarmManager.INTERVAL_DAY.
    alarmMgr.setInexactRepeating(
        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
        1 * 60 * 1000,
        alarmIntent
    )
}

private fun createPendingIntent(context: Context): PendingIntent {
    return Intent(context, AlarmReceiver::class.java).let { intent ->
        intent.action = "playground.alarm.AlarmActivity"
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}