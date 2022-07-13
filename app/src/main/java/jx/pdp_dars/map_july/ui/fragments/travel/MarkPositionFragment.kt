package jx.pdp_dars.map_july.ui.fragments.travel

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import jx.pdp_dars.map_july.R
import jx.pdp_dars.map_july.databinding.MarkPositionFragmentBinding
import jx.pdp_dars.map_july.ui.data.models.local.TravelData
import jx.pdp_dars.map_july.ui.data.services.LocationService
import jx.pdp_dars.map_july.ui.data.utils.SharedPref
import jx.pdp_dars.map_july.ui.fragments.BaseFragment


class MarkPositionFragment :
    BaseFragment<MarkPositionFragmentBinding>(MarkPositionFragmentBinding::inflate) {
    lateinit var shared: SharedPref
    val db by lazy {
        Firebase.database
    }
    private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(41.311081, 69.240562)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Tashkent"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        binding.selectPlace.setOnClickListener {
            Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show()
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

            ) {
                requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,

                    ),
                    12345
                )
            } else {
                Toast.makeText(requireContext(), "checkPermissions", Toast.LENGTH_SHORT).show()
                val key = db.getReference("travel").push().key ?: ""
                db.getReference("travel").child(key)
                    .setValue(TravelData(key, binding.travelName.text.toString()))
                    .addOnCompleteListener {
                        shared.setTravelId(key)
                        val intent = Intent(requireContext(), LocationService::class.java)
                        intent.putExtra("TRAVEL_ID", key)
                        requireActivity().startService(intent)
                    }
            }

        }
    }

    override fun onViewCreate() {
        shared = SharedPref(
            requireContext()
        )
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


}