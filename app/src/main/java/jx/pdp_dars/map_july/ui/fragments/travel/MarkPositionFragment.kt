package jx.pdp_dars.map_july.ui.fragments.travel

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
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

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
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

    private fun runTrackingService() {
        val dialog = TravelNameDialog(requireContext())
        dialog.setOnButtonClickListener {
            val key = db.getReference("travel").push().key ?: ""
            db.getReference("travel").child(key)
                .setValue(TravelData(key, it))
                .addOnCompleteListener {
                    shared.setTravelId(key)
                    LocationService.startLocationService(requireActivity(), key)
                    dialog.dismiss()
                    navController.navigate(R.id.action_markPositionFragment_to_showPointsFragment)
                }

        }
        dialog.show()

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