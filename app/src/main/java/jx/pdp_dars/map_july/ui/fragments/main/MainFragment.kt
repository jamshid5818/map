package jx.pdp_dars.map_july.ui.fragments.main

import android.app.ActivityManager
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import jx.pdp_dars.map_july.R
import jx.pdp_dars.map_july.databinding.FragmentMainBinding
import jx.pdp_dars.map_july.ui.data.models.local.UserData
import jx.pdp_dars.map_july.ui.data.services.LocationService
import jx.pdp_dars.map_july.ui.data.utils.SharedPref
import jx.pdp_dars.map_july.ui.fragments.BaseFragment


class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    lateinit var shared: SharedPref
    val db by lazy {
        Firebase.database
    }

    override fun onViewCreate() {
        shared = SharedPref(
            requireContext()
        )
        saveUser()
        binding.startTravel.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_markPositionFragment)
        }

        binding.history.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_historyFragment)
        }

        binding.showCurrentTravel.setOnClickListener {
            navController.navigate(
                R.id.action_mainFragment_to_showPointsFragment,
                bundleOf("TRAVEL_ID" to shared.getTravelId())
            )
        }
        if (isServiceRunningInForeground(requireContext(), LocationService::class.java)) {
            binding.showCurrentTravel.visibility = View.VISIBLE
        } else {
            binding.showCurrentTravel.visibility = View.GONE
        }
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

    fun saveUser() {
        if (shared.getUsername().isNullOrEmpty()) {
            val dialog = UserNameDialog(requireContext())
            dialog.setOnButtonClickListener { username, password ->
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    shared.setUsername(username)
                    db.getReference("users").child(username).setValue(UserData(username, password))
                        .addOnCompleteListener {
                            dialog.dismiss()
                        }
                } else {
                    Toast.makeText(requireContext(), "Please enter username", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            dialog.show()
        }
    }

}