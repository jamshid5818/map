//package jx.pdp_dars.map_july.ui.fragments.splash
//
//import android.util.Log
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
//import jx.pdp_dars.map_july.databinding.FragmentSplashBinding
//import jx.pdp_dars.map_july.ui.data.models.local.HistoryData
//import jx.pdp_dars.map_july.ui.fragments.BaseFragment
//import java.util.*
//
//class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
//    val db by lazy {
//        Firebase.database
//    }
//
//    override fun onViewCreate() {
//        binding.add.setOnClickListener {
//            db.getReference("Nurdiyor").child("track-my-way-example").push().setValue(
//                HistoryData(
//                    "My First Travel",
//                    Random().nextInt(99999).toLong(), 45.0, 112454345
//                )
//            )
//        }
//        binding.delete.setOnClickListener {
//            db.getReference("track-my-way").child("-N6TK4GXn9WIB33BdKLu").removeValue()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        db.getReference("track-my-way").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d("MY_DB", "Value size: ${snapshot.childrenCount}")
//                Log.d("MY_DB", "Value size: ${snapshot.children.count()}")
//
//                snapshot.children.forEachIndexed {index,data->
//                    val h=data.getValue(HistoryData::class.java)
//                    Log.d("MY_DB", "$index Value is: ${h?.travelName} ${h?.travelDist}")
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("MY_DB", "Failed to read value.", error.toException())
//            }
//        })
//    }
//}