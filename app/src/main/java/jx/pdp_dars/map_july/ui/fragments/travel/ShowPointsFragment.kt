package jx.pdp_dars.map_july.ui.fragments.travel

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import jx.pdp_dars.map_july.R
import jx.pdp_dars.map_july.databinding.FragmentShowPointsBinding
import jx.pdp_dars.map_july.ui.data.models.local.HistoryData
import jx.pdp_dars.map_july.ui.data.services.LocationService
import jx.pdp_dars.map_july.ui.data.utils.SharedPref
import jx.pdp_dars.map_july.ui.fragments.BaseFragment
import java.text.SimpleDateFormat
import java.util.*


class ShowPointsFragment :
    BaseFragment<FragmentShowPointsBinding>(FragmentShowPointsBinding::inflate) {
    val db by lazy {
        Firebase.database
    }
    lateinit var shared: SharedPref
    var googleMap: GoogleMap? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun onViewCreate() {
        shared = SharedPref(
            requireContext()
        )
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.finishTravel.setOnClickListener {
            LocationService.stopLocationService(requireActivity())
        }
    }

    override fun onResume() {
        super.onResume()
        binding.id.text = requireArguments().getString("TRAVEL_ID", "")
        db.getReference("history").child(requireArguments().getString("TRAVEL_ID", ""))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("MY_DB", "Value size: ${snapshot.childrenCount}")
                    Log.d("MY_DB", "Value size: ${snapshot.children.count()}")
                    var speed = 0.0
                    var distance = 0.0
                    val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
                    snapshot.children.forEachIndexed { index, data ->
                        val h = data.getValue(HistoryData::class.java)
                        h?.let {
                            if (index == 0) {
                                val calendar = Calendar.getInstance()
                                calendar.timeInMillis = h.historyTime
                                binding.starTime.text =
                                    SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(calendar.time)
                            }
                            if (h.historyAvgSpeed > 0.0) {
                                speed += h.historyAvgSpeed
                                var d=0
                            }
                            distance += h.historyAvgDistance
                            options.add(LatLng(it.lat, it.lon))
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = h.historyTime
                            googleMap?.addMarker(
                                MarkerOptions()
                                    .position(LatLng(h.lat, h.lon))
                                    .title(
                                        "${h.historyAvgSpeed}/${
                                            SimpleDateFormat("hh:mm:ss").format(
                                                calendar.time
                                            )
                                        }"
                                    )
                            )
                        }
                    }
                    binding.avgSpeed.text = "${speed / snapshot.childrenCount.toDouble()} m/s"
                    binding.distance.text = "$distance meter"
                    googleMap?.addPolyline(options)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("MY_DB", "Failed to read value.", error.toException())
                }
            })
    }
}