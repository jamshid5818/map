package jx.pdp_dars.map_july.ui.fragments.history

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import jx.pdp_dars.map_july.R
import jx.pdp_dars.map_july.databinding.FragmentHistoryBinding
import jx.pdp_dars.map_july.ui.data.models.local.TravelData
import jx.pdp_dars.map_july.ui.data.utils.SharedPref
import jx.pdp_dars.map_july.ui.fragments.BaseFragment

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
    private val adapter by lazy {
        HistoryAdapter()
    }
    private val db by lazy {
        Firebase.database
    }
    lateinit var shared: SharedPref

    override fun onViewCreate() {
        shared = SharedPref(
            requireContext()
        )
        binding.travelList.layoutManager = LinearLayoutManager(requireContext())
        binding.travelList.adapter = adapter
        adapter.setClickListener {
            navController.navigate(
                R.id.action_historyFragment_to_showPointsFragment,
                bundleOf("TRAVEL_ID" to it.id)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        db.getReference("travel").child(shared.getUsername() ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.map {
                        val h = it.getValue(TravelData::class.java) ?: TravelData("", "")
                        TravelData(h.id, h.name)
                    }
                    adapter.setTravelList(list)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}