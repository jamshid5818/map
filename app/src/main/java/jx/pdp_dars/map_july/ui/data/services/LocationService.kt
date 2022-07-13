package jx.pdp_dars.map_july.ui.data.services

import android.app.Service
import android.content.Intent
import android.location.Location
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import jx.pdp_dars.map_july.ui.data.models.local.HistoryData
import jx.pdp_dars.map_july.ui.data.utils.LocationHelper

class LocationService : Service() {

    private var locationHelper: LocationHelper? = null
    val db by lazy {
        Firebase.database
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

        return START_STICKY
    }

    override fun onCreate() {
        locationHelper = LocationHelper()
        locationHelper?.getLocation(this)
        super.onCreate()
    }


    override fun onBind(p0: Intent?) = null

}