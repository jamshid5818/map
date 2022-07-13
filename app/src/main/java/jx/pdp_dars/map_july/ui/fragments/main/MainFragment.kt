package jx.pdp_dars.map_july.ui.fragments.main

import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import jx.pdp_dars.map_july.R
import jx.pdp_dars.map_july.databinding.FragmentMainBinding
import jx.pdp_dars.map_july.ui.data.models.local.UserData
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