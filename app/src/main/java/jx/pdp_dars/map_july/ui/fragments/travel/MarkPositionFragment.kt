package jx.pdp_dars.map_july.ui.fragments.travel

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
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
    private val PERMISSION_REQUEST_CODE = 123
    lateinit var shared: SharedPref
    val db by lazy {
        Firebase.database
    }
    private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(41.311081, 69.240562)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Tashkent"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        binding.selectPlace.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION,
                        ) == PackageManager.PERMISSION_GRANTED
//                        &&
//                        ContextCompat.checkSelfPermission(
//                            requireContext(),
//                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//                        ) == PackageManager.PERMISSION_GRANTED
                -> {
                    runTrackingService()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please give me access !!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                        ),
                        PERMISSION_REQUEST_CODE
                    )
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please give me access !!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                        ),
                        PERMISSION_REQUEST_CODE
                    )
                }
                else -> {
                    // You can directly ask for the permission.
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                        ),
                        PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    fun runTrackingService() {
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

    override fun onViewCreate() {
        shared = SharedPref(
            requireContext()
        )
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                ) {
                    runTrackingService()
                } else {
                    Toast.makeText(requireContext(), "Please give me access", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}