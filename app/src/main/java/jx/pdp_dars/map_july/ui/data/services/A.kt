//package jx.pdp_dars.map_july.ui.data.services
//
//import android.app.*
//import android.content.Intent
//import android.os.Build
//import android.os.IBinder
//import androidx.core.app.NotificationCompat
//import jx.pdp_dars.map_july.R
//import jx.pdp_dars.map_july.ui.MainActivity
//
//class SimpleForegroundService : Service() {
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        startForeground(1,notificationToDisplayServiceInfor())
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//
//}