package jx.pdp_dars.map_july.ui.data.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import jx.pdp_dars.map_july.R
import jx.pdp_dars.map_july.ui.MainActivity
import jx.pdp_dars.map_july.ui.data.models.local.HistoryData
import jx.pdp_dars.map_july.ui.data.utils.LocationHelper

class LocationService : Service() {

    private var locationHelper: LocationHelper? = null
    val db by lazy {
        Firebase.database
    }

    companion object {
        fun startLocationService(context: Activity, travelId: String) {
            if (isServiceRunningInForeground(context, LocationService::class.java)) {
                stopLocationService(context)
            }
            val intent = Intent(context, LocationService::class.java)
            intent.putExtra("TRAVEL_ID", travelId)
            context.startService(intent)
        }

        fun stopLocationService(context: Activity) {
            val intent = Intent(context, LocationService::class.java)
            context.stopService(intent)
        }

        fun isServiceRunningInForeground(context: Context, serviceClass: Class<*>): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return service.foreground
                }
            }
            return false
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.extras?.let {
            val travelId = it.getString("TRAVEL_ID") ?: ""
            locationHelper?.setOnChangeLocation { lat, lon, speed, time ->
                val key = db.getReference("history").push().key ?: ""
                db.getReference("history").child(travelId).child(key)
                    .setValue(HistoryData(key, speed, time, travelId, lat, lon))
            }
        }
        startForeground(1, notificationToDisplayServiceInfor())
        return START_STICKY
    }

    override fun onCreate() {
        locationHelper = LocationHelper()
        locationHelper?.getLocation(this)
        super.onCreate()
    }


    fun notificationToDisplayServiceInfor(): Notification {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Simple Foreground Service")
            .setContentText("Explain about the service")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
        return notification
    }

    val CHANNEL_ID = "ForegroundServiceChannel"
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(p0: Intent?) = null

}